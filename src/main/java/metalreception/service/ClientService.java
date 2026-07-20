package main.java.metalreception.service;

import main.java.metalreception.exception.business.ClientInUseException;
import main.java.metalreception.exception.notfound.ClientNotFoundException;
import main.java.metalreception.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientService {
    private final List<Client> clients = new ArrayList<>();
    private int nextId = 1;
    private final UsageChecker usageChecker;

    public ClientService(UsageChecker usageChecker) {
        this.usageChecker = usageChecker;
    }

    public Client addClient(String name, String phone) {
        Client client = new Client(nextId, name, phone);
        clients.add(client);
        nextId++;
        return client;
    }

    public List<Client> getAllClients() {
        return new ArrayList<>(clients);
    }

    public Optional<Client> findById(int id) {
        for (Client client : clients) {
            if (client.getId() == id) {
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    public Client getByIdOrThrow(int id) {
        return findById(id).orElseThrow(() -> new ClientNotFoundException("Клиент с id=" + id + " не найден."));
    }

    public List<Client> findByName(String namePart) {
        List<Client> result = new ArrayList<>();
        for (Client client : clients) {
            if (client.getName().toLowerCase().contains(namePart.toLowerCase())) {
                result.add(client);
            }
        }
        return result;
    }

    public Client updateClient(int id, String newName, String newPhone, boolean clearPhone) {
        Client client = getByIdOrThrow(id);
        if (newName != null && !newName.isBlank()) {
            client.setName(newName);
        }
        if (clearPhone) {
            client.setPhone(null);
        } else if (newPhone != null && !newPhone.isBlank()) {
            client.setPhone(newPhone);
        }
        return client;
    }

    public void deleteClient(int id) {
        getByIdOrThrow(id);
        if (usageChecker.isClientInUse(id)) {
            throw new ClientInUseException("Нельзя удалить клиента с id=" + id + ": он есть в истории приёмок.");
        }
        clients.removeIf(client -> client.getId() == id);
    }
}
