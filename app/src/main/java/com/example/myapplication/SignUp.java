package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText signUpnameId, signUpemailId, signUppasswordId;
    private Button signup_btn;
    private TextView signUpSignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.setTitle("Sign Up Here");


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressbarId);
        signUpnameId = (EditText) findViewById(R.id.signUpnameId);
        signUpemailId = (EditText) findViewById(R.id.signUpemailId);
        signup_btn = (Button) findViewById(R.id.signup_btn);
        signUpSignIn = (TextView) findViewById(R.id.signUpSignIn);
        signUppasswordId = (EditText) findViewById(R.id.signUppasswordId);


        signUpSignIn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);

        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    // NOTE: this Activity should get onpen only when the user is not signed in, otherwise
                    // the user will receive another verification email.
                    sendVerificationEmail();
                } else {
                    // User is signed out

                }
                // ...
            }
        };

    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), LogIn.class));
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signup_btn:
                userRegister();
                break;

            case R.id.signUpSignIn:
                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                startActivity(intent);
                break;

        }

    }

    private void userRegister() {

        String name =  signUpnameId.getText().toString();
        String email =  signUpemailId.getText().toString().trim();
        String password =  signUppasswordId.getText().toString().trim();

        if(email.isEmpty())
        {
            signUpemailId.setError("Enter an email address");
            signUpemailId.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            signUpemailId.setError("Enter a valid email address");
            signUpemailId.requestFocus();
            return;
        }

        //checking the validity of the password
        if(password.isEmpty())
        {
            signUppasswordId.setError("Enter a password");
            signUppasswordId.requestFocus();
            return;
        }

        if(password.length() < 6)
        {
            signUppasswordId.setError("Password is too short. Password Should be more than 6");
            signUppasswordId.requestFocus();
            return;
        }

        //checking the validity of the password
        if(name.isEmpty())
        {
            signUpnameId.setError("Enter a Name");
            signUpnameId.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                Log.d("TAG", "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {

                }
                else
                {

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.sendEmailVerification();// successfully account created
                    // now the AuthStateListener runs the onAuthStateChanged callback
                }
            }
        });

    }
}
