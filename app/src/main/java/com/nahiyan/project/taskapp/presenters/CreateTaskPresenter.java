package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.notifications.APIService;

public interface CreateTaskPresenter {
    void setTask(UserTasks userTasks);
}
