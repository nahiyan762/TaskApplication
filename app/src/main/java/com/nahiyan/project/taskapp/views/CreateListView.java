package com.nahiyan.project.taskapp.views;

import com.nahiyan.project.taskapp.models.User;

public interface CreateListView {
    void setUsers(User userArrayList);
    void showValidationError(String s);
    void insertListSuccess();
}
