package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WhoJoined extends AppCompatActivity {

    ListView listView;
    DatabaseReference databaseReference;
    private List<Join> joinList;
    private whoijoinedAdapter whoijoinedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_who_joined);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        listView = findViewById(R.id.whojoinlist);
        String key = getIntent().getStringExtra("key");

        databaseReference = FirebaseDatabase.getInstance().getReference("Event Join").child(key);

        joinList = new ArrayList<>();

        whoijoinedAdapter = new whoijoinedAdapter(WhoJoined.this,joinList);
        TextView nodata = findViewById(R.id.empty_view);
        listView.setEmptyView(nodata);
        listView.setAdapter(whoijoinedAdapter);

    }


    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                joinList.clear();
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                {
                    Join who = dataSnapshot2.getValue(Join.class);
                    joinList.add(who);
                }

                listView.setAdapter(whoijoinedAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }
}
