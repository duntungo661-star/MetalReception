package metalreception.console;

import metalreception.exception.business.MetalInUseException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.validation.ValidationException;
import metalreception.model.Metal;
import metalreception.service.MetalService;

import java.math.BigDecimal;
import java.util.List;

public class MetalMenuHandler {

    private final MetalService metalService;
    private final ConsoleInputReader inputReader;

    public MetalMenuHandler(
            MetalService metalService,
            ConsoleInputReader inputReader
    ) {
        this.metalService = metalService;
        this.inputReader = inputReader;
    }

    public void addMetal() {
        System.out.println("Введите наименование металла:");
        String name = inputReader.readLine();

        System.out.println("Введите цену за кг:");
        BigDecimal pricePerKg =
                inputReader.readBigDecimal();

        try {
            Metal metal =
                    metalService.addMetal(name, pricePerKg);

            System.out.println("Металл добавлен: " + metal);
        } catch (ValidationException e) {
            printError(e);
        }
    }

    public void showAllMetals() {
        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println("Металлов пока нет.");
            return;
        }

        printMetals(metals);
    }

    public void deleteMetal() {
        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println("Металлов пока нет.");
            return;
        }

        printMetals(metals);

        System.out.println();
        System.out.println("Введите ID металла для удаления:");
        int metalId = inputReader.readInt();

        try {
            metalService.deleteMetal(metalId);

            System.out.println(
                    "Металл с id=" + metalId + " удалён."
            );
        } catch (MetalNotFoundException |
                 MetalInUseException e) {

            printError(e);
        }
    }

    public void editMetal() {
        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println("Металлов пока нет.");
            return;
        }

        printMetals(metals);

        System.out.println();
        System.out.println("Введите ID металла для изменения:");
        int metalId = inputReader.readInt();

        Metal metal;

        try {
            metal = metalService.getByIdOrThrow(metalId);
        } catch (MetalNotFoundException e) {
            printError(e);
            return;
        }

        System.out.println(
                "Текущее наименование: " + metal.getName()
        );
        System.out.println(
                "Введите новое название или оставьте пустым:"
        );
        String newName = inputReader.readLine();

        System.out.println(
                "Текущая цена за кг: " + metal.getPricePerKg()
        );
        System.out.println("Хотите изменить цену? (да/нет):");
        boolean changePrice = inputReader.readYesNo();

        BigDecimal newPrice = null;

        if (changePrice) {
            System.out.println("Введите новую цену за кг:");
            newPrice = inputReader.readBigDecimal();
        }

        try {
            Metal updated = metalService.updateMetal(
                    metalId,
                    newName,
                    newPrice
            );

            System.out.println("Металл изменён: " + updated);
        } catch (ValidationException |
                 MetalNotFoundException e) {

            printError(e);
        }
    }

    public void searchMetalByName() {
        System.out.println("Введите часть названия металла:");
        String namePart = inputReader.readLine();

        try {
            List<Metal> found =
                    metalService.findByName(namePart);

            if (found.isEmpty()) {
                System.out.println("Металлы не найдены.");
                return;
            }

            System.out.println("\n=== Найденные металлы ===");

            for (Metal metal : found) {
                System.out.println(metal);
            }
        } catch (ValidationException e) {
            printError(e);
        }
    }

    private void printMetals(List<Metal> metals) {
        System.out.println("\n=== Список металлов ===");

        for (Metal metal : metals) {
            System.out.println(metal);
        }
    }

    private void printError(Exception e) {
        System.out.println("Ошибка: " + e.getMessage());
    }
}