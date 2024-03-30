package com.example.cyberquize.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyberquize.Classes.TextOverlayImageView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import de.hdodenhof.circleimageview.CircleImageView;

public class QuizeScoreActivity extends AppCompatActivity {


    TextView scoreTxt,userName;
    CircleImageView userImg;
    RelativeLayout download_eCertificate;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextOverlayImageView imageView;
    private static final String CERTIFICATE_FILE_NAME = "certificate.pdf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quize_score);

       scoreTxt = findViewById(R.id.scoreText);
       userImg = findViewById(R.id.score_user_img);
       download_eCertificate = findViewById(R.id.download_ly);
       userName = findViewById(R.id.score_user_name);


        Intent intent = getIntent();
        Integer score = intent.getIntExtra("scoreOfQuizOfWeek",0);
        Integer discoverScore = intent.getIntExtra("scoreOfDiscoverQuiz",0);
        int reqCode = intent.getIntExtra("reqCode",0);
        int reqCodeForQoW = intent.getIntExtra("reqCode",0);
        if (reqCodeForQoW==1){
            scoreTxt.setText(score+"/10");
        }
        if (reqCode==2){
            scoreTxt.setText(discoverScore+"/10");
        }


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
                        Picasso.get().load(imgUrl).into(userImg);
                        imageView = findViewById(R.id.cer_img);
                        imageView.setText(userFirstName+" "+userLastName);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }

        download_eCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                imageView.draw(canvas);

                // Save the Bitmap to the device storage
                try {
                    // Define a file name and path where you want to save the image
                    String fileName = "e-Certificate.png";
                    File file = new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS), fileName);

                    // Create a file output stream
                    FileOutputStream fos = new FileOutputStream(file);

                    // Compress and save the Bitmap to the output stream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

                    // Close the output stream
                    fos.close();

                    // Notify the user that the image has been saved
                    Toast.makeText(QuizeScoreActivity.this, "Image saved to " + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle any errors that occur during the file saving process
                }

            }
        });


    }



    @Override
    public void onBackPressed() {
        // Go to the home activity
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish(); // Finish the current activity
    }


}