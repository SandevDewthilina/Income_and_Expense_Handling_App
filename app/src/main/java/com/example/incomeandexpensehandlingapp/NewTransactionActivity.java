package com.example.incomeandexpensehandlingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class NewTransactionActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    String[] type = { "Income", "Expense"};
    private String transaction_type = null;
    private String userId = null;

    //firebase connection
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        //toolbar
        Toolbar toolbar = findViewById(R.id.new_transaction_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Enter a new transaction");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase connection
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (currentUser != null) {
                userId = currentUser.getUid();
        }

        //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.new_transaction_type);
        final TextView mAmount = findViewById(R.id.new_transaction_amount);
        final TextView mDesc = findViewById(R.id.new_transaction_desc);
        spin.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        final Button new_transaction_btn = findViewById(R.id.new_transaction_btn);

        new_transaction_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new_transaction_btn.setEnabled(false);

                String amount = mAmount.getText().toString();
                String desc = mDesc.getText().toString();

                HashMap<String, Object> transactionEntry = new HashMap<>();
                transactionEntry.put("type", transaction_type);
                transactionEntry.put("amount", amount);
                transactionEntry.put("desc", desc);
                transactionEntry.put("time_stamp", FieldValue.serverTimestamp());

                firebaseFirestore.collection("BankTransactions/" + userId + "/transactions").add(transactionEntry)
                        .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(NewTransactionActivity.this, "Entering transaction is successful", Toast.LENGTH_LONG).show();

                                    Intent pastIntent = new Intent(NewTransactionActivity.this, PastTransactionActivity.class);
                                    startActivity(pastIntent);

                                } else {

                                    Toast.makeText(NewTransactionActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                                new_transaction_btn.setEnabled(true);

                            }
                        });

            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            transaction_type = type[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}