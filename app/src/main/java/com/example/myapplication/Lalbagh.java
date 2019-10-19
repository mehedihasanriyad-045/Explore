package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.firebase.auth.FirebaseAuth;

public class Lalbagh extends AppCompatActivity {

    PDFView lalbagh;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lalbagh);
        setTitle("LALBAGH FORT");

        lalbagh= (PDFView) findViewById(R.id.PDFlalbagh);

        lalbagh.fromAsset("lalbagh.pdf").load();

        mAuth = FirebaseAuth.getInstance();
    }
}
