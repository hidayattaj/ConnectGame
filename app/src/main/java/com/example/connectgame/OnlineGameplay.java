package com.example.connectgame;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class OnlineGameplay extends AppCompatActivity {

    // If value is 0 then image view is yellow (There is no token yet)
    // If value is 1 then image view is red (Player1's token is there)
    // If value is 2 then image view is blue (Player2's token is there)

    int[][] grid = {
            {1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0},
            {1, 0, 0, 0, 0}
    };

    TextView currentTurn_textView;



    Button column1_button, column2_button, column3_button, column4_button;
    Button newGame_button, quit_button;



    ImageView row1_col1, row2_col1, row3_col1, row4_col1;
    ImageView row1_col2, row2_col2, row3_col2, row4_col2;
    ImageView row1_col3, row2_col3, row3_col3, row4_col3;
    ImageView row1_col4, row2_col4, row3_col4, row4_col4;



    boolean player1Turn;
    boolean player2Turn;



    boolean player1Won;
    boolean player2Won;



    boolean gameDrawn;



    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    DatabaseReference gamesRef;

    ValueEventListener databaseListener;

    // The player who quit the game, this will be true because
    // In the onDataChange(), I need to know which player quit the game
    // So I do not show the dialog who quit the game
    // But show the dialog to other player to let him know
    boolean gameLeft;


    String player1ID, player2ID;
    String player1Move, player2Move;
    String thisDevicePlayerID;
    String thisDevicePlayerString;

    String gameID;
    Game game;

    int currentRow, currentColumn = -1;

    HashMap<String, Object> hashMapUpdates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_gameplay);


        //*********************
        initialize_views();
        initialize_values();
        //*********************



        Intent receivedIntent = getIntent();
        gameID = receivedIntent.getStringExtra("GAME_ID");



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        gamesRef = database.getReference("Games");

        hashMapUpdates = new HashMap<>();

        newGame_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { newGame(); }
        });
        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { quitGame(); }
        });


        column1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    insertToken(1);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                } else {
                    insertToken(1);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                }
            }
        });
        column2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    insertToken(2);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                } else {
                    insertToken(2);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                }
            }
        });
        column3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    insertToken(3);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                } else {
                    insertToken(3);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                }
            }
        });
        column4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    insertToken(4);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                } else {
                    insertToken(4);
                    disableButtons();
                    updateValuesAndCheckWinConditions();
                }
            }
        });

        //*********************
        disableButtons();
        getPlayersID();
        checkPlayerTurn();
        //*********************
    }



    private void setCurrentTurn() {
        if (player1Turn) {
            player1Turn = false;
            player2Turn = true;
            currentTurn_textView.setText("Player2 Turn");
        } else {
            player1Turn = true;
            player2Turn = false;
            currentTurn_textView.setText("Player1 Turn");
        }

    }

    private void updateValuesAndCheckWinConditions() {

        checkHorizontal();
        checkVertical();
        checkDiagonal();

        if (!checkEmptyCell()) { gameDrawn = true; }

        if (gameDrawn) {
            hashMapUpdates.put("winner", "drawn");
            hashMapUpdates.put("status", "game complete");
            gamesRef.child(gameID).updateChildren(hashMapUpdates);
            return;
        }

        if (player1Won) {
            hashMapUpdates.put("winner", player1ID);
            hashMapUpdates.put("status", "game complete");
            gamesRef.child(gameID).updateChildren(hashMapUpdates);
            return;
        }

        if (player2Won) {
            hashMapUpdates.put("winner", player2ID);
            hashMapUpdates.put("status", "game complete");
            gamesRef.child(gameID).updateChildren(hashMapUpdates);
            return;
        }

        if (player1Turn) {
            setCurrentTurn();
            hashMapUpdates.put("player1Move", player1Move);
            hashMapUpdates.put("turn", player2ID);
            gamesRef.child(gameID).updateChildren(hashMapUpdates);
        } else {
            setCurrentTurn();
            hashMapUpdates.put("player2Move", player2Move);
            hashMapUpdates.put("turn", player1ID);
            gamesRef.child(gameID).updateChildren(hashMapUpdates);
        }

    }



    private void initialize_views() {
        row1_col1 = findViewById(R.id.row1_col1);
        row2_col1 = findViewById(R.id.row2_col1);
        row3_col1 = findViewById(R.id.row3_col1);
        row4_col1 = findViewById(R.id.row4_col1);

        row1_col2 = findViewById(R.id.row1_col2);
        row2_col2 = findViewById(R.id.row2_col2);
        row3_col2 = findViewById(R.id.row3_col2);
        row4_col2 = findViewById(R.id.row4_col2);

        row1_col3 = findViewById(R.id.row1_col3);
        row2_col3 = findViewById(R.id.row2_col3);
        row3_col3 = findViewById(R.id.row3_col3);
        row4_col3 = findViewById(R.id.row4_col3);

        row1_col4 = findViewById(R.id.row1_col4);
        row2_col4 = findViewById(R.id.row2_col4);
        row3_col4 = findViewById(R.id.row3_col4);
        row4_col4 = findViewById(R.id.row4_col4);

        currentTurn_textView = findViewById(R.id.currentTurn_textView);

        column1_button = findViewById(R.id.column1_button);
        column2_button = findViewById(R.id.column2_button);
        column3_button = findViewById(R.id.column3_button);
        column4_button = findViewById(R.id.column4_button);

        newGame_button = findViewById(R.id.newGame_button);
        quit_button = findViewById(R.id.quit_button);
    }

    private void initialize_values() {
        player1Turn = false;
        player2Turn = false;

        player1Won = false;
        player2Won = false;

        gameDrawn = false;

        gameLeft = false;
    }



    private void getPlayersID() {
        Query query = gamesRef.child(gameID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                player1ID = snapshot.child("player1ID").getValue().toString();
                player2ID = snapshot.child("player2ID").getValue().toString();
                gamesRef.child(gameID).child("status").setValue("match in progress");

                if (mAuth.getCurrentUser().getUid().equals(player1ID)) {
                    thisDevicePlayerID = player1ID;
                    thisDevicePlayerString = "player1";
                }

                if (mAuth.getCurrentUser().getUid().equals(player2ID)) {
                    thisDevicePlayerID = player2ID;
                    thisDevicePlayerString = "player2";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void checkPlayerTurn() {
        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                game = snapshot.getValue(Game.class);

                if (game.status.equals("match quit by player")) {
                    if (gameLeft == false) {
                        infoDialog();
                        gameLeft = true;
                        disableButtons();
                    }
                    return;
                }

                if (game.winner.equals("drawn")) {
                    currentTurn_textView.setText("Game Drawn");
                    gameDrawn = true;
                    dialogGameDrawn();
                    return;
                }
                if (game.winner.equals(player1ID)) {
                    currentTurn_textView.setText("Player 1 Won");
                    player1Won = true;
                    dialogPlayer1Win();
                    return;
                }
                if (game.winner.equals(player2ID)) {
                    currentTurn_textView.setText("Player 2 Won");
                    player2Won = true;
                    dialogPlayer2Win();
                    return;
                }

                displayTurnTextView();

                if (game.turn.equals(thisDevicePlayerID)) {

                    if (thisDevicePlayerString.equals("player1")) {

                        if (game.player2Move.equals("none") == false) {
                            player2Move = game.player2Move;
                            String[] str = player2Move.split("_");
                            char rowChar = str[0].charAt(3);
                            char colChar = str[1].charAt(3);
                            int rowInt = Character.getNumericValue(rowChar);
                            int colInt = Character.getNumericValue(colChar);
                            updateGrid("player2", rowInt, colInt);
                        }

                        player1Turn = true;
                        player2Turn = false;
                        enableButtons();
                    }

                    if (thisDevicePlayerString.equals("player2")) {
                        player1Turn = false;
                        player2Turn = true;

                        player1Move = game.player1Move;
                        String[] str = player1Move.split("_");
                        char rowChar = str[0].charAt(3);
                        char colChar = str[1].charAt(3);
                        int rowInt = Character.getNumericValue(rowChar);
                        int colInt = Character.getNumericValue(colChar);
                        updateGrid("player1", rowInt, colInt);

                        enableButtons();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        };

        Query query = gamesRef.child(gameID);
        query.addValueEventListener(databaseListener);
    }


    /*private void checkPlayerTurn() {
        Query query = gamesRef.child(gameID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                game = snapshot.getValue(Game.class);

                if (game.winner.equals("drawn")) {
                    currentTurn_textView.setText("Game Drawn");
                    gameDrawn = true;
                    dialogGameDrawn();
                    return;
                }
                if (game.winner.equals(player1ID)) {
                    currentTurn_textView.setText("Player 1 Won");
                    player1Won = true;
                    dialogPlayer1Win();
                    return;
                }
                if (game.winner.equals(player2ID)) {
                    currentTurn_textView.setText("Player 2 Won");
                    player2Won = true;
                    dialogPlayer2Win();
                    return;
                }

                displayTurnTextView();

                if (game.turn.equals(thisDevicePlayerID)) {

                    if (thisDevicePlayerString.equals("player1")) {

                        if (game.player2Move.equals("none") == false) {
                            player2Move = game.player2Move;
                            String[] str = player2Move.split("_");
                            char rowChar = str[0].charAt(3);
                            char colChar = str[1].charAt(3);
                            int rowInt = Character.getNumericValue(rowChar);
                            int colInt = Character.getNumericValue(colChar);
                            updateGrid("player2", rowInt, colInt);
                        }

                        player1Turn = true;
                        player2Turn = false;
                        enableButtons();
                    }

                    if (thisDevicePlayerString.equals("player2")) {
                        player1Turn = false;
                        player2Turn = true;

                        player1Move = game.player1Move;
                        String[] str = player1Move.split("_");
                        char rowChar = str[0].charAt(3);
                        char colChar = str[1].charAt(3);
                        int rowInt = Character.getNumericValue(rowChar);
                        int colInt = Character.getNumericValue(colChar);
                        updateGrid("player1", rowInt, colInt);

                        enableButtons();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/


    private void displayTurnTextView() {
        if (thisDevicePlayerString.equals("player1")) {
            if (game.turn.equals(thisDevicePlayerID)) {
                currentTurn_textView.setText("Player 1 Turn");
            } else {
                currentTurn_textView.setText("Player 2 Turn");
            }
        } else {
            if (game.turn.equals(thisDevicePlayerID)) {
                currentTurn_textView.setText("Player 2 Turn");
            } else {
                currentTurn_textView.setText("Player 1 Turn");
            }
        }
    }



    private void insertToken(int column) {

        if (!checkEmptyCell()) {
            gameDrawn = true;
            return;
        }

        if (player1Won || player2Won) { return; }


        if (column == 1) {
            for (int i = 4; i >= 1; i--) {
                if (player1Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 1;        // Player1 token
                        player1Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col1.setImageResource(R.drawable.redstar); }
                        if (i == 2) { row2_col1.setImageResource(R.drawable.redstar); }
                        if (i == 3) { row3_col1.setImageResource(R.drawable.redstar); }
                        if (i == 4) { row4_col1.setImageResource(R.drawable.redstar); }
                        return;
                    }
                }
                if (player2Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 2;        // Player2 token
                        player2Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col1.setImageResource(R.drawable.bluestar); }
                        if (i == 2) { row2_col1.setImageResource(R.drawable.bluestar); }
                        if (i == 3) { row3_col1.setImageResource(R.drawable.bluestar); }
                        if (i == 4) { row4_col1.setImageResource(R.drawable.bluestar); }
                        return;
                    }
                }
            }
        }

        if (column == 2) {
            for (int i = 4; i >= 1; i--) {
                if (player1Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 1;
                        player1Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col2.setImageResource(R.drawable.redstar); }
                        if (i == 2) { row2_col2.setImageResource(R.drawable.redstar); }
                        if (i == 3) { row3_col2.setImageResource(R.drawable.redstar); }
                        if (i == 4) { row4_col2.setImageResource(R.drawable.redstar); }
                        return;
                    }
                }
                if (player2Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 2;
                        player2Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col2.setImageResource(R.drawable.bluestar); }
                        if (i == 2) { row2_col2.setImageResource(R.drawable.bluestar); }
                        if (i == 3) { row3_col2.setImageResource(R.drawable.bluestar); }
                        if (i == 4) { row4_col2.setImageResource(R.drawable.bluestar); }
                        return;
                    }
                }
            }
        }

        if (column == 3) {
            for (int i = 4; i >= 1; i--) {
                if (player1Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 1;
                        player1Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col3.setImageResource(R.drawable.redstar); }
                        if (i == 2) { row2_col3.setImageResource(R.drawable.redstar); }
                        if (i == 3) { row3_col3.setImageResource(R.drawable.redstar); }
                        if (i == 4) { row4_col3.setImageResource(R.drawable.redstar); }
                        return;
                    }
                }
                if (player2Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 2;
                        player2Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col3.setImageResource(R.drawable.bluestar); }
                        if (i == 2) { row2_col3.setImageResource(R.drawable.bluestar); }
                        if (i == 3) { row3_col3.setImageResource(R.drawable.bluestar); }
                        if (i == 4) { row4_col3.setImageResource(R.drawable.bluestar); }
                        return;
                    }
                }
            }
        }

        if (column == 4) {
            for (int i = 4; i >= 1; i--) {
                if (player1Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 1;
                        player1Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col4.setImageResource(R.drawable.redstar); }
                        if (i == 2) { row2_col4.setImageResource(R.drawable.redstar); }
                        if (i == 3) { row3_col4.setImageResource(R.drawable.redstar); }
                        if (i == 4) { row4_col4.setImageResource(R.drawable.redstar); }
                        return;
                    }
                }
                if (player2Turn) {
                    if (grid[i][column] == 0) {
                        currentRow = i;
                        currentColumn = column;
                        grid[i][column] = 2;
                        player2Move = "row" + currentRow + "_" + "col" + currentColumn;
                        if (i == 1) { row1_col4.setImageResource(R.drawable.bluestar); }
                        if (i == 2) { row2_col4.setImageResource(R.drawable.bluestar); }
                        if (i == 3) { row3_col4.setImageResource(R.drawable.bluestar); }
                        if (i == 4) { row4_col4.setImageResource(R.drawable.bluestar); }
                        return;
                    }
                }
            }
        }

    }

    private void updateGrid(String player, int row, int column) {

        if (player.equals("player1")) {
            if (column == 1) {
                grid[row][column] = 1;
                if (row == 1) { row1_col1.setImageResource(R.drawable.redstar); }
                if (row == 2) { row2_col1.setImageResource(R.drawable.redstar); }
                if (row == 3) { row3_col1.setImageResource(R.drawable.redstar); }
                if (row == 4) { row4_col1.setImageResource(R.drawable.redstar); }
                return;
            }
            if (column == 2) {
                grid[row][column] = 1;
                if (row == 1) { row1_col2.setImageResource(R.drawable.redstar); }
                if (row == 2) { row2_col2.setImageResource(R.drawable.redstar); }
                if (row == 3) { row3_col2.setImageResource(R.drawable.redstar); }
                if (row == 4) { row4_col2.setImageResource(R.drawable.redstar); }
                return;
            }
            if (column == 3) {
                grid[row][column] = 1;
                if (row == 1) { row1_col3.setImageResource(R.drawable.redstar); }
                if (row == 2) { row2_col3.setImageResource(R.drawable.redstar); }
                if (row == 3) { row3_col3.setImageResource(R.drawable.redstar); }
                if (row == 4) { row4_col3.setImageResource(R.drawable.redstar); }
                return;
            }
            if (column == 4) {
                grid[row][column] = 1;
                if (row == 1) { row1_col4.setImageResource(R.drawable.redstar); }
                if (row == 2) { row2_col4.setImageResource(R.drawable.redstar); }
                if (row == 3) { row3_col4.setImageResource(R.drawable.redstar); }
                if (row == 4) { row4_col4.setImageResource(R.drawable.redstar); }
                return;
            }
        }

        if (player.equals("player2")) {
            if (column == 1) {
                grid[row][column] = 2;
                if (row == 1) { row1_col1.setImageResource(R.drawable.bluestar); }
                if (row == 2) { row2_col1.setImageResource(R.drawable.bluestar); }
                if (row == 3) { row3_col1.setImageResource(R.drawable.bluestar); }
                if (row == 4) { row4_col1.setImageResource(R.drawable.bluestar); }
                return;
            }
            if (column == 2) {
                grid[row][column] = 2;
                if (row == 1) { row1_col2.setImageResource(R.drawable.bluestar); }
                if (row == 2) { row2_col2.setImageResource(R.drawable.bluestar); }
                if (row == 3) { row3_col2.setImageResource(R.drawable.bluestar); }
                if (row == 4) { row4_col2.setImageResource(R.drawable.bluestar); }
                return;
            }
            if (column == 3) {
                grid[row][column] = 2;
                if (row == 1) { row1_col3.setImageResource(R.drawable.bluestar); }
                if (row == 2) { row2_col3.setImageResource(R.drawable.bluestar); }
                if (row == 3) { row3_col3.setImageResource(R.drawable.bluestar); }
                if (row == 4) { row4_col3.setImageResource(R.drawable.bluestar); }
                return;
            }
            if (column == 4) {
                grid[row][column] = 2;
                if (row == 1) { row1_col4.setImageResource(R.drawable.bluestar); }
                if (row == 2) { row2_col4.setImageResource(R.drawable.bluestar); }
                if (row == 3) { row3_col4.setImageResource(R.drawable.bluestar); }
                if (row == 4) { row4_col4.setImageResource(R.drawable.bluestar); }
                return;
            }
        }

    }



    private boolean checkEmptyCell() {
        for (int row = 4; row >= 1; row--) {
            for (int column = 4; column >= 1; column--) {
                if (grid[row][column] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void checkHorizontal() {
        for (int row = 1; row <= 4; row++) {
            for (int col = 1; col <= 2; col++) {
                int cell = grid[row][col];
                // For player1
                if (cell == 1) {
                    if (cell == grid[row][col + 1] && cell == grid[row][col + 2]) {
                        player1Won = true;
                        return;
                    }
                }
                // For player2
                if (cell == 2) {
                    if (cell == grid[row][col + 1] && cell == grid[row][col + 2]) {
                        player2Won = true;
                        return;
                    }
                }
            }
        }
    }

    private void checkVertical() {
        for (int col = 1; col <= 4; col++) {
            for (int row = 1; row <= 2; row++) {
                int cell = grid[row][col];
                // For player1
                if (cell == 1) {
                    if (cell == grid[row + 1][col] && cell == grid[row + 2][col]) {
                        player1Won = true;
                        return;
                    }
                }
                // For player2
                if (cell == 2) {
                    if (cell == grid[row + 1][col] && cell == grid[row + 2][col]) {
                        player2Won = true;
                        return;
                    }
                }
            }
        }
    }

    private void checkDiagonal() {

        // check left-to-right up-down Diagonal Win
        for (int row = 1; row <= 2; row++) {
            for (int col = 1; col <= 2; col++) {
                int cell = grid[row][col];
                if (cell == 1) {
                    if (cell == grid[row + 1][col + 1] && cell == grid[row + 2][col + 2]) {
                        player1Won = true;
                        return;
                    }
                }
                if (cell == 2) {
                    if (cell == grid[row + 1][col + 1] && cell == grid[row + 2][col + 2]) {
                        player2Won = true;
                        return;
                    }
                }
            }
        }

        // check left-to-right down-up Diagonal Win
        for (int row = 4; row >= 3; row--) {
            for (int col = 1; col <= 2; col++) {
                int cell = grid[row][col];
                if (cell == 1) {
                    if (cell == grid[row - 1][col + 1] && cell == grid[row - 2][col + 2]) {
                        player1Won = true;
                        return;
                    }
                }
                if (cell == 2) {
                    if (cell == grid[row - 1][col + 1] && cell == grid[row - 2][col + 2]) {
                        player2Won = true;
                        return;
                    }
                }
            }
        }

    }



    private void enableButtons() {
        column1_button.setEnabled(true);
        column2_button.setEnabled(true);
        column3_button.setEnabled(true);
        column4_button.setEnabled(true);
    }

    private void disableButtons() {
        column1_button.setEnabled(false);
        column2_button.setEnabled(false);
        column3_button.setEnabled(false);
        column4_button.setEnabled(false);
    }



    private void dialogPlayer1Win() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player 1 Won");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialogPlayer2Win() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player 2 Won");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void dialogGameDrawn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Drawn");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void infoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your opponent left the match.");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void newGame() {
        if (gameLeft == false) {
            gameLeft = true;
            gamesRef.child(gameID).child("status").setValue("match quit by player");
        }
        if (databaseListener != null) {
            gamesRef.child(gameID).removeEventListener(databaseListener);
        }
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void quitGame() {
        if (gameLeft == false) {
            gameLeft = true;
            gamesRef.child(gameID).child("status").setValue("match quit by player");
        }
        if (databaseListener != null) {
            gamesRef.child(gameID).removeEventListener(databaseListener);
        }
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }


}