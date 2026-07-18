package metalreception.service;

import metalreception.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientService {
    private final List<Client> clients = new ArrayList<>();
    private int nextId = 1;

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

    public boolean deleteById(int id) {
        return clients.removeIf(client -> client.getId() == id);
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
}
