package com.example.myapplication23;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class DailyReminderActivity extends AppCompatActivity {

    Button buttonAddQuote;
    BottomNavigationView bottomNavigationView;


    FirebaseUser firebaseUser;
    Quote quote;
    String quoteId;
    Button favoriteButton;
    private ViewPager2 viewPager;
    private FirebaseAuth authProfile;
    FirebaseDatabase firebaseDatabase;
    private QuoteAdapter quoteAdapter;
    private ArrayList<Quote> quoteArrayList;
    private ArrayList<String> quoteArrayIdlist;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencee;
    Button plus;
    private AnimatedVectorDrawable emptyHeart;
    private AnimatedVectorDrawable fillHeart;
    private boolean full = false;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getSupportActionBar().setTitle("Possitive Quotes");


        databaseReference = firebaseDatabase.getReference().child("Quote");
        databaseReferencee = firebaseDatabase.getReference().child("Favorites");

        setContentView(R.layout.activity_daily_reminder);


        viewPager = findViewById(R.id.viewPager2);

        plus = findViewById(R.id.imagePlusoneOne);
        favoriteButton = findViewById(R.id.buttonFavorite);



        quoteArrayList = new ArrayList<>();
        quoteArrayIdlist = new ArrayList<>();
        quoteAdapter = new QuoteAdapter(quoteArrayList);
        viewPager.setAdapter(quoteAdapter);

        emptyHeart
                = (AnimatedVectorDrawable)
                getDrawable(
                        R.drawable.avd_heart_empty);
        fillHeart
                = (AnimatedVectorDrawable)
                getDrawable(
                        R.drawable.avd_heart_fill);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_comment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_mood) {
                    startActivity(new Intent(getApplicationContext(), MoodActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_comment) {
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_favorite) {
                    startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                    return true;
                }

                return false;

            }
        });
//Firebase'Den okuma işlemleri
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Quote quote = dataSnapshot.getValue(Quote.class);
                        quoteId = dataSnapshot.getKey();
                        if (quote != null) {
                            Collections.shuffle
                        (quoteArrayList);
                            quoteArrayList.add(quote);
                            quoteArrayIdlist.add(quoteId);
                            Log.d("Firebase", "Quote added: " + quote.getQuote());

                        }

                    }
                    quoteAdapter.notifyDataSetChanged();
                } else {

                    Log.d("Firebase", "No data found at the reference.");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Veri okuma işlemi iptal edildiğinde burası çalışır.
                Log.w("Firebase", "Failed to read value.", error.toException());
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DailyReminderActivity.this, QuoteActivity.class);
                startActivity(intent);

            }


        });
//Favorilere okuma işlemi

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = viewPager.getCurrentItem();


               // quoteId = quoteArrayIdlist.get(currentPosition);

                if (quoteId != null) {
                    addToFavorites();
                    Toast.makeText(DailyReminderActivity.this, "The quote is added to favorites.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DailyReminderActivity.this, "The quote isn't added to favorites.", Toast.LENGTH_SHORT).show();
                }

                AnimatedVectorDrawable drawable = full ? emptyHeart : fillHeart;
                favoriteButton.setBackground(drawable);
                drawable.start();
                full = !full;
                resetFavoriteButton();

            }


            public void addToFavorites() {
                String userId = firebaseUser.getUid();
                DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference("Favorites");
                Quote quote = quoteArrayList.get(currentPosition);

                if (quote != null) {
                    favoritesRef.child(userId).push().setValue(quote); // Benzersiz anahtar ile ekleme yap
                } else {
                    Log.e("AddToFavorites", "Quote is null at position: " + currentPosition);
                }
            }


        });


    }
    public void resetFavoriteButton() {
        full = false;
        favoriteButton.setBackground(emptyHeart);
        emptyHeart.start();
    }
}






