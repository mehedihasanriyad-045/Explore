package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private EditText signUpnameId, signUpemailId, signUppasswordId,signUpPhoneId;
    private Button signup_btn;
    private TextView signUpSignIn;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private DatabaseReference mDatabase;


    private StorageReference storageReference;
    private StorageTask uploadTask;


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

        signUpPhoneId = findViewById(R.id.signUpphoneId);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("usersImage");


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

        final String name =  signUpnameId.getText().toString();
        final String email =  signUpemailId.getText().toString().trim();
        final String password = signUppasswordId.getText().toString().trim();
        final String phoneNum = signUpPhoneId.getText().toString();

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
        if(phoneNum.isEmpty())
        {
            signUpPhoneId.setError("Enter a phone number please.");
            signUpPhoneId.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    // Sign in is successful
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name).build();



                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            mAuth.signOut();
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(),"Please verify.",Toast.LENGTH_SHORT).show();

                                            UserInfo userInfo = new UserInfo(name,email,phoneNum);
                                            String uploadId = mDatabase.push().getKey();
                                            mDatabase.child(uploadId).setValue(userInfo);
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
                    });
                }

            }
        });

    }
}
