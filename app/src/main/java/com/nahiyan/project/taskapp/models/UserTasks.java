package com.nahiyan.project.taskapp.models;

import java.io.Serializable;

public class UserTasks implements Serializable {
    private String taskName;
    private String taskDate;
    private String taskTime;
    private User taskAssign;
    private String taskNote;
    private String taskPriority;
    private String taskStatus;
    private UserLists userLists;
    private String taskOwner;
    private String taskId;

    public UserTasks() {
    }

    public UserTasks(String taskName, String taskDate, String taskTime, User taskAssign, String taskNote,
                     String taskPriority, String taskStatus, UserLists userLists, String taskOwner, String taskId) {

        this.taskName = taskName;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskAssign = taskAssign;
        this.taskNote = taskNote;
        this.taskPriority = taskPriority;
        this.taskStatus = taskStatus;
        this.userLists = userLists;
        this.taskOwner = taskOwner;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public User getTaskAssign() {
        return taskAssign;
    }

    public void setTaskAssign(User taskAssign) {
        this.taskAssign = taskAssign;
    }

    public String getTaskNote() {
        return taskNote;
    }

    public void setTaskNote(String taskNote) {
        this.taskNote = taskNote;
    }

    public String getTaskPriority() {
        return taskPriority;
    }

    public void setTaskPriority(String taskPriority) {
        this.taskPriority = taskPriority;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public UserLists getTaskUserLists() {
        return userLists;
    }

    public void setTaskUserLists(UserLists userLists) {
        this.userLists = userLists;
    }

    public String getTaskOwner() {
        return taskOwner;
    }

    public void setTaskOwner(String taskOwner) {
        this.taskOwner = taskOwner;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
