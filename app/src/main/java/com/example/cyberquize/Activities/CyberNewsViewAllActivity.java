package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cyberquize.Adapters.CyberNewsAdapter;
import com.example.cyberquize.Adapters.CyberNewsAdapterViewAllAdapter;
import com.example.cyberquize.Models.CyberNewsModel;
import com.example.cyberquize.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class CyberNewsViewAllActivity extends AppCompatActivity {

    ImageView backImg;
    RecyclerView cyberNewsViewAllRv;

    CyberNewsAdapterViewAllAdapter cyberNewsAdapterViewAllAdapter;
    ArrayList<CyberNewsModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyber_news_view_all);

        backImg = findViewById(R.id.cyberNewsViewAllBackImg);
        cyberNewsViewAllRv = findViewById(R.id.cyberNewsViewAllRv);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CyberNews");
        reference.keepSynced(true);
        list = new ArrayList<>();
        cyberNewsViewAllRv.setLayoutManager(new LinearLayoutManager(this));
        cyberNewsAdapterViewAllAdapter = new CyberNewsAdapterViewAllAdapter(this, list);
        cyberNewsViewAllRv.setAdapter(cyberNewsAdapterViewAllAdapter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    list.clear(); // Clear the list before adding new data

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        CyberNewsModel model = dataSnapshot.getValue(CyberNewsModel.class);

                        list.add(model);
                    }

                    // Set up RecyclerView and adapter
                    Collections.reverse(list);

                    cyberNewsAdapterViewAllAdapter.notifyDataSetChanged(); // Notify the adapter about the data set change
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
}