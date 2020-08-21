package com.example.willaboy.whatpicture;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Activity_Login extends AppCompatActivity{


    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();
    private DatabaseReference userDataReference;

    private EditText    usernameText;
    private EditText    passwordText;
    private Button      loginBtn;
    private Button      registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);



        usernameText = (EditText) findViewById(R.id.username);
        passwordText = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginbtn);
        registerBtn = (Button) findViewById(R.id.registerbtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!usernameText.getText().toString().isEmpty() && !passwordText.getText().toString().isEmpty()){
                    userDataReference = databaseReference.child("Users");
                    userDataReference.orderByChild("uname").equalTo(usernameText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Model_User user;
                                int check = 0;
                                for(DataSnapshot childUser: dataSnapshot.getChildren()){
                                    user = childUser.getValue(Model_User.class);
                                    if(user.getPassword().equals(passwordText.getText().toString())){
                                        check++;
                                    }
                                    Log.d("MAMAMO", "Users: " + user.getUname() + " HEHE");
                                }
                                if(check > 0){
                                    Intent intent = new Intent(Activity_Login.this, Activity_Main.class);
                                    String userN = usernameText.getText().toString();
                                    intent.putExtra("username", userN);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(Activity_Login.this, "The Username and Password does not match", Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(Activity_Login.this, "Wrong input Username and Password", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else
                    Toast.makeText(Activity_Login.this, "Please Fill Up the Empty Text Box", Toast.LENGTH_LONG).show();

            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(intent);
            }
        });

    }
}
