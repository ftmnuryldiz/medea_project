package com.example.myapplication23;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
   private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB,textViewGender, textViewMobile;
   private ProgressBar progressBar;
   private String fullName, email,doB, gender, mobile;
   private ImageView imageView;
   private FirebaseAuth authProfile;
   private Button buttonSignOut;

   BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Profile");


        textViewWelcome=findViewById(R.id.textView_show_welcome);
        textViewFullName=findViewById(R.id.textView_show_full_name);
        textViewEmail=findViewById(R.id.textView_show_email);
        textViewDoB=findViewById(R.id.textView_show_dob);
        textViewGender= findViewById(R.id.textView_show_gender);
        textViewMobile=findViewById(R.id.textView_show_mobile);
         buttonSignOut=findViewById(R.id.btnSignOut);
         buttonSignOut.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 authProfile.signOut();
                 Toast.makeText(UserProfileActivity.this,"Sign Out", Toast.LENGTH_SHORT).show();
                 Intent intent=new Intent(UserProfileActivity.this,MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
               finish();
             }

         });

        bottomNavigationView=findViewById(R.id.bottomNavigationVieww);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
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
                    return true;
                }
                if(menuItem.getItemId()==R.id.bottom_favorite)
                {
                    startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                    return true;
                }


                return false;
            }




        });
         progressBar=findViewById(R.id.progress_bar);
         imageView=findViewById(R.id.imageView_profile_dp);
         imageView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
                 startActivity(intent);
             }
         });
         authProfile=FirebaseAuth.getInstance();
         FirebaseUser firebaseUser=authProfile.getCurrentUser();

         if(firebaseUser==null){
             Toast.makeText(UserProfileActivity.this, "Something went wrong! User's details are not available at the moment.", Toast.LENGTH_SHORT).show();
         }else {
             checkIfEmailVerified(firebaseUser);
             progressBar.setVisibility(View.VISIBLE);
             showUserProfile(firebaseUser);
         }


        }

        //Başarılı kayıttan sonra UserProfileActivity'e geçiş
        private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }


    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("E mail is not verified");
        builder.setMessage("Please verify your e-mail now. You can not login without email verification next time.");

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog=builder.create();

        alertDialog.show();

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID=firebaseUser.getUid();

            DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");
            referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                    if(readWriteUserDetails!=null){
                        fullName=firebaseUser.getDisplayName();
                        email=firebaseUser.getEmail();
                        doB=readWriteUserDetails.doB;
                        gender=readWriteUserDetails.gender;
                        mobile=readWriteUserDetails.mobile;


                        textViewWelcome.setText("Welcome "+ fullName);
                        textViewFullName.setText(fullName);
                        textViewEmail.setText(email);
                        textViewDoB.setText(doB);
                        textViewMobile.setText(mobile);
                        textViewGender.setText(gender);


                        Uri uri= firebaseUser.getPhotoUrl();
                        Picasso.with(UserProfileActivity.this).load(uri).into(imageView);


                    }
                    else{
                        Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

         }

         //Actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        }else{
            Toast.makeText(UserProfileActivity.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
