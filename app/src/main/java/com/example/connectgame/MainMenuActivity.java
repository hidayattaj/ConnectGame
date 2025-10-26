package com.example.connectgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class MainMenuActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    TextView playerEmail;

    Button playComputerButton;
    Button playPartnerButton;
    Button playOnlineButton;
    Button logOutButton;


    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        playerEmail.setText("Logged in: " + currentUser.getEmail());

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mAuth = FirebaseAuth.getInstance();

        playComputerButton = findViewById(R.id.playComputer_button);
        playPartnerButton = findViewById(R.id.playPartner_button);
        playOnlineButton = findViewById(R.id.playOnline_button);
        logOutButton = findViewById(R.id.logOut_button);



        playerEmail = findViewById(R.id.playerEmail_textView);



        playComputerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ComputerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        playPartnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PartnerActivity.class);
                startActivity(intent);
                finish();
            }
        });

        playOnlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LobbyActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}