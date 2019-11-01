package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    private TextView detailsName, detailsDesc;
    private ImageView detailsImage;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Review");

        //Set back button
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        Intent getIntent = getIntent();
        String placeName = getIntent.getStringExtra("PlaceName");
        String description = getIntent.getStringExtra("Description");
        String url = getIntent.getStringExtra("URL");

        detailsImage = findViewById(R.id.detailsImage);
        detailsName = findViewById(R.id.detailsName);
        detailsDesc = findViewById(R.id.detailsDesc);
        progressBar = findViewById(R.id.progressbardet);
        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(url).fit().centerCrop().into(detailsImage);
        detailsName.setText(placeName);
        detailsDesc.setText(description);
        progressBar.setVisibility(View.GONE);

    }

    //handle onBackPressed (go to previous activity)


    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return super.onSupportNavigateUp();
    }
}
