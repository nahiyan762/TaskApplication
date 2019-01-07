package com.nahiyan.project.taskapp.views.activitys;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.adapter.CreateListUserAdapter;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.presenters.CreateListPresenter;
import com.nahiyan.project.taskapp.presenters.implementation.CreateListPresenterImpl;
import com.nahiyan.project.taskapp.views.CreateListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.adapter.CreateListUserAdapter;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.models.UserLists;
import com.nahiyan.project.taskapp.presenters.CreateListPresenter;
import com.nahiyan.project.taskapp.views.CreateListView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateListFragment extends Fragment implements CreateListView {

    FirebaseAuth auth;
    UserLists userLists;
    CreateListPresenter createListPresenter;
    ArrayList<User> userArrayList;
    RecyclerView recyclerView;
    CreateListUserAdapter createListUserAdapter;
    private InterstitialAd mInterstitialAd;
    EditText et_listName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_list, container, false);

        mInterstitialAd = new InterstitialAd(AppMain.instance);
        mInterstitialAd.setAdUnitId("ca-app-pub-9786524414253219/4867015813");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        auth = FirebaseAuth.getInstance();
        this.userArrayList = new ArrayList<>();

        this.createListPresenter = new CreateListPresenterImpl(CreateListFragment.this,auth);

        //toolbar initialization and back button listener
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setTitle("Create List");
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
            }
        });

        //fragment view element initialization
        this.et_listName = view.findViewById(R.id.et_listName);
        ImageView iv_done = view.findViewById(R.id.iv_done);

        this.recyclerView = view.findViewById(R.id.userList);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),4);
        recyclerView.setLayoutManager(layoutManager);
        this.createListUserAdapter = new CreateListUserAdapter(getContext(),userArrayList,CreateListFragment.this);
        recyclerView.setAdapter(createListUserAdapter);


        if(this.getArguments() != null){
            userLists = (UserLists) getArguments().getSerializable("UserListsObject");
            et_listName.setText(userLists.getListName());
            this.userArrayList.addAll(userLists.getListAssignUser());
            this.createListUserAdapter.notifyDataSetChanged();
        } else{
            userLists = new UserLists();
        }


        //List creation for click listener
        iv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listName = et_listName.getText().toString();
                if(userLists.getListOwner() == null && userLists.getListId() == null){
                    createListPresenter.addListName(listName,userArrayList);
                } else{
                    createListPresenter.editListName(listName,userArrayList,userLists);
                }
            }
        });

        return view;
    }

    //get Userlist from Presenter getUsers() and set in ListView
    @Override
    public void setUsers(User users) {
        int counter = 0;
        if(userArrayList.isEmpty()){
            userArrayList.add(users);
            this.createListUserAdapter = new CreateListUserAdapter(getContext(),userArrayList,CreateListFragment.this);
            recyclerView.setAdapter(createListUserAdapter);
        } else{
            for(User user : userArrayList){
                if(!user.getId().equals(users.getId())){
                    counter++;
                    if(counter == userArrayList.size()){
                        userArrayList.add(users);
                        this.createListUserAdapter.notifyItemInserted(counter);
                    }
                } else{
                    break;
                }
            }
        }


    }

    //list name validation error
    @Override
    public void showValidationError(String s) {
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorWhite));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(s);
        spannableStringBuilder.setSpan(foregroundColorSpan, 0, s.length(), 0);
        this.et_listName.setError(spannableStringBuilder);
    }

    //list name insert success
    @Override
    public void insertListSuccess() {
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStackImmediate();
        }
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_menu,menu);
        MenuItem item = menu.findItem(R.id.menu_list_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search User By Email");
        searchView(searchView);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void searchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                createListPresenter.searchUser(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public void deleteUserFromList(int position) {
        this.userArrayList.remove(position);
        this.createListUserAdapter.notifyItemRemoved(position);
    }
}
