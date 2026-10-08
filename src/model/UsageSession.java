package model;

import java.time.LocalDateTime;

public class UsageSession {

    private final int id;
    private final int userId;
    private final int resourceId;
    private final int serviceId;

    private final LocalDateTime startTime;
    private LocalDateTime endTime;

    public UsageSession(
            int id,
            int userId,
            int resourceId,
            int serviceId,
            LocalDateTime startTime
    ) {
        this.id = id;
        this.userId = userId;
        this.resourceId = resourceId;
        this.serviceId = serviceId;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean isActive() {
        return endTime == null;
    }

    public void stop(LocalDateTime endTime) {

        if (!isActive()) {
            throw new IllegalStateException(
                    "Usage session has already been stopped"
            );
        }

        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException(
                    "End time cannot be before start time"
            );
        }

        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "UsageSession{" +
                "id=" + id +
                ", userId=" + userId +
                ", resourceId=" + resourceId +
                ", serviceId=" + serviceId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", active=" + isActive() +
                '}';
    }
}
