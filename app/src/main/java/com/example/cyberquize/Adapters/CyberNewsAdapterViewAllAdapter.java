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

import com.example.cyberquize.Models.CyberNewsModel;
import com.example.cyberquize.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CyberNewsAdapterViewAllAdapter extends RecyclerView.Adapter<CyberNewsAdapterViewAllAdapter.myviewholder>{

    Context context;
    ArrayList<CyberNewsModel> list;

    public CyberNewsAdapterViewAllAdapter(Context context, ArrayList<CyberNewsModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CyberNewsAdapterViewAllAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cyber_news_view_all_design,parent,false);

        return new CyberNewsAdapterViewAllAdapter.myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CyberNewsAdapterViewAllAdapter.myviewholder holder, int position) {
        CyberNewsModel model1 = list.get(position);
        holder.cyberNewsHeading.setText(model1.getCyberNewsHeading());
        Picasso.get().load(model1.getCyberNewsImg()).into(holder.cyberNewsImg);

        holder.cyberNewsViewAllLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(model1.getCyberNewsWebUrl()));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class myviewholder extends RecyclerView.ViewHolder{

        TextView cyberNewsHeading;
        ShapeableImageView cyberNewsImg;
        LinearLayout cyberNewsViewAllLy;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            cyberNewsHeading = itemView.findViewById(R.id.cyberNewsViewALlTxtHeading);
            cyberNewsImg = itemView.findViewById(R.id.cyberNewsViewAllImg);
            cyberNewsViewAllLy = itemView.findViewById(R.id.cyberNewsViewAllLy);

        }
    }

}
