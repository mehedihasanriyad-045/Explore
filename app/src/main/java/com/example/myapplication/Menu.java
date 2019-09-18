package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    private Button bt_review ,bt_plan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bt_review = (Button) findViewById(R.id.bt_Review);

        bt_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu.this,Review.class);
                startActivity(intent);
            }
        });

        bt_plan = (Button) findViewById(R.id.bt_tourplan);

        bt_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Menu.this,CretePlan.class);
                startActivity(intent1);
            }
        });
    }
}
