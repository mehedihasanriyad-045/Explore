package com.example.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ReviewRequest extends AppCompatActivity {


    private RecyclerView recyclerView;

    private UserReqAdapter userReqAdapter;
    private List<PlacesDesc>placesDescList;
    DatabaseReference databaseReference,databaseReference1;
    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private String key,div;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        div = getIntent().getStringExtra("div");

        this.setTitle("User Review Request for: "+div);
        recyclerView = findViewById(R.id.dhakaRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progress);

        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference(div+"user-Places:");
        databaseReference1 = FirebaseDatabase.getInstance().getReference(div+"-Places:");



        placesDescList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.VISIBLE);
                placesDescList.clear();
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    PlacesDesc placesDesc = dataSnapshot1.getValue(PlacesDesc.class);
                    placesDesc.setKey(dataSnapshot1.getKey());
                    placesDescList.add(placesDesc);
                }


                userReqAdapter  = new UserReqAdapter(getApplicationContext(),placesDescList);
                recyclerView.setAdapter(userReqAdapter);
                userReqAdapter.setOnItemClickListener(new UserReqAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick( View v, int position) {


                        String imageName = placesDescList.get(position).getImagename();
                        String desc = placesDescList.get(position).getPlacesdesc();
                        String imageurl = placesDescList.get(position).getImageurl();
                        String key1 = placesDescList.get(position).getKey();
                        Intent intent = new Intent(getApplicationContext(),UserReviewDetails.class);
                        intent.putExtra("PlaceName",imageName);
                        intent.putExtra("Description", desc);
                        intent.putExtra("URL", imageurl);
                        intent.putExtra("Div",div+" ");
                        intent.putExtra("Key",key1);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),key1,Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void Select(int position) {

                        pos = position;

                        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewRequest.this);
                        builder.setTitle("Select Option");

                        // Set up the buttons
                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String imageName = placesDescList.get(pos).getImagename();
                                String desc = placesDescList.get(pos).getPlacesdesc();
                                String imageurl = placesDescList.get(pos).getImageurl();
                                PlacesDesc placesDesc = new PlacesDesc(imageName,imageurl,desc,0.0,0.0,0);
                                String key1 = placesDescList.get(pos).getKey();
                                //String id = databaseReference.push().getKey();
                                databaseReference1.child(key1).setValue(placesDesc);

                                PlacesDesc selectedItem = placesDescList.get(pos);
                                final String key = selectedItem.getKey();
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageurl());
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReference.child(key).removeValue();

                                    }
                                });

                            }
                        });
                        builder.setNegativeButton("Cancel Request", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                PlacesDesc selectedItem = placesDescList.get(pos);
                                final String key = selectedItem.getKey();
                                StorageReference storageReference = firebaseStorage.getReferenceFromUrl(selectedItem.getImageurl());
                                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        databaseReference.child(key).removeValue();

                                    }
                                });
                                dialog.cancel();
                            }
                        });
                        builder.show();





                    }
                });
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Error: "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.nlogin_menu_layout, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.login_menu_layout, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.signOutMenuId)
        {
            if(FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                Toast.makeText(getApplicationContext(), "You aren't logged in.", Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        }

        if(item.getItemId() == R.id.logInMenuId)
        {
            if(FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                Toast.makeText(getApplicationContext(), "You are already logged in.", Toast.LENGTH_SHORT).show();
            }
            else {

                finish();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                intent.putExtra("prevActivity", "Menu");
                startActivity(intent);
            }
        }


        return super.onOptionsItemSelected(item);
    }
}