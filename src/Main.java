import exception.BusinessException;
import model.Bill;
import model.Resource;
import model.Service;
import model.UsageSession;
import model.User;
import service.BillingService;
import service.ResourceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static ResourceService resourceService;

    public static void main(String[] args) {

        initializeSystem();

        runApplication();

        scanner.close();
    }

    // =========================================================
    // INITIALIZE SAMPLE DATA
    // =========================================================

    private static void initializeSystem() {

        BillingService billingService = new BillingService();

        resourceService = new ResourceService(billingService);

        // -----------------------------------------------------
        // Users
        // -----------------------------------------------------

        resourceService.addUser(
                new User(101, "Anuj")
        );

        resourceService.addUser(
                new User(102, "Rahul")
        );

        resourceService.addUser(
                new User(103, "Amit")
        );

        // -----------------------------------------------------
        // Services
        // -----------------------------------------------------

        Service meetingRoomService = new Service(
                1,
                "Meeting Room Hourly",
                new BigDecimal("30"),
                new BigDecimal("10")
        );

        Service workstationService = new Service(
                2,
                "Workstation Hourly",
                new BigDecimal("50"),
                new BigDecimal("20")
        );

        // -----------------------------------------------------
        // Resources
        // -----------------------------------------------------

        Resource meetingRoom = new Resource(
                1,
                "Meeting Room A",
                2
        );

        meetingRoom.addService(meetingRoomService);

        Resource workstation = new Resource(
                2,
                "Workstation A",
                1
        );

        workstation.addService(workstationService);

        resourceService.addResource(meetingRoom);
        resourceService.addResource(workstation);
    }

    // =========================================================
    // MAIN APPLICATION LOOP
    // =========================================================

    private static void runApplication() {

        boolean running = true;

        while (running) {

            printMenu();

            int choice = readInt("Enter choice: ");

            System.out.println();

            try {

                switch (choice) {

                    case 1:
                        listResources();
                        break;

                    case 2:
                        startUsage();
                        break;

                    case 3:
                        stopUsage();
                        break;

                    case 4:
                        viewActiveSessions();
                        break;

                    case 5:
                        viewBills();
                        break;

                    case 6:
                        running = false;
                        System.out.println(
                                "Thank you for using the system!"
                        );
                        break;

                    default:
                        System.out.println(
                                "Invalid choice. Please select 1-6."
                        );
                }

            } catch (BusinessException | IllegalArgumentException e) {

                System.out.println(
                        "ERROR: " + e.getMessage()
                );
            }

            System.out.println();
        }
    }

    // =========================================================
    // MENU
    // =========================================================

    private static void printMenu() {

        System.out.println();
        System.out.println("========================================");
        System.out.println("   RESOURCE USAGE & BILLING SYSTEM");
        System.out.println("========================================");
        System.out.println("1. List Resources");
        System.out.println("2. Start Usage");
        System.out.println("3. Stop Usage");
        System.out.println("4. View Active Sessions");
        System.out.println("5. View Bills");
        System.out.println("6. Exit");
        System.out.println("========================================");
    }

    // =========================================================
    // LIST RESOURCES
    // =========================================================

    private static void listResources() {

        List<Resource> resources =
                resourceService.getAllResources();

        System.out.println("AVAILABLE RESOURCES");
        System.out.println("----------------------------------------");

        for (Resource resource : resources) {

            int activeUsers =
                    resourceService.getActiveUserCount(
                            resource.getId()
                    );

            System.out.println(
                    "ID       : " + resource.getId()
            );

            System.out.println(
                    "Name     : " + resource.getName()
            );

            System.out.println(
                    "Capacity : " +
                            activeUsers +
                            "/" +
                            resource.getCapacity()
            );

            System.out.println("Services :");

            for (Service service : resource.getServices()) {

                System.out.println(
                        "  [" +
                                service.getId() +
                                "] " +
                                service.getName() +
                                " | First Hour: ₹" +
                                service.getFirstHourPrice() +
                                " | Additional Hour: ₹" +
                                service.getAdditionalHourPrice()
                );
            }

            System.out.println("----------------------------------------");
        }
    }

    // =========================================================
    // START USAGE
    // =========================================================

    private static void startUsage() {

        System.out.println("START USAGE");
        System.out.println("----------------------------------------");

        int userId = readInt("Enter User ID: ");
        int resourceId = readInt("Enter Resource ID: ");
        int serviceId = readInt("Enter Service ID: ");

        UsageSession session =
                resourceService.startUsage(
                        userId,
                        resourceId,
                        serviceId
                );

        System.out.println();
        System.out.println("Usage started successfully!");
        System.out.println(
                "Session ID : " + session.getId()
        );
        System.out.println(
                "Start Time : " + session.getStartTime()
        );
    }

    // =========================================================
    // STOP USAGE
    // =========================================================

    private static void stopUsage() {

        System.out.println("STOP USAGE");
        System.out.println("----------------------------------------");

        int sessionId = readInt(
                "Enter Session ID: "
        );

        Bill bill =
                resourceService.stopUsage(sessionId);

        UsageSession session =
                resourceService.getSession(sessionId);

        Resource resource =
                resourceService.getResource(
                        session.getResourceId()
                );

        User user =
                resourceService.getUser(
                        session.getUserId()
                );

        System.out.println();
        System.out.println("========================================");
        System.out.println("                 BILL");
        System.out.println("========================================");

        System.out.println(
                "Session ID      : " + session.getId()
        );

        System.out.println(
                "User            : " +
                        user.getName()
        );

        System.out.println(
                "Resource        : " +
                        resource.getName()
        );

        System.out.println(
                "Start Time      : " +
                        session.getStartTime()
        );

        System.out.println(
                "End Time        : " +
                        session.getEndTime()
        );

        System.out.println(
                "Billed Duration : " +
                        bill.getBilledHours() +
                        " hour(s)"
        );

        System.out.println(
                "Total Amount    : ₹" +
                        bill.getAmount()
        );

        System.out.println("========================================");
    }

    // =========================================================
    // ACTIVE SESSIONS
    // =========================================================

    private static void viewActiveSessions() {

        List<UsageSession> sessions =
                resourceService.getActiveSessions();

        System.out.println("ACTIVE SESSIONS");
        System.out.println("----------------------------------------");

        if (sessions.isEmpty()) {

            System.out.println(
                    "No active sessions."
            );

            return;
        }

        for (UsageSession session : sessions) {

            User user =
                    resourceService.getUser(
                            session.getUserId()
                    );

            Resource resource =
                    resourceService.getResource(
                            session.getResourceId()
                    );

            System.out.println(
                    "Session ID : " +
                            session.getId()
            );

            System.out.println(
                    "User       : " +
                            user.getName()
            );

            System.out.println(
                    "Resource   : " +
                            resource.getName()
            );

            System.out.println(
                    "Started    : " +
                            session.getStartTime()
            );

            System.out.println("----------------------------------------");
        }
    }

    // =========================================================
    // BILLS
    // =========================================================

    private static void viewBills() {

        List<Bill> bills =
                resourceService.getAllBills();

        System.out.println("GENERATED BILLS");
        System.out.println("----------------------------------------");

        if (bills.isEmpty()) {

            System.out.println(
                    "No bills generated yet."
            );

            return;
        }

        for (Bill bill : bills) {

            System.out.println(
                    "Session ID : " +
                            bill.getSessionId()
            );

            System.out.println(
                    "Billed Hours : " +
                            bill.getBilledHours()
            );

            System.out.println(
                    "Amount       : ₹" +
                            bill.getAmount()
            );

            System.out.println("----------------------------------------");
        }
    }

    // =========================================================
    // INPUT
    // =========================================================

    private static int readInt(String message) {

        System.out.print(message);

        while (!scanner.hasNextInt()) {

            System.out.println(
                    "Please enter a valid number."
            );

            scanner.next();

            System.out.print(message);
        }

        return scanner.nextInt();
    }
}