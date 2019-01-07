package com.nahiyan.project.taskapp.views.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.presenters.LoginPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.LoginPresenterImpl;
import com.nahiyan.project.taskapp.views.LoginView;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements LoginView, View.OnClickListener {

    private LoginPresenter loginPresenter ;
    private ProgressBar progressBar;
    private MaterialEditText met_email;
    private MaterialEditText met_password;
    private Button btn_register,btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);

        met_email = findViewById(R.id.met_email);
        met_password = findViewById(R.id.met_password);
        progressBar = findViewById(R.id.progressBar);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        loginPresenter = new LoginPresenterImpl(auth);
        loginPresenter.attachView(this);
        loginPresenter.checkLogin();

    }

    @Override
    public void loginSuccess(String message) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginError(String message) {
        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.main_layout), message, Snackbar.LENGTH_SHORT)
                .setActionTextColor(Color.RED);
        snackbar.show();
        buttonClickable(true);
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        if (visibility){
            progressBar.setVisibility(View.VISIBLE);
            buttonClickable(false);
        } else{
            progressBar.setVisibility(View.GONE);
            buttonClickable(true);
        }
    }

    @Override
    public void showEmailValidationError() {
        met_email.setError("Email required!");
        buttonClickable(true);
    }

    @Override
    public void showPasswordValidationError() {
        met_password.setError("Password required!");
        buttonClickable(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_register){
            buttonClickable(false);
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.btn_login){
            buttonClickable(false);
            String email_text = Objects.requireNonNull(met_email.getText()).toString().trim();
            String password_text = Objects.requireNonNull(met_password.getText()).toString().trim();
            loginPresenter.login(email_text, password_text);
        }
    }

    private void buttonClickable(boolean b){
        btn_login.setClickable(b);
        btn_register.setClickable(b);
    }
}
