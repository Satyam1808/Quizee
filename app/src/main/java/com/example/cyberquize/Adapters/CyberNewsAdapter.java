package com.example.cyberquize.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cyberquize.Models.CyberNewsModel;
import com.example.cyberquize.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CyberNewsAdapter extends RecyclerView.Adapter<CyberNewsAdapter.myviewholder>{

    Context context;
    ArrayList<CyberNewsModel> list;

    public CyberNewsAdapter(Context context, ArrayList<CyberNewsModel> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public CyberNewsAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cyber_news_design,parent,false);

        return new CyberNewsAdapter.myviewholder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull CyberNewsAdapter.myviewholder holder, int position) {
        CyberNewsModel model1 = list.get(position);
        holder.cyberNewsHeading.setText(model1.getCyberNewsHeading());
        Picasso.get().load(model1.getCyberNewsImg()).into(holder.cyberNewsImg);

        holder.cyberNewsImg.setOnClickListener(new View.OnClickListener() {
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


        public myviewholder(@NonNull View itemView) {
            super(itemView);
           cyberNewsHeading = itemView.findViewById(R.id.cyberNewsTxtHeading);
           cyberNewsImg = itemView.findViewById(R.id.cyberNewsImg);

        }
    }

}
