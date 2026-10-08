package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Resource {

    private final int id;
    private final String name;
    private final int capacity;

    private final List<Service> services = new ArrayList<>();

    public Resource(int id, String name, int capacity) {

        if (capacity <= 0) {
            throw new IllegalArgumentException(
                    "Resource capacity must be greater than zero"
            );
        }

        this.id = id;
        this.name = name;
        this.capacity = capacity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void addService(Service service) {

        if (service == null) {
            throw new IllegalArgumentException(
                    "Service cannot be null"
            );
        }

        if (getServiceById(service.getId()) != null) {
            throw new IllegalArgumentException(
                    "Service already exists: " +
                            service.getId()
            );
        }

        services.add(service);
    }

    public List<Service> getServices() {
        return Collections.unmodifiableList(services);
    }

    public Service getServiceById(int serviceId) {

        for (Service service : services) {
            if (service.getId() == serviceId) {
                return service;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}
