package com.nahiyan.project.taskapp.models;

import java.io.Serializable;
import java.util.List;

public class UserLists implements Serializable {
    private String listId;
    private String listName;
    private User listOwner;
    private List<User> listAssignUser;

    public UserLists() {
    }

    public UserLists(String listId, String listName, User listOwner, List<User> listAssignUser) {
        this.listId = listId;
        this.listName = listName;
        this.listOwner = listOwner;
        this.listAssignUser = listAssignUser;
    }

    public String getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public User getListOwner() {
        return listOwner;
    }

    public List<User> getListAssignUser() {
        return listAssignUser;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public void setListOwner(User listOwner) {
        this.listOwner = listOwner;
    }

    public void setListAssignUser(List<User> listAssignUser) {
        this.listAssignUser = listAssignUser;
    }
}
