package com.nahiyan.project.taskapp.presenters;

import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.models.UserTasks;
import com.nahiyan.project.taskapp.models.UserTasks;

public interface TaskListPresenter {
    void getTaskData(String list);

    void setStatus(UserTasks userTasks);

    void getDoneTaskData(String listId);

    void getPauseTaskData(String listId);

    void getProgressTaskData(String listId);

    void deleteTask(UserTasks userTasks);
}
