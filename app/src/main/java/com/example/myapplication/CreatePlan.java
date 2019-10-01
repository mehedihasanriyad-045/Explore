package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/*

public class CreatePlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
    }
}



*/



        import android.content.Intent;

        import android.view.View;
        import android.widget.Button;

public class CreatePlan extends AppCompatActivity {
    Button createPlanButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_plan);
        createPlanButton=(Button)findViewById(R.id.createPlanButton);
        createPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePlan.this, CreatePlanDetails.class);
                startActivity(intent);
            }
        });
    }
}
