package com.example.connectgame;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button loginActivity;
    Button createAccountButton;

    EditText email_editText;
    EditText password_editText;

    TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        loginActivity = findViewById(R.id.loginActivity_button);

        createAccountButton = findViewById(R.id.createAccount_button);


        email_editText = findViewById(R.id.email_editText);
        password_editText = findViewById(R.id.password_editText);

        error = findViewById(R.id.error_textView);


        loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(email_editText.getText());
                String password = String.valueOf(password_editText.getText());

                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                    error.setText("Please enter your information.");
                    error.setVisibility(VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    error.setText("Please enter your email.");
                    error.setVisibility(VISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    error.setText("Please enter your password.");
                    error.setVisibility(VISIBLE);
                    return;
                }

                createAccount(email, password);
            }
        });


    }


    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), SuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    error.setText("Could not create an account. Try again later.");
                    error.setVisibility(VISIBLE);
                }
            }
        });

    }


}