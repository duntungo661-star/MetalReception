package metalreception.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import metalreception.exception.validation.InvalidChangeReasonException;
import metalreception.exception.validation.InvalidReceptionDataException;
import metalreception.exception.validation.InvalidWeightException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reception_changes")
public class ReceptionChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reception_id", nullable = false)
    private Reception reception;

    @Column(name = "old_weight", nullable = false, precision = 12, scale = 3)
    private BigDecimal oldWeight;

    @Column(name = "new_weight", nullable = false, precision = 12, scale = 3)
    private BigDecimal newWeight;

    @Column(name = "old_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal oldTotalPrice;

    @Column(name = "new_total_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal newTotalPrice;

    @Column(nullable = false)
    private String reason;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    protected ReceptionChange() {
        // Пустой конструктор нужен Hibernate — не использовать напрямую в коде.
    }

    public ReceptionChange(
            BigDecimal oldWeight,
            BigDecimal newWeight,
            BigDecimal oldTotalPrice,
            BigDecimal newTotalPrice,
            String reason
    ) {
        validateWeight(oldWeight, "Старый вес");
        validateWeight(newWeight, "Новый вес");
        validatePrice(oldTotalPrice, "Старая сумма");
        validatePrice(newTotalPrice, "Новая сумма");
        validateReason(reason);

        this.oldWeight = oldWeight;
        this.newWeight = newWeight;
        this.oldTotalPrice = oldTotalPrice;
        this.newTotalPrice = newTotalPrice;
        this.reason = reason.strip();
        this.changedAt = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Reception getReception() {
        return reception;
    }

    void setReception(Reception reception) {
        this.reception = reception;
    }

    public BigDecimal getOldWeight() {
        return oldWeight;
    }

    public BigDecimal getNewWeight() {
        return newWeight;
    }

    public BigDecimal getOldTotalPrice() {
        return oldTotalPrice;
    }

    public BigDecimal getNewTotalPrice() {
        return newTotalPrice;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    private void validateWeight(BigDecimal weight, String fieldName) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidWeightException(fieldName + " должен быть больше нуля.");
        }
    }

    private void validatePrice(BigDecimal price, String fieldName) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidReceptionDataException(fieldName + " должна быть больше нуля.");
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidChangeReasonException("Причина изменения не может быть пустой.");
        }
    }

    @Override
    public String toString() {
        return "ReceptionChange{" +
                "oldWeight=" + oldWeight +
                ", newWeight=" + newWeight +
                ", oldTotalPrice=" + oldTotalPrice +
                ", newTotalPrice=" + newTotalPrice +
                ", reason='" + reason + '\'' +
                ", changedAt=" + changedAt +
                '}';
    }
}