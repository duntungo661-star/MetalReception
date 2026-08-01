package metalreception.service;

import metalreception.exception.business.ClientInUseException;
import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Client;
import metalreception.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final UsageChecker usageChecker;

    public ClientService(ClientRepository clientRepository, UsageChecker usageChecker) {
        this.clientRepository = clientRepository;
        this.usageChecker = usageChecker;
    }

    public Client addClient(String name, String phone) {
        Client client = new Client(name, phone);
        return clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client getByIdOrThrow(int id) {
        return clientRepository.findById(id)
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

        return clientRepository.findAll().stream()
                .filter(client -> client.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(namePart.strip().toLowerCase(Locale.ROOT)))
                .toList();
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

        return clientRepository.save(client);
    }

    public void deleteClient(int id) {
        Client client = getByIdOrThrow(id);

        if (usageChecker.isClientInUse(id)) {
            throw new ClientInUseException(
                    "Нельзя удалить клиента с id=" + id +
                            ": он есть в истории приёмок."
            );
        }

        clientRepository.delete(client);
    }
}