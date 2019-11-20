

package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Calendar;

public class AddPost extends AppCompatActivity implements View.OnClickListener {

    private Button postBtn, dateBtn, viewEvents;
    private EditText addTourplace,tourDesc;
    private EditText tourDuration, tourCost;
    private TextView dateView;
    private TextView date;
    DatePickerDialog.OnDateSetListener dateSetListener;
    private DatabaseReference mDatabase;
    private FirebaseStorage mStorage;
    private StorageReference mStorageref,imageRef;
    private ImageView addPlacesImg;

    private FirebaseAuth mAuth;





    // Image request code for onActivityResult() .
    int Image_Request_Code = 7;

    private ProgressDialog loadingBar ;
    private ProgressBar progressBaraddpost;

    // Folder path for Firebase Storage.
    private String Storage_Path = "Event_Photos";

    // Root Database Name for Firebase Database.
    private String Database_Path = "All_Event_Images";
    StorageTask uploadTask;

    // Creating URI.
    private Uri FilePathUri;

    // Creating StorageReference and DatabaseReference object.
    private StorageReference storageReference;
    private DatabaseReference databaseReference;


    EventsAdapter eventsAdapter;

    Context context;

    FirebaseUser firebaseUser;

    String prev,det_name;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);



        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#000000"));
        actionBar.setBackgroundDrawable(colorDrawable);


        postBtn = findViewById(R.id.btnPost);
        viewEvents = findViewById(R.id.btnNewsFeed);
        addTourplace = findViewById(R.id.tourPlace);
        dateBtn = findViewById(R.id.btnDate);
        tourCost = findViewById(R.id.approxTourCost);
        tourDuration = findViewById(R.id.fldDuration);
        addPlacesImg = findViewById(R.id.imageSelectForTour);
        dateView = findViewById(R.id.fldDesc);
        progressBaraddpost = findViewById(R.id.progressbaraddpost);
        tourDesc = findViewById(R.id.tourDesc);

        //eventsAdapter = new EventsAdapter(getApplicationContext(), )

        prev = getIntent().getStringExtra("prev");
        if(prev.equals("Details")){
            det_name = getIntent().getStringExtra("name");
            addTourplace.setText(det_name);
        }



        mDatabase = FirebaseDatabase.getInstance().getReference("Events");
        mStorage = FirebaseStorage.getInstance();
        mStorageref = mStorage.getReference();

        viewEvents.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        addPlacesImg.setOnClickListener(this);
        dateBtn.setOnClickListener(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar= Calendar.getInstance();

                int  year= calendar.get(Calendar.YEAR);
                int  month= calendar.get(Calendar.MONTH);
                int  day= calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog= new DatePickerDialog(AddPost.this,R.style.DialogTheme,
                        dateSetListener,
                        year,month,day);

                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dateDialog.show();
            }
        });



        dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                dateView.setText(""+day+"/"+(month+1)+"/"+year);

            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            getMenuInflater().inflate(R.menu.nlogin_menu_layout, menu);
        }
        else{
            getMenuInflater().inflate(R.menu.login_menu_layout, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.signOutMenuId)
        {
            if(FirebaseAuth.getInstance().getCurrentUser() == null)
            {
                Toast.makeText(getApplicationContext(), "You aren't logged in.", Toast.LENGTH_SHORT).show();
            }
            else {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(getApplicationContext(), Menu.class);
                startActivity(intent);
            }
        }

        if(item.getItemId() == R.id.logInMenuId)
        {
            if(FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                Toast.makeText(getApplicationContext(), "You are already logged in.", Toast.LENGTH_SHORT).show();
            }
            else {

                finish();
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                intent.putExtra("prevActivity", "Menu");
                startActivity(intent);
            }
        }
        if(item.getItemId() == R.id.ProfileMenuId){

            Intent intent = new Intent(getApplicationContext(), Profile.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        String addtourplaces = addTourplace.getText().toString();
        String duration = tourDuration.getText().toString();
        String amount = tourCost.getText().toString();
        String adate = dateView.getText().toString().trim();

        if(v.getId() == R.id.btnNewsFeed){
            Intent intent = new Intent(getApplicationContext(), NewsFeed.class);
            startActivity(intent);
        }


        else if(v.getId() == R.id.btnPost){
            if(uploadTask != null && uploadTask.isInProgress())
            {
                Toast.makeText(getApplicationContext(),"Upload in progress.",Toast.LENGTH_SHORT).show();
            }

            else {
                if(FilePathUri == null)
                {
                    Toast.makeText(getApplicationContext(),"Fill all the fields.",Toast.LENGTH_LONG).show();
                    postBtn.requestFocus();
                    return;
                }

                else if (TextUtils.isEmpty(addtourplaces) || TextUtils.isEmpty(amount) || TextUtils.isEmpty(adate) || TextUtils.isEmpty(duration)){
                    Toast.makeText(getApplicationContext(),"Fill all the fields.",Toast.LENGTH_LONG).show();
                    postBtn.requestFocus();
                    return;
                }

                else
                {
                    progressBaraddpost.setVisibility(View.VISIBLE);
                    UploadImageFileToFirebaseStorage();
                }


            }
        }

        else if(v.getId() == R.id.imageSelectForTour){
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
                Picasso.get().load(FilePathUri).into(addPlacesImg);
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

        final String addtourplaces = addTourplace.getText().toString();
        final String duration = tourDuration.getText().toString();
        final String amount = tourCost.getText().toString();
        final String adate = dateView.getText().toString().trim();
        final String desc = tourDesc.getText().toString().trim();
        addTourplace.setText("");
        tourDuration.setText("");
        tourCost.setText("");
        dateView.setText("");
        tourDesc.setText("");


        StorageReference ref = storageReference.child(System.currentTimeMillis()+"."+GetFileExtension(FilePathUri));
        ref.putFile(FilePathUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBaraddpost.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Yay New event created!",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), NewsFeed.class);
                        startActivity(intent);
                        Task<Uri> uriTask = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloduri = uriTask.getResult();
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        String name = firebaseUser.getDisplayName();
                        String email = firebaseUser.getEmail();
                        String id = mDatabase.push().getKey();
                        EventImage eventImage = new EventImage(downloduri.toString(), amount, duration, addtourplaces, adate, FirebaseAuth.getInstance().getCurrentUser().getUid(), java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()), name,
                          email,id
                        );

                        mDatabase.child(id).setValue(eventImage);
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