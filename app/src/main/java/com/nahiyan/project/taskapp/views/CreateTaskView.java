package com.nahiyan.project.taskapp.views;

public interface CreateTaskView {
    void showValidationError(boolean b);

    void dataInsertionSuccessfull();

    void sendPushMessageFailed();
}
