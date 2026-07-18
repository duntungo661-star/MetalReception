package metalreception.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reception {
    private final int id;
    private final Client client;
    private final Metal metal;
    private BigDecimal weight;
    private BigDecimal totalPrice;
    private final LocalDate date;
    private final BigDecimal pricePerKgAtReception;

    public Reception(int id, Client client, Metal metal, BigDecimal weight, LocalDate date) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID приёма металла должен быть больше нуля.");
        }
        if (client == null) {
            throw new IllegalArgumentException("Клиент не может быть null.");
        }
        if (metal == null) {
            throw new IllegalArgumentException("Металл не может быть null.");
        }
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Вес должен быть больше нуля.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Дата приёма металла не может быть null");
        }
        this.id = id;
        this.client = client;
        this.metal = metal;
        this.weight = weight;
        this.pricePerKgAtReception = metal.getPricePerKg();
        this.totalPrice = weight.multiply(pricePerKgAtReception);
        this.date = date;
    }

    public int getId() { return id; }
    public Client getClient() { return client; }
    public Metal getMetal() { return metal; }
    public BigDecimal getWeight() { return weight; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public LocalDate getDate() { return date; }
    public BigDecimal getPricePerKgAtReception() { return pricePerKgAtReception; }

    public void setWeight(BigDecimal weight) {
        if (weight == null || weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Вес должен быть больше нуля.");
        }
        this.weight = weight;
        this.totalPrice = weight.multiply(pricePerKgAtReception);
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
                '}';
    }
}

