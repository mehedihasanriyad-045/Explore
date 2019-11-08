package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {
    Button addContacts;
    private ListView listView;
    DatabaseReference databaseReference;
    private List<contacts>contactsList;
    private contactAdapter contactAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        listView = findViewById(R.id.contactview);

        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");

        contactsList = new ArrayList<>();

        contactAdapter = new contactAdapter(ContactList.this,contactsList);
        listView.setAdapter(contactAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = contactsList.get(position).getPhone();

                Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);

            }



        });





        addContacts= findViewById(R.id.addContacts);
        addContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddContact.class);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                contactsList.clear();
                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren())
                {
                    contacts fontacts = dataSnapshot2.getValue(contacts.class);
                    contactsList.add(fontacts);
                }

                listView.setAdapter(contactAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }
}
