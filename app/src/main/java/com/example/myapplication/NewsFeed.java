package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NewsFeed extends AppCompatActivity implements  View.OnTouchListener{

    private RecyclerView recyclerView;
    private EventsAdapter eventsAdapter;
    private List<EventImage> eventlist;
    DatabaseReference databaseReference;
    private ProgressBar progressBar;

    RecyclerTouchListener touchListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);



        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        recyclerView = findViewById(R.id.rViewEvents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Swipe Options
        touchListener = new RecyclerTouchListener(this,recyclerView);
        touchListener
                .setClickable(new RecyclerTouchListener.OnRowClickListener() {
                    @Override
                    public void onRowClicked(int position) {
                        //showToast("Clicked");
                        // HANDLE CLICK ACTION




                        String placeName = eventlist.get(position).getPlace();
                        String duration = eventlist.get(position).getDuration();
                        String amount = eventlist.get(position).getAmount();
                        String date = eventlist.get(position).getDate();
                        String owner = eventlist.get(position).getName();
                        String imagename = eventlist.get(position).getImageName();
                        String phnNum = eventlist.get(position).getDesc();
                        String key = eventlist.get(position).getKey();


                        Intent intent = new Intent(getApplicationContext(), EventDetails.class);

                        intent.putExtra("PlaceName", placeName);
                        intent.putExtra("Duration", duration);
                        intent.putExtra("Amount", amount);
                        intent.putExtra("Date", date);
                        intent.putExtra("Owner", owner);
                        intent.putExtra("ImageName", imagename);
                        intent.putExtra("PhoneNumber", phnNum);
                        intent.putExtra("key",key);

                        startActivity(intent);
                    }

                    @Override
                    public void onIndependentViewClicked(int independentViewID, int position) {

                    }
                });

        progressBar = findViewById(R.id.newsFeedProgressBar);




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
    public void onResume() {
        super.onResume();
        recyclerView.addOnItemTouchListener(touchListener);
    }



    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    public void showToast(String message){
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
