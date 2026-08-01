package metalreception.service;

import metalreception.exception.business.ClientInUseException;
import metalreception.exception.business.MetalInUseException;
import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({ClientService.class, MetalService.class, ReceptionService.class})
class ServicesIntegrationTest {

    @Autowired
    private ReceptionService receptionService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private MetalService metalService;

    @Test
    void shouldAllowDeletingClientWithoutReceptions() {
        Client client = clientService.addClient("Иван", null);

        clientService.deleteClient(client.getId());

        assertTrue(clientService.getAllClients().isEmpty());
    }

    @Test
    void shouldNotAllowDeletingClientWithExistingReceptions() {
        Client client = clientService.addClient("Иван", null);
        Metal metal = metalService.addMetal("Медь", new BigDecimal("300"));

        receptionService.createReception(client, metal, new BigDecimal("10"));

        assertThrows(ClientInUseException.class,
                () -> clientService.deleteClient(client.getId()));

        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void shouldNotAllowDeletingMetalWithExistingReceptions() {
        Client client = clientService.addClient("Иван", null);
        Metal metal = metalService.addMetal("Медь", new BigDecimal("300"));

        receptionService.createReception(client, metal, new BigDecimal("10"));

        assertThrows(MetalInUseException.class,
                () -> metalService.deleteMetal(metal.getId()));

        assertEquals(1, metalService.getAllMetals().size());
    }

    @Test
    void shouldAllowDeletingOtherClientNotInvolvedInReception() {
        Client clientWithReception = clientService.addClient("Иван", null);
        Client freeClient = clientService.addClient("Пётр", null);
        Metal metal = metalService.addMetal("Медь", new BigDecimal("300"));

        receptionService.createReception(clientWithReception, metal, new BigDecimal("10"));

        clientService.deleteClient(freeClient.getId());

        assertEquals(1, clientService.getAllClients().size());
        assertEquals(clientWithReception.getId(), clientService.getAllClients().getFirst().getId());
    }

    @Test
    void shouldReflectRealReceptionDataInCreatedReception() {
        Client client = clientService.addClient("Иван", null);
        Metal metal = metalService.addMetal("Медь", new BigDecimal("300"));

        Reception reception = receptionService.createReception(
                client, metal, new BigDecimal("5")
        );

        assertEquals(client.getId(), reception.getClient().getId());
        assertEquals(metal.getId(), reception.getMetal().getId());
        assertEquals(new BigDecimal("1500.00"), reception.getTotalPrice());
    }
}