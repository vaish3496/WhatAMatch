package com.example.tinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    public static String FEMALE_DEFAULT_IMAGE = "https://rehrealestate.com/wp-content/uploads/2015/08/facebook-default-no-profile-pic-girl.jpg";
    public static String MALE_DEFAULT_IMAGE = "https://i.stack.imgur.com/l60Hf.png";

    private TextInputLayout mEmail, mPassword, mName;
    private Button mRegister;

    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mEmail = findViewById(R.id.register_email);
        mPassword = findViewById(R.id.register_password);
        mRegister = findViewById(R.id.register_register);
        mName = findViewById(R.id.register_name);
        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.check(R.id.radio_button_female);


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mRadioGroup.getCheckedRadioButtonId() == -1 | !checkName() | !checkEmail() | !checkPassword()) {
                    return;
                }
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = findViewById(selectedId);
                final String email = mEmail.getEditText().getText().toString();
                final String password = mPassword.getEditText().getText().toString();
                final String name = mName.getEditText().getText().toString();
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "sign up error!", Toast.LENGTH_SHORT).show();
                        } else {
                            String userId = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserdb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", name);
                            if (radioButton.getText().toString().equals("Male")) {
                                hashMap.put("imageURL", MALE_DEFAULT_IMAGE);
                            } else {
                                hashMap.put("imageURL", FEMALE_DEFAULT_IMAGE);
                            }
                            currentUserdb.updateChildren(hashMap);

                        }
                    }
                });
            }
        });
    }

    private boolean checkName() {
        String name = mName.getEditText().getText().toString();
        if (name.isEmpty()) {
            mName.setError("Field can't be empty");
            return false;
        } else {
            mName.setError(null);
            return true;
        }
    }

    private boolean checkEmail() {
        String email = mEmail.getEditText().getText().toString();
        if (email.isEmpty()) {
            mEmail.setError("Field can't be empty");
            return false;
        } else {
            mEmail.setError(null);
            return true;
        }
    }

    private boolean checkPassword() {
        String password = mPassword.getEditText().getText().toString();
        if (password.isEmpty()) {
            mPassword.setError("Field can't be empty");
            return false;
        } else {
            mPassword.setError(null);
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
