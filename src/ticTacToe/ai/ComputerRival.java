package ticTacToe.ai;

import ticTacToe.game.Cell;
import ticTacToe.game.Game;
import ticTacToe.game.GameResult;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class for making moves against human or another computer
*/
public class ComputerRival {
    /**
     * Qnt of empty cells when minimax algorithm working good.(If number of empty cells is greater than limit
     * waiting of results is too long
     */
    private static final int MINI_MAX_DEPTH_LIMIT = 8;

    /**
    * Easy level method - randomly selects cell in field
    * @param field where should be found a coordinate of move
    * @return coordinate of cell to make move
    */
    public Cell easy(Game.Figure[][] field) {
        List<Cell> emptyCells = new GameResult().emptyCells(field);
        return emptyCells.get(new Random().nextInt(emptyCells.size()));
    }

    /**
     * Method calls scanning of sequence of equal non empty cells with one empty one - horizontal,vertical, diagonal
     * for example for string (OO.) where . is empty, method will return coordinate of this empty cell. So method able to
     * finish game if winning move exists or prevent winning of opponent if winning move exists for him.
     * If this cell not found, method goes to easy level(random move).
     * @return coordinate of cell to make move
     * @see ComputerRival
     */
    public Cell medium() {
        MediumLevel mediumLevel = new MediumLevel();
        Game.Figure valueOfComputer = game.getActiveFigure();
        Optional<Cell> cell = Optional.ofNullable(mediumLevel.scan(valueOfComputer));
        if (cell.isPresent()) {
            return cell.get();
        }
        cell = Optional.ofNullable(mediumLevel.scan(game.getOppositeFigure(valueOfComputer)));
        return cell.orElseGet(() -> easy(game.getGameField()));
    }

    /**
     *  Hard level using minimax algorithm at small field sizes
     * @param field where should be found a coordinate of move
     * @param activeFigure figure which should moves now
     * @param playerFigure is a figure of player which considering
     *                     mininmax algorithm
     * @return coordinate of cell to make move
     */
    public Cell hard(Game.Figure[][] field, Game.Figure activeFigure, Game.Figure playerFigure) {
        if (new GameResult().emptyCells(game.getGameField()).size() > MINI_MAX_DEPTH_LIMIT) {
            return medium();
        }
        return new MiniMax().minimax(field, activeFigure, playerFigure);
    }

    /**
     *  If selfExperienced algorithm now is free and loaded it using
     *  @return move coordinate Cell
     */
    public Cell selfExperienced() {
        if (game.isLearningInProcess()) {
            return hard(game.getGameField(), game.getActiveFigure(), game.getActiveFigure());
        } else {
            if (game.selfExperiencedAI.isLoadedFromFile()) {
                return game.selfExperiencedAI.makeMove(game.getGameField());
            } else {
                return hard(game.getGameField(), game.getActiveFigure(), game.getActiveFigure());
            }
        }
    }
}
