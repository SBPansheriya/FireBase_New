package com.kmsoft.firebase_new;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kmsoft.firebase_new.databinding.ActivitySigninEmailBinding;

public class SignIn_Email extends AppCompatActivity {

    ActivitySigninEmailBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySigninEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        binding.signin.setOnClickListener(v -> SignInUser());

        binding.notregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignIn_Email.this, SignUp_Email.class);
                startActivity(intent);
            }
        });
    }

    private void SignInUser() {
        String email = binding.email1.getText().toString();
        String password = binding.password1.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignIn_Email.this, MainActivity.class);
                    startActivity(intent);
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TTT", "signInWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TTT", "signInWithEmail:failure", task.getException());
                    Toast.makeText(SignIn_Email.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                }
            }
        });
    }
}
