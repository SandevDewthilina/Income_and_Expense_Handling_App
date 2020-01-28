package com.example.incomeandexpensehandlingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartActivity extends AppCompatActivity {

    /*
    * Firebase connection
    * */
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    /*
    * Ui items
    * */
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //firebase decleration
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //ui elements
        startBtn = findViewById(R.id.sendLoginBtn);

        //listner to the button
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(loginIntent);

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser != null) {

            Intent mainIntent = new Intent(StartActivity.this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();

        }

    }
}

