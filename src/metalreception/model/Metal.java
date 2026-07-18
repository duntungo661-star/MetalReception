package metalreception.model;

import java.math.BigDecimal;

public class Metal {
    private final int id;
    private String name;
    private BigDecimal pricePerKg;

    public Metal(int id, String name, BigDecimal pricePerKg) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID металла должен быть больше нуля.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Наименование металла не может быть пустым.");
        }
        if (pricePerKg == null || pricePerKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена за кг должна быть больше нуля.");
        }
        this.id = id;
        this.name = name.strip();
        this.pricePerKg = pricePerKg;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPricePerKg() {
        return pricePerKg;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Наименование металла не может быть пустым.");
        }
        this.name = name.strip();
    }

    public void setPricePerKg(BigDecimal pricePerKg) {
        if (pricePerKg == null || pricePerKg.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Цена за кг должна быть больше нуля.");
        }
        this.pricePerKg = pricePerKg;
    }

    @Override
    public String toString() {
        return "Metal{id=" + id + ", name='" + name + "', pricePerKg=" + pricePerKg + "}";
    }
}
