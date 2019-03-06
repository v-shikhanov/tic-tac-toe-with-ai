package ticTacToe.ai;

import ticTacToe.game.Cell;
import ticTacToe.game.Game;
import ticTacToe.game.GameResult;

import java.util.List;
import java.util.Random;

import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class for making moves against human or another computer
*/
public class ComputerRival {

    /**
     * Qnt of empty cells when minimax algorithm working good.(If number of empty cells is greater than limit
     * waiting of results is too long
     */
    private static final int miniMaxDepthLimit = 8;

    /**
    * Easy level method - randomly selects cell in field
    * @param field where should be found a coordinate of move
    * @return move coordinate Cell
    */
    public Cell easy(Game.Figure[][] field) {
        List<Cell> emptyCells = new GameResult().emptyCells(field);
        return emptyCells.get(new Random().nextInt(emptyCells.size()));
    }

    /**
     * Method calls scanning of sequence, if it not found making a random move
     * @return move coordinate Cell
     * @see ComputerRival
     */
    public Cell medium() {
        MediumLevel mediumLevel = new MediumLevel();
        Game.Figure valueOfComputer = game.getActiveFigure();
        Game.Figure valueOfOpponent;
        Cell cell = mediumLevel.scan(valueOfComputer);
        if (cell != null) {
            return cell;
        }

        if (valueOfComputer == CROSS) {
            valueOfOpponent = ZERO;
        } else {
            valueOfOpponent = CROSS;
        }

        cell = mediumLevel.scan(valueOfOpponent);
        if (cell != null) {
            return cell;
        }

        return easy(game.getGameField());
    }

    /**
     *  Hard level using minimax algorithm at small field sizes
     * @param field where should be found a coordinate of move
     * @param activeFigure figure which should moves now
     * @param playerFigure is a figure of player which considering
     *                     mininmax algorithm
     * @return move coordinate Cell
     */
    public Cell hard(Game.Figure[][] field, Game.Figure activeFigure, Game.Figure playerFigure) {

        if (new GameResult().emptyCells(game.getGameField()).size() > miniMaxDepthLimit) {
            return easy(game.getGameField());
        }

        Cell cell = new MiniMax().minimax(field, activeFigure, playerFigure);
        return cell;
    }

    /**
     *  If learning algorithm now is free and loaded it used.
     *  @return move coordinate Cell
     */
    public Cell learning() {
        if (game.isLearningInProcess()) {
            return hard(game.getGameField(), game.getActiveFigure(), game.getActiveFigure());
        } else {
            if ( game.learningAlgorithm.isLoadedFromFile()) {
                return game.learningAlgorithm.makeMove(game.getGameField());
            } else {
                return hard(game.getGameField(), game.getActiveFigure(), game.getActiveFigure());
            }
        }
    }
}
