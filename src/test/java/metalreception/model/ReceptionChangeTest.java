package metalreception.model;

import metalreception.exception.validation.InvalidChangeReasonException;
import metalreception.exception.validation.InvalidReceptionDataException;
import metalreception.exception.validation.InvalidWeightException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ReceptionChangeTest {
    private static final BigDecimal VALID_OLD_WEIGHT = new BigDecimal("10");
    private static final BigDecimal VALID_NEW_WEIGHT = new BigDecimal("20");
    private static final BigDecimal VALID_OLD_PRICE = new BigDecimal("500.00");
    private static final BigDecimal VALID_NEW_PRICE = new BigDecimal("1000.00");
    private static final String VALID_REASON = "Ошибка при взвешивании";

    @Test
    void shouldCreateReceptionChangeWithValidData () {
        ReceptionChange change = new ReceptionChange(VALID_OLD_WEIGHT, VALID_NEW_WEIGHT,
                VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON);

        assertEquals(VALID_OLD_WEIGHT, change.getOldWeight());
        assertEquals(VALID_NEW_WEIGHT, change.getNewWeight());
        assertEquals(VALID_OLD_PRICE, change.getOldTotalPrice());
        assertEquals(VALID_NEW_PRICE, change.getNewTotalPrice());
        assertEquals(VALID_REASON, change.getReason());
    }

    @Test
    void shouldSetChangeAtToCurrentMoment () {
        LocalDateTime before = LocalDateTime.now();

        ReceptionChange change = new ReceptionChange(VALID_OLD_WEIGHT, VALID_NEW_WEIGHT,
                VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON);

        LocalDateTime after = LocalDateTime.now();

        assertFalse(change.getChangedAt().isBefore(before));
        assertFalse(change.getChangedAt().isAfter(after));
    }

    @Test
    void shouldStripWhitespaceFromReason() {
        ReceptionChange change = new ReceptionChange(VALID_OLD_WEIGHT, VALID_NEW_WEIGHT,
                VALID_OLD_PRICE, VALID_NEW_PRICE, " Пересчёт веса ");

        assertEquals("Пересчёт веса", change.getReason());
    }

    @Test
    void shouldThrowExceptionWhenOldWeightIsNull() {
        assertThrows(InvalidWeightException.class, () -> new ReceptionChange(null,
                VALID_NEW_WEIGHT, VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON));
    }

    @Test
    void shouldThrowExceptionWhenOldWeightIsZeroOrNegative() {
        assertThrows(InvalidWeightException.class,
                () -> new ReceptionChange(
                        BigDecimal.ZERO, VALID_NEW_WEIGHT, VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON
                ));

        assertThrows(InvalidWeightException.class,
                () -> new ReceptionChange(
                        new BigDecimal("-5"), VALID_NEW_WEIGHT, VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenNewWeightIsNull() {
        assertThrows(InvalidWeightException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, null, VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenNewWeightIsZeroOrNegative() {
        assertThrows(InvalidWeightException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, BigDecimal.ZERO, VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON
                ));

        assertThrows(InvalidWeightException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, new BigDecimal("-5"), VALID_OLD_PRICE, VALID_NEW_PRICE, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenOldTotalPriceIsNull() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, null, VALID_NEW_PRICE, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenOldTotalPriceIsZeroOrNegative() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, BigDecimal.ZERO, VALID_NEW_PRICE, VALID_REASON
                ));

        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, new BigDecimal("-100"), VALID_NEW_PRICE, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenNewTotalPriceIsNull() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, VALID_OLD_PRICE, null, VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenNewTotalPriceIsZeroOrNegative() {
        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, VALID_OLD_PRICE, BigDecimal.ZERO, VALID_REASON
                ));

        assertThrows(InvalidReceptionDataException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, VALID_OLD_PRICE, new BigDecimal("-100"), VALID_REASON
                ));
    }

    @Test
    void shouldThrowExceptionWhenReasonIsNull() {
        assertThrows(InvalidChangeReasonException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, VALID_OLD_PRICE, VALID_NEW_PRICE, null
                ));
    }

    @Test
    void shouldThrowExceptionWhenReasonIsBlank() {
        assertThrows(InvalidChangeReasonException.class,
                () -> new ReceptionChange(
                        VALID_OLD_WEIGHT, VALID_NEW_WEIGHT, VALID_OLD_PRICE, VALID_NEW_PRICE, "   "
                ));
    }
}
