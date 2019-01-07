package com.nahiyan.project.taskapp.models;

public class UserListView {
    private UserLists userLists;
    private boolean isSelected;

    public UserListView(UserLists userLists, boolean isSelected) {
        this.userLists = userLists;
        this.isSelected = isSelected;
    }

    public UserLists getUserLists() {
        return userLists;
    }

    public boolean getSelected() {
        return isSelected;
    }

    public void setUserLists(UserLists userLists) {
        this.userLists = userLists;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
