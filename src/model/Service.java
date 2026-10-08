package model;

import java.math.BigDecimal;

public class Service {

    private final int id;
    private final String name;
    private final BigDecimal firstHourPrice;
    private final BigDecimal additionalHourPrice;

    public Service(
            int id,
            String name,
            BigDecimal firstHourPrice,
            BigDecimal additionalHourPrice
    ) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Service ID must be greater than zero"
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Service name cannot be empty"
            );
        }

        if (firstHourPrice == null || firstHourPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "First-hour price cannot be negative"
            );
        }

        if (additionalHourPrice == null || additionalHourPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Additional-hour price cannot be negative"
            );
        }

        this.id = id;
        this.name = name;
        this.firstHourPrice = firstHourPrice;
        this.additionalHourPrice = additionalHourPrice;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getFirstHourPrice() {
        return firstHourPrice;
    }

    public BigDecimal getAdditionalHourPrice() {
        return additionalHourPrice;
    }

    @Override
    public String toString() {
        return "Service{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", firstHourPrice=" + firstHourPrice +
                ", additionalHourPrice=" + additionalHourPrice +
                '}';
    }
}