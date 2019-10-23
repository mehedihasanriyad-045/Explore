package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Menu extends AppCompatActivity {

    private Button bt_review , bt_writereview, bt_tourplan;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setTitle("Menu");

        mAuth = FirebaseAuth.getInstance();


        bt_review = (Button) findViewById(R.id.bt_Review);

        bt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Review.class);
                startActivity(intent);
            }
        });

        bt_writereview = (Button) findViewById(R.id.bt_wtitereview);

        bt_writereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() == null)
                {
                    Toast.makeText(getApplicationContext(), "You have to log in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Menu.this,LogIn.class);
                    intent1.putExtra("prevActivity", "write");
                    startActivity(intent1);
                }
                else {
                    Intent intent1 = new Intent(Menu.this,WriteReview.class);
                    startActivity(intent1);
                }

            }
        });

        bt_tourplan = (Button) findViewById(R.id.bt_tourplan);

        bt_tourplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() == null)
                {
                    Toast.makeText(getApplicationContext(), "You have to log in", Toast.LENGTH_SHORT).show();

                    Intent intent1 = new Intent(Menu.this,LogIn.class);
                    intent1.putExtra("prevActivity", "CreatePlan");
                    startActivity(intent1);
                }
                else {
                    Intent intent1 = new Intent(Menu.this,CreatePlan.class);
                    startActivity(intent1);
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout,menu);
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

        return super.onOptionsItemSelected(item);
    }
}
