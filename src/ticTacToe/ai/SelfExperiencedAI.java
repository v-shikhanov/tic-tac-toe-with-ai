package ticTacToe.ai;

import ticTacToe.game.*;
import java.io.*;
import java.util.*;
import static ticTacToe.game.Game.Figure.*;
import static ticTacToe.ui.UserInterface.game;
/**
 * Class for storing and load information about fields for better use. And making move
 * using computer experience.
 */
public class SelfExperiencedAI extends Thread {
    /**
     * Field size to work with
     */
    private int fieldSize;

    /**
     * Is a ready status that all fields are loaded from file (because on big field sizes it could takes a really big
     * time to load all information)
     */
    private boolean loadedFromFile;

    /**
     * It is a pearl of this class - collection of fields with its rates. Key is a field converted to long value like
     * a threefold.
     */
    private Map<Long, Integer> fieldsMap;

    /**
     * Constructor sets field values to a default
     * @param filedSize is size of field with class should work with
     */
    public SelfExperiencedAI(int filedSize) {
        this.loadedFromFile = false;
        this.fieldSize = filedSize;
    }

    /**
     *  Run method load fields from file. For it game created a new thread, because it could takes a long time
     */
    @Override
    public void run() {
        File savedFields;
        loadedFromFile = false;
        switch (fieldSize) {
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
            fieldsMap = new HashMap<>();
            return;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(savedFields);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            fieldsMap = (Map<Long, Integer>) objectInputStream.readObject();
            System.out.println("\nFields  are load from file");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        loadedFromFile = true;
    }

    /**
     * Method checks fields map and returns coordinate of better move
     * @param field game field
     * @return coordinate of move
     */
    public Cell makeMove(Game.Figure[][] field) {
        List<Cell> emptyCells = new GameResult().emptyCells(field);
        int currentRate;
        int selectedMoveIndex = 0;
        Cell cell;
        Game.Figure activeFigure = game.getActiveFigure();

        if (activeFigure.equals(CROSS)) {
            currentRate = Integer.MIN_VALUE;
        } else {
            currentRate = Integer.MAX_VALUE;
        }

        for (int i = 0; i < emptyCells.size(); i++) {
            long code;
            cell = emptyCells.get(i);
            field[cell.string][cell.row] = activeFigure;
            code = new FieldCoder().getCode(field);

            if (fieldsMap.containsKey(code)) {
                cell.rate = fieldsMap.get(code);
            }

            if (activeFigure.equals(CROSS)) {
                if (cell.rate > currentRate) {
                    selectedMoveIndex = i;
                    currentRate = cell.rate;
                }
            } else {
                if (cell.rate < currentRate) {
                    selectedMoveIndex = i;
                    currentRate = cell.rate;
                }
            }

            field[cell.string][cell.row] = EMPTY;
        }

        cell = emptyCells.get(selectedMoveIndex);
        field[cell.string][cell.row] = game.getActiveFigure();
        return cell;
    }

    /**
     * Getter for status of fields map
     * @return true if fields are loaded from file
     */
    public boolean isLoadedFromFile() {
        return loadedFromFile;
    }

    public Map<Long, Integer> getFieldsMap() {
        return fieldsMap;
    }

    public void setFieldsMap(Map<Long, Integer> fieldsMap) {
        this.fieldsMap = new HashMap<>(fieldsMap);
    }
}
