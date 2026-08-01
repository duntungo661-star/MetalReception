package metalreception.model;

import metalreception.exception.validation.InvalidChangeReasonException;
import metalreception.exception.validation.InvalidReceptionDataException;
import metalreception.exception.validation.InvalidWeightException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionTest {

    private Client client;
    private Metal metal;

    @BeforeEach
    void setUp() {
        client = new Client("Иван", null);
        metal = new Metal("Железо", new BigDecimal("50"));
    }

    @Test
    void shouldCalculateTotalPriceOnCreation() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        // 10 кг * 50 за кг = 500
        assertEquals(new BigDecimal("500.00"), reception.getTotalPrice());
    }

    @Test
    void shouldFixHistoricalPriceAtCreationTime() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        metal.setPricePerKg(new BigDecimal("100"));

        assertEquals(new BigDecimal("50"), reception.getPricePerKgAtReception());
        assertEquals(new BigDecimal("500.00"), reception.getTotalPrice());
    }

    @Test
    void shouldThrowExceptionWhenClientIsNull() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new Reception(1, null, metal, new BigDecimal("10"), LocalDate.now()));
    }

    @Test
    void shouldThrowExceptionWhenMetalIsNull() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new Reception(1, client, null, new BigDecimal("10"), LocalDate.now()));
    }

    @Test
    void shouldThrowExceptionWhenWeightIsZeroOrNegative() {
        assertThrows(InvalidWeightException.class,
                () -> new Reception(1, client, metal, BigDecimal.ZERO, LocalDate.now()));
        assertThrows(InvalidWeightException.class,
                () -> new Reception(1, client, metal, new BigDecimal("-5"), LocalDate.now()));
    }

    @Test
    void shouldThrowExceptionWhenDateIsNull() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new Reception(1, client, metal, new BigDecimal("10"), null));
    }

    @Test
    void correctWeightShouldRecalculateTotalPrice() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        reception.correctWeight(new BigDecimal("20"), "Ошибка при взвешивании");

        assertEquals(new BigDecimal("20"), reception.getWeight());
        // 20 кг * 50 (историческая цена, не текущая) = 1000
        assertEquals(new BigDecimal("1000.00"), reception.getTotalPrice());
    }

    @Test
    void correctWeightShouldAddEntryToHistory() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        reception.correctWeight(new BigDecimal("20"), "Ошибка при взвешивании");

        assertEquals(1, reception.getChanges().size());

        ReceptionChange change = reception.getChanges().getFirst();
        assertEquals(new BigDecimal("10"), change.getOldWeight());
        assertEquals(new BigDecimal("20"), change.getNewWeight());
        assertEquals(new BigDecimal("500.00"), change.getOldTotalPrice());
        assertEquals(new BigDecimal("1000.00"), change.getNewTotalPrice());
        assertEquals("Ошибка при взвешивании", change.getReason());
    }

    @Test
    void correctWeightShouldThrowExceptionWhenNewWeightEqualsOld() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        assertThrows(InvalidWeightException.class,
                () -> reception.correctWeight(new BigDecimal("10"), "Причина"));
    }

    @Test
    void correctWeightShouldThrowExceptionWhenReasonIsBlank() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );

        assertThrows(InvalidChangeReasonException.class,
                () -> reception.correctWeight(new BigDecimal("20"), ""));
    }

    @Test
    void getChangesShouldReturnDefensiveCopy() {
        Reception reception = new Reception(
                1, client, metal, new BigDecimal("10"), LocalDate.now()
        );
        reception.correctWeight(new BigDecimal("20"), "Причина");

        reception.getChanges().clear(); // пытаемся испортить список извне

        // внутренний список не должен пострадать
        assertEquals(1, reception.getChanges().size());
    }

    @Test
    void shouldRoundTotalPriceToTwoDecimalPlaces() {
        Metal priceseMetal = new Metal("Медь", new BigDecimal("87.333"));
        Reception reception = new Reception(1, client, priceseMetal, new BigDecimal("12.345"), LocalDate.now());

        assertEquals(new BigDecimal("1078.13"), reception.getTotalPrice());
    }

    @Test
    void  correctWeightShouldAlsoRoundNewTotalPrice() {
        Metal pricesMetal = new Metal("Медь", new BigDecimal("87.333"));
        Reception reception = new Reception(1, client, pricesMetal, new BigDecimal("10"), LocalDate.now());

        reception.correctWeight(new BigDecimal("12.345"), "Пересчет веса");

        assertEquals(new BigDecimal("1078.13"), reception.getTotalPrice());
    }
}