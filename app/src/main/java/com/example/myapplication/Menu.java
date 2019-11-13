package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    private Button bt_review , bt_writereview, bt_tourplan,bt,bt_weather;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        this.setTitle("Menu");

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        mAuth = FirebaseAuth.getInstance();


        bt_review =  findViewById(R.id.bt_Review);

        bt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Review.class);
                startActivity(intent);
            }
        });



        bt_tourplan =  findViewById(R.id.bt_tourplan);

        bt_tourplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(FirebaseAuth.getInstance().getCurrentUser() == null )
                {
                    Toast.makeText(getApplicationContext(), "You have to log in", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(Menu.this,LogIn.class);
                    intent1.putExtra("prevActivity", "CreatePlan");
                    startActivity(intent1);
                }
                else {
                    Intent intent1 = new Intent(Menu.this,AddPost.class);
                    startActivity(intent1);
                }

            }
        });


        bt= findViewById(R.id.bt_Find);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, ContactList.class);
                startActivity(intent);
            }
        });

        bt_weather =findViewById(R.id.bt_weather);
        bt_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpEditText();
            }
        });

        

    }

    private void popUpEditText() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your City Name with country code with comma (example: Dhaka,BD): ");

        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        input.setLayoutParams(lp);

        builder.setView(input);




        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String city = input.getText().toString();

                Intent intent = new Intent(getApplicationContext(),weather.class);
                intent.putExtra("city",city);
                startActivity(intent);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
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
            //String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            //Toast.makeText(getApplicationContext(),email,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
