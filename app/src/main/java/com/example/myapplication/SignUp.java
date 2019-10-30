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
    private ImageView signUpimageView;

    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    private DatabaseReference mDatabase;

    private Uri imageUri;
    private String imageurl;

    private static final int IMAGE_REQUEST = 1;

    private StorageReference storageReference,imagename;
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
        signUpimageView =findViewById(R.id.signUpImageId);
        signUpPhoneId = findViewById(R.id.signUpphoneId);

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference("usersImage");


        signUpSignIn.setOnClickListener(this);
        signup_btn.setOnClickListener(this);
        signUpimageView.setOnClickListener(this);




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

            case R.id.signUpImageId:
                if(uploadTask != null && uploadTask.isInProgress())
                {
                    Toast.makeText(getApplicationContext(),"Upload in process.",Toast.LENGTH_SHORT).show();
                }else {
                    showPictureDialog();
                }

        }

    }


    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(galleryIntent, GALLERY);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && data != null && data.getData() != null ) {
            imageUri = data.getData();
            try {
                Picasso.get().load(imageUri).into(signUpimageView);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
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
        if(imageUri == null)
        {
            signUpSignIn.setError("Please select an image first");

        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {


                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                final StorageReference Imagename = storageReference.child("image"+imageUri.getLastPathSegment());
                                imageurl = imageUri.toString();
                                Imagename.putFile(imageUri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                Toast.makeText(getApplicationContext(),"Place added Done!",Toast.LENGTH_SHORT).show();
                                                Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                                                while(!uriTask.isSuccessful());
                                                Uri downloadUrl = uriTask.getResult();
                                                UserInfo userInfo = new UserInfo(name,email,phoneNum,downloadUrl.toString());
                                                String uploadId = mDatabase.push().getKey();
                                                mDatabase.child(uploadId).setValue(userInfo);

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                // ...
                                            }
                                        });

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
