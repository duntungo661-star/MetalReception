package metalreception.model;

import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPriceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MetalTest {

    @Test
    void shouldCreateMetalWithValidData() {
        Metal metal = new Metal("Железо", new BigDecimal("50.5"));

        assertEquals("Железо", metal.getName());
        assertEquals(new BigDecimal("50.5"), metal.getPricePerKg());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> new Metal("", new BigDecimal("10")));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal("Железо", null));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal("Железо", BigDecimal.ZERO));
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        assertThrows(InvalidPriceException.class,
                () -> new Metal("Железо", new BigDecimal("-5")));
    }

    @Test
    void shouldUpdatePriceThroughSetter() {
        Metal metal = new Metal("Железо", new BigDecimal("50"));

        metal.setPricePerKg(new BigDecimal("60"));

        assertEquals(new BigDecimal("60"), metal.getPricePerKg());
    }

    @Test
    void setPricePerKgShouldThrowExceptionWhenNegative() {
        Metal metal = new Metal("Железо", new BigDecimal("50"));

        assertThrows(InvalidPriceException.class,
                () -> metal.setPricePerKg(new BigDecimal("-1")));
    }

    @Test
    void shouldStripWhitespaceFromName() {
        Metal metal = new Metal("  Железо  ", new BigDecimal("10"));

        assertEquals("Железо", metal.getName());
    }
}