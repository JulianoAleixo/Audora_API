package model;

import java.util.UUID;

public class Album {
    private String id;
    private String title;
    private int releaseYear;
    private String coverUrl;
    private String artists;
    private String genres;

    public Album() {
        this.id = UUID.randomUUID().toString();
    }

    public Album(String id, String title, int releaseYear, String coverUrl, String artists, String genres) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.coverUrl = coverUrl;
        this.artists = artists;
        this.genres = genres;
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }
}
