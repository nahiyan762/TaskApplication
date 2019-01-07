package com.nahiyan.project.taskapp.presenters.implementation;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.nahiyan.project.taskapp.presenters.LoginPresenter;
import com.nahiyan.project.taskapp.views.LoginView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.views.LoginView;

public class LoginPresenterImpl implements LoginPresenter {

    private FirebaseAuth auth ;
    private LoginView loginView ;

    public LoginPresenterImpl(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void login(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            loginView.showEmailValidationError();
        } else if(TextUtils.isEmpty(password)){
            loginView.showPasswordValidationError();
        } else {
            loginView.setProgressVisibility(true);

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) loginView, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        loginView.setProgressVisibility(false);
                        if(!task.isSuccessful()) {
                            loginView.loginError("Login Error ! ");
                        } else {
                            loginView.loginSuccess("Login Success !");
                        }
                    }
                });
        }
    }

    @Override
    public void checkLogin() {
        if(auth.getCurrentUser() != null){
            loginView.loginSuccess("Login Success !");
        }
    }

    @Override
    public void attachView(LoginView view) {
        loginView = view ;
    }
}
