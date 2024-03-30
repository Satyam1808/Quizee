package com.example.cyberquize.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberquize.Activities.DiscoverQuizActivity;
import com.example.cyberquize.Models.CyberNewsModel;
import com.example.cyberquize.Models.DiscoverModel;
import com.example.cyberquize.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverAdapter.myviewholder>{


    Context context;
    ArrayList<DiscoverModel> list;

    public DiscoverAdapter(Context context, ArrayList<DiscoverModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public DiscoverAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_item_design,parent,false);

        return new DiscoverAdapter.myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull DiscoverAdapter.myviewholder holder, int position) {
        DiscoverModel model1 = list.get(position);
        holder.discoverItemTxt.setText(model1.getTopicName());
        Picasso.get().load(model1.getTopicImg()).into(holder.discoverItemImg);

        holder.discoverLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(context, DiscoverQuizActivity.class);
              intent.putExtra("discoverQuizImg",model1.getTopicImg());
              intent.putExtra("discoverQuizName",model1.getTopicName());
              context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class myviewholder extends RecyclerView.ViewHolder{

        TextView discoverItemTxt;
        ShapeableImageView discoverItemImg;

        LinearLayout discoverLy;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            discoverItemTxt = itemView.findViewById(R.id.discoverTopicName);
            discoverItemImg = itemView.findViewById(R.id.discoverImg);
            discoverLy = itemView.findViewById(R.id.discoverLy);



        }
    }


}
