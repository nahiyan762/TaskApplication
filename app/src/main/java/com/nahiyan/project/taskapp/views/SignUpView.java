package com.nahiyan.project.taskapp.views;

public interface SignUpView {
    void showValidationError(String message);
    void signUpSuccess(String message);
    void signUpError(String message);
    void setProgressVisibility(boolean visibility);
    void showPasswordValidationError(String s);
    void showEmailValidationError(String s);
}
