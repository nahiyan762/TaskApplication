package com.nahiyan.project.taskapp.notifications;

public class Data {
    private String listId;
    private int ListOrTask;
    private int icon;
    private String body;
    private String title;
    private String sentId;

    public Data(String listId,int ListOrTask, int icon, String body, String title, String sentId) {
        this.listId = listId;
        this.ListOrTask = ListOrTask;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sentId = sentId;
    }

    public Data() {
    }

    public int getListOrTask() {
        return ListOrTask;
    }

    public String getListId() {
        return listId;
    }

    public int getIcon() {
        return icon;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getSentId() {
        return sentId;
    }
}
