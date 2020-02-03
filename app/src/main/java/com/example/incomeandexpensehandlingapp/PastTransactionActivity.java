package com.example.incomeandexpensehandlingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class PastTransactionActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private TextView error_message;
    private List<Transactions> transactionsList;
    private RecyclerView transaction_list_view;

    private String userId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_transaction);

        toolbar = findViewById(R.id.past_transaction_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("See your transaction history");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        transaction_list_view = findViewById(R.id.past_transaction_list);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



            userId = currentUser.getUid();



        transactionsList = new ArrayList<>();
        final TransactionAdapter transactionAdapter = new TransactionAdapter(transactionsList);
        transaction_list_view.setHasFixedSize(true);
        transaction_list_view.setLayoutManager(new LinearLayoutManager(this));
        transaction_list_view.setAdapter(transactionAdapter);


        Query query = firebaseFirestore.collection("BankTransactions/" + userId + "/transactions")
                .orderBy("time_stamp", Query.Direction.DESCENDING)
                .limit(30);

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    error_message = findViewById(R.id.error_message);
                    error_message.setVisibility(View.INVISIBLE);

                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {

                        if (doc.getType() == DocumentChange.Type.ADDED) {

                            Transactions transactions = doc.getDocument().toObject(Transactions.class);

                            transactionsList.add(transactions);
                            transactionAdapter.notifyDataSetChanged();

                        }

                    }

                } else {

                    error_message = findViewById(R.id.error_message);
                    error_message.setVisibility(View.VISIBLE);

                }

            }
        });

    }

}
