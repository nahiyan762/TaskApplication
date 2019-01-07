package com.nahiyan.project.taskapp.views;

import com.nahiyan.project.taskapp.models.UserMessage;

import java.util.ArrayList;

public interface MessageView {
    void validationError();

    void insertMessageSuccess();

    void setMessage(ArrayList<UserMessage> userMessages);

    void sendPushMessageSuccess();

    void sendPushMessageFailed();
}
