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

    public ClientMenuHandler(ClientService clientService, ConsoleInputReader inputReader)  {
        this.clientService = clientService;
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
        } catch (ValidationException e) {
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

        try {
            clientService.deleteClient(clientId);
            System.out.println("Клиент с id=" + clientId + " удалён.");
        } catch (ClientNotFoundException | ClientInUseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editClient() {
        showAllClients();
        System.out.println();

        System.out.println("Введите id для изменения: ");
        int clientId = inputReader.readInt();

       Client client;
       try {
           client = clientService.getByIdOrThrow(clientId);
       } catch (ClientNotFoundException e) {
           System.out.println(e.getMessage());
           return;
       }

        System.out.println("Текущее имя: " + client.getName());
        System.out.println("Введите новое имя (или оставьте пустым чтобы не менять): ");
        String newName = inputReader.readLine();

        System.out.println("Текущий телефон: " + (client.getPhone() == null ? "не указан" : client.getPhone()));
        System.out.println("Введите новый телефон, 'очистить' чтобы удалить телефон, \" +\n" +
                "                \"или оставьте пустым, чтобы не менять: ");
        String phoneInput = inputReader.readLine();
        boolean clearPhone = phoneInput.equalsIgnoreCase("очистить");

        try {
            Client updated = clientService.updateClient(clientId, newName, clearPhone ? null : phoneInput, clearPhone);
            System.out.println("Клиент изменен: " + updated);
        } catch (ValidationException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void searchClientByName() {
        System.out.println("Введите часть имени клиента: ");
        String namePart = inputReader.readLine();

        if (namePart.isBlank()) {
            System.out.println("Строка поиска не может быть пустой.");
            return;
        }

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
