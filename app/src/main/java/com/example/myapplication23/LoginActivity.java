package com.example.myapplication23;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
private EditText editTextloginMail, editTextloginPwd;
private ProgressBar progressBar;
private FirebaseAuth authProfile;
 private static final String TAG="LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        editTextloginMail=findViewById(R.id.editText_login_mail);
        editTextloginPwd=findViewById(R.id.editText_loginpwd);
        progressBar=findViewById(R.id.progressBar);

        authProfile= FirebaseAuth.getInstance();
        Button buttonForgotPass=findViewById(R.id.button_forgot_password_link);
        buttonForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this,"You can reset your password now",Toast.LENGTH_SHORT).show();
                startActivity(new Intent (LoginActivity.this,ForgotPasswordActivity.class));
            }
        });

        Button buttonLogin=findViewById(R.id.button_login);


         buttonLogin.setOnClickListener(new View.OnClickListener() {
           @Override
             public void onClick(View v) {
                 String textEmail=editTextloginMail.getText().toString();
                 String textPwd=editTextloginPwd.getText().toString();

                 if(TextUtils.isEmpty(textEmail)){
                     Toast.makeText(LoginActivity.this,"Please enter your mail.",Toast.LENGTH_SHORT).show();
                     editTextloginMail.setError("Email is required");
                     editTextloginMail.requestFocus();
                    //E- mailim daha önce tanımlanan bir kalıpla eşleşiyor mu kontrolü yapmak için:
                 }else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                     Toast.makeText(LoginActivity.this,"Please re-enter your mail.",Toast.LENGTH_SHORT).show();
                     editTextloginMail.setError("Valid email is required ");
                     editTextloginMail.requestFocus();
                 }else if(TextUtils.isEmpty(textPwd))
                 {
                     Toast.makeText(LoginActivity.this,"Please re-enter your password.",Toast.LENGTH_SHORT).show();
                     editTextloginPwd.setError("Password is required ");
                     editTextloginPwd.requestFocus();
                 }
                 else{
                     progressBar.setVisibility(View.VISIBLE);
                     loginUser(textEmail,textPwd);
                 }


             }
         });


        }

    private void loginUser(String textEmail, String textPwd) {
        authProfile.signInWithEmailAndPassword(textEmail,textPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    FirebaseUser firebaseUser=authProfile.getCurrentUser();

                    if(firebaseUser.isEmailVerified()){

                        Toast.makeText(LoginActivity.this,"You are logged in now", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,UserProfileActivity.class));
                        finish();
                    }else if(!firebaseUser.isEmailVerified()){
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }

                }else{
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidUserException e){
                        editTextloginMail.setError("User does not exists or is no longer valid. Please register again.");
                        editTextloginMail.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e)
                    {
                        editTextloginMail.setError("Invalid credentials. Kindly,check and re-enter.");
                        editTextloginMail.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(LoginActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(LoginActivity.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("E mail is not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");

        builder.setPositiveButton("Contunie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();

        alertDialog.show();
    }

    //Kullanıcı daha önce kayıt yapmış mı ona bakıyoruz
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null)
        {
            Toast.makeText(LoginActivity.this, "Already Logged In", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }else{
            Toast.makeText(LoginActivity.this, "You can login now!", Toast.LENGTH_SHORT).show();
        }
    }
}
