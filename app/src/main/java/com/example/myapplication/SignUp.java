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

        progressBar =  findViewById(R.id.progressbarId);
        signUpnameId =  findViewById(R.id.signUpnameId);
        signUpemailId =  findViewById(R.id.signUpemailId);
        signup_btn =  findViewById(R.id.signup_btn);
        signUpSignIn =  findViewById(R.id.signUpSignIn);
        signUppasswordId =  findViewById(R.id.signUppasswordId);


        signUpSignIn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);



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

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    finish();
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Intent intent = new Intent(getApplicationContext(),LogIn.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(),"Registered is succesfull. Please verify.",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Error: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(),"Already registered",Toast.LENGTH_SHORT).show();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Eroor: "+ task.getException(),Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }
}
