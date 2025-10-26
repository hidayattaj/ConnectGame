package com.example.connectgame;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;



public class LobbyActivity extends AppCompatActivity {



    FirebaseAuth mAuth;



    FirebaseDatabase database;
    DatabaseReference gamesRef;



    Handler handler;
    Runnable runnable;



    HashMap<String, Object> hashMap;



    TextView wait_textView, searchingPlayer_textView;
    Button proceedToGameplayButton;



    String gameID;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);



        initializeViews();



        mAuth = FirebaseAuth.getInstance();



        database = FirebaseDatabase.getInstance();
        gamesRef = database.getReference("Games");



        hashMap = new HashMap<>();



        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                waitForOtherPlayer();
                handler.postDelayed(this, 3000);
            }
        };



        checkForAvailableGameSession();


    }



    private void initializeViews() {
        searchingPlayer_textView = findViewById(R.id.searchingPlayer_textView);
        wait_textView = findViewById(R.id.wait_textView);

        proceedToGameplayButton = findViewById(R.id.proceedToGameplay_button);
        proceedToGameplayButton.setEnabled(false);

        proceedToGameplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToGameplay();
            }
        });
    }



    private void checkForAvailableGameSession() {
        Query query = gamesRef.orderByChild("status").equalTo("waiting for other player").limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        joinGameSession(child.getKey());
                    }
                } else {
                    createGameSession();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void createGameSession() {
        gameID  = gamesRef.push().getKey();

        hashMap.put("player1ID", mAuth.getCurrentUser().getUid());
        hashMap.put("turn", mAuth.getCurrentUser().getUid());
        hashMap.put("status", "waiting for other player");
        hashMap.put("player1Move", "none");
        hashMap.put("player2Move", "none");
        hashMap.put("winner", "none");

        gamesRef.child(gameID).setValue(hashMap);
        handler.postDelayed(runnable, 3000);
    }

    private void joinGameSession(String gameIdentity) {
        gameID = gameIdentity;

        gamesRef.child(gameID).child("player2ID").setValue(mAuth.getCurrentUser().getUid());
        gamesRef.child(gameID).child("status").setValue("match ready");

        searchingPlayer_textView.setText("Player found");
        wait_textView.setText("Match ready. Click Start");

        proceedToGameplayButton.setEnabled(true);
    }



    private void waitForOtherPlayer() {
        Query query = gamesRef.orderByChild("status").equalTo("match ready").limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    searchingPlayer_textView.setText("Player found");
                    wait_textView.setText("Match ready. Click Start");
                    proceedToGameplayButton.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void proceedToGameplay() {
        Intent intent = new Intent(getApplicationContext(), OnlineGameplay.class);
        intent.putExtra("GAME_ID", gameID);
        startActivity(intent);
        finish();
    }



}