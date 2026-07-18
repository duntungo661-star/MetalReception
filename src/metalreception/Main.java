package metalreception;

import metalreception.console.*;
import metalreception.service.ClientService;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

public class Main {
    public static void main(String[] args) {
        ClientService clientService = new ClientService();
        MetalService metalService = new MetalService();
        ReceptionService receptionService = new ReceptionService();
        ConsoleInputReader inputReader = new ConsoleInputReader();

        ClientMenuHandler clientMenuHandler = new ClientMenuHandler(clientService, receptionService, inputReader);
        MetalMenuHandler metalMenuHandler = new MetalMenuHandler(metalService, receptionService, inputReader);
        ReceptionMenuHandler receptionMenuHandler = new ReceptionMenuHandler(receptionService, inputReader,
                metalService, clientService, clientMenuHandler, metalMenuHandler);

        ConsoleMenu menu = new ConsoleMenu(inputReader, clientMenuHandler, metalMenuHandler, receptionMenuHandler);
        menu.run();
    }
}
