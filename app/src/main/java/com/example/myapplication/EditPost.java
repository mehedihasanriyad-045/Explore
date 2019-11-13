package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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


            }
        });


    }
}


