package com.example.myapplication23;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MoodActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    ListView listView;


    ArrayList<Pair<String, String>> itemList;


    FirebaseDatabase firebaseDatabase;
    LayoutInflater layoutInflater;


    public MoodActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemList = new ArrayList<>();
        setContentView(R.layout.activity_mood);
        firebaseDatabase = FirebaseDatabase.getInstance();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listView = findViewById(R.id.listView);

        getSupportActionBar().setTitle("Moods");


        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Moods");

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int calmCount = 0, manicCount = 0, happyCount = 0, sadCount = 0, angryCount = 0;
                if (snapshot.exists()) {

                    for (DataSnapshot moodSnapshot : snapshot.getChildren()) {
                        UsersMood moodEntry = moodSnapshot.getValue(UsersMood.class);
                        String mood = moodSnapshot.child("mod").getValue(String.class);
                        switch (mood) {
                            case "Calm":
                                calmCount++;
                                break;
                            case "Manic":
                                manicCount++;
                                break;
                            case "Happy":
                                happyCount++;
                                break;
                            case "Sad":
                                sadCount++;
                                break;
                            case "Angry":
                                angryCount++;
                                break;
                        }

                        if (moodEntry != null) {
                            Pair<String, String> moodPair = new Pair<>(moodEntry.mod, moodEntry.date);
                            itemList.add(0, moodPair);
                        }
                    }
                    drawBarChart(calmCount, manicCount, happyCount, sadCount, angryCount);

                    Log.d("Firebase", "itemList size: " + itemList.size());

                    if (listView != null) {
                        // Adapter oluştururken itemList'i kullanın
                        MoodAdapter adapter = new MoodAdapter(itemList);
                        listView.setAdapter(adapter);
                    } else {
                        Log.e("Firebase", "ListView is null.");
                    }
                } else {
                    Log.e("Firebase", "Snapshot does not exist.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MoodActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_mood);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.bottom_home) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    return true;
                }
                if (menuItem.getItemId() == R.id.bottom_mood) {
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
                    startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                    return true;
                }

                return false;

            }

        });
    }

    private void drawBarChart(int calmCount, int manicCount, int happyCount, int sadCount, int angryCount) {
        BarChart barChart = findViewById(R.id.barChart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, happyCount));
        entries.add(new BarEntry(1, calmCount));
        entries.add(new BarEntry(2, manicCount));
        entries.add(new BarEntry(3, angryCount));
        entries.add(new BarEntry(4, sadCount));

        BarDataSet dataSet = new BarDataSet(entries, "Mood Counts");
        int[] pastelColors = new int[]{
                Color.rgb(186, 255, 201),
                Color.rgb(186, 225, 255),
                Color.rgb(255, 200, 165),
                Color.rgb(255, 179, 186),
                Color.rgb(221, 160, 221),
        };
        dataSet.setColors(pastelColors);



        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.9f);

        barChart.setData(data);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Happy", "Calm", "Manic", "Angry", "Sad"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setGranularity(1f);
        rightAxis.setGranularity(1f);
//Bazı x özelliklerini devre dışı bırakır.
        barChart.getAxisRight().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.setFitBars(true);
        barChart.invalidate();
//Animasyon 2 saniye gösterilsin
        barChart.animateY(2000);
    }
    public LayoutInflater getLayoutInflater() {

        return layoutInflater;
    }
}











