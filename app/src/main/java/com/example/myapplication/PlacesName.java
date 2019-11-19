package com.example.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlacesName extends AppCompatActivity {


    private AutoCompleteTextView atv;
    private Button bt_seeWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_name);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Explore!!");
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        StringBuilder sb = new StringBuilder();
        String strLine = "";
        String str_data = "";
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(getAssets().open("File.txt")));
            while (strLine != null)
            {
                if (strLine == null)
                    break;
                str_data += strLine;
                strLine = br.readLine();

            }
            //System.out.println(str_data);
            br.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("Unable to read the file.");
        }
        final String[] city_str = str_data.split("\",\"");

        atv = findViewById(R.id.atv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,city_str);
        atv.setAdapter(adapter);
        bt_seeWeather =findViewById(R.id.bt_seeWeather);
        bt_seeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), city_str[0], Toast.LENGTH_SHORT).show();
                String city = atv.getText().toString();
                Intent intent = new Intent(getApplicationContext(),weather.class);
                intent.putExtra("city",city);
                startActivity(intent);
            }
        });
    }
}
