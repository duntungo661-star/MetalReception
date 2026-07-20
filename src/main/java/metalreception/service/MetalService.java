package main.java.metalreception.service;

import main.java.metalreception.exception.business.MetalInUseException;
import main.java.metalreception.exception.notfound.MetalNotFoundException;
import main.java.metalreception.model.Metal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetalService {

    private final List<Metal> metals = new ArrayList<>();
    private int nextId = 1;
    private final UsageChecker usageChecker;

    public MetalService(UsageChecker usageChecker) {
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

    public List<Metal> findByName(String namePart) {
        List<Metal> result = new ArrayList<>();
        for (Metal metal : metals) {
            if (metal.getName().toLowerCase().contains(namePart.toLowerCase()))
            {
                result.add(metal);
            }
        }
        return result;
    }

    public Metal getByIdOrThrow(int id) {
        return findById(id).orElseThrow(() -> new MetalNotFoundException("Металл с id=" + id + " не найден."));
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
        getByIdOrThrow(id);
        if (usageChecker.isMetalInUse(id)) {
            throw new MetalInUseException("Нельзя удалить металл с id=" + id + ": он есть в истории приёмок.");
        }
        metals.removeIf(metal -> metal.getId() == id);
    }
}
