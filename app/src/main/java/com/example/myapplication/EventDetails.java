package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EventDetails extends AppCompatActivity {


    private TextView placeName, duration, date, amount, owner, phnNum;
    private ImageView detailsImage;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText detComment;
    private Button add,submit;
    RatingBar ratingBar;
    RecyclerView RecyclerViewComment;
    DataSnapshot dataSnapshot;

    DatabaseReference databaseReference;

    CommentAdapter commentAdapter;
    List<Comment> commentList;
    static String COMMENT_KEY = "Comment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        add = findViewById(R.id.post_detail_add_comment_btn);
        detComment = findViewById(R.id.post_detail_comment);


        RecyclerViewComment = findViewById(R.id.rec_comment);

        placeName = findViewById(R.id.detCardPlaceName);
        duration = findViewById(R.id.detCardDuration);
        date = findViewById(R.id.detCardStartingDate);
        amount = findViewById(R.id.detCardTourCost);
        detailsImage = findViewById(R.id.detCardImageView);
        owner = findViewById(R.id.detCardHostName);
        phnNum = findViewById(R.id.detContactNum);

        final Intent intent = getIntent();

        placeName.setText(intent.getStringExtra("PlaceName"));
        duration.setText(intent.getStringExtra("Duration"));
        date.setText(intent.getStringExtra("Date"));
        amount.setText(intent.getStringExtra("Amount"));
        Picasso.get().load(intent.getStringExtra("ImageName")).fit().centerCrop().into(detailsImage);
        owner.setText(intent.getStringExtra("Owner"));
        phnNum.setText(intent.getStringExtra("PhoneNumber"));

    }



}
