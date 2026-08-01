package metalreception.service;

import metalreception.exception.business.ClientInUseException;
import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Disabled("Временно отключено: миграция на Spring Data JPA")
class ClientServiceTest {

    private FakeUsageChecker usageChecker;
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        usageChecker = new FakeUsageChecker();
        clientService = new ClientService(null, usageChecker);
    }

    @Test
    void shouldAddClientAndAssignSequentialIds() {
        Client first = clientService.addClient("Иван", null);
        Client second = clientService.addClient("Пётр", null);

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
    }

    @Test
    void shouldFindClientById() {
        Client added = clientService.addClient("Иван", null);

        Client found = clientService.getByIdOrThrow(added.getId());

        assertEquals(added.getId(), found.getId());
        assertEquals("Иван", found.getName());
    }

    @Test
    void getByIdOrThrowShouldThrowWhenClientNotFound() {
        assertThrows(ClientNotFoundException.class,
                () -> clientService.getByIdOrThrow(999));
    }

    @Test
    void shouldReturnAllClients() {
        clientService.addClient("Иван", null);
        clientService.addClient("Пётр", null);

        List<Client> all = clientService.getAllClients();

        assertEquals(2, all.size());
    }

    @Test
    void getAllClientsShouldReturnDefensiveCopy() {
        clientService.addClient("Иван", null);

        clientService.getAllClients().clear();

        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void shouldFindClientsByPartialNameCaseInsensitive() {
        clientService.addClient("Иван Иванов", null);
        clientService.addClient("Пётр Петров", null);

        List<Client> found = clientService.findByName("ИВАН");

        assertEquals(1, found.size());
        assertEquals("Иван Иванов", found.getFirst().getName());
    }

    @Test
    void findByNameShouldThrowExceptionWhenSearchStringIsBlank() {
        assertThrows(InvalidNameException.class,
                () -> clientService.findByName(""));
        assertThrows(InvalidNameException.class,
                () -> clientService.findByName("   "));
    }

    @Test
    void shouldUpdateClientName() {
        Client client = clientService.addClient("Иван", "+79991234567");

        Client updated = clientService.updateClient(
                client.getId(), "Пётр", null, false
        );

        assertEquals("Пётр", updated.getName());
        assertEquals("+79991234567", updated.getPhone()); // телефон не тронут
    }

    @Test
    void shouldUpdateClientPhone() {
        Client client = clientService.addClient("Иван", null);

        Client updated = clientService.updateClient(
                client.getId(), null, "+79991234567", false
        );

        assertEquals("+79991234567", updated.getPhone());
    }

    @Test
    void shouldClearPhoneWhenRequested() {
        Client client = clientService.addClient("Иван", "+79991234567");

        Client updated = clientService.updateClient(
                client.getId(), null, null, true
        );

        assertNull(updated.getPhone());
    }

    @Test
    void updateClientShouldThrowWhenClientNotFound() {
        assertThrows(ClientNotFoundException.class,
                () -> clientService.updateClient(999, "Иван", null, false));
    }

    @Test
    void shouldDeleteClientWhenNotInUse() {
        Client client = clientService.addClient("Иван", null);
        usageChecker.setClientInUse(false);

        clientService.deleteClient(client.getId());

        assertTrue(clientService.getAllClients().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenDeletingClientInUse() {
        Client client = clientService.addClient("Иван", null);
        usageChecker.setClientInUse(true);

        assertThrows(ClientInUseException.class,
                () -> clientService.deleteClient(client.getId()));

        // клиент не должен быть удалён
        assertEquals(1, clientService.getAllClients().size());
    }

    @Test
    void deleteClientShouldThrowWhenClientNotFound() {
        assertThrows(ClientNotFoundException.class,
                () -> clientService.deleteClient(999));
    }

    @Test
    void findByNameShouldThrowExceptionWhenSearchStringIsNull() {
        assertThrows(InvalidNameException.class,
                () -> clientService.findByName(null));
    }
}