package com.example.myapplication23;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class QuoteWorker extends Worker {

    public QuoteWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Quote");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> quotes = (HashMap<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> entry : quotes.entrySet()) {
                    Map singleQuote = (Map) entry.getValue();
                    String quoteText = (String) singleQuote.get("quote");
                    sendNotification(quoteText);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Hata durumunda yapılacak işlemler
            }
        });

        return Result.success();
    }

    private void sendNotification(String quoteText) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "QUOTE_CHANNEL")
                .setSmallIcon(R.drawable.ic_birthday)
                .setContentTitle("You are loved.")
                .setContentText(quoteText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        notificationManager.notify(1, builder.build());
    }
}