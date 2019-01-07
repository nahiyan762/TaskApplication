package com.nahiyan.project.taskapp.views.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.presenters.SignUpPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.SignUpPresenterImpl;
import com.nahiyan.project.taskapp.views.SignUpView;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity implements SignUpView, View.OnClickListener {

    private SignUpPresenter signUpPresenter ;
    MaterialEditText met_username, met_email, met_password;
    ProgressBar progressBar;
    Button btn_register;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.app_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        met_username = findViewById(R.id.met_username);
        met_email = findViewById(R.id.met_email);
        met_password = findViewById(R.id.met_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        signUpPresenter = new SignUpPresenterImpl(mAuth);
        signUpPresenter.attachView(this);

    }

    @Override
    public void showValidationError(String message) {
        met_username.setError(message);
        buttonClickable(true);
    }

    @Override
    public void signUpSuccess(String message) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void signUpError(String message) {
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
    public void showPasswordValidationError(String message) {
        met_password.setError(message);
        buttonClickable(true);
    }

    @Override
    public void showEmailValidationError(String message) {
        met_email.setError(message);
        buttonClickable(true);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn_register){
            buttonClickable(false);
            String name = Objects.requireNonNull(met_username.getText()).toString().trim();
            String email = Objects.requireNonNull(met_email.getText()).toString().trim();
            String password = Objects.requireNonNull(met_password.getText()).toString().trim();
            signUpPresenter.signUp(name,email,password);

        }
    }

    private void buttonClickable(boolean b){
        btn_register.setClickable(b);
    }
}
