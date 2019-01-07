package com.nahiyan.project.taskapp.models;

import java.io.Serializable;
import java.util.ArrayList;

public class TaskList implements Serializable {
    private String listId;
    private String listName;
    private String listOwner;
    private ArrayList<String> listAssignUsers;

    public TaskList(String listId, String listName, String listOwner, ArrayList<String> listAssignUsers) {
        this.listId = listId;
        this.listName = listName;
        this.listOwner = listOwner;
        this.listAssignUsers = listAssignUsers;
    }

    public String getListId() {
        return listId;
    }

    public String getListName() {
        return listName;
    }

    public String getListOwner() {
        return listOwner;
    }

    public ArrayList<String> getListAssignUsers() {
        return listAssignUsers;
    }
}
