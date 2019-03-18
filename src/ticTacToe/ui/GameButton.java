package ticTacToe.ui;

import ticTacToe.game.Game;

import javax.swing.*;
import java.awt.*;
import static ticTacToe.game.Game.*;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class for Game Button ui element, contains button, and it coordinate according to field matrix
 */
public class GameButton extends JFrame{
    private JButton button = new JButton();
    private int string;
    private int row;

    GameButton(int string, int row, int size){
        this.string = string;
        this.row = row;
        int fontSize;
        switch (game.getFieldSize()) {
            case 3 : fontSize = 140; break;
            case 4 : fontSize = 90; break;
            case 5 : fontSize = 60; break;
            case 6 : fontSize = 45; break;
            default : fontSize = 45; break;
        }
        Font font = new Font(null,Font.BOLD,fontSize);
        Dimension dimension = new Dimension(size,size);
        button.setMinimumSize(new Dimension(dimension));
        button.setMaximumSize(new Dimension(dimension));
        button.setFont(font);
        button.addActionListener( actionEvent -> getUserSelection());
    }

    /**
     * Method that updates button text according to field matrix element
     * that it was bound by string and row parameter     *
     * @see Game
     */
    public void printFieldElement() {
        Figure val = game.getFieldValue(string,row);
        switch (val) {
            case ZERO : {
                button.setText("O");
                break;
            }
            case CROSS : {
                button.setText("X");
                break;
            }
            case EMPTY: {
                button.setText("");
                break;
            }
            default : break;
        }
    }

    /**
     * Method that process user selection to button
     */
    private void getUserSelection () {
        if (game.isGameStarted()) {
            button.setEnabled(false);
            game.nextMove(string,row, game.getActiveFigure());
        }
    }

    /**
     * Button state setter
     * @param state enable button when true
     */
    public void setButtonEnabled(boolean state) {
        button.setEnabled(state);
    }

    /**
     * Button getter
     */
    public JButton getButton() {
        return button;
    }

    /**
     * Method founds size of button depending on field size (for field good look)
     * @param qnt quantity of buttons in one string
     * @return size of button for current field configuration
     */
    public static int foundSize(int qnt) {
       int autoGapSize = 5;
       int autoContainerGapSize = 40;
       return ((UserInterface.getWindowWidth() - autoGapSize * (qnt - 1) - autoContainerGapSize) / qnt);
    }
}
