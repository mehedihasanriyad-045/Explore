package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener{

    private EditText signInEmailEditText, signInPasswordEditText;
    private Button loginButton;
    private TextView signInforgetpasswordtextView;
    private TextView signInSignUptextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        this.setTitle("Sign In Here");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



        signInEmailEditText = (EditText) findViewById(R.id.signInEmailId);
        signInPasswordEditText = (EditText) findViewById(R.id.signInpasswordId);
        progressBar = (ProgressBar) findViewById(R.id.progressbarId);
        loginButton = (Button) findViewById(R.id.login_btn);
        signInforgetpasswordtextView = (TextView) findViewById(R.id.signInforgetPasswordtextviewid);
        signInSignUptextView = (TextView) findViewById(R.id.signInSignUp);

        signInSignUptextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_btn:
                userLogin();
                break;

            case R.id.signInSignUp:
                Intent intent = new Intent(getApplicationContext(),SignUp.class);
                startActivity(intent);
                break;

        }

    }

    private void userLogin() {


        String email =  signInEmailEditText.getText().toString().trim();
        String password =  signInPasswordEditText.getText().toString().trim();

        if(email.isEmpty())
        {
            signInEmailEditText.setError("Enter an email address");
            signInEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signInEmailEditText.setError("Enter a valid email address");
            signInEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signInPasswordEditText.setError("Enter a password");
            signInPasswordEditText.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            signInPasswordEditText.setError("Password is too short. Password Should be more than 6");
            signInPasswordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (!task.isSuccessful()) {
                    //Log.w("TAG", "signInWithEmail:failed", task.getException());

                } else {
                    checkIfEmailVerified();
                }

            }
        });


    }

    private void checkIfEmailVerified() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(getApplicationContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
            Intent intent = getIntent();
            String prevActivity = intent.getStringExtra("prevActivity");
            if(prevActivity.equals("CreatePlan"))
            {
                Intent intent1 = new Intent(getApplicationContext(),CreatePlan.class);
                startActivity(intent1);
            }
            else if(prevActivity.equals("write"))
            {
                Intent intent1 = new Intent(getApplicationContext(),WriteReview.class);
                startActivity(intent1);
            }
            else if(prevActivity.equals("Menu"))
            {
                Intent intent1 = new Intent(getApplicationContext(),Menu.class);
                startActivity(intent1);
            }
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();

            //restart this activity

        }
    }
}
