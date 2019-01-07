package com.nahiyan.project.taskapp.views;

import com.nahiyan.project.taskapp.models.User;

public interface ProfileFragmentView {
    void setUser(User user);
    void imageUploadSuccess();
    void imageUploadFailed();
}
