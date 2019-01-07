package com.nahiyan.project.taskapp.views.activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.adapter.MyListAdapter;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserListView;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.presenters.ListPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.ListPresenterImpl;
import com.nahiyan.project.taskapp.views.ListFragmentView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nahiyan.project.taskapp.models.UserListView;

import java.util.ArrayList;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

// RecyclerItemTouchHelper.RecyclerItemTouchHelperListener

public class ListFragment extends Fragment implements ListFragmentView, View.OnClickListener{

    public boolean isInActionMood;
    private RecyclerView recyclerViewList;
    private MyListAdapter myListAdapter;
    private Toolbar toolbar;
    private CircleImageView iv_profileImage;
    private TextView tv_totalListSelectedItem,tv_profileName;
    private ArrayList<UserListView> selectedList;
    private int counter;
    private ArrayList<UserListView> userListViews;
    private FirebaseAuth mAuth;
    private ListPresenter listPresenter;
    private ProgressDialog progressDialog;
    private User users;
    private AdView mAdView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //set Banner Ad
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Loading");
        this.progressDialog.show();

        mAuth = FirebaseAuth.getInstance();
        userListViews = new ArrayList<>();
        selectedList = new ArrayList<>();
        counter = 0;
        isInActionMood = false;

        this.listPresenter = new ListPresenterImpl(ListFragment.this, mAuth);
        listPresenter.updateToken(FirebaseInstanceId.getInstance().getToken());
        listPresenter.getTaskLists();
        listPresenter.getUser(mAuth.getCurrentUser().getUid());

        //toolbar data initialization
        toolbar = view.findViewById(R.id.toolbar);
        tv_totalListSelectedItem = view.findViewById(R.id.tv_totalListSelectedItem);
        iv_profileImage = view.findViewById(R.id.iv_profileImage);
        iv_profileImage.setOnClickListener(this);

        tv_profileName = view.findViewById(R.id.tv_profileName);
        tv_profileName.setOnClickListener(this);

        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        tv_totalListSelectedItem.setVisibility(View.GONE);

        ImageView actionButton = view.findViewById(R.id.fab_add_list);
        actionButton.setOnClickListener(this);

        recyclerViewList = view.findViewById(R.id.recyclerViewList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false);
        recyclerViewList.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void getList(ArrayList<UserLists> userListsArrayList) {
        for(UserLists userLists: userListsArrayList){
            UserListView userListView = new UserListView(userLists,false);
            userListViews.add(userListView);
        }
        myListAdapter = new MyListAdapter(getContext(),this.userListViews ,ListFragment.this, mAuth);
        recyclerViewList.setAdapter(myListAdapter);
        this.progressDialog.dismiss();
    }

    @Override
    public void onItemClick(View view, int position, ImageView iv_listEdit) {

        if(userListViews.get(position).getSelected()){
            if(userListViews.get(position).getUserLists().getListOwner().getId().equals(mAuth.getCurrentUser().getUid())){
                iv_listEdit.findViewWithTag(position).setVisibility(View.GONE);
            }
            view.setBackgroundColor(Color.WHITE);
            userListViews.get(position).setSelected(false);
            selectedList.remove(userListViews.get(position));
            counter -=1;
            updateCounter(counter);
        } else {
            if(userListViews.get(position).getUserLists().getListOwner().getId().equals(mAuth.getCurrentUser().getUid())){
                iv_listEdit.findViewWithTag(position).setVisibility(View.VISIBLE);
            }
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            userListViews.get(position).setSelected(true);
            selectedList.add(userListViews.get(position));
            counter +=1;
            updateCounter(counter);
        }

        if(selectedList.size() == 0 && isInActionMood){
            clearActionMode();
        }
    }

    //RecylerView Long click Listener
    @Override
    public void onItemLongClick(View view, int position, ImageView iv_listEdit) {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.list_selected_menu);
        tv_totalListSelectedItem.setVisibility(View.VISIBLE);
        iv_profileImage.setVisibility(View.GONE);
        tv_profileName.setVisibility(View.GONE);
        isInActionMood = true;
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if(selectedList.size() == 0){
            if(userListViews.get(position).getUserLists().getListOwner().getId().equals(mAuth.getCurrentUser().getUid())){
                iv_listEdit.findViewWithTag(position).setVisibility(View.VISIBLE);
            }
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            userListViews.get(position).setSelected(true);
            selectedList.add(userListViews.get(position));
            counter +=1;
            updateCounter(counter);
        }
    }

    @Override
    public void setUser(User users) {
        this.users = users;
        if(users.getImageUrl().equals("default")){
            iv_profileImage.setBackgroundResource(R.drawable.user);
        } else{
            Glide.with(AppMain.instance).load(users.getImageUrl()).into(iv_profileImage);
        }
        tv_profileName.setText(users.getUsername());
    }

    @Override
    public void deleteSuccessful() {
        listPresenter.getUser(mAuth.getCurrentUser().getUid());
    }

    private void updateCounter(int counter){
        if(counter == 0){
            tv_totalListSelectedItem.setText("0 item selected");
        } else{
            tv_totalListSelectedItem.setText(counter+" item selected");
        }
    }

    //Floting button click listener
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.fab_add_list){
            CreateListFragment createListFragment = new CreateListFragment();
            FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer,createListFragment, "AddListFragment")
                    .addToBackStack("ListFragment");

            transaction.commit();
        } else if(view.getId() == R.id.iv_profileImage){

            Bundle bundle = new Bundle();
            bundle.putSerializable("Users", this.users);
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer,profileFragment, "ProfileFragment")
                    .addToBackStack("ListFragment");
            transaction.commit();

        } else if(view.getId() == R.id.tv_profileName){
            Bundle bundle = new Bundle();
            bundle.putSerializable("Users", this.users);
            ProfileFragment profileFragment = new ProfileFragment();
            profileFragment.setArguments(bundle);

            FragmentTransaction transaction = ((FragmentActivity)Objects.requireNonNull(getContext())).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.listContainer,profileFragment, "ProfileFragment")
                    .addToBackStack("ListFragment");
            transaction.commit();
        }
    }

    //set option menu in fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_list_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) item.getActionView();
        searchView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchView(android.support.v7.widget.SearchView searchView) {
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                myListAdapter.getFilter().filter(s);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_list_delete){
            myListAdapter.removeItem(selectedList);
            this.listPresenter.deleteUserLists(selectedList);
            clearActionMode();
        } else if(item.getItemId() == android.R.id.home){
            myListAdapter.refreshList(selectedList);
            clearActionMode();
        }

        return true;
    }

    private void clearActionMode() {
        isInActionMood = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.list_menu);
        tv_profileName.setVisibility(View.VISIBLE);
        iv_profileImage.setVisibility(View.VISIBLE);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter = 0;
        tv_totalListSelectedItem.setVisibility(View.GONE);
        tv_totalListSelectedItem.setText("0 item selected");
        selectedList.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        listPresenter.getUser(mAuth.getCurrentUser().getUid());
    }
}
