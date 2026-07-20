package main.java.metalreception;

import main.java.metalreception.console.*;
import main.java.metalreception.service.ClientService;
import main.java.metalreception.service.MetalService;
import main.java.metalreception.service.ReceptionService;

public class Main {
    public static void main(String[] args) {
        ReceptionService receptionService = new ReceptionService();
        ConsoleInputReader inputReader = new ConsoleInputReader();
        ClientService clientService = new ClientService(receptionService);
        MetalService metalService = new MetalService(receptionService);

        ClientMenuHandler clientMenuHandler = new ClientMenuHandler(clientService, inputReader);
        MetalMenuHandler metalMenuHandler = new MetalMenuHandler(metalService, inputReader);
        ReceptionMenuHandler receptionMenuHandler = new ReceptionMenuHandler(receptionService, inputReader,
                metalService, clientService);

        ConsoleMenu menu = new ConsoleMenu(inputReader, clientMenuHandler, metalMenuHandler, receptionMenuHandler);
        menu.run();
    }
}
