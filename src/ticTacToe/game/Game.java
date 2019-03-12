package ticTacToe.game;

import ticTacToe.ai.LearningAlgorithm;
import ticTacToe.ui.DisplayPlayer;
import ticTacToe.ui.UserInterface;
import javax.swing.*;
import java.util.Random;
/**
 * This class saves all game settings, and current parameters
 */
public class Game {

    /**
     * Instance of learning algorithm class using in game.
     */
    public LearningAlgorithm learningAlgorithm;

    /**
     * Using for game setting who moves first
     */
    public enum FirstPlayerSelection {
        PLAYER1,
        PLAYER2,
        RANDOM
    }

    /**
     * Using for game level identification
     */
    public enum Levels {
        EASY,
        MEDIUM,
        HARD,
        LEARNING
    }

    /**
     * Constants for cell state identification
     */
    public enum Figure {
        ZERO,
        CROSS,
        EMPTY
    }

    /**
     * Players declaration
     */
    private Player player1;
    private Player player2;

    /**
     * Contains setting for game start- which player should make first move
     */
    private FirstPlayerSelection firstPlayerUserSelection;

    /**
     *  Contains information which player turn now
     */
    private Player activePlayer;

    /**
     *  Parameter deactivating start game method after game start until it finished
     */
    private boolean gameStarted;

    /**
     *  Contains game field and it'string size.
     */
    private int fieldSize;
    private Figure[][] gameField;

    /**
     * field contains information about learning process. is it active or not. To avoid problems with multiply
     * threading
     */
    private boolean learningInProcess;

    /**
     * Contains which figure is active now - zero or cross
     */
    private Figure activeFigure;

    private GameResult gameResult;

    /**
     * Constructor of class
     * @param fieldSize size of game field, configured by UI
     * @see UserInterface
     */
    public Game(int fieldSize) {
        gameResult = new GameResult();
        player1 = new Player(Figure.CROSS);
        player2 = new Player(Figure.ZERO);
        this.fieldSize = fieldSize;
        firstPlayerUserSelection = FirstPlayerSelection.RANDOM;
        gameStarted = false;
        gameField = new Figure[fieldSize][fieldSize];
        activeFigure = Figure.CROSS;
        learningAlgorithm = new LearningAlgorithm(fieldSize);
        learningAlgorithm.start();
    }

    /**
     * Method restarting the game
     */
    public void restartTheGame() {
        gameStarted = false;
        startTheGame();
    }

    /**
     * Method configures game to a start position depending on game settings
     */
    public  void startTheGame() {
        if (gameStarted) {
            JOptionPane.showMessageDialog(null,"Game already started!");
            return;
        }

        if (learningInProcess && (player1.getLevel() == Levels.LEARNING ||  player2.getLevel() == Levels.LEARNING)) {
            JOptionPane.showMessageDialog(null,"Learning in process. " +
                    "Please change game level to any another, or wait until learning will be finished.");
            return;
        }

        gameStarted = true;
        activeFigure = Figure.CROSS;

        switch (firstPlayerUserSelection) {
            case PLAYER1 : {
                setFirstPlayer(player1);
                break;
            }
            case PLAYER2 : {
                setFirstPlayer(player2);
                break;
            }
            case RANDOM : {
                if (new Random().nextBoolean()) {
                    setFirstPlayer(player1);
                } else {
                    setFirstPlayer(player2);
                }
                break;
            }
        }
        UserInterface.displayPlayer.display(false);
        updateField(true, true);
        activePlayer.makeMove();
    }

    /**
     * Method that bounds players to its figures and sets active player at start of game
     * @param firstPlayer player1 or player2
     */
    private void setFirstPlayer(Player firstPlayer) {
        activePlayer = firstPlayer;
        if (activePlayer == player1) {
            player1.setFigure(Figure.CROSS);
            player2.setFigure(Figure.ZERO);

        } else {
            player2.setFigure(Figure.CROSS);
            player1.setFigure(Figure.ZERO);
        }
    }

    /**
     * Method stops game and freezing it, or totally resets depending on
     * is it called by user or by program
     */
    public void stopGame(boolean setAllCellsEmpty) {
        gameStarted = false;
        UserInterface.displayPlayer.display(true);
        updateField(setAllCellsEmpty, false);
        if (!setAllCellsEmpty) {
            System.out.println("game finished");
        }
    }

    /**
     * method for update field matrix with new value and current game turn
     * changing active player, displaying it and making next move
     *
     * @param string string number in matrix
     * @param row row number in matrix
     * @param fieldValue value which should be write
     * @see Player
     * @see DisplayPlayer
     */
    public void nextMove(int string, int row, Figure fieldValue) {
        updateCell(string,row,fieldValue);
        UserInterface.getButton(string,row).printFieldElement();

        gameResult.checkGameResult();
        activeFigure = getOppositeFigure(activeFigure);
        changeActivePlayer();
        UserInterface.displayPlayer.display(false);

        if (gameStarted) {
            activePlayer.makeMove();
        }
    }

    /**
     * Method changes active figure, calling when move is finished
     */
    public Figure getOppositeFigure(Figure activeFigure) {
        if (activeFigure == Figure.CROSS) {
            return Figure.ZERO;
        }
        return Figure.CROSS;
    }

    /**
     * Method changes active player, calling when move is finished
     */
    private void changeActivePlayer() {
        if (activePlayer == player1) {
            activePlayer = player2;
        } else {
            activePlayer = player1;
        }
    }

    /**
     * Method updates all field elements
     * @param setAllCellsEmpty method sets all cells to empty state if enabled
     * @param setButtonEnabled method sets buttons (in ui) to enabled, or disabled state
     */
    public void updateField(boolean setAllCellsEmpty, boolean setButtonEnabled) {
        for (int striing = 0; striing < fieldSize; striing++) {
            for ( int row = 0; row < fieldSize; row++) {
                if (setAllCellsEmpty) {
                    updateCell(striing, row, Figure.EMPTY);
                }
                UserInterface.getButton(striing,row).printFieldElement();
                UserInterface.getButton(striing,row).setButtonEnabled(setButtonEnabled);
            }
        }
    }

    /**
     * Method updates cell in field by given coordinates
     * @param string number of string
     * @param row number of row
     * @param fieldValue value that should be written
     */
    public void updateCell(int string, int row, Figure fieldValue) {
        gameField[string][row] = fieldValue;
    }

    /**
     * Getters and setters for game parameters
     */
    public Figure getActiveFigure() {
        return activeFigure;
    }

    public Figure getFieldValue(Integer string, Integer row) {
        return gameField[string][row];
    }

    public Figure[][] getGameField() {
        return gameField;
    }

    public void setFirstPlayerUserSelection(FirstPlayerSelection firstPlayerUserSelection) {
        this.firstPlayerUserSelection = firstPlayerUserSelection;
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getPlayer(int id) {
        if (id == 1) {
            return player1;
        }
        return player2;
    }

    public int getFieldSize() {
        return fieldSize;
    }

    public boolean isLearningInProcess() {
        return learningInProcess;
    }

    public void setLearningInProcess(boolean learningInProcess) {
        this.learningInProcess = learningInProcess;
    }
}
