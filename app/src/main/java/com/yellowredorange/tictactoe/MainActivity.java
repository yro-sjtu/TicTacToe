package com.yellowredorange.tictactoe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    AI ai;
    int[] state;
    int[][] winningPositions;
    ImageView[] cell;
    Random rand;
    LinearLayout playAgainLayout;
    TextView message;
    Button playAgainBtn;
    boolean gameOver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGame();
    }

    public void initGame() {
        ai = new AI();

        cell = new ImageView[9];
        // get imageview by id
        cell[0] = (ImageView) findViewById(R.id.cell1);
        cell[1] = (ImageView) findViewById(R.id.cell2);
        cell[2] = (ImageView) findViewById(R.id.cell3);

        cell[3] = (ImageView) findViewById(R.id.cell4);
        cell[4] = (ImageView) findViewById(R.id.cell5);
        cell[5] = (ImageView) findViewById(R.id.cell6);

        cell[6] = (ImageView) findViewById(R.id.cell7);
        cell[7] = (ImageView) findViewById(R.id.cell8);
        cell[8] = (ImageView) findViewById(R.id.cell9);
        // add click listeners for each imageview
        for(int i = 0; i < 9; i++) {
            cell[i].setOnClickListener(new MyClickListener(i));
        }

        // init state of each cell
        state = new int[9];
        Arrays.fill(state, -1); // -1: unoccupied cell

        // all possible positions to check the winner
        winningPositions = new int[][]{{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

        // init random
        rand = new Random();

        // display message / play again
        playAgainLayout = (LinearLayout) findViewById(R.id.playAgainLayout);
        message = (TextView) findViewById(R.id.winningMessage);
        playAgainBtn = (Button) findViewById(R.id.playAgainButton);

        // reset to false
        gameOver = false;
    }

    private class AI {
        public void takeTurn() {
            if(state[0] == -1 &&
                    ((state[1] == 0 && state[2] == 0) ||
                    (state[4] == 0 && state[8] == 0) ||
                    (state[3] == 0 && state[6] == 0))) {
                markSquare(0);
            } else if(state[1] == -1 &&
                    ((state[4] == 0 && state[7] == 0) ||
                    (state[0] == 0 && state[2] == 0))) {
                markSquare(1);
            } else if(state[2] == -1 &&
                    ((state[4] == 0 && state[6] == 0) ||
                    (state[0] == 0 && state[1] == 0) ||
                    (state[5] == 0 && state[8] == 0))) {
                markSquare(2);
            } else if(state[3] == -1 &&
                    ((state[0] == 0 && state[6] == 0) ||
                    (state[4] == 0 && state[5] == 0))) {
                markSquare(3);
            } else if(state[4] == -1 &&
                    ((state[0] == 0 && state[8] == 0) ||
                    (state[2] == 0 && state[6] == 0) ||
                    (state[3] == 0 && state[5] == 0) ||
                    (state[1] == 0 && state[7] == 0))) {
                markSquare(4);
            } else if(state[5] == -1 &&
                    ((state[2] == 0 && state[8] == 0) ||
                    (state[3] == 0 && state[4] == 0))) {
                markSquare(5);
            } else if(state[6] == -1 &&
                    ((state[7] == 0 && state[8] == 0) ||
                    (state[0] == 0 && state[3] == 0) ||
                    (state[4] == 0 && state[2] == 0))) {
                markSquare(6);
            } else if(state[7] == -1 &&
                    ((state[6] == 0 && state[8] == 0) ||
                    (state[1] == 0 && state[4] == 0))) {
                markSquare(7);
            } else if(state[8] == -1 &&
                    ((state[2] == 0 && state[5] == 0) ||
                    (state[6] == 0 && state[7] == 0) ||
                    (state[4] == 0 && state[0] == 0))) {
                markSquare(8);
            } else {
                // make random move
                int id = rand.nextInt(9);
                while(state[id] != -1) {
                    id = rand.nextInt(9);
                }
                markSquare(id);
            }
        }
    }

    private class MyClickListener implements View.OnClickListener {

        int id;

        public MyClickListener(int i) {
            this.id = i;
        }

        public void onClick(View view) {
            if(gameOver) return;
            cell[id].setTranslationY(1000f);
            cell[id].setImageResource(R.drawable.red);
            cell[id].animate().translationYBy(-1000f).rotationBy(360f).setDuration(300);
            state[id] = 0;
            if (!checkBoard()) {
                ai.takeTurn();
            }
        }
    }

    private void markSquare(int id) {
        cell[id].setTranslationY(-1000f);
        cell[id].setImageResource(R.drawable.yellow);
        cell[id].animate().translationYBy(1000f).rotationBy(360f).setDuration(300);
        state[id] = 1;
        checkBoard();
    }

    private boolean checkBoard() {
        for(int[] pos: winningPositions) {
            if(state[pos[0]] != -1
                && state[pos[1]] == state[pos[0]]
                && state[pos[2]] == state[pos[0]]) {
                if(state[pos[0]] == 0) {
                    message.setText("Congrats, you has defeated AI");
                } else {
                    message.setText("Sorry, AI has beat you");
                }
                playAgainLayout.setVisibility(View.VISIBLE);
                gameOver = true;
                return true;
            }
        }
        // check for draw
        boolean isDraw = true;
        for(int i = 0; i < state.length; i++) {
            if(state[i] == -1) {
                isDraw = false;
            }
        }
        if(isDraw) {
            message.setText("It's a draw");
            playAgainLayout.setVisibility(View.VISIBLE);
            gameOver = true;
            return true;
        }
        return gameOver;
    }

    public void reset(View view) {
        message.setText("");
        playAgainLayout.setVisibility(View.INVISIBLE);
        Arrays.fill(state, -1);
        for(int i = 0; i < cell.length; i++) {
            cell[i].setImageResource(android.R.color.transparent);
        }
        gameOver = false;
    }

}
