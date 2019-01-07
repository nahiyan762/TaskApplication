package com.nahiyan.project.taskapp.presenters.implementation;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Patterns;

import com.nahiyan.project.taskapp.presenters.SignUpPresenter;
import com.nahiyan.project.taskapp.views.SignUpView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nahiyan.project.taskapp.presenters.SignUpPresenter;
import com.nahiyan.project.taskapp.views.SignUpView;

import java.util.HashMap;

public class SignUpPresenterImpl implements SignUpPresenter {

    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    SignUpView signUpView;

    public SignUpPresenterImpl(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    @Override
    public void signUp(final String name, final String email, String password) {
        if(TextUtils.isEmpty(name)){
            signUpView.showValidationError("Username Required!");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() || TextUtils.isEmpty(email)){
            signUpView.showEmailValidationError("Valid Email Required!");
        } else if(password.length() < 6 || TextUtils.isEmpty(password)){
            signUpView.showPasswordValidationError("Password at least 6 characters Required!");
        } else {
            signUpView.setProgressVisibility(true);
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        signUpView.setProgressVisibility(false);
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;
                        String userId = firebaseUser.getUid();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id",userId);
                        hashMap.put("username",name);
                        hashMap.put("email",email);
                        hashMap.put("imageUrl","default");

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                signUpView.signUpSuccess("SignUp Successful");
                            }
                        });
                    } else{
                        signUpView.signUpError("You can't register with this email or password");
                        signUpView.setProgressVisibility(false);
                    }
                    }
                });
        }

    }

    @Override
    public void attachView(SignUpView signUpView) {
        this.signUpView = signUpView;
    }
}
