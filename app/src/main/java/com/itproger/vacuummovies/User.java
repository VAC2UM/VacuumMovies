package com.itproger.vacuummovies;

public class User {
    String email, username;
    boolean superUser;

    public User() {
    }

    public User(String email, String username, boolean superUser) {
        this.email = email;
        this.username = username;
        this.superUser = superUser;
    }

    public User(String email, boolean superUser) {
        this.email = email;
        this.superUser = superUser;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
