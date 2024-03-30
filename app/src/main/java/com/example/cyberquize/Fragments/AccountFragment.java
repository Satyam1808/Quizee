package com.example.cyberquize.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.cyberquize.Activities.AboutUsActivity;
import com.example.cyberquize.Activities.OtpAuthenticationActivity;
import com.example.cyberquize.BottomSheetFragments.PersonalInfoBottomSheet;
import com.example.cyberquize.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class AccountFragment extends Fragment {

    RelativeLayout personalInfoLy,myQuizLy,rateUsLy,shareLy,aboutLy,logOutLy;
    TextView userAccountNameTxt;
    CircleImageView userAccountUserImage;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public AccountFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_account, container, false);

        personalInfoLy = view.findViewById(R.id.personalInfoLy);
        myQuizLy = view.findViewById(R.id.myQuizLy);
        rateUsLy = view.findViewById(R.id.rateUsLy);
        shareLy = view.findViewById(R.id.shareLy);
        aboutLy = view.findViewById(R.id.aboutUsLy);
        logOutLy = view.findViewById(R.id.logOutLy);
        userAccountNameTxt = view.findViewById(R.id.user_account_name);
        userAccountUserImage = view.findViewById(R.id.user_acc_profile);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        personalInfoLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               PersonalInfoBottomSheet personalInfoBottomSheetFragment = new PersonalInfoBottomSheet();
                personalInfoBottomSheetFragment.show(getActivity().getSupportFragmentManager(),personalInfoBottomSheetFragment.getTag());

            }
        });

        aboutLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AboutUsActivity.class));
            }
        });

        logOutLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle("Are you sure you want to log out?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Sign out the user
                                FirebaseAuth.getInstance().signOut();

                                // Navigate to OtpAuthenticationActivity
                                Intent intent = new Intent(getActivity(), OtpAuthenticationActivity.class);
                                startActivity(intent);

                                // Finish the current activity
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel", null) // No action needed on cancel
                        .show();
            }

        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userFirstName = snapshot.child(userId).child("firstName").getValue(String.class);
                        String userLastName = snapshot.child(userId).child("lastName").getValue(String.class);
                        String imgUrl = snapshot.child(userId).child("profileImageUrl").getValue(String.class);
                        // Set fetched name on TextViews
                      userAccountNameTxt.setText(userFirstName+" "+userLastName);
                        Picasso.get().load(imgUrl).into(userAccountUserImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }


        return view;
    }
}