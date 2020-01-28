package com.example.incomeandexpensehandlingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    //firebase connection
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    //ui elements
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Authenticating and get the current user
        * */

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        /*
        * Making ui elements
        * */
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()) {

            case R.id.main_menu_logout:
                mAuth.signOut();
                sendToStart();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    private void sendToStart() {

        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (currentUser == null) {

            Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(startIntent);
            finish();

        }

    }
}
