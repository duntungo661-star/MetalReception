package metalreception.console;

import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.notfound.ReceptionNotFoundException;
import metalreception.exception.validation.ValidationException;
import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.service.ClientService;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

import java.math.BigDecimal;
import java.util.List;

public class ReceptionMenuHandler {
    private final ReceptionService receptionService;
    private final ConsoleInputReader inputReader;
    private final MetalService metalService;
    private final ClientService clientService;

    public ReceptionMenuHandler(ReceptionService receptionService, ConsoleInputReader inputReader,
                                MetalService metalService, ClientService clientService) {
        this.receptionService = receptionService;
        this.inputReader = inputReader;
        this.metalService = metalService;
        this.clientService = clientService;
    }

    public void createReception() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty()) {
            System.out.println("Сначала добавьте хотя бы одного клиента.");
            return;
        }
        List<Metal> metals = metalService.getAllMetals();
        if (metals.isEmpty()) {
            System.out.println("Сначала добавьте хотя бы один металл.");
            return;
        }
        printClients(clients);
        System.out.println();
        System.out.println("Введите id клиента: ");
        int clientId = inputReader.readInt();

        Client client;
        try {
            client = clientService.getByIdOrThrow(clientId);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        printMetals(metals);
        System.out.println();
        System.out.println("Введите id металла: ");
        int metalId = inputReader.readInt();

        Metal metal;
        try {
            metal = metalService.getByIdOrThrow(metalId);
        } catch (MetalNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Введите вес (кг): ");
        BigDecimal weight = inputReader.readBigDecimal();

        try {
            Reception reception = receptionService.createReception(client, metal, weight);
            System.out.println("Приёмка создана: " + reception);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printClients(List<Client> clients) {
        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }
        System.out.println("\n=== Список клиентов ===");
        for (Client client : clients) {
            System.out.println(client);
        }
    }

    private void printMetals(List<Metal> metals) {
        if (metals.isEmpty()) {
            System.out.println("Металлов пока нет.");
            return;
        }
        System.out.println("\n=== Список металлов ===");
        for (Metal metal : metals) {
            System.out.println(metal);
        }
    }

    public void showAllReceptions() {
        List<Reception> receptions = receptionService.getAllReceptions();

        if (receptions.isEmpty()) {
            System.out.println("Приёмок пока нет.");
            return;
        }
        System.out.println("\n=== Список приёма металла ===");

        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }

    public void editReception() {
        showAllReceptions();
        System.out.println();

        System.out.println("Введите id приёмки для изменения: ");
        int receptionId = inputReader.readInt();

        Reception current;
        try {
            current = receptionService.getByIdOrThrow(receptionId);
        } catch (ReceptionNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Текущий вес: " + current.getWeight());
        System.out.println("Введите новый вес приёмки: ");
        BigDecimal newWeight = inputReader.readBigDecimal();

        try {
            Reception updated = receptionService.updateReceptionWeight(receptionId, newWeight);
            System.out.println("Приёмка изменена: " + updated);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void searchReceptionByClient() {
        printClients(clientService.getAllClients());
        System.out.println();

        System.out.println("Введите id клиента: ");
        int clientId = inputReader.readInt();

        try {
            clientService.getByIdOrThrow(clientId);
        } catch (ClientNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        List<Reception> receptions = receptionService.findByClientId(clientId);
        if (receptions.isEmpty()) {
            System.out.println("У этого клиента приёмок не найдено.");
            return;
        }

        System.out.println("\n=== Приёмки клиента ===");
        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }
}
