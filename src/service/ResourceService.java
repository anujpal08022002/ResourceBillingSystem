package service;

import exception.BusinessException;
import model.Bill;
import model.Resource;
import model.Service;
import model.UsageSession;
import model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceService {

    private final Map<Integer, User> users = new HashMap<>();
    private final Map<Integer, Resource> resources = new HashMap<>();
    private final Map<Integer, UsageSession> sessions = new HashMap<>();
    private final Map<Integer, Bill> bills = new HashMap<>();

    private final BillingService billingService;

    private int nextSessionId = 1001;

    public ResourceService(BillingService billingService) {
        this.billingService = billingService;
    }

    // ---------------------------------------------------------
    // User management
    // ---------------------------------------------------------

    public void addUser(User user) {

        if (user == null) {
            throw new IllegalArgumentException(
                    "User cannot be null"
            );
        }

        if (users.containsKey(user.getId())) {
            throw new BusinessException(
                    "User already exists: " + user.getId()
            );
        }

        users.put(user.getId(), user);
    }

    public User getUser(int userId) {

        User user = users.get(userId);

        if (user == null) {
            throw new BusinessException(
                    "User not found: " + userId
            );
        }

        return user;
    }

    // ---------------------------------------------------------
    // Resource management
    // ---------------------------------------------------------

    public void addResource(Resource resource) {

        if (resource == null) {
            throw new IllegalArgumentException(
                    "Resource cannot be null"
            );
        }

        if (resources.containsKey(resource.getId())) {
            throw new BusinessException(
                    "Resource already exists: " +
                            resource.getId()
            );
        }

        resources.put(resource.getId(), resource);
    }

    public Resource getResource(int resourceId) {

        Resource resource = resources.get(resourceId);

        if (resource == null) {
            throw new BusinessException(
                    "Resource not found: " + resourceId
            );
        }

        return resource;
    }

    public List<Resource> getAllResources() {
        return new ArrayList<>(resources.values());
    }

    // ---------------------------------------------------------
    // Start usage
    // ---------------------------------------------------------

    public UsageSession startUsage(
            int userId,
            int resourceId,
            int serviceId
    ) {

        User user = getUser(userId);
        Resource resource = getResource(resourceId);

        Service service = resource.getServiceById(serviceId);

        if (service == null) {
            throw new BusinessException(
                    "Service " + serviceId +
                    " is not available for resource " +
                    resource.getName()
            );
        }

        // Check whether the user is already using this resource.
        if (hasActiveSession(userId, resourceId)) {
            throw new BusinessException(
                    "User " + user.getName() +
                    " is already using " +
                    resource.getName()
            );
        }

        // Check resource capacity.
        int activeUsers = getActiveUserCount(resourceId);

        if (activeUsers >= resource.getCapacity()) {
            throw new BusinessException(
                    "Resource capacity is full. " +
                    "Capacity: " + resource.getCapacity()
            );
        }

        UsageSession session = new UsageSession(
                nextSessionId++,
                userId,
                resourceId,
                serviceId,
                LocalDateTime.now()
        );

        sessions.put(session.getId(), session);

        return session;
    }

    // ---------------------------------------------------------
    // Stop usage
    // ---------------------------------------------------------

    public Bill stopUsage(int sessionId) {

        UsageSession session = sessions.get(sessionId);

        if (session == null) {
            throw new BusinessException(
                    "Usage session not found: " + sessionId
            );
        }

        if (!session.isActive()) {
            throw new BusinessException(
                    "Usage session has already been stopped"
            );
        }

        Resource resource = getResource(session.getResourceId());

        Service service = resource.getServiceById(
                session.getServiceId()
        );

        if (service == null) {
            throw new BusinessException(
                    "Service associated with this session no longer exists"
            );
        }

        session.stop(LocalDateTime.now());

        Bill bill = billingService.generateBill(
                session,
                service
        );

        bills.put(sessionId, bill);

        return bill;
    }

    // ---------------------------------------------------------
    // Capacity
    // ---------------------------------------------------------

    public int getActiveUserCount(int resourceId) {

        int count = 0;

        for (UsageSession session : sessions.values()) {

            if (session.getResourceId() == resourceId
                    && session.isActive()) {

                count++;
            }
        }

        return count;
    }

    // ---------------------------------------------------------
    // Active session check
    // ---------------------------------------------------------

    private boolean hasActiveSession(
            int userId,
            int resourceId
    ) {

        for (UsageSession session : sessions.values()) {

            if (session.getUserId() == userId
                    && session.getResourceId() == resourceId
                    && session.isActive()) {

                return true;
            }
        }

        return false;
    }

    // ---------------------------------------------------------
    // Session and bill access
    // ---------------------------------------------------------

    public List<UsageSession> getActiveSessions() {

        List<UsageSession> activeSessions = new ArrayList<>();

        for (UsageSession session : sessions.values()) {

            if (session.isActive()) {
                activeSessions.add(session);
            }
        }

        return activeSessions;
    }

    public List<Bill> getAllBills() {
        return new ArrayList<>(bills.values());
    }

    public UsageSession getSession(int sessionId) {

        UsageSession session = sessions.get(sessionId);

        if (session == null) {
            throw new BusinessException(
                    "Usage session not found: " + sessionId
            );
        }

        return session;
    }
}
