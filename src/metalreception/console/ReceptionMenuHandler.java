package metalreception.console;

import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.service.ClientService;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ReceptionMenuHandler {
    private final ReceptionService receptionService;
    private final ConsoleInputReader inputReader;
    private final MetalService metalService;
    private final ClientService clientService;
    private final ClientMenuHandler clientMenuHandler;
    private final MetalMenuHandler metalMenuHandler;

    public ReceptionMenuHandler(ReceptionService receptionService, ConsoleInputReader inputReader,
                                MetalService metalService, ClientService clientService, ClientMenuHandler clientMenuHandler,
                                MetalMenuHandler metalMenuHandler) {
        this.receptionService = receptionService;
        this.inputReader = inputReader;
        this.metalService = metalService;
        this.clientService = clientService;
        this.clientMenuHandler = clientMenuHandler;
        this.metalMenuHandler = metalMenuHandler;
    }

    public void createReception() {
        if (clientService.getAllClients().isEmpty()) {
            System.out.println("Сначала добавьте хотя бы одного клиента.");
            return;
        }
        if (metalService.getAllMetals().isEmpty()) {
            System.out.println("Сначала добавьте хотя бы один металл.");
            return;
        }
        clientMenuHandler.showAllClients();
        System.out.println();

        System.out.println("Введите id клиента: ");
        int clientId = inputReader.readInt();

        Optional<Client> clientResult = clientService.findById(clientId);
        if (clientResult.isEmpty()) {
            System.out.println("Клиент с id=" + clientId + " не найден.");
            return;
        }
        Client client = clientResult.get();

        metalMenuHandler.showAllMetals();
        System.out.println();

        System.out.println("Введите id металла: ");
        int metalId = inputReader.readInt();

        Optional<Metal> metalResult = metalService.findById(metalId);
        if (metalResult.isEmpty()) {
            System.out.println("Металл с id=" + metalId + " не найден.");
            return;
        }
        Metal metal = metalResult.get();

        System.out.println("Введите вес(кг): ");
        BigDecimal weight = inputReader.readBigDecimal();

        try {
            Reception reception = receptionService.createReception(client, metal, weight);
            System.out.println("Приёмка создана: " + reception);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }


    public void showAllReceptions() {
        List<Reception> receptions = receptionService.getAllReceptions();

        if (receptions.isEmpty()) {
            System.out.println("Приемок пока нет.");
            return;
        }
        System.out.println("\n=== Список приема металла ===");

        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }

    public void editReception() {
        showAllReceptions();
        System.out.println();

        System.out.println("Введите id приёмки для изменения: ");
        int receptionId = inputReader.readInt();

        Optional<Reception> receptionResult = receptionService.findById(receptionId);
        if (receptionResult.isEmpty()) {
            System.out.println("Приёмка с таким id=" + receptionId + " не найден.");
            return;
        }
        Reception reception = receptionResult.get();

        System.out.println("Текущий вес: " + reception.getWeight());
        System.out.println("Введите новый вес приёмки: ");
        BigDecimal newWeight = inputReader.readBigDecimal();

        try {
            reception.setWeight(newWeight);
            System.out.println("Приёмка изменена: " + reception);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void searchReceptionByClient() {
        clientMenuHandler.showAllClients();
        System.out.println();

        System.out.println("Введите id клиента: ");
        int clientId = inputReader.readInt();

        Optional<Client> clientResult = clientService.findById(clientId);
        if (clientResult.isEmpty()) {
            System.out.println("Клиент с id=" + clientId + " не найден.");
            return;
        }

        List<Reception> receptions = receptionService.findByClientId(clientId);
        if (receptions.isEmpty()) {
            System.out.println("У этого клиента приёмок не найдено.");
            return;
        }

        System.out.println("\n=========Приёмки клиента==========");
        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }
}
