package model;

import java.math.BigDecimal;

public class Bill {

    private final int sessionId;
    private final long billedHours;
    private final BigDecimal amount;

    public Bill(
            int sessionId,
            long billedHours,
            BigDecimal amount
    ) {
        this.sessionId = sessionId;
        this.billedHours = billedHours;
        this.amount = amount;
    }

    public int getSessionId() {
        return sessionId;
    }

    public long getBilledHours() {
        return billedHours;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Bill{" +
                "sessionId=" + sessionId +
                ", billedHours=" + billedHours +
                ", amount=" + amount +
                '}';
    }
}
