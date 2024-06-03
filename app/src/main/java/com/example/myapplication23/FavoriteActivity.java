package com.example.myapplication23;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private DatabaseReference databaseReference;
    List<String> favoriteList;
    private List<String> quoteIds;
    FavoriteAdapter adapter;
    FirebaseUser firebaseUser;
    String userId;
    RecyclerView recyclerView;
    Quote quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
        getSupportActionBar().setTitle("Favorites");
        favoriteList = new ArrayList<String>();
        databaseReference = firebaseDatabase.getReference().child("Favorites");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_favorite);
        recyclerView = findViewById(R.id.recycler);

quoteIds=new ArrayList<>();
        adapter = new FavoriteAdapter(favoriteList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        setupFirebaseListener();
        setupSwipeToDelete();

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
                if (menuItem.getItemId() == R.id.bottom_favorite) {
                    return true;
                }
                return false;

            }


        });
    }
    private void setupFirebaseListener() {
        DatabaseReference userFavoritesRef = databaseReference.child(userId);
        userFavoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteList.clear();
                quoteIds.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Quote quote = dataSnapshot.getValue(Quote.class);
                        if (quote != null) {
                            favoriteList.add(quote.getQuote());
                            quoteIds.add(dataSnapshot.getKey());
                        } else {
                            Log.d("Favorites", "No favorites found");
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error reading favorites: " + error.getMessage());
            }
        });
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // We are not moving items, so return false
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                // Remove item from your data source
                String quoteToRemove = favoriteList.get(position);
                String quoteId = quoteIds.get(position);

                Log.d("SwipeToDelete", "Removing quote: " + quoteToRemove + " with ID: " + quoteId);

                // Remove from Firebase
                DatabaseReference userFavoritesRef = databaseReference.child(userId).child(quoteId);
                userFavoritesRef.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("SwipeToDelete", "Successfully removed from Firebase");
                        Toast.makeText(FavoriteActivity.this,"You have successfully deleted.",Toast.LENGTH_SHORT).show();

                    } else {
                        Log.e("SwipeToDelete", "Failed to remove from Firebase", task.getException());
                    }
                });


                adapter.removeItem(position);

                quoteIds.remove(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }


        private String sanitizeKey (String key){
            return key.replace(".", "_").replace("$", "_")
                    .replace("#", "_").replace("[", "_").replace("]", "_");
        }
    }





