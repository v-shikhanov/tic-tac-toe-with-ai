package ticTacToe.game;

import ticTacToe.ui.UserInterface;
import java.util.ArrayList;
import java.util.List;
import static ticTacToe.game.Game.*;
import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;

/**
 *  Class for field analyse and winner detecting
 */
public class GameResult {

    /**
     * Method checks game checkWin and tie for players 1 and 2
     */
    public void checkGameResult() {
        if (checkWin(game.getGameField(), CROSS)) {
            printGameResult(CROSS);
            return;
        } else if (checkWin(game.getGameField(), ZERO)) {
            printGameResult(ZERO);
            return;
        }

        if (emptyCells(game.getGameField()).isEmpty()) {
            printGameResult(EMPTY);
        }
    }

    /**
     * Method gets field and check all winning states for given figure
     * @param field game field that should be checked
     * @param figure figure for which game result should be checked
     * @return true if winning combination for given figure exists in field
     */
    public boolean checkWin(Figure[][] field, Figure figure) {
        if (checkWinVertical(field, figure)) {
            return true;
        }

        if (checkWinHorizontal(field, figure)) {
            return true;
        }

        if (checkWinFirstDiagonal(field, figure)) {
            return true;
        }

        return checkWinSecondDiagonal(field, figure);
    }

    /**
     * Method checks for winning combination fields upside down.
     * @param field game field that should be checked
     * @param figure figure for which game result should be checked
     * @return true if winning combination in given field for given figure exists
     */
    private boolean checkWinVertical(Figure[][] field, Figure figure) {
        for (int string = 0; string < game.getFieldSize(); string++) {
            for(int row = 0; row < game.getFieldSize(); row++) {
                if (field[string][row] != figure) {
                    break;
                }
                if (row == game.getFieldSize() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method checks for winning combination fields left to right.
     * @param field game field that should be checked
     * @param figure figure for which game result should be checked
     * @return true if winning combination in given field for given figure exists
     */
    private boolean checkWinHorizontal(Figure[][] field, Figure figure) {
        for (int row = 0; row < game.getFieldSize(); row++) {
            for(int string = 0; string < game.getFieldSize(); string++) {
                if (field[string][row] != figure) {
                    break;
                }
                if (string == game.getFieldSize() - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method checks for winning combination first diagonal
     * @param field game field that should be checked
     * @param figure figure for which game result should be checked
     * @return true if winning combination in given field for given figure exists
     */
    private boolean checkWinFirstDiagonal(Figure[][] field, Figure figure) {
        for (int string = 0; string < game.getFieldSize(); string++) {
            if (field[string][string] != figure) {
                break;
            }
            if (string == game.getFieldSize() - 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method checks for winning combination second diagonal
     * @param field game field that should be checked
     * @param figure figure for which game result should be checked
     * @return true if winning combination in given field for given figure exists
     */
    private boolean checkWinSecondDiagonal(Figure[][] field, Figure figure) {
        int row = 0;
        for (int string = game.getFieldSize()-1; string >= 0; string--) {
            if (field[string][row] != figure) {
                break;
            }
            if (row == game.getFieldSize() - 1) {
                return true;
            }
            row++;
        }
        return false;
    }

    /**
     * Method creates list of cells able to move
     * @param field should be checked
     * @return list of empty cells
     */
    public List<Cell> emptyCells(Figure[][] field) {
        List<Cell> emptyCells = new ArrayList<>();
        for (int row = 0; row < field.length; row++) {
            for (int string = 0; string < field.length; string++) {
                if (field[string][row] == EMPTY) {
                    emptyCells.add(new Cell(string,row));
                }
            }
        }
        return emptyCells;
    }

    /**
     * Opens new information window according game result
     * @param result figure that checkWin the game
     */
    private void printGameResult(Figure result){
        String winnersName;
        switch (result) {
            case ZERO : {
                if (game.getPlayer(1).getFigure() == ZERO) {
                    winnersName = UserInterface.getPlayer1().getText();
                } else {
                    winnersName = UserInterface.getPlayer2().getText();
                }
                break;
            }
            case CROSS : {
                if (game.getPlayer(1).getFigure() == CROSS) {
                    winnersName = UserInterface.getPlayer1().getText();
                } else {
                    winnersName = UserInterface.getPlayer2().getText();
                }
                break;
            }

            case EMPTY : {
                winnersName = "Friendship";
                break;
            }
            default : winnersName = "No one"; break;
        }

        UserInterface.showMessage(winnersName + " wins!!!");
        game.stopGame(false);
    }
}
