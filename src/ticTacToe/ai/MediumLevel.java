package ticTacToe.ai;

import ticTacToe.game.Cell;

import static ticTacToe.game.Game.*;
import static ticTacToe.game.Game.Figure.EMPTY;
import static ticTacToe.ui.UserInterface.game;

/**
 *  Class that implements medium level for game - it finished sequence if it found
 */
public class MediumLevel {
    /**
     * Method scanning field upside down row by row, if row contains target values and one empty cell, this cell selects
     * else method goes to horizontal scan, then diagonal
     * @param value which combination should be checked
     */
    public Cell scan(Figure value) {
        Figure oppositeValue = game.getOppositeFigure(value);
        int emptyString = 0;
        int emptyRow = 0;
        int emptyCnt;
        for (int row = 0; row < game.getFieldSize(); row++) {
            emptyCnt = 0;
            for (int string = 0; string < game.getFieldSize(); string++) {
                Figure fieldValue = game.getFieldValue(string,row);
                if (fieldValue.equals(oppositeValue)) {
                    emptyCnt = 0;
                    break;
                } else if (fieldValue.equals(EMPTY)) {
                    emptyCnt++;
                    if (emptyCnt < 2) {
                        emptyRow = row;
                        emptyString = string;
                    } else {
                        break;
                    }
                }
            }
            if (emptyCnt == 1) {
                return new Cell(emptyString, emptyRow);
            }
        }
        return scanHorizontal(value);
    }

    /**
     * Method scanning field left to right string by string, if string contains target values and one empty cell,
     * this cell selects else method goes to diagonal scan
     * @param value which combination should be checked
     */
    private Cell scanHorizontal(Figure value) {
        Figure oppositeValue = game.getOppositeFigure(value);
        int emptyString = 0;
        int emptyRow = 0;
        int emptyCnt;
        for (int string = 0; string < game.getFieldSize(); string++) {
            emptyCnt = 0;
            for (int row = 0; row < game.getFieldSize(); row++) {
                Figure fieldValue = game.getFieldValue(string,row);
                if (fieldValue.equals(oppositeValue)) {
                    emptyCnt = 0;
                    break;
                } else if (fieldValue.equals(EMPTY)) {
                    emptyCnt++;
                    if (emptyCnt < 2) { //too much empty cells
                        emptyRow = row;
                        emptyString = string;
                    } else {
                        break;
                    }
                }
            }
            if (emptyCnt == 1) {
                return new Cell(emptyString, emptyRow);
            }
        }
        return scanFirstDiagonal(value);
    }

    /**
     * Method scanning first diagonal, if it contains target values and one empty cell,
     * this cell selects else method goes to scan second diagonal
     * @param value which combination should be checked
     */
    private Cell scanFirstDiagonal(Figure value) {
        Figure oppositeValue = game.getOppositeFigure(value);
        int emptyString = 0;
        int emptyRow = 0;
        int emptyCnt = 0;
        int row = 0;
        for (int string = 0; string < game.getFieldSize(); string++) {
            Figure fieldValue = game.getFieldValue(string,row);
            if (fieldValue.equals(oppositeValue)) {
                emptyCnt = 0;
                break;
            } else if (fieldValue.equals(EMPTY)) {
                emptyCnt++;
                emptyRow = row;
                emptyString = string;
            }
            row++;
        }
        if (emptyCnt == 1) {
            return new Cell(emptyString, emptyRow);
        }
        return scanSecondDiagonal(value);
    }

    /**
     * Method scanning second diagonal, if it contains target values and one empty cell,
     * this cell selects else method returns null.
     * @param value which combination should be checked
     */
    private Cell scanSecondDiagonal(Figure value) {
        Figure oppositeValue = game.getOppositeFigure(value);
        int emptyString = 0;
        int emptyRow = 0;
        int emptyCnt = 0;
        int row = 0;
        for (int string = game.getFieldSize() - 1; string >= 0; string--) {
            Figure fieldValue = game.getFieldValue(string,row);
            if (fieldValue.equals(oppositeValue)) {
                emptyCnt = 0;
                break;
            } else if (fieldValue.equals(EMPTY)) {
                emptyCnt++;
                emptyRow = row;
                emptyString = string;

            }
            row++;
        }
        if (emptyCnt == 1) {
            return new Cell(emptyString, emptyRow);
        }
        return null;
    }
}
