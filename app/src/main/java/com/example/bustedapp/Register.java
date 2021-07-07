package com.example.bustedapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bustedapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText mFName,  mEmail, mPosition, mPhone, mPassword;
    private Button mGo;
    private TextView mLogin;
    private ImageView mProfile;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFName = findViewById(R.id.fName);
        mEmail = findViewById(R.id.email);
        mPosition = findViewById(R.id.position);
        mPhone = findViewById(R.id.phone);
        mPassword = findViewById(R.id.password);
        mGo = findViewById(R.id.btnSignup);
        mLogin = findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        mGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateData();
            }
        });

    }

    private String full_name = "", email = "", position = "", phone = "", pwd = "";
    private void validateData() {

        full_name = mFName.getText().toString().trim();
        email = mEmail.getText().toString().trim();
        position = mPosition.getText().toString().trim();
        phone = mPhone.getText().toString().trim();
        pwd = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(full_name)){
            Toast.makeText(this, "Enter your first name...", Toast.LENGTH_SHORT).show();
        }
//        else if (TextUtils.isEmpty(last_name)){
//            Toast.makeText(this, "Enter your last name...", Toast.LENGTH_SHORT).show();
//        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(position)){
            Toast.makeText(this, "Enter your position...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Enter your phone number...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "Enter your password...", Toast.LENGTH_SHORT).show();
        }
        else if(pwd.length() < 6){
            mPassword.setError("Password must be 6 characters or more");
            return;
        }
        else{
            createUserAccount();
        }

    }

    private void createUserAccount() {

        progressDialog.setMessage("Creating account ...");
        progressDialog.show();

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email,pwd)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Toast.makeText(Register.this, "Success to Register!", Toast.LENGTH_SHORT).show();
                        //add user in realtime database
                        updateUserInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {

                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInfo() {
        progressDialog.setMessage("Saving user info ...");

        //timestamp
        long timestamp = System.currentTimeMillis();

        //get current uid
        String uid = firebaseAuth.getUid();

        //set data tp ass in realtime db
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("full_name", full_name);
        hashMap.put("position", position);
        hashMap.put("email", email);
        hashMap.put("phone", phone);
        hashMap.put("password", pwd);
        hashMap.put("timestamp", timestamp);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //set data to db
        databaseReference= database.getReference("Users");
        databaseReference.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //add data on db
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Account created......", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this,MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //data failed to add on db
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }


}



