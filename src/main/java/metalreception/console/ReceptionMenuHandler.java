package metalreception.console;

import metalreception.exception.notfound.ClientNotFoundException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.notfound.ReceptionNotFoundException;
import metalreception.exception.validation.ValidationException;
import metalreception.model.Client;
import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.model.ReceptionChange;
import metalreception.service.ClientService;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReceptionMenuHandler {

    private static final DateTimeFormatter CHANGE_DATE_FORMAT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final ReceptionService receptionService;
    private final ConsoleInputReader inputReader;
    private final MetalService metalService;
    private final ClientService clientService;

    public ReceptionMenuHandler(
            ReceptionService receptionService,
            ConsoleInputReader inputReader,
            MetalService metalService,
            ClientService clientService
    ) {
        this.receptionService = receptionService;
        this.inputReader = inputReader;
        this.metalService = metalService;
        this.clientService = clientService;
    }

    public void createReception() {
        List<Client> clients = clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println(
                    "Сначала добавьте хотя бы одного клиента."
            );
            return;
        }

        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println(
                    "Сначала добавьте хотя бы один металл."
            );
            return;
        }

        printClients(clients);

        System.out.println();
        System.out.println("Введите ID клиента:");
        int clientId = inputReader.readInt();

        Client client;

        try {
            client = clientService.getByIdOrThrow(clientId);
        } catch (ClientNotFoundException e) {
            printError(e);
            return;
        }

        printMetals(metals);

        System.out.println();
        System.out.println("Введите ID металла:");
        int metalId = inputReader.readInt();

        Metal metal;

        try {
            metal = metalService.getByIdOrThrow(metalId);
        } catch (MetalNotFoundException e) {
            printError(e);
            return;
        }

        System.out.println("Введите вес в кг:");
        BigDecimal weight = inputReader.readBigDecimal();

        try {
            Reception reception =
                    receptionService.createReception(
                            client,
                            metal,
                            weight
                    );

            System.out.println(
                    "Приёмка создана: " + reception
            );
        } catch (ValidationException e) {
            printError(e);
        }
    }

    public void showAllReceptions() {
        List<Reception> receptions =
                receptionService.getAllReceptions();

        if (receptions.isEmpty()) {
            System.out.println("Приёмок пока нет.");
            return;
        }

        printReceptions(receptions);
    }

    public void editReception() {
        List<Reception> receptions =
                receptionService.getAllReceptions();

        if (receptions.isEmpty()) {
            System.out.println("Приёмок пока нет.");
            return;
        }

        printReceptions(receptions);

        System.out.println();
        System.out.println(
                "Введите ID приёмки для изменения:"
        );
        int receptionId = inputReader.readInt();

        Reception current;

        try {
            current =
                    receptionService.getByIdOrThrow(receptionId);
        } catch (ReceptionNotFoundException e) {
            printError(e);
            return;
        }

        System.out.println(
                "Текущий вес: " + current.getWeight() + " кг"
        );
        System.out.println("Введите новый вес:");
        BigDecimal newWeight =
                inputReader.readBigDecimal();

        System.out.println("Введите причину изменения:");
        String reason = inputReader.readLine();

        try {
            Reception updated =
                    receptionService.correctReceptionWeight(
                            receptionId,
                            newWeight,
                            reason
                    );

            System.out.println(
                    "Приёмка изменена: " + updated
            );
        } catch (ValidationException |
                 ReceptionNotFoundException e) {

            printError(e);
        }
    }

    public void showReceptionHistory() {
        List<Reception> receptions =
                receptionService.getAllReceptions();

        if (receptions.isEmpty()) {
            System.out.println("Приёмок пока нет.");
            return;
        }

        printReceptions(receptions);

        System.out.println();
        System.out.println(
                "Введите ID приёмки для просмотра истории:"
        );
        int receptionId = inputReader.readInt();

        try {
            Reception reception =
                    receptionService.getByIdOrThrow(receptionId);

            List<ReceptionChange> changes =
                    receptionService.getReceptionChanges(
                            receptionId
                    );

            System.out.println(
                    "\n=== История приёмки №" +
                            reception.getId() + " ==="
            );

            if (changes.isEmpty()) {
                System.out.println(
                        "У этой приёмки пока нет изменений."
                );
                return;
            }

            for (int i = 0; i < changes.size(); i++) {
                printChange(i + 1, changes.get(i));
            }
        } catch (ReceptionNotFoundException e) {
            printError(e);
        }
    }

    public void searchReceptionByClient() {
        List<Client> clients =
                clientService.getAllClients();

        if (clients.isEmpty()) {
            System.out.println("Клиентов пока нет.");
            return;
        }

        printClients(clients);

        System.out.println();
        System.out.println("Введите ID клиента:");
        int clientId = inputReader.readInt();

        try {
            clientService.getByIdOrThrow(clientId);
        } catch (ClientNotFoundException e) {
            printError(e);
            return;
        }

        List<Reception> receptions =
                receptionService.findByClientId(clientId);

        if (receptions.isEmpty()) {
            System.out.println(
                    "У этого клиента приёмок не найдено."
            );
            return;
        }

        System.out.println("\n=== Приёмки клиента ===");

        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }

    private void printClients(List<Client> clients) {
        System.out.println("\n=== Список клиентов ===");

        for (Client client : clients) {
            System.out.println(client);
        }
    }

    private void printMetals(List<Metal> metals) {
        System.out.println("\n=== Список металлов ===");

        for (Metal metal : metals) {
            System.out.println(metal);
        }
    }

    private void printReceptions(
            List<Reception> receptions
    ) {
        System.out.println("\n=== Список приёмок ===");

        for (Reception reception : receptions) {
            System.out.println(reception);
        }
    }

    private void printChange(
            int number,
            ReceptionChange change
    ) {
        System.out.println();
        System.out.println("Изменение №" + number);
        System.out.println(
                "Дата: " +
                        change.getChangedAt()
                                .format(CHANGE_DATE_FORMAT)
        );
        System.out.println(
                "Вес до изменения: " +
                        change.getOldWeight() + " кг"
        );
        System.out.println(
                "Вес после изменения: " +
                        change.getNewWeight() + " кг"
        );
        System.out.println(
                "Сумма до изменения: " +
                        change.getOldTotalPrice()
        );
        System.out.println(
                "Сумма после изменения: " +
                        change.getNewTotalPrice()
        );
        System.out.println(
                "Причина: " + change.getReason()
        );
    }

    private void printError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}