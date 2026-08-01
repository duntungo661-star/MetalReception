package metalreception.service;

import metalreception.exception.business.MetalInUseException;
import metalreception.exception.notfound.MetalNotFoundException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.model.Metal;
import metalreception.repository.MetalRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Service
public class MetalService {

    private final MetalRepository metalRepository;
    private final UsageChecker usageChecker;

    public MetalService(MetalRepository metalRepository, UsageChecker usageChecker) {
        this.metalRepository = metalRepository;
        this.usageChecker = usageChecker;
    }

    public Metal addMetal(String name, BigDecimal pricePerKg) {
        Metal metal = new Metal(name, pricePerKg);
        return metalRepository.save(metal);
    }

    public List<Metal> getAllMetals() {
        return metalRepository.findAll();
    }

    public Metal getByIdOrThrow(int id) {
        return metalRepository.findById(id)
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

        return metalRepository.findAll().stream()
                .filter(metal -> metal.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(namePart.strip().toLowerCase(Locale.ROOT)))
                .toList();
    }

    public Metal updateMetal(int id, String newName, BigDecimal newPrice) {
        Metal metal = getByIdOrThrow(id);

        if (newName != null && !newName.isBlank()) {
            metal.setName(newName);
        }
        if (newPrice != null) {
            metal.setPricePerKg(newPrice);
        }

        return metalRepository.save(metal);
    }

    public void deleteMetal(int id) {
        Metal metal = getByIdOrThrow(id);

        if (usageChecker.isMetalInUse(id)) {
            throw new MetalInUseException(
                    "Нельзя удалить металл с id=" + id +
                            ": он есть в истории приёмок."
            );
        }

        metalRepository.delete(metal);
    }
}