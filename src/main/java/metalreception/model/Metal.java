package metalreception.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import metalreception.exception.validation.InvalidNameException;
import metalreception.exception.validation.InvalidPriceException;


import java.math.BigDecimal;

@Entity
@Table (name = "metals")
public class Metal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "price_per_kg", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerKg;

    protected Metal() {

    }

    public Metal(String name, BigDecimal pricePerKg) {
        validateName(name);
        validatePrice(pricePerKg);

        this.name = name.strip();
        this.pricePerKg = pricePerKg;
    }

    public Integer getId() {
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