package com.example.incomeandexpensehandlingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;

public class ManageBankActivity extends AppCompatActivity {

    /*
    * firebase connection
    * */
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    /*
     *make ui elements
     */
    private Toolbar toolbar;
    private TextView newBalance;
    private EditText newAmountTxt;
    private Button enterBtn;
    private Button checkBalanceBtn;
    private ProgressBar progressBar;

    /*
    * user details
    * */
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bank);

        toolbar = findViewById(R.id.bank_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Bank deposites");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        //user id
        userId = currentUser.getUid();

        //ui items
        newBalance = findViewById(R.id.bank_balance_txt);
        newAmountTxt = findViewById(R.id.new_balance_enter_txt);
        enterBtn = findViewById(R.id.enter_bank_balance_btn);
        checkBalanceBtn = findViewById(R.id.check_bank_balance_btn);
        progressBar = findViewById(R.id.manage_bank_account_progress);

        //set visibility of the progressbar
        progressBar.setVisibility(View.VISIBLE);

        //update textbox
        readBankBalanceFromdatabase();

        //listener
        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String checkedBankBalance = newAmountTxt.getText().toString();

                HashMap<String, String> bankBalanceEntry = new HashMap<>();
                bankBalanceEntry.put("bank_balance", checkedBankBalance);
                //bankBalanceEntry.put("time_entered", FieldValue.serverTimestamp());

                //databaseReference.child("updatedBalance").child(userId).removeValue();
                databaseReference.child("updatedBalance").child(userId).setValue(bankBalanceEntry)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                               readBankBalanceFromdatabase();

                            } else {

                                Toast.makeText(ManageBankActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }

                        }
                    });

            }
        });

        //listner to open the phone app
        checkBalanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialPhoneNumber("0112303080");

            }
        });

    }

    private void readBankBalanceFromdatabase() {

        databaseReference.child("updatedBalance").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("bank_balance").exists()) {

                    String bank_balance_received = dataSnapshot.child("bank_balance").getValue().toString();

                    newBalance.setText(bank_balance_received);
                    progressBar.setVisibility(View.INVISIBLE);
                    newAmountTxt.setText(null);

                } else {

                    newBalance.setText("00.00");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });

    }

    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
