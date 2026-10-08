package service;

import model.Bill;
import model.Service;
import model.UsageSession;

import java.math.BigDecimal;
import java.time.Duration;

public class BillingService {

    /**
     * Generates a bill for a completed usage session.
     *
     * Billing rules:
     * - Up to 1 hour is charged at the first-hour price.
     * - Every additional billed hour is charged at the additional-hour price.
     * - Any usage beyond a complete hour is rounded up.
     */
    public Bill generateBill(UsageSession session, Service service) {

        if (session == null) {
            throw new IllegalArgumentException("Usage session cannot be null");
        }

        if (service == null) {
            throw new IllegalArgumentException("Service cannot be null");
        }

        if (session.isActive()) {
            throw new IllegalStateException(
                    "Cannot generate a bill for an active usage session"
            );
        }

        long usageMinutes = Duration.between(
                session.getStartTime(),
                session.getEndTime()
        ).toMinutes();

        // Round up to the next complete hour.
        long billedHours = (usageMinutes + 59) / 60;

        // A session that starts and stops immediately still costs 1 hour.
        if (billedHours == 0) {
            billedHours = 1;
        }

        BigDecimal amount;

        if (billedHours == 1) {
            amount = service.getFirstHourPrice();
        } else {
            BigDecimal additionalHours = BigDecimal.valueOf(billedHours - 1);

            amount = service.getFirstHourPrice()
                    .add(
                            service.getAdditionalHourPrice()
                                    .multiply(additionalHours)
                    );
        }

        return new Bill(
                session.getId(),
                billedHours,
                amount
        );
    }
}
