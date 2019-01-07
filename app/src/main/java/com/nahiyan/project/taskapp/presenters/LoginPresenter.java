package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.views.LoginView;
import com.nahiyan.project.taskapp.views.LoginView;

public interface LoginPresenter {
    void login(String email, String password);
    void checkLogin();
    void attachView(LoginView view);
}
