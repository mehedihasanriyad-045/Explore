package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity implements View.OnClickListener{

    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<EventImage> eventlist;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private Button btnPostFromNewsFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        recyclerView = findViewById(R.id.rViewEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.newsFeedProgressBar);

        btnPostFromNewsFeed = findViewById(R.id.btnGoPostEvent);
        btnPostFromNewsFeed.setOnClickListener(this);

        eventlist = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    EventImage eventImage = dataSnapshot1.getValue(EventImage.class);
                    eventlist.add(eventImage);
                }


                eventsAdapter = new EventsAdapter(NewsFeed.this, eventlist);
                recyclerView.setAdapter(eventsAdapter);


                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Error: " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnGoPostEvent){
            Intent intent = new Intent(getApplicationContext(), AddPost.class);
            startActivity(intent);
        }
    }
}
