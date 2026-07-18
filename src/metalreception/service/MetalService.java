package metalreception.service;

import metalreception.model.Metal;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MetalService {

    private final List<Metal> metals = new ArrayList<>();
    private int nextId = 1;

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

    public boolean deleteById(int id) {
        return metals.removeIf(metal -> metal.getId() == id);
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
}
