package metalreception.service;

import metalreception.exception.business.MetalInUseException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Metal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MetalServiceTest {

    private FakeUsageChecker usageChecker;
    private MetalService metalService;

    @BeforeEach
    void setUp() {
        usageChecker = new FakeUsageChecker();
        metalService = new MetalService(usageChecker);
    }

    @Test
    void shouldAddMetalAndAssignSequentialIds() {
        Metal first = metalService.addMetal("Железо", new BigDecimal("50"));
        Metal second = metalService.addMetal("Медь", new BigDecimal("300"));

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
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
        assertEquals("Железо", found.get(0).getName());
    }

    @Test
    void findByNameShouldThrowExceptionWhenSearchStringIsBlank() {
        // Проверка бага: пустая строка поиска не должна возвращать всё подряд
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName(""));
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName("   "));
    }

    @Test
    void findByNameShouldNotReturnAllMetalsForBlankQuery() {
        metalService.addMetal("Железо", new BigDecimal("50"));
        metalService.addMetal("Медь", new BigDecimal("300"));

        // раньше здесь возвращались бы все металлы — теперь должно быть исключение
        assertThrows(InvalidNameException.class,
                () -> metalService.findByName(""));
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
}