package com.example.willaboy.whatpicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Activity_Register extends AppCompatActivity {


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private EditText    fname;
    private EditText    uname;
    private EditText    password;
    private Button      regBtn;


    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_view);

        Intent intent = getIntent();

        fname = (EditText) findViewById(R.id.playername);
        uname = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        regBtn = (Button) findViewById(R.id.btnRegister);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fname.getText().toString().isEmpty() && !uname.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    Model_User user = new Model_User(fname.getText().toString(), uname.getText().toString(), password.getText().toString());

                    databaseReference.child("Users").child(user.getUname()).setValue(user);

                    finish();
                }
                else {
                    Toast.makeText(Activity_Register.this, "Please Fill Up all the Text Fields", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
