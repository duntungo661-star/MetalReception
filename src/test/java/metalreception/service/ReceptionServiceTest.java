package metalreception.service;

import metalreception.exception.notfound.ReceptionNotFoundException;
import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.model.ReceptionChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Временно отключено: миграция на Spring Data JPA")
class ReceptionServiceTest {

    private ReceptionService receptionService;
    private Client client;
    private Metal metal;

    @BeforeEach
    void setUp() {
        receptionService = new ReceptionService(null);
        client = new Client("Иван", null);
        metal = new Metal("Железо", new BigDecimal("50"));
    }

    @Test
    void shouldCreateReceptionAndAssignSequentialIds() {
        Reception first = receptionService.createReception(
                client, metal, new BigDecimal("10")
        );
        Reception second = receptionService.createReception(
                client, metal, new BigDecimal("5")
        );

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
    }

    @Test
    void shouldFindReceptionById() {
        Reception created = receptionService.createReception(
                client, metal, new BigDecimal("10")
        );

        Reception found = receptionService.getByIdOrThrow(created.getId());

        assertEquals(created.getId(), found.getId());
    }

    @Test
    void getByIdOrThrowShouldThrowWhenReceptionNotFound() {
        assertThrows(ReceptionNotFoundException.class,
                () -> receptionService.getByIdOrThrow(999));
    }

    @Test
    void shouldFindReceptionsByClientId() {
        receptionService.createReception(client, metal, new BigDecimal("10"));

        Client otherClient = new Client("Пётр", null);
        receptionService.createReception(otherClient, metal, new BigDecimal("5"));

        List<Reception> found = receptionService.findByClientId(client.getId());

        assertEquals(1, found.size());
        assertEquals(client.getId(), found.getFirst().getClient().getId());
    }

    @Test
    void shouldFindReceptionsByMetalId() {
        receptionService.createReception(client, metal, new BigDecimal("10"));

        Metal otherMetal = new Metal("Медь", new BigDecimal("300"));
        receptionService.createReception(client, otherMetal, new BigDecimal("5"));

        List<Reception> found = receptionService.findByMetalId(metal.getId());

        assertEquals(1, found.size());
        assertEquals(metal.getId(), found.getFirst().getMetal().getId());
    }

    @Test
    void isClientInUseShouldReturnTrueWhenClientHasReceptions() {
        receptionService.createReception(client, metal, new BigDecimal("10"));

        assertTrue(receptionService.isClientInUse(client.getId()));
    }

    @Test
    void isClientInUseShouldReturnFalseWhenClientHasNoReceptions() {
        assertFalse(receptionService.isClientInUse(999));
    }

    @Test
    void isMetalInUseShouldReturnTrueWhenMetalHasReceptions() {
        receptionService.createReception(client, metal, new BigDecimal("10"));

        assertTrue(receptionService.isMetalInUse(metal.getId()));
    }

    @Test
    void isMetalInUseShouldReturnFalseWhenMetalHasNoReceptions() {
        assertFalse(receptionService.isMetalInUse(999));
    }

    @Test
    void correctReceptionWeightShouldUpdateWeightAndTotalPrice() {
        Reception created = receptionService.createReception(
                client, metal, new BigDecimal("10")
        );

        Reception updated = receptionService.correctReceptionWeight(
                created.getId(), new BigDecimal("20"), "Пересчёт"
        );

        assertEquals(new BigDecimal("20"), updated.getWeight());
        assertEquals(new BigDecimal("1000.00"), updated.getTotalPrice());
    }

    @Test
    void correctReceptionWeightShouldThrowWhenReceptionNotFound() {
        assertThrows(ReceptionNotFoundException.class,
                () -> receptionService.correctReceptionWeight(999, new BigDecimal("20"), "Причина"));
    }

    @Test
    void shouldReturnReceptionChangeHistory() {
        Reception created = receptionService.createReception(
                client, metal, new BigDecimal("10")
        );
        receptionService.correctReceptionWeight(created.getId(), new BigDecimal("15"), "Причина 1");
        receptionService.correctReceptionWeight(created.getId(), new BigDecimal("20"), "Причина 2");

        List<ReceptionChange> changes = receptionService.getReceptionChanges(created.getId());

        assertEquals(2, changes.size());
        assertEquals("Причина 1", changes.get(0).getReason());
        assertEquals("Причина 2", changes.get(1).getReason());
    }
}