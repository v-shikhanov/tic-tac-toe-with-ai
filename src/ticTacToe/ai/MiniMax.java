package ticTacToe.ai;

import ticTacToe.game.Cell;
import ticTacToe.game.Game;
import ticTacToe.game.GameResult;
import java.util.List;
import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class that implements algorithm minimax to find the best move *
 */
public class MiniMax extends GameResult {
    /**
     * Minimax method that completes recursively until the terminal game state will not found.
     * A result of this algorithm work is a tree with all possible game combinations from which selected move with
     * minimum probability of checkWin for opponent and maximum for computer
     *
     * @param field game field that should be analysed
     * @param activeFigure figure that should moves now
     * @param currentPlayerFigure player figure that should moves now
     * @return cell with best move coordinates and its rate
     */
    public Cell minimax (Game.Figure[][] field, Game.Figure activeFigure, Game.Figure currentPlayerFigure) {
        Cell cell = new Cell(0,0);
        Game.Figure computer = currentPlayerFigure;
        Game.Figure opponent = game.getOppositeFigure(currentPlayerFigure);
        List<Cell> emptyCells = emptyCells(field);
        int bestNodeIndex = 0;
        int nodeRate;

        if (checkWin(field, computer)) {
            cell.rate = 10;
            return cell;
        } else if (checkWin(field, opponent)) {
            cell.rate = -10;
            return cell;
        } else if (emptyCells(field).isEmpty()) {
            cell.rate = 0;
            return cell;
        }

        for ( int i = 0; i < emptyCells.size(); i++) {
            cell= emptyCells.get(i);
            field[cell.string][cell.row] = activeFigure;

            if (activeFigure == computer) {
                cell.rate = minimax(field, opponent, currentPlayerFigure).rate ;
            } else {
                cell.rate  = minimax(field, computer, currentPlayerFigure).rate ;
            }
            field[cell.string][cell.row] = EMPTY;
            emptyCells.set(i, cell);
        }

        if (activeFigure == computer) {
            nodeRate = -100;
        } else {
            nodeRate = 100;
        }

        for (int i = 0; i < emptyCells.size(); i++) {
            int rate = emptyCells.get(i).rate;
            if (activeFigure == computer) {
                if (rate > nodeRate) {
                    nodeRate = rate;
                    bestNodeIndex = i;
                }
            } else {
                if (nodeRate > rate) {
                    nodeRate = rate;
                    bestNodeIndex = i;
                }
            }
        }
        return emptyCells.get(bestNodeIndex);
    }
}
