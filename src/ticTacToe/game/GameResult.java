package ticTacToe.game;

import ticTacToe.ui.UserInterface;

import javax.swing.*;

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
     * Method checks game win and tie for players 1 and 2
     */
    public  void checkGameResult() {
        if (win(game.getGameField(), CROSS)) {
            printGameResult(CROSS);
            return;
        } else if (win(game.getGameField(), ZERO)) {
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
    public boolean win(Figure[][] field, Figure figure) {
        for (int s = 0; s < game.getFieldSize(); s++) {
            for(int r = 0; r < game.getFieldSize(); r++) {
                if (field[s][r] != figure) {
                    break;
                }
                if (r == game.getFieldSize() - 1) {
                    return true;
                }
            }
        }

        for (int r = 0; r < game.getFieldSize(); r++) {
            for(int s = 0; s < game.getFieldSize(); s++) {
                if (field[s][r] != figure) {
                    break;
                }
                if (s == game.getFieldSize() - 1) {
                    return true;
                }
            }
        }

        for (int s = 0; s < game.getFieldSize(); s++) {
            if (field[s][s] != figure) {
                break;
            }
            if (s == game.getFieldSize() - 1) {
                return true;
            }
        }

        int r = 0;
        for (int s = game.getFieldSize()-1; s >= 0; s--) {

            if (field[s][r] != figure) {
                break;
            }
            if (r == game.getFieldSize() - 1) {
                return true;
            }
            r++;
        }
        return false;
    }

    /**
     * Method creates list of cells free to move
     * @param field field to check
     * @return list of cells
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
     * Open new information window according game result
     * @param result figure that win the game
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

        JOptionPane.showMessageDialog(null,
                winnersName + " wins!!!");
        game.stopGame(false);
    }
}
