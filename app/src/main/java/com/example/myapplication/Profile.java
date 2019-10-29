package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity implements View.OnClickListener {

   private ImageView profileImageview;
   private TextView profilenameview,profileemailview, profilephoneview;
   private Button profilesignoutbtn;
   private FirebaseUser firebaseUser;
   private FirebaseAuth mAuth;
   private FirebaseDatabase firebaseDatabase;

   private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        profileImageview = findViewById(R.id.profileImageView);
        profileemailview = findViewById(R.id.profileemailview);
        profilenameview = findViewById(R.id.profilenameview);
        profilephoneview = findViewById(R.id.profilephoneview);
        progressBar = findViewById(R.id.progressbarproId);
        profilesignoutbtn = findViewById(R.id.profilesignout_btn);
        profilesignoutbtn.setOnClickListener(this);

        // Initialize Firebase Auth
       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Image");
        mAuth = FirebaseAuth.getInstance();
        String userKey = user.getUid();


        databaseReference.child("Profile");
        databaseReference.child(userKey);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("users").getValue(String.class);
                profilenameview.setText(user.getDisplayName());
                profileemailview.setText(user.getEmail());
                Picasso.get().load(user.getPhotoUrl()).into(profileImageview);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profilesignout_btn:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
        }
    }
}


