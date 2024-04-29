package com.itproger.vacuummovies;

public class Film {
    String name, date, director;

    public Film() {
    }

    public Film(String name, String date, String director) {
        this.name = name;
        this.date = date;
        this.director = director;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
