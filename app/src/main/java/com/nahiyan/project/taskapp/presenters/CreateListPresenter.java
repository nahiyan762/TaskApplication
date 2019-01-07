package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserLists;

import java.util.List;

public interface CreateListPresenter {
    void addListName(String list, List<User> selectedUsers);

    void editListName(String listName, List<User> selectedUsers, UserLists userLists);

    void searchUser(String newText);
}
