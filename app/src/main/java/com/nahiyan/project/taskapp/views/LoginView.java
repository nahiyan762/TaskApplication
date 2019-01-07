package com.nahiyan.project.taskapp.views;

public interface LoginView {
    void loginSuccess(String message);
    void loginError(String message);
    void setProgressVisibility(boolean visibility);
    void showEmailValidationError();
    void showPasswordValidationError();
}
