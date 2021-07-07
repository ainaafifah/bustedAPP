package com.example.bustedapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class profileFragment extends Fragment {

    private ImageView mProfilePicture;
    private TextView mLogout, fName, position, userEmail, phone, fullName;
    private Button mEdit, mPictureChange;

    private String currentUserId;
//    private String email;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference ref;

//    private FirebaseAuth mAuth;
    StorageReference storageReference;
//    private static final String USERS = "Users";

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri = data.getData();
//                mProfilePicture.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }

        }

    }

    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        final StorageReference fileRef = storageReference.child("Users/+"+currentUserId+"+/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(mProfilePicture);
                        }
                });
            }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(profileFragment.this.getActivity(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference("Users");


        currentUserId = user.getUid();
        database = FirebaseDatabase.getInstance();
//        ref = database.getReference(USERS);
        storageReference = FirebaseStorage.getInstance().getReference();


        StorageReference profileRef = storageReference.child("Users/+" + currentUserId + "+/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(mProfilePicture);
            }
        });

//        Intent intent = getActivity().getIntent();
//        email = intent.getStringExtra("email");

        mProfilePicture = view.findViewById(R.id.profilePicture);
        mLogout = view.findViewById(R.id.logoutButton);
        fName = view.findViewById(R.id.firstName);
        position = view.findViewById(R.id.position);
        userEmail = view.findViewById(R.id.userEmail);
        phone = view.findViewById(R.id.phoneNumber);
        fullName = view.findViewById(R.id.fullName);
        mEdit = view.findViewById(R.id.editProfileButton);
        mPictureChange = view.findViewById(R.id.uploadPicture);


        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(profileFragment.this.getActivity(), "Logged Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(profileFragment.this.getActivity(), Login.class));
            }
        });

        mPictureChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 1000);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                updateProfile();
            }
        });

        ref.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                for (DataSnapshot ds : snapshot.getChildren()){

                    if (userProfile != null) {
                        String full_name = userProfile.full_name;
                        String p = userProfile.user_position;
                        String e = userProfile.user_email;
                        String ph = userProfile.user_phone;

                        fName.setText(full_name);
                        fullName.setText(full_name);
                        position.setText(p);
                        userEmail.setText(e);
                        phone.setText(ph);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profileFragment.this.getActivity(), "Something wrong happen!", Toast.LENGTH_SHORT).show();

            }
        });

        return view;

    }
}