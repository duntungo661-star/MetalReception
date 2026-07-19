package metalreception;

import metalreception.console.*;
import metalreception.service.ClientService;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

public class Main {
    static void main() {
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
