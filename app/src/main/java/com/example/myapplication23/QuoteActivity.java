package com.example.myapplication23;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

public class QuoteActivity extends AppCompatActivity {
    EditText editTextquote;
    Button buttonSave;
    private DatabaseReference databaseReference;
    private FirebaseAuth authProfile;
Quote quote;
    String userID;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);

        editTextquote = findViewById(R.id.edit_quote);
        buttonSave = findViewById(R.id.button_add_quote);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        userID = firebaseUser.getUid();
        getSupportActionBar().setTitle("Enter Positive Quotes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Quote");
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextquote.getText().toString();
                if (message != null && !message.isEmpty()) {
                    Quote quote = new Quote(message);
                    databaseReference.push().setValue(quote, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@NonNull DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(QuoteActivity.this, "Message saved", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuoteActivity.this, DailyReminderActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(QuoteActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(QuoteActivity.this, "Please enter a quote.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }
    }


















