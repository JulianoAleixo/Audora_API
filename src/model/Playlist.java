package model;

import java.util.UUID;

public class Playlist {
    private String id;
    private String title;
    private String description;
    private String users;

    public Playlist() {
        this.id = UUID.randomUUID().toString();
    }

    public Playlist(String id, String title, String description, String users) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
