package com.example.cyberquize.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cyberquize.Activities.CyberNewsViewAllActivity;
import com.example.cyberquize.Activities.QuizOfWeekActivity;
import com.example.cyberquize.Activities.discoverViewAllActivity;
import com.example.cyberquize.Adapters.CyberNewsAdapter;
import com.example.cyberquize.Adapters.DiscoverAdapter;
import com.example.cyberquize.Models.CyberNewsModel;
import com.example.cyberquize.Models.DiscoverModel;
import com.example.cyberquize.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

  CircleImageView userProfileImg;
  ShapeableImageView quizOfWeekImage,webCert;
  TextView discoverViewTxt,tipsAndBestPracticeViewTxt,cyberNewsViewTxt,userName;
  RecyclerView discoverRv,tipsAndBestPracticeRv,cyberNewsRv;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    CyberNewsAdapter cyberNewsAdapter;
    ArrayList<CyberNewsModel> list;
    DiscoverAdapter discoverAdapter;
    ArrayList<DiscoverModel> discoverList;
    String imgUrl;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userProfileImg = view.findViewById(R.id.user_profile_pic);
        userName = view.findViewById(R.id.user_name);
        quizOfWeekImage = view.findViewById(R.id.quize_of_week_img);
        webCert = view.findViewById(R.id.goto_certin_website_Img);
        discoverViewTxt = view.findViewById(R.id.discover_viewTxt);
        cyberNewsViewTxt = view.findViewById(R.id.cyberNewsViewTxt);
        discoverRv = view.findViewById(R.id.discoverRv);
        cyberNewsRv = view.findViewById(R.id.cyberNewsRv);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
                        userName.setText(userFirstName+" "+userLastName);
                        Picasso.get().load(imgUrl).into(userProfileImg);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        webCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://cert-in.org.in/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CyberNews");
        reference.keepSynced(true);
        list = new ArrayList<>();


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    list.clear(); // Clear the list before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CyberNewsModel model = dataSnapshot.getValue(CyberNewsModel.class);
                        list.add(model);
                    }

                    Collections.reverse(list);
                    ArrayList<CyberNewsModel> listSecond = new ArrayList<>(list.subList(0, Math.min(list.size(), 4)));

                    // Set up RecyclerView and adapter
                    cyberNewsRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    cyberNewsAdapter = new CyberNewsAdapter(getContext(), listSecond);
                    cyberNewsRv.setAdapter(cyberNewsAdapter);

                   //cyberNewsAdapter.notifyDataSetChanged(); // Notify the adapter about the data set change
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        cyberNewsViewTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getContext(), CyberNewsViewAllActivity.class));
            }
        });

        DatabaseReference quizOfWeekReference = FirebaseDatabase.getInstance().getReference("QuizOfWeek");

        quizOfWeekReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    imgUrl = snapshot.child("QuizeOfWeek01").child("quizOfWeekImg").getValue(String.class);

                    Picasso.get().load(imgUrl).into(quizOfWeekImage);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

quizOfWeekImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
     Intent intent = new  Intent(getContext(), QuizOfWeekActivity.class);
     intent.putExtra("quickImg",imgUrl);
     startActivity(intent);
    }
});

        DatabaseReference discoverRef = FirebaseDatabase.getInstance().getReference("DiscoverQuiz");
        discoverRef.keepSynced(true);
        discoverList = new ArrayList<>();


        discoverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                   discoverList.clear(); // Clear the list before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DiscoverModel model = dataSnapshot.getValue(DiscoverModel.class);
                        discoverList.add(model);
                    }

                    Collections.reverse(discoverList);
                    ArrayList<DiscoverModel> disListSecond = new ArrayList<>(discoverList.subList(0, Math.min(discoverList.size(), 4)));

                    // Set up RecyclerView and adapter
                    discoverRv.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                    discoverAdapter = new DiscoverAdapter(getContext(), disListSecond);
                    discoverRv.setAdapter(discoverAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

discoverViewTxt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       startActivity(new Intent(getContext(), discoverViewAllActivity.class));
    }
});
        return view;
    }
}