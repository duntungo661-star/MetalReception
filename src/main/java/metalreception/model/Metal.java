package metalreception.model;

import metalreception.exception.validation.InvalidIdException;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPriceException;

import java.math.BigDecimal;

public class Metal {

    private final int id;
    private String name;
    private BigDecimal pricePerKg;

    public Metal(int id, String name, BigDecimal pricePerKg) {
        validateId(id);
        validateName(name);
        validatePrice(pricePerKg);

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
        validateName(name);
        this.name = name.strip();
    }

    public void setPricePerKg(BigDecimal pricePerKg) {
        validatePrice(pricePerKg);
        this.pricePerKg = pricePerKg;
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new InvalidIdException(
                    "ID металла должен быть больше нуля."
            );
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidNameException(
                    "Наименование металла не может быть пустым."
            );
        }
    }

    private void validatePrice(BigDecimal pricePerKg) {
        if (pricePerKg == null ||
                pricePerKg.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidPriceException(
                    "Цена за кг должна быть больше нуля."
            );
        }
    }

    @Override
    public String toString() {
        return "Metal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pricePerKg=" + pricePerKg +
                '}';
    }
}