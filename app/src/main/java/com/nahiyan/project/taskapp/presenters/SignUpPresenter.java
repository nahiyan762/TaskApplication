package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.views.SignUpView;

public interface SignUpPresenter {
    void signUp(String name, String email, String password);
    void attachView(SignUpView signUpView);
}
