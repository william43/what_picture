package com.example.willaboy.whatpicture;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Activity_Main extends AppCompatActivity {

    private ImageView   seeStanding;
    private ImageView   seeConnections;
    private ImageView   addPhoto;
    private ImageView   seeProfile;
    private Button      playNow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        seeStanding = (ImageView) findViewById(R.id.seeAchievements);
        seeConnections = (ImageView) findViewById(R.id.seeConnections);
        addPhoto = (ImageView) findViewById(R.id.addPhoto);
        seeProfile = (ImageView) findViewById(R.id.seeProfile);
        playNow = (Button) findViewById(R.id.playNow);

        playNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Main.this, Activity_ChoosePuzzleMenu.class);
                startActivity(intent);
            }
        });

        seeStanding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        seeConnections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Main.this, Activity_AddPhoto.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        seeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });







    }
}
