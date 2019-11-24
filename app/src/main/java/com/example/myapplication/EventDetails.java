package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDetails extends AppCompatActivity {


    private TextView placeName, duration, date, amount, owner, phnNum,joined,shortDesc,needmem;
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
    StorageReference storageReference;
    private FirebaseStorage firebaseStorage;

    Event_Comment_Adapter comment_Adapter;
    List<eventcomment> list;
    static String COMMENT_KEY = "Comment";
    String key,url,need;
    int needed;

    int flag = 0,member;
    Button join1;
    DatabaseReference commentref,join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        add = findViewById(R.id.event_detail_add_comment_btn);
        detComment = findViewById(R.id.event_detail_comment);


        RecyclerViewComment = findViewById(R.id.event_rec_comment);

        placeName = findViewById(R.id.detCardPlaceName);
        duration = findViewById(R.id.detCardDuration);
        date = findViewById(R.id.detCardStartingDate);
        amount = findViewById(R.id.detCardTourCost);
        detailsImage = findViewById(R.id.detCardImageView);
        owner = findViewById(R.id.detCardHostName);
        phnNum = findViewById(R.id.detContactNum);
        join1 = findViewById(R.id.join);
        joined = findViewById(R.id.joined);
        shortDesc = findViewById(R.id.shortDesc);
        needmem = findViewById(R.id.needMem);

        storageReference = FirebaseStorage.getInstance().getReference("Events");
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");
        firebaseStorage = FirebaseStorage.getInstance();

        final Intent intent = getIntent();
        key = intent.getStringExtra("key");
        url = intent.getStringExtra("ImageName");
        String mail = intent.getStringExtra("PhoneNumber");
        placeName.setText(intent.getStringExtra("PlaceName"));
        duration.setText(intent.getStringExtra("Duration"));
        date.setText(intent.getStringExtra("Date"));
        amount.setText(intent.getStringExtra("Amount"));
        Picasso.get().load(intent.getStringExtra("ImageName")).fit().centerCrop().into(detailsImage);
        owner.setText("posted by: "+intent.getStringExtra("Owner"));
        phnNum.setText(intent.getStringExtra("PhoneNumber"));
        shortDesc.setText(intent.getStringExtra("short"));
        String mem = intent.getStringExtra("member");
        String neededMembers = intent.getStringExtra("need");
        needed = Integer.parseInt(neededMembers);

        needmem.setText("Member/s needed: "+neededMembers);

        member = Integer.parseInt(mem);

        Log.d("Member: "," "+member);

        joined.setText(intent.getStringExtra("member"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Events");

        String email = firebaseUser.getEmail();

        if(email.equals(mail)) {
            flag = 1;
        }
        Log.d("News"," "+flag+ " "+email+" "+mail);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(FirebaseAuth.getInstance().getCurrentUser() != null)
                        {

                            DatabaseReference commentReference = FirebaseDatabase.getInstance().getReference("EventComment").child(key).push();
                            String comment_content = detComment.getText().toString();
                            if(comment_content.isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please, write something.",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else
                            {
                                String uname =firebaseUser.getDisplayName();
                                String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                eventcomment event = new eventcomment(uname,time,comment_content);

                                commentReference.setValue(event).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
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
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Log in.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        join1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {

                    if(member < needed){
                        join = FirebaseDatabase.getInstance().getReference("Event Join").child(key);
                        final String uname =firebaseUser.getDisplayName();
                        final String email = firebaseUser.getEmail();
                        final String uid = firebaseUser.getUid();
                        //String time = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


                        /*join.setValue(join1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Joined",Toast.LENGTH_SHORT).show();
                                member++;
                                String meme = String.valueOf(member);
                                joined.setText(meme);
                                databaseReference.child(key).child("member").setValue(meme);



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                            }
                        });*/



                        join.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.exists())
                                {
                                    Toast.makeText(getApplicationContext(),"Joined",Toast.LENGTH_SHORT).show();
                                    member++;
                                    String meme = String.valueOf(member);
                                    joined.setText(meme);
                                    Join join1 = new Join(email,uname);
                                    join.child(uid).setValue(join1);
                                    databaseReference.child(key).child("member").setValue(meme);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Already Joined",Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    else {
                        Toast.makeText(getApplicationContext(),"Sorry! Fulfilled.",Toast.LENGTH_SHORT).show();

                    }

                    //join.setVisibility(View.GONE);


                }
                else {
                    Toast.makeText(getApplicationContext(),"Please Log in.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        iniRecyclerViewComment();

    }

    private void iniRecyclerViewComment() {

        RecyclerViewComment.setLayoutManager(new LinearLayoutManager(this));

        commentref = firebaseDatabase.getReference("EventComment").child(key);
        commentref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    eventcomment comment = snapshot.getValue(eventcomment.class);
                    list.add(comment);

                }

                comment_Adapter = new Event_Comment_Adapter(getApplicationContext(),list);
                RecyclerViewComment.setAdapter(comment_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        if(flag == 1) {
            getMenuInflater().inflate(R.menu.event_details_menu, menu);
        }
        else {
            getMenuInflater().inflate(R.menu.join_not_creator, menu);

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.delete_event)
        {
            databaseReference.child(key).removeValue();
            commentref.removeValue();
            join.removeValue();

            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Intent intent = new Intent(getApplicationContext(),NewsFeed.class);
                    startActivity(intent);

                }
            });
        }

        if(item.getItemId() == R.id.whojoined)
        {

            Intent intent = new Intent(getApplicationContext(),WhoJoined.class);
            intent.putExtra("key",key);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
