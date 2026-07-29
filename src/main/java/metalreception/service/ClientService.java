package metalreception.service;

import metalreception.exception.business.ClientInUseException;
import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Client;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ClientService {

    private final List<Client> clients = new ArrayList<>();
    private final UsageChecker usageChecker;

    private int nextId = 1;

    public ClientService(UsageChecker usageChecker) {
        if (usageChecker == null) {
            throw new IllegalArgumentException(
                    "UsageChecker не может быть null."
            );
        }

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
        return findById(id)
                .orElseThrow(() -> new ClientNotFoundException(
                        "Клиент с id=" + id + " не найден."
                ));
    }

    public List<Client> findByName(String namePart) {
        if (namePart == null || namePart.isBlank()) {
            throw new InvalidNameException(
                    "Строка поиска клиента не может быть пустой."
            );
        }

        String normalizedNamePart =
                namePart.strip().toLowerCase(Locale.ROOT);

        List<Client> result = new ArrayList<>();

        for (Client client : clients) {
            String clientName =
                    client.getName().toLowerCase(Locale.ROOT);

            if (clientName.contains(normalizedNamePart)) {
                result.add(client);
            }
        }

        return result;
    }

    public Client updateClient(
            int id,
            String newName,
            String newPhone,
            boolean clearPhone
    ) {
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
        Client client = getByIdOrThrow(id);

        if (usageChecker.isClientInUse(id)) {
            throw new ClientInUseException(
                    "Нельзя удалить клиента с id=" + id +
                            ": он есть в истории приёмок."
            );
        }

        clients.remove(client);
    }
}