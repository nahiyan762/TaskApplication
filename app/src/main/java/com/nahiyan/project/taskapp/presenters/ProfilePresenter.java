package com.nahiyan.project.taskapp.presenters;

import android.net.Uri;

public interface ProfilePresenter {
    void getUser(String userId);

    void uploadImage(Uri imageUri, String fileExtension);
}
