package metalreception.console;

import metalreception.exception.business.ClientInUseException;
import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.validation.ValidationException;
import metalreception.model.Client;
import metalreception.service.ClientService;

import java.util.List;

public class ClientMenuHandler {

    private final ClientService clientService;
    private final ConsoleInputReader inputReader;

    public ClientMenuHandler(
            ClientService clientService,
            ConsoleInputReader inputReader
    ) {
        this.clientService = clientService;
        this.inputReader = inputReader;
    }

    public void addClient() {
        System.out.println("Введите имя клиента:");
        String name = inputReader.readLine();

        System.out.println(
                "Введите телефон клиента (можно оставить пустым):"
        );
        String phone = inputReader.readLine();

        if (phone.isBlank()) {
            phone = null;
        }

        try {
            Client client = clientService.addClient(name, phone);
            System.out.println("Клиент добавлен: " + client);
        } catch (ValidationException e) {
            printError(e);
        }
    }

    public void showAllClients() {
        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }

        printClients(clients);
    }

    public void deleteClient() {
        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }

        printClients(clients);

        System.out.println();
        System.out.println("Введите ID клиента для удаления:");
        int clientId = inputReader.readInt();

        try {
            clientService.deleteClient(clientId);

            System.out.println(
                    "Клиент с id=" + clientId + " удалён."
            );
        } catch (ClientNotFoundException |
                 ClientInUseException e) {

            printError(e);
        }
    }

    public void editClient() {
        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }

        printClients(clients);

        System.out.println();
        System.out.println("Введите ID клиента для изменения:");
        int clientId = inputReader.readInt();

        Client client;

        try {
            client = clientService.getByIdOrThrow(clientId);
        } catch (ClientNotFoundException e) {
            printError(e);
            return;
        }

        System.out.println("Текущее имя: " + client.getName());
        System.out.println(
                "Введите новое имя или оставьте пустым:"
        );
        String newName = inputReader.readLine();

        String currentPhone = client.getPhone() == null
                ? "не указан"
                : client.getPhone();

        System.out.println("Текущий телефон: " + currentPhone);
        System.out.println(
                "Введите новый телефон, «очистить» для удаления " +
                        "или оставьте пустым:"
        );

        String phoneInput = inputReader.readLine();
        boolean clearPhone =
                phoneInput.equalsIgnoreCase("очистить");

        try {
            Client updated = clientService.updateClient(
                    clientId,
                    newName,
                    clearPhone ? null : phoneInput,
                    clearPhone
            );

            System.out.println("Клиент изменён: " + updated);
        } catch (ValidationException |
                 ClientNotFoundException e) {

            printError(e);
        }
    }

    public void searchClientByName() {
        System.out.println("Введите часть имени клиента:");
        String namePart = inputReader.readLine();

        try {
            List<Client> found =
                    clientService.findByName(namePart);

            if (found.isEmpty()) {
                System.out.println("Клиенты не найдены.");
                return;
            }

            System.out.println("\n=== Найденные клиенты ===");

            for (Client client : found) {
                System.out.println(client);
            }
        } catch (ValidationException e) {
            printError(e);
        }
    }

    private void printClients(List<Client> clients) {
        System.out.println("\n=== Список клиентов ===");

        for (Client client : clients) {
            System.out.println(client);
        }
    }

    private void printError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}