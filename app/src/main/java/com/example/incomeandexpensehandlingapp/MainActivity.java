package com.example.incomeandexpensehandlingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    //firebase connection
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;

    //ui elements
    private Toolbar toolbar;
    private Button manageBankBtn, pastTransactionBtn, newTransactionBtn;
    private TextView income, expense;

    private String userId;
    private double total_income = 0d;
    private double total_expense = 0d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        * Authenticating and get the current user
        * */

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {

            userId = currentUser.getUid();

        }

        /*
        * Making ui elements
        * */
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        manageBankBtn = findViewById(R.id.main_mange_bank_account_btn);
        pastTransactionBtn = findViewById(R.id.main_past_transaction_btn);
        newTransactionBtn = findViewById(R.id.main_new_transaction_btn);
        income = findViewById(R.id.dashboard_income_txt);
        expense = findViewById(R.id.dashboard_expence_txt);

        /*
        * set Listners to the buttons
        * */
        manageBankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bankIntent = new Intent(MainActivity.this, ManageBankActivity.class);
                startActivity(bankIntent);
            }
        });

        newTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newTransactionIntent = new Intent(MainActivity.this, NewTransactionActivity.class);
                startActivity(newTransactionIntent);
            }
        });

        pastTransactionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pastTransactionIntent = new Intent(MainActivity.this, PastTransactionActivity.class);
                startActivity(pastTransactionIntent);
            }
        });

        calculateTurnover();

    }

    private void calculateTurnover() {

        if (currentUser != null) {

            firebaseFirestore.collection("BankTransactions/" + userId + "/transactions")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                            for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                                if (doc.getType() == DocumentChange.Type.ADDED) {

                                    String type = doc.getDocument().getString("type");

                                    if (type.equals("Income")) {

                                        total_income += Double.parseDouble(doc.getDocument().getString("amount"));

                                    } else {

                                        total_expense += Double.parseDouble(doc.getDocument().getString("amount"));

                                    }

                                }
                            }

                            income.setText(String.valueOf(total_income));
                            expense.setText(String.valueOf(total_expense));

                        }
                    });
        }

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
