package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private StorageReference mStorageref,imageRef;
    private ImageView addplacesimg;
    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    private ProgressDialog progressDialog ;
    // Folder path for Firebase Storage.
    private String Storage_Path = "All_Image_Uploads/";

    // Root Database Name for Firebase Database.
    private String Database_Path = "All_Image_Uploads_Database";
    StorageTask uploadTask;

    // Creating URI.
    private Uri FilePathUri;
    // Creating StorageReference and DatabaseReference object.
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_places);

        addplacesbtn = findViewById(R.id.addplacesbtn);
        addplacesedit = findViewById(R.id.addplacesedit);
        addplacesimg = findViewById(R.id.addplaceimg);
        addplacesname = findViewById(R.id.addplacesname);
        mDatabase = FirebaseDatabase.getInstance().getReference("Places: ");
        mStorage = FirebaseStorage.getInstance();
        mStorageref = mStorage.getReference();
        addplacesbtn.setOnClickListener(this);
        addplacesimg.setOnClickListener(this);






        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.addplacesbtn){
            if(uploadTask != null && uploadTask.isInProgress())
            {
                Toast.makeText(getApplicationContext(),"Upload in progress.",Toast.LENGTH_SHORT).show();
            } else {
                UploadImageFileToFirebaseStorage();
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

                Picasso.get().load(FilePathUri).into(addplacesimg);


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

        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));

        ref.putFile(FilePathUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Place added Done!",Toast.LENGTH_SHORT).show();

                        Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downlodur = uriTask.getResult();
                        PlacesDesc placesDesc = new PlacesDesc(imagename,downlodur.toString(),placesdetails);
                        String id = mDatabase.push().getKey();
                        mDatabase.child(id).setValue(placesDesc);
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
