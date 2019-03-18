package ticTacToe.ai;

import ticTacToe.game.*;
import ticTacToe.ui.UserInterface;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class that implements self learning process. It just simulate game with two computers and writes results
 */
public class SelfLearning extends Thread {
    /**
     * List of fields, converted to long values which were made by computers
     */
    private ArrayList<Long> log = new ArrayList<>();

    /**
     * Number of games which should be played in this selfExperienced process
     */
    private int iterations;

    /**
     * Instance of computer rival class using for self-selfExperienced moves simulation
     */
    private ComputerRival ai;

    /**
     * Is a map which contains moves and its rate (updating in learning process).
     */
    private Map<Long, Integer> fieldsMap;

    /**
     * Is a ratio between current learning iteration and common iterations number
     * when easy making move level changing to medium (to get more different moves)
     */
    private static final double MOVE_TYPE_CHANGING_BORDER = 0.7d;

    /**
     * Constructor sets field values to a default
     * @param iterations number of games which should be played in this selfExperienced process
     */
    public SelfLearning(int iterations) {
        this.iterations = iterations;
        ai = new ComputerRival();
    }

    /**
     * Training game process which starts in new thread for non-blocking application work
     */
    @Override
    public void run() {
        game.setLearningInProcess(true);
        try {
            game.selfExperiencedAI.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fieldsMap = new HashMap<>(game.selfExperiencedAI.getFieldsMap());

        int currentIteration = 0;
        for ( ; currentIteration < iterations; currentIteration++) {
            log.clear();
            selfLearningGame(currentIteration);
            if (!game.isLearningInProcess()) {
                break;
            }
        }
        save();
        game.selfExperiencedAI.setFieldsMap(fieldsMap);
        String message =  "Learning with " + currentIteration + " iterations completed. " +
                " Fields collection size now is " + fieldsMap.keySet().size();
        System.out.println();
        UserInterface.showMessage(message);
        game.setLearningInProcess(false);
    }

    /**
     * Method makes a simulation of game with random(or medium-level) moves
     */
    private void selfLearningGame(int currentIteration) {
        Game.Figure[][] field = createEmptyCellsField();
        Game.Figure activeFigure = CROSS;
        boolean isEasy;
        isEasy = currentIteration < iterations * MOVE_TYPE_CHANGING_BORDER;
        while (true) {
            field = makeMove(field, activeFigure, isEasy).clone();
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
    private Game.Figure[][] makeMove(Game.Figure[][] field, Game.Figure activeFigure, boolean isEasy) {
        Cell cell;

        if (isEasy) {
            cell = ai.easy(field);
        } else {
            cell = ai.medium();
        }

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
            updateFieldsMap(CROSS, log);
            return true;
        }

        if (gameResult.checkWin(field, ZERO)) {
            updateFieldsMap(ZERO, log);
            return true;
        }

        if (gameResult.emptyCells(field).isEmpty()) {
            updateFieldsMap(EMPTY,log);
            return true;
        }

        return false;
    }

    /**
     * Method updates fields collection and rates of existing elements
     * @param result which figure wins- X or O
     * @param movesLog list of fields converted to long. it'string moves that were made in this game
     */
    public void updateFieldsMap(Game.Figure result, List<Long> movesLog) {
        int deltaRate = 0;

        if (result.equals(CROSS)) {
            deltaRate = 1;
        } else if (result.equals(ZERO)){
            deltaRate = -1;
        }

        for (long move : movesLog) {
            Integer prevRate = fieldsMap.putIfAbsent(move, deltaRate);
            if (prevRate != null) {
                prevRate += deltaRate;
                fieldsMap.put(move, prevRate);
            }
        }
    }

    /**
     * Method saved fields map to file
     */
    private void save() {
        File savedFields;
        switch (game.getFieldSize()) {
            case 3 : {
                savedFields = new File("fields3x3.ser");
                break;
            }
            case 4 : {
                savedFields = new File("fields4x4.ser");
                break;
            }
            case 5 : {
                savedFields = new File("fields5x5.ser");
                break;
            }
            case 6 : {
                savedFields = new File("fields6x6.ser");
                break;
            }
            default : savedFields = new File("fields3x3.ser");
        }

        if (!savedFields.isFile()) {
            try {
                savedFields.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("\nFile creation is impossible!");
                return;
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(savedFields);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            objectOutputStream.writeObject(fieldsMap);
            objectOutputStream.close();
            System.out.println("Moves saved to file, size " + fieldsMap.keySet().size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
