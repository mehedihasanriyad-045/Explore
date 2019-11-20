package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AddPlaces extends AppCompatActivity implements View.OnClickListener {

    private Button addplacesbtn;
    private EditText addplacesedit, addplacesname;

    private ImageView addplacesimg;

    int Image_Request_Code = 7;

    private Uri FilePathUri;

    private ProgressBar progressBarAdd;
    private String div;
    StorageTask uploadTask;

    private DatabaseReference databaseReference,databaseReference1;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);

        div = getIntent().getStringExtra("div");
        addplacesbtn = findViewById(R.id.addplacesbtn);
        addplacesedit = findViewById(R.id.addplacesedit);
        addplacesimg = findViewById(R.id.addplaceimg);
        addplacesname = findViewById(R.id.addplacesname);
        progressBarAdd = findViewById(R.id.progressbarAdd);
        addplacesbtn.setOnClickListener(this);
        addplacesimg.setOnClickListener(this);
        storageReference = FirebaseStorage.getInstance().getReference(div+"'s Place");
        databaseReference = FirebaseDatabase.getInstance().getReference(div+"-Places:");
        databaseReference1 = FirebaseDatabase.getInstance().getReference(div+"user-Places:");

    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.addplacesbtn){
            if(FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                Toast.makeText(getApplicationContext(),"Please log in first",Toast.LENGTH_SHORT).show();

            }
            else {
                if(uploadTask != null && uploadTask.isInProgress())
                {
                    Toast.makeText(getApplicationContext(),"Upload in progress.",Toast.LENGTH_SHORT).show();
                } else {

                    UploadImageFileToFirebaseStorage();
                }
            }

        }
        else if(v.getId() == R.id.addplaceimg){
            addplacesimage();
        }
    }



    private void addplacesimage() {
        // Creating intent.
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                //Picasso.get().load(FilePathUri).into(addplacesimg);
                Picasso.get().load(String.valueOf(FilePathUri)).into(addplacesimg);


            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    // Creating Method to get the selected image file Extension from File Path URI.
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void UploadImageFileToFirebaseStorage() {
        final String imagename = addplacesname.getText().toString().trim();
        final String placesdetails = addplacesedit.getText().toString();


        if(imagename.isEmpty()){
            addplacesname.setError("Enter the image name.");
            addplacesname.requestFocus();
            return;
        }
        if(placesdetails.isEmpty()){
            addplacesedit.setError("Please, write details.");
            addplacesedit.requestFocus();
            return;
        }
        if(placesdetails.length() < 20)
        {
            addplacesedit.setError("Details should be more than 20 letters");
            addplacesedit.requestFocus();
            return;
        }
        progressBarAdd.setVisibility(View.VISIBLE);

        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));

        ref.putFile(FilePathUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        progressBarAdd.setVisibility(View.GONE);
                        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                        if(email.equals("mehedi.24csedu.045@gmail.com") || email.equals("riyadmehedihasan19@gmail.com"))
                        {
                            Toast.makeText(getApplicationContext(),"Place added Done!",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Your review has been stored. Need admin approval.",Toast.LENGTH_SHORT).show();

                        }
                        Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downlodur = uriTask.getResult();
                        PlacesDesc placesDesc = new PlacesDesc(imagename,downlodur.toString(),placesdetails,0.0,0.0,0);

                        if(email.equals("mehedi.24csedu.045@gmail.com") || email.equals("riyadmehedihasan19@gmail.com"))
                        {


                            String id = databaseReference.push().getKey();
                            databaseReference.child(id).setValue(placesDesc);

                        }


                        else {
                            String id  = databaseReference1.push().getKey();
                            databaseReference1.child(id).setValue(placesDesc);
                        }

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
}
