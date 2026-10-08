package model;

public class User {

    private final int id;
    private final String name;

    public User(int id, String name) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be greater than zero"
            );
        }

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "User name cannot be empty"
            );
        }

        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}