package metalreception.model;

import metalreception.exception.validation.InvalidChangeReasonException;
import metalreception.exception.validation.InvalidReceptionDataException;
import metalreception.exception.validation.InvalidWeightException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReceptionChange {

    private final BigDecimal oldWeight;
    private final BigDecimal newWeight;

    private final BigDecimal oldTotalPrice;
    private final BigDecimal newTotalPrice;

    private final String reason;
    private final LocalDateTime changedAt;

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
        if (weight == null ||
                weight.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidWeightException(
                    fieldName + " должен быть больше нуля."
            );
        }
    }

    private void validatePrice(BigDecimal price, String fieldName) {
        if (price == null ||
                price.compareTo(BigDecimal.ZERO) <= 0) {

            throw new InvalidReceptionDataException(
                    fieldName + " должна быть больше нуля."
            );
        }
    }

    private void validateReason(String reason) {
        if (reason == null || reason.isBlank()) {
            throw new InvalidChangeReasonException(
                    "Причина изменения не может быть пустой."
            );
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