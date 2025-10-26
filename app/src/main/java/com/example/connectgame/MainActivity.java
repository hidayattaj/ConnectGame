package com.example.connectgame;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button createAccountActivity;
    Button loginButton;

    EditText email_editText;
    EditText password_editText;

    CheckBox rememberMe;

    TextView error;


    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null && rememberMe == false) { mAuth.signOut(); }

        if (currentUser != null && rememberMe == true) {
            Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMe", rememberMe.isChecked());
        editor.apply();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();


        createAccountActivity = findViewById(R.id.createAccountActivity_button);

        loginButton = findViewById(R.id.login_button);


        email_editText = findViewById(R.id.email_editText);
        password_editText = findViewById(R.id.password_editText);


        error = findViewById(R.id.error_textView);


        rememberMe = findViewById(R.id.rememberMe_checkBox);


        createAccountActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
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

                login(email, password);
            }
        });


    }

    private void login(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    error.setText("Could not login. Do you have an account? or try again later.");
                    error.setVisibility(VISIBLE);
                }
            }
        });

    }

    
}