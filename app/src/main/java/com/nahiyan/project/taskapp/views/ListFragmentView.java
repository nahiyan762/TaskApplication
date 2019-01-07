package com.nahiyan.project.taskapp.views;

import android.view.View;
import android.widget.ImageView;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;

import java.util.ArrayList;
import java.util.List;

public interface ListFragmentView {
    void getList(ArrayList<UserLists> userListsArrayList);
    void onItemClick(View view, int position, ImageView iv_listEdit);
    void onItemLongClick(View view, int position, ImageView iv_listEdit);
    void setUser(User users);
    void deleteSuccessful();
}
