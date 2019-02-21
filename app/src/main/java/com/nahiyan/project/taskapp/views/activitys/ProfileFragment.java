package com.nahiyan.project.taskapp.views.activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.nahiyan.project.taskapp.R;
import com.nahiyan.project.taskapp.application.AppMain;
import com.nahiyan.project.taskapp.models.User;
import com.nahiyan.project.taskapp.presenters.ProfilePresenter;
import com.nahiyan.project.taskapp.presenters.implementation.ProfilePresenterImpl;
import com.nahiyan.project.taskapp.views.ProfileFragmentView;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.nahiyan.project.taskapp.application.AppMain;

import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements ProfileFragmentView, View.OnClickListener, ListView.OnItemClickListener {

    TextView profile_name;
    CircleImageView profile_image;
    private static final int IMAGE_REQUEST = 1;
    ProfilePresenter profilePresenter;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    User user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.progressDialog = new ProgressDialog(getContext());
        this.mAuth = FirebaseAuth.getInstance();
        profilePresenter = new ProfilePresenterImpl(ProfileFragment.this,mAuth);

        if(getArguments() != null){
            this.user = (User) getArguments().getSerializable("Users");
        } else{
            profilePresenter.getUser(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)Objects.requireNonNull(getActivity())).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) getActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objects.requireNonNull(getFragmentManager()).popBackStackImmediate();
            }
        });

        this.profile_name = view.findViewById(R.id.profile_name);
        this.profile_image = view.findViewById(R.id.profile_image);
        this.profile_image.setOnClickListener(this);
        ListView listView = view.findViewById(R.id.listView);

        if(user.getImageUrl().equals("default")){
            profile_image.setBackgroundResource(R.drawable.user);
        } else{
            Glide.with(AppMain.instance).load(user.getImageUrl()).into(profile_image);
        }
        profile_name.setText(user.getUsername());

        String[] files = {"Rate App","Share App","Contact Us","Logout"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(AppMain.instance, R.layout.profile_listview,files);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void setUser(User users) {
        if(users.getImageUrl().equals("default")){
            profile_image.setBackgroundResource(R.drawable.user);
        } else{
            Glide.with(AppMain.instance).load(users.getImageUrl()).into(profile_image);
        }
        profile_name.setText(users.getUsername());
    }

    @Override
    public void imageUploadSuccess() {
        progressDialog.dismiss();
        Glide.with(AppMain.instance).load(user.getImageUrl()).into(profile_image);
    }

    @Override
    public void imageUploadFailed() {
        progressDialog.dismiss();
        Toast.makeText(AppMain.instance,"Failed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.profile_image){
            if (ActivityCompat.checkSelfPermission(AppMain.instance, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions( //Method of Fragment
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        11
                );
            } else {
                openImage();
            }
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && data != null && data.getData() != null){
            Uri imageUri = data.getData();
            if(imageUri != null){
                progressDialog.setMessage("Uploading");
                progressDialog.show();
                String fileExtension = getFileExtension(imageUri);
                profilePresenter.uploadImage(imageUri,fileExtension);
            } else{
                Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = Objects.requireNonNull(getContext()).getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if(position == 3){
            this.mAuth.signOut();
            Intent i = new Intent(getActivity(), LoginActivity.class);
            startActivity(i);
            Objects.requireNonNull(getActivity()).finish();
        } else if(position == 2){
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"nahiyan00000@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Task Application");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        } else if(position == 1){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Task Application");
            intent.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + Objects.requireNonNull(AppMain.instance).getPackageName());
            startActivity(Intent.createChooser(intent, "choose one"));
        } else if(position == 0){
            Uri uri = Uri.parse("market://details?id=" + Objects.requireNonNull(AppMain.instance).getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + AppMain.instance.getPackageName())));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 11) {
            if (permissions[0].equals(Manifest.permission.READ_EXTERNAL_STORAGE) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImage();
            }
        }
    }
}
