package ticTacToe.game;

import ticTacToe.ai.MiniMax;

/**
 * Class contains coordinate and rate of field cell
 *
 * @see MiniMax
 * @see ticTacToe.ai.SelfLearning
 */
public class Cell {
    public int string;
    public int row;
    public int rate;

    public Cell(int string, int row) {
        this.string = string;
        this.row = row;
        this.rate = 0;
    }
}