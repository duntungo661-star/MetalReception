package metalreception.model;

import metalreception.exception.validation.InvalidIdException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPriceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MetalTest {

    @Test
    void shouldCreateMetalWithValidData() {
        Metal metal = new Metal(1, "Железо", new BigDecimal("50.5"));

        assertEquals(1, metal.getId());
        assertEquals("Железо", metal.getName());
        assertEquals(new BigDecimal("50.5"), metal.getPricePerKg());
    }

    @Test
    void shouldThrowExceptionWhenIdIsZeroOrNegative() {
        assertThrows(InvalidIdException.class,
                () -> new Metal(0, "Железо", new BigDecimal("10")));
        assertThrows(InvalidIdException.class,
                () -> new Metal(-1, "Железо", new BigDecimal("10")));
    }


    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> new Metal(1, "", new BigDecimal("10")));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal(1, "Железо", null));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal(1, "Железо", BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal(1, "Железо", new BigDecimal("-5")));
    }

    @Test
    void shouldUpdatePriceThroughSetter() {
        Metal metal = new Metal(1, "Железо", new BigDecimal("50"));

        metal.setPricePerKg(new BigDecimal("60"));

        assertEquals(new BigDecimal("60"), metal.getPricePerKg());
    }

    @Test
    void setPricePerKgShouldThrowExceptionWhenNegative() {
        Metal metal = new Metal(1, "Железо", new BigDecimal("50"));

        assertThrows(InvalidPriceException.class,
                () -> metal.setPricePerKg(new BigDecimal("-1")));
    }

    @Test
    void shouldStripWhitespaceFromName() {
        Metal metal = new Metal(1, "  Железо  ", new BigDecimal("10"));

        assertEquals("Железо", metal.getName());
    }
}