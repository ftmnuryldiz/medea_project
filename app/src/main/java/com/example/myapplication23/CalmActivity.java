package com.example.myapplication23;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalmActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private ArrayList<String> quoteArrayList;
    private CalmAdapter quotePageAdapter;
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calm);

        viewPager2 = findViewById(R.id.viewPager2);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Calm Quote");


        quoteArrayList = new ArrayList<>();

        getSupportActionBar().setTitle("Calm Quotes");

        quotePageAdapter = new CalmAdapter(quoteArrayList);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    quoteArrayList.clear();  // Clear existing quotes before adding new ones
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        String quote = dataSnapshot.getValue(String.class);
                        if (quote != null) {
                            quoteArrayList.add(quote);
                            Log.d("Firebase", "Calm quote added: " + quote);
                        }
                    }


                    viewPager2.setAdapter(quotePageAdapter);
                    quotePageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}

