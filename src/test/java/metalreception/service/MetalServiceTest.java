package metalreception.service;

import metalreception.exception.business.MetalInUseException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Metal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MetalService.class)
class MetalServiceTest {

    @TestConfiguration
    static class TestConfig {
        @Bean
        FakeUsageChecker fakeUsageChecker() {
            return new FakeUsageChecker();
        }
    }

    @Autowired
    private MetalService metalService;

    @Autowired
    private FakeUsageChecker usageChecker;

    @BeforeEach
    void resetUsageChecker() {
        usageChecker.setClientInUse(false);
        usageChecker.setMetalInUse(false);
    }

    @Test
    void shouldAddMetalAndAssignSequentialIds() {
        Metal first = metalService.addMetal("Железо", new BigDecimal("50"));
        Metal second = metalService.addMetal("Медь", new BigDecimal("300"));

        assertNotNull(first.getId());
        assertNotNull(second.getId());
        assertNotEquals(first.getId(), second.getId());
    }

    @Test
    void shouldFindMetalById() {
        Metal added = metalService.addMetal("Железо", new BigDecimal("50"));

        Metal found = metalService.getByIdOrThrow(added.getId());

        assertEquals("Железо", found.getName());
    }

    @Test
    void getByIdOrThrowShouldThrowWhenMetalNotFound() {
        assertThrows(MetalNotFoundException.class,
                () -> metalService.getByIdOrThrow(999));
    }

    @Test
    void shouldFindMetalsByPartialNameCaseInsensitive() {
        metalService.addMetal("Железо", new BigDecimal("50"));
        metalService.addMetal("Медь", new BigDecimal("300"));

        List<Metal> found = metalService.findByName("ЖЕЛЕЗ");

        assertEquals(1, found.size());
        assertEquals("Железо", found.getFirst().getName());
    }

    @Test
    void findByNameShouldThrowExceptionWhenSearchStringIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName(""));
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName("   "));
    }

    @Test
    void findByNameShouldThrowExceptionWhenSearchStringIsNull() {
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName(null));
    }

    @Test
    void shouldUpdateMetalNameOnly() {
        Metal metal = metalService.addMetal("Железо", new BigDecimal("50"));

        Metal updated = metalService.updateMetal(
                metal.getId(), "Сталь", null
        );

        assertEquals("Сталь", updated.getName());
        assertEquals(new BigDecimal("50"), updated.getPricePerKg());
    }

    @Test
    void shouldUpdateMetalPriceOnly() {
        Metal metal = metalService.addMetal("Железо", new BigDecimal("50"));

        Metal updated = metalService.updateMetal(
                metal.getId(), null, new BigDecimal("70")
        );

        assertEquals("Железо", updated.getName());
        assertEquals(new BigDecimal("70"), updated.getPricePerKg());
    }

    @Test
    void shouldDeleteMetalWhenNotInUse() {
        Metal metal = metalService.addMetal("Железо", new BigDecimal("50"));
        usageChecker.setMetalInUse(false);

        metalService.deleteMetal(metal.getId());

        assertTrue(metalService.getAllMetals().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDeletingMetalInUse() {
        Metal metal = metalService.addMetal("Железо", new BigDecimal("50"));
        usageChecker.setMetalInUse(true);

        assertThrows(MetalInUseException.class,
                () -> metalService.deleteMetal(metal.getId()));

        assertEquals(1, metalService.getAllMetals().size());
    }

    @Test
    void updateMetalShouldThrowWhenMetalNotFound() {
        assertThrows(MetalNotFoundException.class,
                () -> metalService.updateMetal(999, "Сталь", new BigDecimal("70")));
    }

    @Test
    void deleteMetalShouldThrowWhenMetalNotFound() {
        assertThrows(MetalNotFoundException.class,
                () -> metalService.deleteMetal(999));
    }
}