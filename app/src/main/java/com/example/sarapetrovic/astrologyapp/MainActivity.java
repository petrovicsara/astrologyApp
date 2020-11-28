package com.example.sarapetrovic.astrologyapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etDate, etName, etEmail, etPassword;
    TextView tvLoginRedirect;
    ProgressBar progressBar;
    Button btnSignUp;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDate = findViewById(R.id.etDate);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvLoginRedirect = findViewById(R.id.btnRedirectLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressBar = findViewById(R.id.progressBar);

        Calendar calendar = Calendar.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDataPicker(year, month, day);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                authenticate();
            }
        });

        tvLoginRedirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

            }
        });

    }

    private void displayDataPicker(int year, int month, int day){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month++;
                String date = day + "/" + month + "/" + year;
                etDate.setText(date);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseAuth.getCurrentUser() != null){
            FirebaseAuth.getInstance().signOut();
        }
    }

    private void authenticate(){
        final String name = etName.getText().toString().trim();
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String date = etDate.getText().toString();

        if (email.isEmpty()){
            etEmail.setError("Please enter your email adress!");
            etEmail.requestFocus();
        } else if (password.isEmpty()){
            etPassword.setError("Please enter your password!");
            etPassword.requestFocus();
        } else if(name.isEmpty()){
            etName.setError("Please enter your name!");
            etName.requestFocus();
        } else if(date.isEmpty()){
            etDate.setError("Please enter your date of birth!");
            etDate.requestFocus();
        } else if(email.isEmpty() && password.isEmpty() && name.isEmpty() && date.isEmpty()){
            Toast.makeText(MainActivity.this, "Please fill up all the fields!",Toast.LENGTH_LONG).show();
        } else if (!(email.isEmpty()) && !(password.isEmpty())){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        int day = Integer.parseInt(date.split("/")[0].trim());
                        int month = Integer.parseInt(date.split("/")[1].trim());
                        String sign = getSign(day,month);
                        User user = new User(name, email, password, date,"", "other", "other", sign);

                        FirebaseDatabase.getInstance().getReference("Users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    finish();
                                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                }
                            }
                        });
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Error occurred!",Toast.LENGTH_LONG).show();
        }
    }

    public String getSign(int day, int month){
        if (month == 12){

            if (day < 22)
                return  "Sagittarius";
            else
                return "capricorn";
        }

        else if (month == 1){
            if (day < 20)
                return  "Capricorn";
            else
                return  "aquarius";
        }

        else if (month == 2){
            if (day < 19)
                return  "Aquarius";
            else
                return "pisces";
        }

        else if(month == 3){
            if (day < 21)
                return "Pisces";
            else
                return "aries";
        }
        else if (month == 4){
            if (day < 20)
                return  "Aries";
            else
                return "taurus";
        }

        else if (month == 5){
            if (day < 21)
                return  "Taurus";
            else
                return  "gemini";
        }

        else if( month == 6){
            if (day < 21)
                return  "Gemini";
            else
                return "cancer";
        }

        else if (month == 7){
            if (day < 23)
                return "Cancer";
            else
                return  "leo";
        }

        else if( month == 8){
            if (day < 23)
                return "Leo";
            else
                return "virgo";
        }

        else if (month == 9){
            if (day < 23)
                return "Virgo";
            else
                return "libra";
        }

        else if (month == 10){
            if (day < 23)
                return "Libra";
            else
                return "scorpio";
        }

        else if (month == 11){
            if (day < 22)
                return "scorpio";
            else
                return "sagittarius";
        }
        return "";
    }

}
