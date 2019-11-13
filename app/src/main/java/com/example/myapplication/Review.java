package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Review extends AppCompatActivity {

    Button bt_dhaka;

    FirebaseAuth mAuth;
    GridView gridView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        this.setTitle("Places' Review");
        mAuth = FirebaseAuth.getInstance();
        final String[] division = {"Dhaka", "Chattagram", "Sylhet", "Rajshahi", "Rangpur", "Khulna","Barishal","Mymensingh"};
        gridView = findViewById(R.id.divisionView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_view,R.id.textViewlist,division);
        gridView.setAdapter(arrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(getApplicationContext(), Dhaka.class);
                intent.putExtra("div", division[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });



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
            //String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
