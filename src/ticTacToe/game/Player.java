package ticTacToe.game;

import ticTacToe.ai.ComputerRival;
import ticTacToe.ui.UserInterface;
import static ticTacToe.ui.UserInterface.game;

/**
 * Class implements players specifications
 */
public class Player {
    ComputerRival computerRival = new ComputerRival();

    /**
     * for which figure player is playing 0 or X
     */
    private Game.Figure figure;

    /**
     * Is player human(false), or computer (true)
     */
    private boolean computer;

    /**
     * Level if player is computer
     */
    private Game.Levels level;

    public Player(Game.Figure figure) {
        this.figure = figure;
        this.computer = false;
        this.level = Game.Levels.MEDIUM;
    }

    public void makeMove() {
        Cell cell = new Cell(0,0);
        if (computer && game.isGameStarted()) {
            switch (level) {
                case EASY: cell = computerRival.easy(game.getGameField());break;
                case MEDIUM: cell = computerRival.medium(); break;
                case HARD: cell = computerRival.hard(game.getGameField(), game.getActiveFigure(), figure); break;
                case LEARNING: cell = computerRival.learning(); break;
            }
            UserInterface.buttonsUpdate(cell.string, cell.row);
        }
    }

    /**
     * Getters and setters for player settings
     */
    public void setFigure(Game.Figure figure) {
        this.figure = figure;
    }

    public void setIsComputer(boolean computer) {
        this.computer = computer;
    }

    public Game.Levels getLevel() {
        return level;
    }

    public void setLevel(Game.Levels level) {
        this.level = level;
    }

    public Game.Figure getFigure() {
        return figure;
    }
}
