import model.Bill;
import model.Service;
import model.UsageSession;
import service.BillingService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BillingTest {

    public static void main(String[] args) {

        BillingService billingService = new BillingService();

        Service service = new Service(
                1,
                "Meeting Room Hourly",
                new BigDecimal("30"),
                new BigDecimal("10")
        );

        LocalDateTime start =
                LocalDateTime.of(2026, 1, 1, 10, 0);

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(20),
                1,
                new BigDecimal("30")
        );

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(60),
                1,
                new BigDecimal("30")
        );

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(61),
                2,
                new BigDecimal("40")
        );

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(80),
                2,
                new BigDecimal("40")
        );

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(120),
                2,
                new BigDecimal("40")
        );

        runTest(
                billingService,
                service,
                start,
                start.plusMinutes(121),
                3,
                new BigDecimal("50")
        );

        System.out.println();
        System.out.println("All billing tests completed successfully!");
    }

    private static void runTest(
            BillingService billingService,
            Service service,
            LocalDateTime start,
            LocalDateTime end,
            long expectedHours,
            BigDecimal expectedAmount
    ) {

        UsageSession session = new UsageSession(
                1001,
                101,
                1,
                1,
                start
        );

        session.stop(end);

        Bill bill = billingService.generateBill(
                session,
                service
        );

        boolean hoursPassed =
                bill.getBilledHours() == expectedHours;

        boolean amountPassed =
                bill.getAmount().compareTo(expectedAmount) == 0;

        if (!hoursPassed || !amountPassed) {
            throw new AssertionError(
                    "Test failed! " +
                            "Expected: " +
                            expectedHours +
                            " hours, ₹" +
                            expectedAmount +
                            " | Actual: " +
                            bill.getBilledHours() +
                            " hours, ₹" +
                            bill.getAmount()
            );
        }

        System.out.println(
                "PASS | Usage: " +
                        java.time.Duration.between(start, end).toMinutes() +
                        " min | Billed: " +
                        bill.getBilledHours() +
                        " hour(s) | Amount: ₹" +
                        bill.getAmount()
        );
    }
}
