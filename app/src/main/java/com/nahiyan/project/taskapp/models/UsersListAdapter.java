package com.nahiyan.project.taskapp.models;

public class UsersListAdapter {
    boolean isSelected;
    User user;

    public UsersListAdapter() {
    }

    public UsersListAdapter(boolean isSelected, User user) {
        this.isSelected = isSelected;
        this.user = user;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
