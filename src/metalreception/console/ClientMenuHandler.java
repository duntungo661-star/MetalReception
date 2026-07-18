package metalreception.console;

import metalreception.model.Client;
import metalreception.model.Reception;
import metalreception.service.ClientService;
import metalreception.service.ReceptionService;

import java.util.List;
import java.util.Optional;

public class ClientMenuHandler {
    private final ClientService clientService;
    private final ReceptionService receptionService;
    private final ConsoleInputReader inputReader;

    public ClientMenuHandler(ClientService clientService, ReceptionService receptionService,
                             ConsoleInputReader inputReader)  {
        this.clientService = clientService;
        this.receptionService = receptionService;
        this.inputReader = inputReader;

    }

    public void addClient() {
        System.out.println("Введите имя клиента: ");
        String name = inputReader.readLine();

        System.out.println("Введите телефон клиента (можно оставить пустым): ");
        String phone = inputReader.readLine();
        if (phone.isBlank()) {
            phone = null;
        }

        try {
            Client client = clientService.addClient(name, phone);
            System.out.println("Клиент добавлен: " + client);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void showAllClients() {
        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }

        System.out.println("\n=== Список клиентов ===");
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    public void deleteClients() {
        showAllClients();
        System.out.println();

        System.out.println("Введите id клиента для удаления: ");
        int clientId = inputReader.readInt();

        Optional<Client> clientResult = clientService.findById(clientId);
        if (clientResult.isEmpty()) {
            System.out.println("Клиент с id=" + clientId + " не найден.");
            return;
        }

        List<Reception> clientReceptions = receptionService.findByClientId(clientId);
        if (!clientReceptions.isEmpty()) {
            System.out.println("Нельзя удалить клиента: у него есть     " + clientReceptions.size()
                    + " приёмка(и) в истории.");
            return;
        }

        clientService.deleteById(clientId);
        System.out.println("Клиент с id=" + clientId + " удалён.");
    }

    public void editClient() {
        showAllClients();
        System.out.println();

        System.out.println("Введите id для изменения: ");
        int clientId = inputReader.readInt();

        Optional<Client> clientResult = clientService.findById(clientId);
        if (clientResult.isEmpty()) {
            System.out.println("Клиент с таким id=" + clientId + " не найден.");
            return;
        }
        Client client = clientResult.get();

        System.out.println("Текущее имя: " + client.getName());
        System.out.println("Введите новое имя (или оставьте пустым чтобы не менять): ");
        String newName = inputReader.readLine();
        System.out.println("Текущий телефон: " + (client.getPhone() == null ? "не указан" : client.getPhone()));
        System.out.println("Введите новый телефон (или оставьте пустым чтобы не менять): ");
        String newPhone = inputReader.readLine();

        try {
            if (!newName.isBlank()) {
                client.setName(newName);
            }
            if (!newPhone.isBlank()) {
                client.setPhone(newPhone);
            }
            System.out.println("Клиент изменён: " + client);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void searchClientByName() {
        System.out.println("Введите часть имени клиента: ");
        String namePart = inputReader.readLine();

        List<Client> found = clientService.findByName(namePart);
        if (found.isEmpty()) {
            System.out.println("Клиенты не найдены.");
            return;
        }

        System.out.println("\n=========Найденные клиенты==========");
        for (Client client : found) {
            System.out.println(client);
        }
    }
}
