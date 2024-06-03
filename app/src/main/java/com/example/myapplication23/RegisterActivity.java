package com.example.myapplication23;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterMail, editTextRegisterDoB, editTextRegisterMobile, editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupGenderReg;
    private RadioButton radioButtonRegisterGenderSelected;
    private static final String TAG="RegisterActivity";
    private DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setTitle("Register");

        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();

        //Edittextlerdeki değişkenleri bu sınıftaki değişkenlere eşitleme
        editTextRegisterFullName = findViewById(R.id.txtFullNameReg);
        editTextRegisterPwd = findViewById(R.id.txtPasswordReg);
        editTextRegisterMail = findViewById(R.id.txtEmailReg);
        editTextRegisterConfirmPwd = findViewById(R.id.txtConfirmPasswordReg);
        editTextRegisterMobile = findViewById(R.id.txtMobileReg);
        editTextRegisterDoB = findViewById(R.id.txtDateBirthReg);

        radioGroupGenderReg = findViewById(R.id.radio_gender);
        radioGroupGenderReg.clearCheck();

        editTextRegisterDoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int day=calendar.get(Calendar.DAY_OF_MONTH);
                int month=calendar.get(Calendar.DAY_OF_MONTH);
                int year=calendar.get(Calendar.YEAR);

                picker=new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     editTextRegisterDoB.setText(dayOfMonth+"/"+(month+1)+"/" +year);
                    }
                },year,month,day);
                picker.show();
            }
        });

        progressBar = findViewById(R.id.progressBar);

        Button buttonRegister = findViewById(R.id.button_registerr);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedGenderId = radioGroupGenderReg.getCheckedRadioButtonId();
                radioButtonRegisterGenderSelected = findViewById(selectedGenderId);

                String textFullName = editTextRegisterFullName.getText().toString();
                String textMail = editTextRegisterMail.getText().toString();
                String textDoB = editTextRegisterDoB.getText().toString();
                String textMobile = editTextRegisterMobile.getText().toString();
                String textPwd = editTextRegisterPwd.getText().toString();
                String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString();
                String textGender;
                 //Türkiye'de telefon numaraları beşle başladığı için
                String mobileRegex = "[5-5][0-9]{9}";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileRegex);
                mobileMatcher = mobilePattern.matcher(textMobile);

                if (TextUtils.isEmpty(textFullName)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your full name.", Toast.LENGTH_LONG).show();
                    editTextRegisterFullName.setError("Full name is required.");
                    editTextRegisterFullName.requestFocus();

                } else if (TextUtils.isEmpty(textMail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your e-mail.", Toast.LENGTH_LONG).show();
                    editTextRegisterMail.setError("E-mail is required.");
                    editTextRegisterMail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textMail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your e-mail.", Toast.LENGTH_LONG).show();
                    editTextRegisterMail.setError("Valid e-mail is required.");
                    editTextRegisterMail.requestFocus();

                } else if (TextUtils.isEmpty(textDoB)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your date of birth", Toast.LENGTH_LONG).show();
                    editTextRegisterDoB.setError("Date of Birth is required.");
                    editTextRegisterDoB.requestFocus();

                } else if (radioGroupGenderReg.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(RegisterActivity.this, "Please select your gender.", Toast.LENGTH_LONG).show();
                    radioButtonRegisterGenderSelected.setError("Gender is required.");
                    radioButtonRegisterGenderSelected.requestFocus();

                } else if (TextUtils.isEmpty(textMobile)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your phone number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone number is required.");
                    editTextRegisterMobile.requestFocus();

                } else if (textMobile.length() != 10) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your phone number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone number sholud be 10 digits.");
                    editTextRegisterMobile.requestFocus();

                }else if(!mobileMatcher.find())
                {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your phone number", Toast.LENGTH_LONG).show();
                    editTextRegisterMobile.setError("Phone number is not valid.");
                    editTextRegisterMobile.requestFocus();

                }else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password.", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.setError("Password is required.");
                    editTextRegisterPwd.requestFocus();

                } else if (textPwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password should be at least 6 digits.", Toast.LENGTH_LONG).show();
                    editTextRegisterPwd.requestFocus();

                } else if (TextUtils.isEmpty(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your password.", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation is required.");
                    editTextRegisterConfirmPwd.requestFocus();

                } else if (!textPwd.equals(textConfirmPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter same password.", Toast.LENGTH_LONG).show();
                    editTextRegisterConfirmPwd.setError("Password Confirmation is required.");
                    editTextRegisterConfirmPwd.requestFocus();

                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirmPwd.clearComposingText();

                } else {
                    textGender = radioButtonRegisterGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textMail, textDoB, textGender, textMobile, textPwd);
                }
            }

        });
    }

         private void registerUser(String textFullName, String textMail, String textDoB, String textGender, String textMobile, String textPwd) {
         FirebaseAuth auth=FirebaseAuth.getInstance();
         auth.createUserWithEmailAndPassword(textMail,textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    //Database yazdırmak istediğimiz için sınıfa yapıcı fonksiyona gönderiyoruz.
                    ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(textDoB,textGender,textMobile);

                    DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                firebaseUser.sendEmailVerification();

                                Toast.makeText(RegisterActivity.this, "User registered successfully. Please verify your email.", Toast.LENGTH_LONG).show();

                            }else {
                                Toast.makeText(RegisterActivity.this, "User registered failed. Please try again.", Toast.LENGTH_LONG).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }


                    });

                }else {
                    try{
                        throw task.getException();

                    }catch(FirebaseAuthWeakPasswordException e){
                        editTextRegisterPwd.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters");
                        editTextRegisterPwd.requestFocus();

                    }catch(FirebaseAuthInvalidCredentialsException e){
                        editTextRegisterMail.setError("Your email is invalid or already use. Kindly re-enter");
                        editTextRegisterMail.requestFocus();

                    }catch(FirebaseAuthUserCollisionException e){
                        editTextRegisterMail.setError("User is already registered with this email. Use another email.");
                        editTextRegisterMail.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(),Toast.LENGTH_LONG ).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}