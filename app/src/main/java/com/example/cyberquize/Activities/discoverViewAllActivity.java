package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cyberquize.Adapters.DiscoverAdapter;
import com.example.cyberquize.Models.DiscoverModel;
import com.example.cyberquize.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class discoverViewAllActivity extends AppCompatActivity {

    Toolbar discoverToolbar;
    TextView toolbarText;
    RecyclerView discoverViewAllRv;
    ImageView discoverBackImg;

    DiscoverAdapter discoverAdapter;
    ArrayList<DiscoverModel> discoverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_view_all);


        discoverToolbar = findViewById(R.id.discoverToolbarView);
        toolbarText = discoverToolbar.findViewById(R.id.discoverViewAllToolbartxt);
        discoverBackImg = discoverToolbar.findViewById(R.id.discoverViewAllBackImg);
        discoverViewAllRv = findViewById(R.id.discoverViewAllRv);


        DatabaseReference discoverRef = FirebaseDatabase.getInstance().getReference("DiscoverQuiz");
        discoverRef.keepSynced(true);
        discoverList = new ArrayList<>();
        discoverViewAllRv.setLayoutManager(new GridLayoutManager(this,2));
        discoverAdapter = new DiscoverAdapter(this, discoverList);
        discoverViewAllRv.setAdapter(discoverAdapter);
        discoverRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    discoverList.clear(); // Clear the list before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DiscoverModel model = dataSnapshot.getValue(DiscoverModel.class);
                        discoverList.add(model);
                    }

             discoverAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}