package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class Dhaka extends AppCompatActivity {

    private Button dhaka;
    private RecyclerView recyclerView;

    private MyAdpater myAdpater;
    private List<PlacesDesc>placesDescList;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private FirebaseStorage firebaseStorage;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dhaka);
        this.setTitle("DHAKA");
        recyclerView = findViewById(R.id.dhakaRecycle);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progress);

        firebaseStorage = FirebaseStorage.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference("Places: ");



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


                myAdpater  = new MyAdpater(getApplicationContext(),placesDescList);
                recyclerView.setAdapter(myAdpater);
                myAdpater.setOnItemClickListener(new MyAdpater.OnItemClickListener() {
                    @Override
                    public void onItemClick( View v, int position) {


                        String imageName = placesDescList.get(position).getImagename();
                        String desc = placesDescList.get(position).getPlacesdesc();
                        String imageurl = placesDescList.get(position).getImageurl();
                        String key1 = placesDescList.get(position).getKey();
                        Intent intent = new Intent(getApplicationContext(),Details.class);
                        intent.putExtra("PlaceName",imageName);
                        intent.putExtra("Description", desc);
                        intent.putExtra("URL", imageurl);
                        intent.putExtra("Div","Dhaka: ");
                        intent.putExtra("Key",key1);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),key1,Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void DELETE(int position) {
                        PlacesDesc selectedItem = placesDescList.get(position);
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
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Error: "+databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        dhaka = (Button) findViewById(R.id.dhakaaddplaces);

        dhaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddPlaces.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout,menu);
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