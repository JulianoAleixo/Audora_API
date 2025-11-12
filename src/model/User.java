package model;

import java.util.UUID;
import java.sql.Timestamp;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private Timestamp createdAt;

    public User() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public User(String id, String name, String email, String password, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
