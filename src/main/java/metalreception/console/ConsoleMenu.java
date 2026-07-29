package metalreception.console;

public class ConsoleMenu {

    private final ConsoleInputReader inputReader;
    private final ClientMenuHandler clientMenuHandler;
    private final MetalMenuHandler metalMenuHandler;
    private final ReceptionMenuHandler receptionMenuHandler;

    public ConsoleMenu(
            ConsoleInputReader inputReader,
            ClientMenuHandler clientMenuHandler,
            MetalMenuHandler metalMenuHandler,
            ReceptionMenuHandler receptionMenuHandler
    ) {
        this.inputReader = inputReader;
        this.clientMenuHandler = clientMenuHandler;
        this.metalMenuHandler = metalMenuHandler;
        this.receptionMenuHandler = receptionMenuHandler;
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();

            int choice = inputReader.readInt();

            switch (choice) {
                case 1 -> clientMenuHandler.addClient();
                case 2 -> metalMenuHandler.addMetal();
                case 3 -> receptionMenuHandler.createReception();

                case 4 -> clientMenuHandler.showAllClients();
                case 5 -> metalMenuHandler.showAllMetals();
                case 6 -> receptionMenuHandler.showAllReceptions();

                case 7 -> clientMenuHandler.deleteClient();
                case 8 -> metalMenuHandler.deleteMetal();

                case 9 -> clientMenuHandler.editClient();
                case 10 -> metalMenuHandler.editMetal();
                case 11 -> receptionMenuHandler.editReception();

                case 12 -> clientMenuHandler.searchClientByName();
                case 13 -> metalMenuHandler.searchMetalByName();
                case 14 -> receptionMenuHandler.searchReceptionByClient();

                case 15 -> receptionMenuHandler.showReceptionHistory();

                case 0 -> running = false;

                default -> System.out.println(
                        "Неизвестная команда. Попробуйте снова."
                );
            }
        }

        System.out.println("Программа завершена.");
    }

    private void printMenu() {
        System.out.println(
                "\n=== Пункт приёма металла ==="
        );

        System.out.println("1. Добавить клиента");
        System.out.println("2. Добавить металл");
        System.out.println("3. Создать приёмку металла");

        System.out.println("4. Показать клиентов");
        System.out.println("5. Показать металлы");
        System.out.println("6. Показать приёмки");

        System.out.println("7. Удалить клиента");
        System.out.println("8. Удалить металл");

        System.out.println("9. Изменить клиента");
        System.out.println("10. Изменить металл");
        System.out.println("11. Исправить вес приёмки");

        System.out.println("12. Найти клиента по имени");
        System.out.println("13. Найти металл по названию");
        System.out.println("14. Найти приёмки клиента");

        System.out.println(
                "15. Показать историю изменений приёмки"
        );

        System.out.println("0. Выход");
        System.out.println("Выберите действие:");
    }
}