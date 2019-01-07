package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserTasks;

public interface MessagePresenter {
    void sendMessage(String message, User senderUser, User receiverUser, UserTasks userTasks);

    void getMessage(User senderUser, User receiverUser, UserTasks userTasks);
}
