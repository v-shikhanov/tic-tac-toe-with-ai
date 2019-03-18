package ticTacToe.ai;

import ticTacToe.game.*;
import ticTacToe.ui.UserInterface;
import java.util.ArrayList;
import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class that implements learning process. It just simulate game with two random going computers and writes results
 */
public class SelfLearning extends Thread{
    /**
     * List of fields, converted to long values which were made by computers
     */
    private ArrayList<Long> log = new ArrayList<>();

    /**
     * Instance of learning algorithm class used for fields load etc
     */
    private LearningAlgorithm learningAlgorithm = new LearningAlgorithm(game.getFieldSize());

    /**
     * Number of games which should be played in this learning process
     */
    private int iterations;

    /**
     * Constructor sets field values to a default
     * @param iterations number of games which should be played in this learning process
     */
    public SelfLearning(int iterations) {
        this.iterations = iterations;
    }

    /**
     * Training game process which starts in new thread for non-blocking application work
     */
    @Override
    public void run() {
        game.setLearningInProcess(true);
        learningAlgorithm.start();

        try {
            learningAlgorithm.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String message;
        int i = 0;
        for (; i < iterations; i++) {
            log.clear();
            selfLearningGame();
            System.out.println("Learning iteration " + i);
            if (!game.isLearningInProcess()) {
                break;
            }
        }
        learningAlgorithm.save();
        game.learningAlgorithm.run();
        message =  "Learning with " + i + " iterations completed. " +
                " Fields collection size now is " + learningAlgorithm.fieldsMap.keySet().size();
        System.out.println();
        UserInterface.showMessage(message);
        game.setLearningInProcess(false);
    }

    /**
     * Method makes a simulation of game with random moves
     */
    private void selfLearningGame() {
        Game.Figure[][] field = createEmptyCellsField();
        Game.Figure activeFigure = CROSS;

        while (true) {
            field = makeMove (field, activeFigure).clone();
            if (isGameFinished(field)) {
                return;
            }
            activeFigure = game.getOppositeFigure(activeFigure);
        }
    }

    /**
     * Method creates field filled by empty cells
     * @return field with empty cells
     */
    private Game.Figure[][] createEmptyCellsField() {
        int size = game.getFieldSize();
        Game.Figure[][] field = new Game.Figure[size][size];
        for ( int i = 0; i < field.length; i++) {
            for ( int j = 0; j < field.length; j++) {
                field[i][j] = EMPTY;
            }
        }
        return field;
    }

    /**
     * Method makes a random move in given field with active figure and logging it
     * @param field where move should be found
     * @param activeFigure is a figure which moves now
     * @return field modified with new move
     */
    private Game.Figure[][] makeMove(Game.Figure[][] field, Game.Figure activeFigure) {
        Cell cell = new ComputerRival().easy(field);
        field[cell.string][cell.row] = activeFigure;
        log.add(new FieldCoder().getCode(field));
        return field;
    }

    /**
     * Method checks for given field is game finished
     * @param field which must be checked
     * @return true if finished
     */
    private boolean isGameFinished(Game.Figure[][] field) {
        GameResult gameResult = new GameResult();

        if (gameResult.checkWin(field, CROSS)) {
            learningAlgorithm.updateFieldsMap(CROSS, log);
            return true;
        }

        if (gameResult.checkWin(field, ZERO)) {
            learningAlgorithm.updateFieldsMap(ZERO, log);
            return true;
        }

        if (gameResult.emptyCells(field).isEmpty()) {
            learningAlgorithm.updateFieldsMap(EMPTY,log);
            return true;
        }

        return false;
    }
}
