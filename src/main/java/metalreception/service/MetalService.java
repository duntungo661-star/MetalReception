package metalreception.service;

import metalreception.exception.business.MetalInUseException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Metal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class MetalService {

    private final List<Metal> metals = new ArrayList<>();
    private final UsageChecker usageChecker;

    private int nextId = 1;

    public MetalService(UsageChecker usageChecker) {
        if (usageChecker == null) {
            throw new IllegalArgumentException(
                    "UsageChecker не может быть null."
            );
        }

        this.usageChecker = usageChecker;
    }

    public Metal addMetal(String name, BigDecimal pricePerKg) {
        Metal metal = new Metal(nextId, name, pricePerKg);
        metals.add(metal);
        nextId++;
        return metal;
    }

    public List<Metal> getAllMetals() {
        return new ArrayList<>(metals);
    }

    public Optional<Metal> findById(int id) {
        for (Metal metal : metals) {
            if (metal.getId() == id) {
                return Optional.of(metal);
            }
        }
        return Optional.empty();
    }

    public Metal getByIdOrThrow(int id) {
        return findById(id)
                .orElseThrow(() -> new MetalNotFoundException(
                        "Металл с id=" + id + " не найден."
                ));
    }

    public List<Metal> findByName(String namePart) {
        if (namePart == null || namePart.isBlank()) {
            throw new InvalidNameException(
                    "Строка поиска металла не может быть пустой."
            );
        }

        String normalizedNamePart =
                namePart.strip().toLowerCase(Locale.ROOT);

        List<Metal> result = new ArrayList<>();

        for (Metal metal : metals) {
            String metalName =
                    metal.getName().toLowerCase(Locale.ROOT);

            if (metalName.contains(normalizedNamePart)) {
                result.add(metal);
            }
        }

        return result;
    }

    public Metal updateMetal(int id, String newName, BigDecimal newPrice) {
        Metal metal = getByIdOrThrow(id);
        if (newName != null && !newName.isBlank()) {
            metal.setName(newName);
        }
        if (newPrice != null) {
            metal.setPricePerKg(newPrice);
        }
        return metal;
    }

    public void deleteMetal(int id) {
        Metal metal = getByIdOrThrow(id);

        if (usageChecker.isMetalInUse(id)) {
            throw new MetalInUseException(
                    "Нельзя удалить металл с id=" + id +
                            ": он есть в истории приёмок."
            );
        }

        metals.remove(metal);
    }
}