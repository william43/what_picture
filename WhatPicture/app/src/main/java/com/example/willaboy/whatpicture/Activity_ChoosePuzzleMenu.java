package com.example.willaboy.whatpicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Activity_ChoosePuzzleMenu extends AppCompatActivity {

    private Button chooseRandom;
    private Button chooseCategory;

    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);


        chooseRandom = (Button) findViewById(R.id.choose_Random);
        chooseCategory = (Button) findViewById(R.id.choose_Categories);

        chooseRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ChoosePuzzleMenu.this, Activity_PuzzleGame.class);
                startActivity(intent);
            }
        });

        chooseCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_ChoosePuzzleMenu.this, Activity_ViewPhotosCategory.class);
                startActivity(intent);
            }
        });

    }

}
