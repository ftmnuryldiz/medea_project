package com.example.myapplication23;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    private FirebaseAuth authProfile;
    TextView textView;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencee;
    FirebaseUser firebaseUser;
    String userID;
    Button homeButton;
    int clickCountHappy, clickCountCalm, clickCountManic, clickCountAngry, clickCountSad;
    int puan;
    ArrayList<String> quotesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getSupportActionBar().setTitle("Home Page");


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Button buttonHappy = findViewById(R.id.happy);
        Button buttonCalm = findViewById(R.id.calm);
        Button buttonManic = findViewById(R.id.manic);
        Button buttonAngry = findViewById(R.id.angry);
        Button buttonSad = findViewById(R.id.sad);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();
        databaseReferencee = FirebaseDatabase.getInstance().getReference().child("Quote");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Moods");
        userID = firebaseUser.getUid();

        homeButton = findViewById(R.id.homeButton);
        quotesList = new ArrayList<>();
        textView = findViewById(R.id.textView);
        Random random = new Random();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_home) {
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_mood) {
                    startActivity(new Intent(getApplicationContext(), MoodActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_comment) {
                    startActivity(new Intent(getApplicationContext(), DailyReminderActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_profile) {
                    startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_favorite) {
                    startActivity(new Intent(getApplicationContext(),   FavoriteActivity.class));
                }
                return false;

            }

        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, DailyReminderActivity.class);
                startActivity(intent);
            }
        });
        buttonHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                String formattedDate = sdf.format(new Date(timestamp));
                String mood = "Happy";

                UsersMood moodEntry = new UsersMood(mood, formattedDate);

                databaseReference.child(userID).push().setValue(moodEntry);

                Toast.makeText(HomeActivity.this, "Keep It Smile", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,HappyActivity.class);
                startActivity(intent);


            }

        });

        buttonCalm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                String formattedDate = sdf.format(new Date(timestamp));
                String mood = "Calm";

                UsersMood moodEntry = new UsersMood(mood, formattedDate);

                databaseReference.child(userID).push().setValue(moodEntry);

                Toast.makeText(HomeActivity.this, "Feel It Peace", Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(HomeActivity.this,CalmActivity.class);
                startActivity(intent);
            }

        });


        buttonManic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                String formattedDate = sdf.format(new Date(timestamp));
                String mood = "Manic";

                UsersMood moodEntry = new UsersMood(mood, formattedDate);

                databaseReference.child(userID).push().setValue(moodEntry);

                Toast.makeText(HomeActivity.this, "Go to doctor.", Toast.LENGTH_SHORT).show();


                Intent intent=new Intent(HomeActivity.this,ManicActivity.class);
                startActivity(intent);
            }

        });


        buttonAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timestamp = System.currentTimeMillis();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                String formattedDate = sdf.format(new Date(timestamp));

                String mood = "Angry";

                UsersMood moodEntry = new UsersMood(mood, formattedDate);

                databaseReference.child(userID).push().setValue(moodEntry);

                Toast.makeText(HomeActivity.this, "Deep breath and calm down", Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(HomeActivity.this,AngryActivity.class);
                startActivity(intent);

            }

        });

        buttonSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long timestamp = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());

                String formattedDate = sdf.format(new Date(timestamp));
                String mood = "Sad";

                UsersMood moodEntry = new UsersMood(mood, formattedDate);
                databaseReference.child(userID).push().setValue(moodEntry);

                Toast.makeText(HomeActivity.this, "This is normal. Sometimes you cry, sometimes you laugh.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this, SadActivity.class);
                startActivity(intent);
            }



        });
        databaseReferencee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String quote = snapshot.child("quote").getValue(String.class);
                    quotesList.add(quote);
                }
                displayRandomQuote();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    private void displayRandomQuote() {
        if (!quotesList.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(quotesList.size());
            String randomQuote = quotesList.get(randomIndex);
            textView.setText(randomQuote);
        }
    }


}








