package com.kmsoft.firebase_new;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Splash extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        auth = FirebaseAuth.getInstance();
    }

    private void goToHome() {

        Intent intent = new Intent(Splash.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoLogin() {

        Intent intent = new Intent(Splash.this, SignIn_Email.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart(){

        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            goToHome();
        }else{
            gotoLogin();
        }
    }
}