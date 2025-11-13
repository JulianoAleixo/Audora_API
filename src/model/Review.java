package model;

import java.sql.Timestamp;
import java.util.UUID;

public class Review {
    private String id;
    private int rating;
    private String comment;
    private Timestamp date;
    private String users;

    public Review() {
        this.id = UUID.randomUUID().toString();
        this.date = new Timestamp(System.currentTimeMillis());
    }

    public Review(String id, int rating, String comment, Timestamp date, String users) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.date = date;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }
}
