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

    public MetalMenuHandler(MetalService metalService,  ConsoleInputReader inputReader) {
        this.metalService = metalService;
        this.inputReader = inputReader;
    }

    public void addMetal() {
        System.out.println("Добавьте наименование металла: ");
        String name = inputReader.readLine();

        System.out.println("Введите цену за кг: ");
        BigDecimal pricePerKg = inputReader.readBigDecimal();

        try {
            Metal metal = metalService.addMetal(name, pricePerKg);
            System.out.println("Металл добавлен: " + metal);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void showAllMetals() {
        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println("Металлов пока нет.");
            return;
        }
        System.out.println("\n=== Список металлов ===");

        for (Metal metal : metals) {
            System.out.println(metal);
        }
    }

    public void deleteMetals() {
        showAllMetals();
        System.out.println();

        System.out.println("Введите id металла для удаления: ");
        int metalId = inputReader.readInt();

        try {
            metalService.deleteMetal(metalId);
            System.out.println("Металл с id=" + metalId + " удалён.");
        } catch (MetalNotFoundException | MetalInUseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void editMetal() {
        showAllMetals();
        System.out.println();

        System.out.println("Введите id для изменения: ");
        int metalId = inputReader.readInt();

        Metal metal;
        try {
            metal = metalService.getByIdOrThrow(metalId);
        } catch (MetalNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Текущее наименование: " + metal.getName());
        System.out.println("Введите новое название (или оставьте пустым чтобы не менять):");
        String newName = inputReader.readLine();

        System.out.println("Текущая цена за кг: " + metal.getPricePerKg());
        System.out.println("Хотите изменить цену? (да/нет): ");
        boolean changePrice = inputReader.readYesNo();

        BigDecimal newPrice = null;
        if (changePrice) {
            System.out.println("Введите цену за кг: ");
            newPrice = inputReader.readBigDecimal();
        }

        try {
            Metal updated = metalService.updateMetal(metalId, newName, newPrice);
            System.out.println("Металл изменён: " + updated);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void searchMetalByName() {
        System.out.println("Введите часть названия металла: ");
        String namePart = inputReader.readLine();

        if (namePart.isBlank()) {
            System.out.println("Строка поиска не может быть пустой.");
            return;
        }

        List<Metal> found = metalService.findByName(namePart);
        if (found.isEmpty()) {
            System.out.println("Металлы не найдены.");
            return;
        }

        System.out.println("\n=== Найденные металлы ===");
        for (Metal metal : found) {
            System.out.println(metal);
        }
    }
}
