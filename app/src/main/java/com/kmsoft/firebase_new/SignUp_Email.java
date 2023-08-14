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
import com.kmsoft.firebase_new.databinding.ActivitySignupEmailBinding;

public class SignUp_Email extends AppCompatActivity {

    ActivitySignupEmailBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupEmailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth = FirebaseAuth.getInstance();

        binding.signup.setOnClickListener(v -> SignUpuser());

        binding.simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp_Email.this, SignIn_Email.class);
                startActivity(intent);
            }
        });
    }

    private void SignUpuser() {
        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUp_Email.this, SignUp_Email.class);
                    startActivity(intent);
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TTT", "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
//                                    updateUI(user);
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TTT", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUp_Email.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                                    updateUI(null);
                }
            }
        });
    }
}