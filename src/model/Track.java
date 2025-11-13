package model;

import java.util.UUID;

public class Track {
    private String id;
    private String title;
    private int duration;
    private String genres;
    private String albums;

    public Track() {
        this.id = UUID.randomUUID().toString();
    }

    public Track(String id, String title, int duration, String genres, String albums) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.genres = genres;
        this.albums = albums;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }
}
