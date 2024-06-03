package com.example.myapplication23;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        checkNotificationPermission();
        getSupportActionBar().setTitle("Medea");
        //Login Activity'i sign in butonuna basınca açmak için onClick metodu verdik.Anasayfadaki.
        Button buttonLogin = findViewById(R.id.button_loginMain);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //Anasayfadaki sign up a basınca kayıt olma sayfasını açması için
        Button buttonRegister = findViewById(R.id.button_registerMain);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        checkNotificationPermission();
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // İzin gerekli, kullanıcıdan isteyin
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_CODE_POST_NOTIFICATIONS);
            } else {
                // İzin verilmiş, zamanlanmış işi başlat
                startQuoteWorker();
            }
        } else {
            // Android 13 öncesi için izin gerekmiyor, zamanlanmış işi başlat
            startQuoteWorker();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // İzin verildi, zamanlanmış işi başlat
                    Log.d("PermissionResult", "Permission granted");
                    startQuoteWorker();
                } else {
                    // İzin reddedildi
                    Log.d("PermissionResult", "Permission denied");
                    Toast.makeText(this, "Permission is denied.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d("PermissionResult", "Grant results array is empty");
            }
        } else {
            Log.d("PermissionResult", "Request code does not match");
        }
    }

    private void startQuoteWorker() {
        //Android'de en kısa 15 dakika aralıklarla çalışır.
        PeriodicWorkRequest quoteWorkRequest = new PeriodicWorkRequest.Builder(QuoteWorker.class, 15, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
                "quoteWork",
                ExistingPeriodicWorkPolicy.KEEP,
                quoteWorkRequest
        );
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "QuoteChannel";
            String description = "Channel for quote notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("QUOTE_CHANNEL", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

}



