package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Details extends AppCompatActivity {

    private TextView detailsName, detailsDesc,rat;
    private ImageView detailsImage;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private EditText detComment;
    private Button add,submit;
    private String prev,key,key1,ratingIndb,sumIndb,countIndb,div;
    RatingBar ratingBar;
    RecyclerView RecyclerViewComment;
    DataSnapshot dataSnapshot;
    public double rate,sum;
    double mrate;
    public int count;
    DatabaseReference databaseReference;

    CommentAdapter commentAdapter;
    List<Comment> commentList;
    static String COMMENT_KEY = "Comment";


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
        final Intent getIntent = getIntent();
        String placeName = getIntent.getStringExtra("PlaceName");
        String description = getIntent.getStringExtra("Description");
        String url = getIntent.getStringExtra("URL");
        prev = getIntent.getStringExtra("Div");
        div = getIntent.getStringExtra("div");
        key =  getIntent.getStringExtra("Key");
        ratingIndb = getIntent.getStringExtra("rat");
        sumIndb = getIntent.getStringExtra("sum");
        countIndb = getIntent.getStringExtra("count");
        count = Integer.parseInt(countIndb);
        rate  = Double.parseDouble(ratingIndb);
        sum = Double.parseDouble(sumIndb);

        databaseReference = FirebaseDatabase.getInstance().getReference(div+"-Places:");


        RecyclerViewComment = findViewById(R.id.rec_comment);
        rat = findViewById(R.id.det_rating);
        submit = findViewById(R.id.rat_submit);
        ratingBar = findViewById(R.id.ratingBar);
        detailsImage = findViewById(R.id.detailsImage);
        detailsName = findViewById(R.id.detailsName);
        detailsDesc = findViewById(R.id.detailsDesc);
        progressBar = findViewById(R.id.progressbardet);
        add = findViewById(R.id.post_detail_add_comment_btn);
        detComment = findViewById(R.id.post_detail_comment);

        progressBar.setVisibility(View.VISIBLE);
        Picasso.get().load(url).fit().centerCrop().into(detailsImage);
        detailsName.setText(placeName);
        detailsDesc.setText(description);
        DecimalFormat dec = new DecimalFormat("#0.00");
        rat.setText("Rating: "+String.valueOf(dec.format(rate)));
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"Your given rating: "+String.valueOf(rating)+". Submit please",Toast.LENGTH_SHORT).show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        count++;
                        sum = sum + rating;

                        DecimalFormat dec = new DecimalFormat("#0.00");
                        //Toast.makeText(getApplicationContext(),"Count "+String.valueOf(count)+" Rate: "+String.valueOf(dec.format(rate))+" Sum"+String.valueOf(dec.format(sum)),Toast.LENGTH_SHORT).show();
                        rate = (sum) / count;
                        rat.setText("Rating: "+String.valueOf(dec.format(rate)));
                        databaseReference.child(key).child("rating").setValue(rate);
                        databaseReference.child(key).child("sum").setValue(sum);
                        databaseReference.child(key).child("count").setValue(count);
                        submit.setEnabled(false);

                    }
                });

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {

                    DatabaseReference commentReference = firebaseDatabase.getReference(prev+"Comment").child(key).push();
                    String comment_content = detComment.getText().toString();
                    String uid = firebaseUser.getUid();
                    String uname = firebaseUser.getDisplayName();
                    Comment comment = new Comment(comment_content,uid,uname);
                    commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"Added"+key,Toast.LENGTH_SHORT).show();
                            detComment.setText("");
                            add.setVisibility(View.VISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Log in.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        iniRecyclerViewComment(key1);


    }


    private void iniRecyclerViewComment(String key1) {

        RecyclerViewComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentref = firebaseDatabase.getReference(prev+"Comment").child(key);
        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);

                }

                commentAdapter = new CommentAdapter(getApplicationContext(),commentList);
                RecyclerViewComment.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //handle onBackPressed (go to previous activity)


    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();

        return super.onSupportNavigateUp();
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
        if(item.getItemId() == R.id.ProfileMenuId){

            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

