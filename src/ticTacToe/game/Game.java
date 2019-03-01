package ticTacToe.game;

import ticTacToe.ai.LearningAlgorithm;
import ticTacToe.ui.DisplayPlayer;
import ticTacToe.ui.UserInterface;

import javax.swing.*;
import java.util.Random;

/**
 *  This class saves all game settings, and current parameters
 */
public class Game {

    /**
     *  Instance of learning algorithm class using in game.
     */
    public LearningAlgorithm learningAlgorithm;

    /**
     *  Using for game setting who moves first
     */
    public enum FirstPlayerSelect {
        PLAYER1,
        PLAYER2,
        RANDOM
    }

    /**
     *  Using for game level identification
     */
    public enum Levels {
        EASY,
        MEDIUM,
        HARD,
        LEARNING
    }

    /**
     *  Constants for cell state identification
     */
    public enum Figure {
        ZERO,
        CROSS,
        EMPTY
    }
//    public static final int ZERO = 0;
//    public static final int CROSS = 1;
//    public static final int EMPTY = 2;

    /**
     *  Players declaration
     */
    private Player player1;
    private Player player2;

    /**
     *  Contains setting for game start- which player should make first move
     */
    private FirstPlayerSelect firstPlayerUserSelection;

    /**
     *  Contains information which player turn now
     */
    private Player currentPlayer;

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

    /**
     * Constructor of class
     * @param fieldSize size of game field, configured by UI
     * @see UserInterface
     */
    public Game(int fieldSize) {
        player1 = new Player(Figure.CROSS);
        player2 = new Player(Figure.ZERO);
        firstPlayerUserSelection = FirstPlayerSelect.RANDOM;
        gameStarted = false;
        gameField = new Figure[fieldSize][fieldSize];
        this.fieldSize = fieldSize;
        setFieldEmpty();
        activeFigure = Figure.CROSS;
        learningAlgorithm = new LearningAlgorithm(fieldSize);
        learningAlgorithm.start();
    }

    /**
     *  method that configure game to a start position depending of game settings
     */
    public  void startTheGame(){
        if (gameStarted) {
            JOptionPane.showMessageDialog(null,"Game already started!");
            return;
        }

        if (learningInProcess && (player1.getLevel() == Levels.LEARNING ||  player2.getLevel() == Levels.LEARNING)) {

            JOptionPane.showMessageDialog(null,"Learning in process. " +
                    "Please change game level to any another, or wait while learning will be finished.");
            return;
        }

        gameStarted = true;
        activeFigure = Figure.CROSS;

        switch (firstPlayerUserSelection) {
            case PLAYER1 : {
                currentPlayer = player1;
                player1.setFigure(Figure.CROSS);
                player2.setFigure(Figure.ZERO);
                break;
            }
            case PLAYER2 : {
                currentPlayer = player2;
                player1.setFigure(Figure.ZERO);
                player2.setFigure(Figure.CROSS);
                break;
            }
            case RANDOM : {
                if (new Random().nextBoolean()) {
                    currentPlayer = player2;
                    player1.setFigure(Figure.ZERO);
                    player2.setFigure(Figure.CROSS);
                } else {
                    currentPlayer = player1;
                    player1.setFigure(Figure.CROSS);
                    player2.setFigure(Figure.ZERO);
                }
                break;
            }
        }

        new DisplayPlayer().display();

        for ( int i = 0; i < fieldSize; i++) {
            for ( int j = 0; j < fieldSize; j++) {
                updateField(i,j, Figure.EMPTY);
                UserInterface.getButton(i,j).printFieldElement();
                UserInterface.getButton(i,j).setButtonEnabled(true);
            }
        }

        currentPlayer.makeMove();
    }

    /**
     *  method restarting the game
     */
    public void restartTheGame(){
        gameStarted = false;
        startTheGame();
    }

    /**
     *  method resets field but not starting a new game
     */
    public void stopGame() {
        gameStarted = false;
        currentPlayer = null;
        new DisplayPlayer().display();
        for (int i = 0; i < fieldSize; i++) {
            for ( int j = 0; j < fieldSize; j++) {
                updateField(i,j, Figure.EMPTY);
                UserInterface.getButton(i,j).setWaitingForGame();
            }
        }
    }

    /**
     * Method sets all elements of field empty
     */
    private void setFieldEmpty() {
        for (int i = 0; i < fieldSize; i++) {
            for ( int j = 0; j < fieldSize; j++) {
                updateField(i,j, Figure.EMPTY);
            }
        }
    }

    /**
     *  method freezing game, calling from results checker. Used for watching the field after game finished.
     *
     * @see GameResult
     */
    public void endTheGame(){
        gameStarted = false;
        currentPlayer = null;
        new DisplayPlayer().display();
        for ( int i = 0; i < fieldSize; i++) {
            for ( int j = 0; j < fieldSize; j++) {
                UserInterface.getButton(i,j).setWaitingForGame();
            }
        }
        System.out.println("game finished");
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
        updateField(string,row,fieldValue);
        UserInterface.getButton(string,row).printFieldElement();

        new GameResult().checkGameResult();

        if (getActiveFigure() == Figure.CROSS) {
            activeFigure = Figure.ZERO;
        } else {
            activeFigure = Figure.CROSS;
        }

        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }

        new DisplayPlayer().display();

        if (currentPlayer != null) {
            currentPlayer.makeMove();
        }
    }

    /**
     * method updates cell in field by given coordinates
     * @param string number of string
     * @param row number of row
     * @param fieldValue valuethat should be written
     */
    public void updateField(int string, int row, Figure fieldValue) {
        gameField[string][row] = fieldValue;
    }

    /**
     * getters and setters for game parameters
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

    public  void setFirstPlayerUserSelection(FirstPlayerSelect firstPlayerUserSelection) {
        this.firstPlayerUserSelection = firstPlayerUserSelection;
    }

    public  boolean isGameStarted() {
        return gameStarted;
    }

    public  Player getCurrentPlayer() {
        return currentPlayer;
    }

    public  Player getPlayer(int id) {
        if (id == 1) {
            return player1;
        } else {
            return player2;
        }
    }

    public  int getFieldSize() {
        return fieldSize;
    }

    public boolean isLearningInProcess() {
        return learningInProcess;
    }

    public void setLearningInProcess(boolean learningInProcess) {
        this.learningInProcess = learningInProcess;
    }


}
