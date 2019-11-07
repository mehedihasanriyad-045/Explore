package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends AppCompatActivity {

    private EditText phone, name;
    private Button add;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);



        phone = findViewById(R.id.addPhone);
        name = findViewById(R.id.addName);
        add = findViewById(R.id.addContacts);
        progressBar = findViewById(R.id.progressbarcontacts);
        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }


    private void saveData() {

        String nameUser = name.getText().toString().trim();
        String phoneNumber = phone.getText().toString().trim();
        Toast.makeText(getApplicationContext(),nameUser+"   "+phoneNumber,Toast.LENGTH_SHORT).show();


        if(nameUser.isEmpty()){
            name.setError("Enter name first");
            name.requestFocus();
            return;
        }

        if(phoneNumber.isEmpty()){
            phone.setError("Enter number first");
            phone.requestFocus();
            return;
        }

        String key  = databaseReference.push().getKey();
        contacts fontacts = new contacts(nameUser,phoneNumber);
        databaseReference.child(key).setValue(fontacts);
        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);


    }
}
