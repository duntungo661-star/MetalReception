package metalreception.console;

import metalreception.model.Metal;
import metalreception.model.Reception;
import metalreception.service.MetalService;
import metalreception.service.ReceptionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class MetalMenuHandler {
    private final MetalService metalService;
    private final ReceptionService receptionService;
    private final ConsoleInputReader inputReader;

    public MetalMenuHandler(MetalService metalService, ReceptionService receptionService,
                            ConsoleInputReader inputReader) {
        this.metalService = metalService;
        this.receptionService = receptionService;
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
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void showAllMetals() {
        List<Metal> metals = metalService.getAllMetals();

        if (metals.isEmpty()) {
            System.out.println("Металла пока нет.");
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

        Optional<Metal> metalResult = metalService.findById(metalId);
        if (metalResult.isEmpty()) {
            System.out.println("Металл с id=" + metalId + " не найден.");
            return;
        }

        List<Reception> metalReceptions = receptionService.findByMetalId(metalId);
        if (!metalReceptions.isEmpty()) {
            System.out.println("Нельзя удалить металл: у него есть " + metalReceptions.size() + " история.");
            return;
        }

        metalService.deleteById(metalId);
        System.out.println("Металл с id=" + metalId + " удалён.");
    }

    public void editMetal() {
        showAllMetals();
        System.out.println();

        System.out.println("Введите id для изменения: ");
        int metalId = inputReader.readInt();

        Optional<Metal> metalResult = metalService.findById(metalId);
        if (metalResult.isEmpty()) {
            System.out.println("Металл с таким id=" + metalId + " не найден.");
            return;
        }
        Metal metal = metalResult.get();

        System.out.println("Текущее наименование: " + metal.getName());
        System.out.println("Введите новое название (или оставьте пустым чтобы не менять):");
        String newName = inputReader.readLine();

        System.out.println("Текущая цена за кг: " + metal.getPricePerKg());
        System.out.println("Хотите изменить цену? (да/нет): ");
        String answer = inputReader.readLine();

        try {
            if (!newName.isBlank()) {
                metal.setName(newName);
            }
            if (answer.equalsIgnoreCase("да")) {
                System.out.println("Введите новую цену за кг: ");
                BigDecimal newPrice = inputReader.readBigDecimal();
                metal.setPricePerKg(newPrice);
            }
            System.out.println("Металл изменён: " + metal);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void searchMetalByName() {
        System.out.println("Введите часть названия металла: ");
        String namePart = inputReader.readLine();

        List<Metal> found = metalService.findByName(namePart);
        if (found.isEmpty()) {
            System.out.println("Металлы не найдены.");
            return;
        }

        System.out.println("\n=========Найденные металлы==========");
        for (Metal metal : found) {
            System.out.println(metal);
        }
    }
}
