package com.example.cyberquize.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cyberquize.Adapters.DiscoverAdapter;
import com.example.cyberquize.Models.DiscoverModel;
import com.example.cyberquize.R;

import java.util.ArrayList;

public class MyQuizActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView myQuizRv;
    ImageView backImg;
    DiscoverAdapter adapter;
    ArrayList<DiscoverModel> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_quiz);

        myQuizRv = findViewById(R.id.myQuizRv);
        toolbar = findViewById(R.id.myQuizToolbar);

        backImg = findViewById(R.id.myQuizBackImg);





        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}