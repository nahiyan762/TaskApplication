package com.nahiyan.project.taskapp.models;

public class UserMessage {
    private String message;
    private User sender;
    private User receiver;
    private UserTasks userTasks;

    public UserMessage() {
    }

    public UserMessage(String message, User sender, User receiver, UserTasks userTasks) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.userTasks = userTasks;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public UserTasks getUserTasks() {
        return userTasks;
    }

    public void setUserTasks(UserTasks userTasks) {
        this.userTasks = userTasks;
    }
}
