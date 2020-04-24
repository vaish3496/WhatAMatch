package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private TextInputLayout mName;
    private TextInputLayout mPhoneNumber;
    private Button mConfirm;
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    private String name, phonenumber, userID, userSex = PeopleFragment.userSex;
    private ImageView mProfilePicture;
    private Uri mImageUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mProfilePicture = findViewById(R.id.profileImage);
        mName = findViewById(R.id.user_name);
        mPhoneNumber = findViewById(R.id.user_phone_number);
        mConfirm = findViewById(R.id.confirm);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userSex).child(userID);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Users").child(userSex).child(userID);


        displayUserInfo();

        mProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadUserInfo();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).fit().into(mProfilePicture);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private boolean checkName(){
        name = mName.getEditText().getText().toString();
        if (name.isEmpty()){
            mName.setError("Field can't be empty");
            return false;
        }else {
            mName.setError(null);
            return true;
        }
    }

    private boolean checkPhoneNumber(){
        phonenumber = mPhoneNumber.getEditText().getText().toString();
        if (phonenumber.isEmpty()){
            mPhoneNumber.setError("Field can't be empty");
            return false;
        }else if (phonenumber.length() != 10){
            mPhoneNumber.setError("Enter valid phone number");
            return false;
        }else {
            mPhoneNumber.setError(null);
            return true;
        }
    }

    private void uploadUserInfo() {
        if (!checkName() | !checkPhoneNumber()){
            return;
        }
        name = mName.getEditText().getText().toString();
        phonenumber = mPhoneNumber.getEditText().getText().toString();
        final Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("name",name);
        hashMap.put("phonenumber",phonenumber);
        mDatabaseRef.updateChildren(hashMap);

        if (mImageUri != null){
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() +"."+ getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            hashMap.put("imageURL",uri.toString());
                            mDatabaseRef.updateChildren(hashMap);
                        }
                    });
                }
            });

        }
        Toast.makeText(EditProfile.this,"Information Saved",Toast.LENGTH_SHORT).show();
    }

    private void displayUserInfo(){
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,Object> map = (Map<String, Object>) dataSnapshot.getValue();
                if (dataSnapshot.exists()){
                    if (map.get("name") != null){
                        mName.getEditText().setText(map.get("name").toString());
                    }
                    if (map.get("phonenumber") != null){
                        mPhoneNumber.getEditText().setText(map.get("phonenumber").toString());
                    }
                    if (map.get("imageURL") != null){
                        Picasso.get().load(map.get("imageURL").toString()).fit().into(mProfilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
