package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContactList extends AppCompatActivity {
    Button bt;
    String number = "[01521252662]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        bt= findViewById(R.id.bt_number);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
                startActivity(intent);
            }
        });
    }
}
