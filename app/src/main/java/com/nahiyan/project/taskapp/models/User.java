package com.nahiyan.project.taskapp.models;

import java.io.Serializable;

public class User implements Serializable {
    private String email, id, username, imageUrl ;

    public User() {
    }

    public User(String email, String id, String username, String imageUrl) {
        this.email = email;
        this.id = id;
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
