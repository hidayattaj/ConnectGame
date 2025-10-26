package com.example.connectgame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class PartnerActivity extends AppCompatActivity {

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
    Button restart_button, quit_button;



    ImageView row1_col1, row2_col1, row3_col1, row4_col1;
    ImageView row1_col2, row2_col2, row3_col2, row4_col2;
    ImageView row1_col3, row2_col3, row3_col3, row4_col3;
    ImageView row1_col4, row2_col4, row3_col4, row4_col4;



    boolean player1Turn;
    boolean player2Turn;



    boolean player1Won;
    boolean player2Won;



    boolean gameDrawn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);


        //*********************
        initialize_views();
        initialize_values();
        //*********************


        restart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { restart_game(); }
        });
        quit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { quit_game(); }
        });


        column1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    player1_turn(1);
                    set_current_turn();
                    check_win_conditions();

                } else {
                    player2_turn(1);
                    set_current_turn();
                    check_win_conditions();
                }
            }
        });
        column2_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    player1_turn(2);
                    set_current_turn();
                    check_win_conditions();

                } else {
                    player2_turn(2);
                    set_current_turn();
                    check_win_conditions();
                }
            }
        });
        column3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    player1_turn(3);
                    set_current_turn();
                    check_win_conditions();

                } else {
                    player2_turn(3);
                    set_current_turn();
                    check_win_conditions();
                }
            }
        });
        column4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player1Turn) {
                    player1_turn(4);
                    set_current_turn();
                    check_win_conditions();

                } else {
                    player2_turn(4);
                    set_current_turn();
                    check_win_conditions();
                }
            }
        });


    }



    private void set_current_turn() {
        if (player1Turn) {
            player1Turn = false;
            player2Turn = true;
            currentTurn_textView.setText("Player2 Turn");
        } else {
            player2Turn = false;
            player1Turn = true;
            currentTurn_textView.setText("Player1 Turn");
        }

    }

    private void check_win_conditions() {

        if (player1Won || player2Won || gameDrawn) { return; }


        check_horizontal();
        check_vertical();
        check_diagonal();


        if (!check_empty_cell()) { gameDrawn = true; }


        if (gameDrawn) {
            currentTurn_textView.setText("Game Drawn");
            currentTurn_textView.setVisibility(View.VISIBLE);
            dialog_game_drawn();
            return;
        }


        if (player1Won) {
            currentTurn_textView.setText("Player1 Won");
            currentTurn_textView.setVisibility(View.VISIBLE);
            dialog_player1_win();
            return;
        }

        if (player2Won) {
            currentTurn_textView.setText("Player2 Won");
            currentTurn_textView.setVisibility(View.VISIBLE);
            dialog_player2_win();
            return;
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


        row1_col1.setTag("row1_col1");
        row2_col1.setTag("row2_col1");
        row3_col1.setTag("row3_col1");
        row4_col1.setTag("row4_col1");

        row1_col2.setTag("row1_col2");
        row2_col2.setTag("row2_col2");
        row3_col2.setTag("row3_col2");
        row4_col2.setTag("row4_col2");

        row1_col3.setTag("row1_col3");
        row2_col3.setTag("row2_col3");
        row3_col3.setTag("row3_col3");
        row4_col3.setTag("row4_col3");

        row1_col4.setTag("row1_col4");
        row2_col4.setTag("row2_col4");
        row3_col4.setTag("row3_col4");
        row4_col4.setTag("row4_col4");


        currentTurn_textView = findViewById(R.id.currentTurn_textView);


        column1_button = findViewById(R.id.column1_button);
        column2_button = findViewById(R.id.column2_button);
        column3_button = findViewById(R.id.column3_button);
        column4_button = findViewById(R.id.column4_button);

        column1_button.setTag("1");
        column2_button.setTag("2");
        column3_button.setTag("3");
        column4_button.setTag("4");


        restart_button = findViewById(R.id.restart_button);
        quit_button = findViewById(R.id.quit_button);
    }

    private void initialize_values() {
        player1Turn = true;
        player2Turn = false;

        player1Won = false;
        player2Won = false;

        gameDrawn = false;
    }



    private void player1_turn(int column) {

        if (!check_empty_cell()) {
            gameDrawn = true;
            return;
        }

        if (player1Won || player2Won) { return; }


        if (column == 1) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 1;        // Player1 token
                    if (i == 1) { row1_col1.setImageResource(R.drawable.redstar); }
                    if (i == 2) { row2_col1.setImageResource(R.drawable.redstar); }
                    if (i == 3) { row3_col1.setImageResource(R.drawable.redstar); }
                    if (i == 4) { row4_col1.setImageResource(R.drawable.redstar); }
                    return;
                }
            }
        }


        if (column == 2) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 1;
                    if (i == 1) { row1_col2.setImageResource(R.drawable.redstar); }
                    if (i == 2) { row2_col2.setImageResource(R.drawable.redstar); }
                    if (i == 3) { row3_col2.setImageResource(R.drawable.redstar); }
                    if (i == 4) { row4_col2.setImageResource(R.drawable.redstar); }
                    return;
                }
            }
        }


        if (column == 3) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 1;
                    if (i == 1) { row1_col3.setImageResource(R.drawable.redstar); }
                    if (i == 2) { row2_col3.setImageResource(R.drawable.redstar); }
                    if (i == 3) { row3_col3.setImageResource(R.drawable.redstar); }
                    if (i == 4) { row4_col3.setImageResource(R.drawable.redstar); }
                    return;
                }
            }
        }


        if (column == 4) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 1;
                    if (i == 1) { row1_col4.setImageResource(R.drawable.redstar); }
                    if (i == 2) { row2_col4.setImageResource(R.drawable.redstar); }
                    if (i == 3) { row3_col4.setImageResource(R.drawable.redstar); }
                    if (i == 4) { row4_col4.setImageResource(R.drawable.redstar); }
                    return;
                }
            }
        }

    }

    private void player2_turn(int column) {

        if (!check_empty_cell()) {
            gameDrawn = true;
            return;
        }

        if (player1Won || player2Won) { return; }


        if (column == 1) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 2;        // Player2 token
                    if (i == 1) { row1_col1.setImageResource(R.drawable.bluestar); }
                    if (i == 2) { row2_col1.setImageResource(R.drawable.bluestar); }
                    if (i == 3) { row3_col1.setImageResource(R.drawable.bluestar); }
                    if (i == 4) { row4_col1.setImageResource(R.drawable.bluestar); }
                    return;
                }
            }
        }


        if (column == 2) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 2;
                    if (i == 1) { row1_col2.setImageResource(R.drawable.bluestar); }
                    if (i == 2) { row2_col2.setImageResource(R.drawable.bluestar); }
                    if (i == 3) { row3_col2.setImageResource(R.drawable.bluestar); }
                    if (i == 4) { row4_col2.setImageResource(R.drawable.bluestar); }
                    return;
                }
            }
        }


        if (column == 3) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 2;
                    if (i == 1) { row1_col3.setImageResource(R.drawable.bluestar); }
                    if (i == 2) { row2_col3.setImageResource(R.drawable.bluestar); }
                    if (i == 3) { row3_col3.setImageResource(R.drawable.bluestar); }
                    if (i == 4) { row4_col3.setImageResource(R.drawable.bluestar); }
                    return;
                }
            }
        }


        if (column == 4) {
            for (int i = 4; i >= 1; i--) {
                if (grid[i][column] == 0) {
                    grid[i][column] = 2;
                    if (i == 1) { row1_col4.setImageResource(R.drawable.bluestar); }
                    if (i == 2) { row2_col4.setImageResource(R.drawable.bluestar); }
                    if (i == 3) { row3_col4.setImageResource(R.drawable.bluestar); }
                    if (i == 4) { row4_col4.setImageResource(R.drawable.bluestar); }
                    return;
                }
            }
        }

    }



    private void restart_game() {
        initialize_values();

        for (int i = 4; i >= 1; i-- ) {
            for (int j = 4; j >= 1; j--) { grid[i][j] = 0; }
        }

        currentTurn_textView.setText("Player1 Turn");

        row1_col1.setImageResource(R.drawable.yellowstar);
        row2_col1.setImageResource(R.drawable.yellowstar);
        row3_col1.setImageResource(R.drawable.yellowstar);
        row4_col1.setImageResource(R.drawable.yellowstar);

        row1_col2.setImageResource(R.drawable.yellowstar);
        row2_col2.setImageResource(R.drawable.yellowstar);
        row3_col2.setImageResource(R.drawable.yellowstar);
        row4_col2.setImageResource(R.drawable.yellowstar);

        row1_col3.setImageResource(R.drawable.yellowstar);
        row2_col3.setImageResource(R.drawable.yellowstar);
        row3_col3.setImageResource(R.drawable.yellowstar);
        row4_col3.setImageResource(R.drawable.yellowstar);

        row1_col4.setImageResource(R.drawable.yellowstar);
        row2_col4.setImageResource(R.drawable.yellowstar);
        row3_col4.setImageResource(R.drawable.yellowstar);
        row4_col4.setImageResource(R.drawable.yellowstar);
    }

    private void quit_game() {
        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
        startActivity(intent);
        finish();
    }



    private boolean check_empty_cell() {
        for (int row = 4; row >= 1; row--) {
            for (int column = 4; column >= 1; column--) {
                if (grid[row][column] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void check_horizontal() {
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

    private void check_vertical() {
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

    private void check_diagonal() {

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



    private void dialog_player1_win() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player1 Won!!!");
        builder.setMessage("Click Restart to play again");

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

    private void dialog_player2_win() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player2 Won!!!");
        builder.setMessage("Click Restart to play again");

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

    private void dialog_game_drawn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Drawn!!!");
        builder.setMessage("Click Restart to play again");

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



}