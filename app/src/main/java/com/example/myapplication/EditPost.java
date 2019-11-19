package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditPost extends AppCompatActivity {

    EditText name,desc;
    Button done;
    String pName,pDesc,key,div;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        name = findViewById(R.id.edit_name);
        desc = findViewById(R.id.edit_desc);
        done = findViewById(R.id.edit_done);

        pName = getIntent().getStringExtra("name");
        pDesc = getIntent().getStringExtra("desc");
        key = getIntent().getStringExtra("key");
        div = getIntent().getStringExtra("div");
        name.setText(pName);
        desc.setText(pDesc);



        databaseReference = FirebaseDatabase.getInstance().getReference(div+"-Places:");

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ename = name.getText().toString();
                String eDesc = desc.getText().toString();
                databaseReference.child(key).child("imagename").setValue(ename);
                databaseReference.child(key).child("placesdesc").setValue(eDesc);
                Toast.makeText(getApplicationContext(),"Edited!",Toast.LENGTH_SHORT).show();


            }
        });


    }
}


