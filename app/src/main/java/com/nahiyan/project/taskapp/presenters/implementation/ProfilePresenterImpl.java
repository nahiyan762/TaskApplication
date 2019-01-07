package com.nahiyan.project.taskapp.presenters.implementation;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.presenters.ProfilePresenter;
import com.nahiyan.project.taskapp.views.ProfileFragmentView;
import com.nahiyan.project.taskapp.views.activitys.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.nahiyan.project.taskapp.presenters.ProfilePresenter;
import com.nahiyan.project.taskapp.views.ProfileFragmentView;
import com.nahiyan.project.taskapp.views.activitys.ProfileFragment;

import java.util.HashMap;
import java.util.Objects;

import static io.fabric.sdk.android.Fabric.TAG;

public class ProfilePresenterImpl implements ProfilePresenter {

    private ProfileFragmentView profileFragmentView;
    private FirebaseAuth mAuth;

    public ProfilePresenterImpl(ProfileFragment profileFragment, FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        this.profileFragmentView = profileFragment;
    }

    @Override
    public void getUser(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.keepSynced(true);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                profileFragmentView.setUser(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void uploadImage(Uri imageUri, String fileExtension) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");
        StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+fileExtension);
        StorageTask uploadTask = fileReference.putFile(imageUri);
        uploadTask.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if(!task.isSuccessful()){
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    String imageUri = Objects.requireNonNull(task.getResult()).toString();

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
                    reference.keepSynced(true);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageUrl",imageUri);
                    reference.updateChildren(hashMap);
                    profileFragmentView.imageUploadSuccess();
                }else{
                    profileFragmentView.imageUploadFailed();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profileFragmentView.imageUploadFailed();
            }
        });
    }
}
