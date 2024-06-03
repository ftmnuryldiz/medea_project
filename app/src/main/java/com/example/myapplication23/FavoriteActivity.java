package com.example.myapplication23;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    BottomNavigationView bottomNavigationView;
    ListView listView;
    List<String> favoriteList;
    ArrayAdapter myArrayAdapter;
    FirebaseUser firebaseUser;
    Quote quote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String userId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle("Favorites");
        favoriteList = new ArrayList<String>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Favorites");
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setSelectedItemId(R.id.bottom_favorite);
            listView=findViewById(R.id.listView);
        myArrayAdapter= new ArrayAdapter(FavoriteActivity.this, android.R.layout.simple_list_item_1,favoriteList);
        listView.setAdapter(myArrayAdapter);

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
                        startActivity(new Intent(getApplicationContext(), DailyReminderActivity.class));
                        return true;
                    }
                    if (menuItem.getItemId() == R.id.bottom_profile) {
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                        return true;
                    }
                    if(menuItem.getItemId()==R.id.bottom_favorite)
                    {
                        return true;
                    }
                    return false;

                };
        });
        DatabaseReference userFavoritesRef = databaseReference.child(userId);

            userFavoritesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteList.clear();
                    if (snapshot.exists()) {


                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            quote=dataSnapshot.getValue(Quote.class);
                            //String quoteFav = dataSnapshot.getValue().toString();

                            if (quote != null) {
                                favoriteList.add(quote.getQuote());
                                Log.e("Firebase", "Reading favorites: " + quote);



                            } else {
                                // Handle the case where there are no favorites
                                Log.d("Favorites", "No favorites found");
                            }
                        }
                        myArrayAdapter.notifyDataSetChanged();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Error reading favorites: " + error.getMessage());
                }
            });

    }
}