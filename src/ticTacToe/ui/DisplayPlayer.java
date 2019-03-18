package ticTacToe.ui;

import ticTacToe.game.Player;
import java.awt.*;
import static ticTacToe.game.Game.Figure.ZERO;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class for indication which player should make a move
 */
public class DisplayPlayer {
    /**
     * Base method which changed name of active player in text field "Now moves"
     * and coloring names of players depending of game turn
     * @param isGameFinished game status to display active layer or message that game not started
     */
    public void display(boolean isGameFinished) {
        Color moves = new Color(69, 222, 111);
        Color waiting = new Color(222, 85, 63);
        Color notStarted = new Color(179, 191, 216);
        String activeFigure;
        Player active = game.getActivePlayer();

        if (game.getActiveFigure() == ZERO) {
            activeFigure = "O";
        } else {
            activeFigure = "X";
        }

        if (isGameFinished) {
            UserInterface.getWhoMoves().setText("Not Started");
            UserInterface.getPlayer1Name().getTextField().setBackground(notStarted);
            UserInterface.getPlayer2Name().getTextField().setBackground(notStarted);
        } else {
            if (active == game.getPlayer(1)) {
                UserInterface.getWhoMoves().setText(UserInterface.getPlayer1Name().getText() + " - " + activeFigure);
                UserInterface.getPlayer1Name().getTextField().setBackground(moves);
                UserInterface.getPlayer2Name().getTextField().setBackground(waiting);
            } else {
                UserInterface.getWhoMoves().setText(UserInterface.getPlayer2Name().getText() + " -  " + activeFigure);
                UserInterface.getPlayer1Name().getTextField().setBackground(waiting);
                UserInterface.getPlayer2Name().getTextField().setBackground(moves);
            }
        }
    }
}
