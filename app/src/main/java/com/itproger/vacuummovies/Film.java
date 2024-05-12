package com.itproger.vacuummovies;

public class Film {
    private String name;
    private String year;
    private String director;
    private String dataImage;
    private String key;
    private String description;

    public Film() {
    }

    public Film(String name, String year, String director, String dataImage, String description) {
        this.name = name;
        this.year = year;
        this.director = director;
        this.dataImage = dataImage;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getDataImage() {
        return dataImage;
    }

    public void setDataImage(String dataImage) {
        this.dataImage = dataImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
