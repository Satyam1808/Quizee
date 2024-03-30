package com.example.cyberquize.BottomSheetFragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyberquize.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalInfoBottomSheet extends BottomSheetDialogFragment {

    CircleImageView userImg;
    RelativeLayout uploadImg;
    TextView firstName,lastName,mobileNumber;
    ImageView fNameEditImg,lNameEditImg;

    private static final int PICK_IMAGE_REQUEST = 2;
    private Bitmap selectedImageBitmap;
    Button saveBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public PersonalInfoBottomSheet() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_personal_info_bottom_sheet, container, false);

        userImg = view.findViewById(R.id.userProfilePicBottomSheet);
        uploadImg = view.findViewById(R.id.uploadImgLy);
        firstName = view.findViewById(R.id.firstNameBottomSheet);
        lastName = view.findViewById(R.id.lastNameBottomSheet);
        fNameEditImg = view.findViewById(R.id.fNameEdit_icn);
        lNameEditImg = view.findViewById(R.id.lNameEdit_icn);
        mobileNumber = view.findViewById(R.id.mobileNumberBottomSheet);
        saveBtn = view.findViewById(R.id.saveBtnBottomSheet);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Fetch user's name from Firebase and set it on TextViews
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userFirstName = snapshot.child("firstName").getValue(String.class);
                        String userLastName = snapshot.child("lastName").getValue(String.class);
                        String userMobileNumber = snapshot.child("phoneNumber").getValue(String.class);
                        String userImgUrl = snapshot.child("profileImageUrl").getValue(String.class);


                        // Set fetched name on TextViews
                        firstName.setText(userFirstName);
                        lastName.setText(userLastName);
                        mobileNumber.setText(userMobileNumber);
                        Picasso.get().load(userImgUrl).into(userImg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }



        uploadImg.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             openImageChooser();
         }
     });

        fNameEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("First Name", firstName);
            }
        });

        lNameEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog("Last Name", lastName);
            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newFirstName = firstName.getText().toString();
                String newLastName = lastName.getText().toString();
                if (!newFirstName.isEmpty() && !newLastName.isEmpty()) {
                    // Perform photo upload and profile update
                    if (selectedImageBitmap != null) {
                        uploadImageAndUpdateProfile(selectedImageBitmap, newFirstName, newLastName);
                    } else {
                        updateProfileInfo(newFirstName, newLastName, null);
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter first name and last name", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return view;

    }

    private void uploadImageAndUpdateProfile(Bitmap imageBitmap, String newFirstName, String newLastName) {
        // Convert Bitmap to Uri
        Uri imageUri = getImageUri(getActivity(), imageBitmap);
        // Upload image to Firebase Storage
        StorageReference profileImageRef = FirebaseStorage.getInstance().getReference()
                .child("profileImages").child(mAuth.getCurrentUser().getUid() + ".jpg");

        profileImageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            profileImageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                // Update profile info with image URL
                updateProfileInfo(newFirstName, newLastName, imageUrl);
            });
        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to upload image!", Toast.LENGTH_SHORT).show());
    }


    private void updateProfileInfo(String newFirstName, String newLastName, String imageUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            DatabaseReference userRef = mDatabase.child("users").child(userId);
            // Update first name and last name
            userRef.child("firstName").setValue(newFirstName);
            userRef.child("lastName").setValue(newLastName);
            // Update profile image URL if provided
            if (imageUrl != null) {
                userRef.child("profileImageUrl").setValue(imageUrl)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to update profile!", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(getActivity(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }


    private void showEditDialog(String title, final TextView textViewToUpdate) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_name, null);
        EditText editText = dialogView.findViewById(R.id.editText);

        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    String newName = editText.getText().toString();
                    textViewToUpdate.setText(newName);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
                userImg.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}