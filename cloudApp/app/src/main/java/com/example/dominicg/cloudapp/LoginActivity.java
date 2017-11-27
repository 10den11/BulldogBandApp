package com.example.dominicg.cloudapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private List<BandMember> roster;
    private DataSnapshot dataSnapshot;
    private DatabaseReference myRoster;
    private List<BandMember> userList;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myRoster = FirebaseDatabase.getInstance().getReference("Users");
        userList = new ArrayList<>();

        Button loginButton = (Button) findViewById(R.id.LoginButton);
        final EditText loginText = (EditText) findViewById(R.id.inputUsername);
        final EditText passwordText = (EditText) findViewById(R.id.inputPassword);
        myRoster.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    userList.add(postSnapshot.getValue(BandMember.class));
                    Log.d(TAG, "onDataChange: " + postSnapshot.getValue(BandMember.class).toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isCorrect = false;
                for(int k = 0; k < userList.size(); k++) {
                    if(userList.get(k).getUserName().equals(loginText.getText().toString())) {
                        if(userList.get(k).getPassword().equals(passwordText.getText().toString())) {
                            isCorrect = true;
                        }
                    }
                }
                if(isCorrect) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Wrong Username or Password",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
