package app;

import java.util.Random;

import javafx.scene.control.Button;

public class Game {
    private String whoStarted;
    private String turn;
    private int[] pointsXO;
    private int moveCount;

    public Game() {
        Random random = new Random();
        this.turn = random.nextInt(2) == 0 ? "X" : "O";
        pointsXO = new int[]{0, 0};
        moveCount = 0;
    }

    public String getWhoStarted() {
        return this.whoStarted;
    }

    public void setWhoStarted(String player) {
        this.whoStarted = player;
    }

    public String getTurn() {
        return turn;
    }

    public int[] getPoints() {
        return pointsXO;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void switchTurn() {
        this.turn = turn.equals("X") ? "O" : "X";
        ++moveCount;
    }

    private void switchGameTurn() {
        this.whoStarted = this.whoStarted.equals("X") ? "O" : "X";
        this.turn = this.whoStarted;
    }

    public void updatePoints(String winner) {
        if (winner.equals("X")) {
            pointsXO[0]++;
        } else {
            pointsXO[1]++;
        }
    }

    public boolean checkWin(Button[][] button, String player) {
        for (int i = 0; i < 3; ++i) {
            if (checkRow(button, i, player) || checkColumn(button, i, player))
                return true;
        }

        return checkDiagonals(button, player);
    }

    private boolean checkRow(Button[][] button, int row, String player) {
        if (button[row][0].getText().equals(player)
                && button[row][1].getText().equals(player)
                && button[row][2].getText().equals(player))
            return true;

        return false;
    }

    private boolean checkColumn(Button[][] button, int col, String player) {
        if (button[0][col].getText().equals(player)
                && button[1][col].getText().equals(player)
                && button[2][col].getText().equals(player))
            return true;

        return false;
    }

    private boolean checkDiagonals(Button[][] button, String player) {
        if (button[0][0].getText().equals(player)
                && button[1][1].getText().equals(player)
                && button[2][2].getText().equals(player))
            return true;

        if (button[0][2].getText().equals(player)
                && button[1][1].getText().equals(player)
                && button[2][0].getText().equals(player))
            return true;

        return false;
    }

    public void reset() {
        this.switchGameTurn();
        moveCount = 0;
    }
}