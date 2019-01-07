package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.views.ListFragmentView;
import com.nahiyan.project.taskapp.models.UserListView;

import java.util.ArrayList;

public interface ListPresenter {
    void getTaskLists();

    void updateToken(String token);

    ListFragmentView getListFragmentView();

    void deleteUserLists(ArrayList<UserListView> selectedList);

    void getUser(String userId);

    void searchList(String s);
}
