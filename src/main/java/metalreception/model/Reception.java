package metalreception.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import metalreception.exception.validation.InvalidChangeReasonException;
import metalreception.exception.validation.InvalidReceptionDataException;
import metalreception.exception.validation.InvalidWeightException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receptions")
public class Reception {

    private static final int MONEY_SCALE = 2;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metal_id", nullable = false)
    private Metal metal;

    @Column(nullable = false, precision = 12, scale = 3)
    private BigDecimal weight;

    @Column(name = "total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "price_per_kg_at_reception", nullable = false, precision = 12, scale = 2)
    private BigDecimal pricePerKgAtReception;

    @OneToMany(mappedBy = "reception", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ReceptionChange> changes = new ArrayList<>();

    protected Reception() {}

    public Reception(Client client, Metal metal, BigDecimal weight, LocalDate date) {
        validateClient(client);
        validateMetal(metal);
        validateWeight(weight);
        validateDate(date);

        this.client = client;
        this.metal = metal;
        this.weight = weight;
        this.date = date;
        this.pricePerKgAtReception = metal.getPricePerKg();
        this.totalPrice = calculateTotalPrice(weight);
    }

    public Integer getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public Metal getMetal() {
        return metal;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getPricePerKgAtReception() {
        return pricePerKgAtReception;
    }

    public List<ReceptionChange> getChanges() {
        return new ArrayList<>(changes);
    }

    public void correctWeight(BigDecimal newWeight, String reason) {
        validateWeight(newWeight);
        validateReason(reason);

        if (weight.compareTo(newWeight) == 0) {
            throw new InvalidWeightException(
                    "Новый вес должен отличаться от текущего."
            );
        }

        BigDecimal oldWeight = this.weight;
        BigDecimal oldTotalPrice = this.totalPrice;
        BigDecimal newTotalPrice = calculateTotalPrice(newWeight);

        ReceptionChange change = new ReceptionChange(
                oldWeight,
                newWeight,
                oldTotalPrice,
                newTotalPrice,
                reason
        );
        change.setReception(this);

        this.weight = newWeight;
        this.totalPrice = newTotalPrice;

        changes.add(change);
    }

    private void validateClient(Client client) {
        if (client == null) {
            throw new InvalidReceptionDataException("Клиент не может быть null.");
        }
    }

    private void validateMetal(Metal metal) {
        if (metal == null) {
            throw new InvalidReceptionDataException("Металл не может быть null.");
        }
    }

    private void validateWeight(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWeightException("Вес должен быть больше нуля.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReceptionDataException("Дата приёмки не может быть null.");
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidChangeReasonException("Необходимо указать причину изменения.");
        }
    }

    private BigDecimal calculateTotalPrice(BigDecimal weight) {
        return weight.multiply(pricePerKgAtReception)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return "Reception{" +
                "id=" + id +
                ", client=" + client.getName() +
                ", metal=" + metal.getName() +
                ", weight=" + weight +
                ", pricePerKg=" + pricePerKgAtReception +
                ", totalPrice=" + totalPrice +
                ", date=" + date +
                ", changesCount=" + changes.size() +
                '}';
    }
}