package model;

import java.util.UUID;

public class Artist {
    private String id;
    private String name;
    private String country;
    private String mainGenre;

    public Artist() {
        this.id = UUID.randomUUID().toString();
    }

    public Artist(String id, String name, String country, String mainGenre) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.mainGenre = mainGenre;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getMainGenre() { return mainGenre; }
    public void setMainGenre(String mainGenre) { this.mainGenre = mainGenre; }
}
