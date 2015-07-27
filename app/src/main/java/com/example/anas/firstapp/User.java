package com.example.anas.firstapp;

/**
 * Created by Anas on 26/7/15.
 */
public class User {
    private int id;
    private String username;
    private String age;
    private String genre;

    public User() {
    }

    public User(int id, String username, String age, String genre) {
        this.id = id;
        this.username = username;
        this.age = age;
        this.genre = genre;
    }

    public User(String username, String age, String genre) {
        this.username = username;
        this.age = age;
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
