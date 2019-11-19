package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends AppCompatActivity {
    private ListView listView;
    DatabaseReference databaseReference;
    private List<contacts>contactsList;
    private contactAdapter contactAdapter;
    private FirebaseStorage firebaseStorage;
    ProgressBar progressBar;
    TextView link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        listView = findViewById(R.id.contactview);



        progressBar = findViewById(R.id.progress);

        firebaseStorage = FirebaseStorage.getInstance();

        link  = findViewById(R.id.link);
        Linkify.addLinks(link, Linkify.WEB_URLS);


        databaseReference = FirebaseDatabase.getInstance().getReference("Contacts");

        contactsList = new ArrayList<>();

        contactAdapter = new contactAdapter(ContactList.this,contactsList);
        listView.setAdapter(contactAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String number = contactsList.get(position).getPhone();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
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


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.nlogin_menu_layout, menu);
        }
        else{

            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            if(email.equals("mehedi.24csedu.045@gmail.com") || email.equals("riyadmehedihasan19@gmail.com"))
            {
                getMenuInflater().inflate(R.menu.admin_contact,menu);
            }

            else {
                getMenuInflater().inflate(R.menu.login_menu_layout, menu);
            }

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

                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                intent.putExtra("prevActivity", "Menu");
                startActivity(intent);
            }
        }
        if(item.getItemId() == R.id.ProfileMenuId){

            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.addNumber){

            Intent intent = new Intent(getApplicationContext(),AddContact.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




}
