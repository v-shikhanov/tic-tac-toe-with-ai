package ticTacToe.ui;

import ticTacToe.game.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;

/**
 * Class that implements UI
 */
public class UserInterface extends JFrame {
    /**
     * Instance of game class which would be controlled via this UI
     */
    public static Game game;

    /**
     * Instance of class that updates names of players on text field
     */
    public static DisplayPlayer displayPlayer;

    /**
     * Buttons matrix that bound to field with matrix values
     * @see Game
     */
    private static GameButton[][] button;

    /**
     * Text fields for gamer names and turn indication
     */
    private static GameTextField player1Name;

    private static GameTextField player2Name;

    private static GameTextField whoMoves;

    private static final int WINDOW_WIDTH = 480;

    private static final int WINDOW_HEIGHT = 610;

    /**
     * Combo box for field size selection
     */
    private JComboBox fieldSizeSetup;
    private int fieldSize;

    /**
     * Constructor of class that creating UI and starts app
     * @param fieldSize size(length of side) of field 3 for 3x3, 4 for 4x4 etc
     */
    public UserInterface(int fieldSize) {
        Dimension dimension = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);
        this.fieldSize = fieldSize;
        game =  new Game(fieldSize);
        button = new GameButton[fieldSize][fieldSize];
        player1Name = new GameTextField("Player 1", true, true,null);
        player2Name = new GameTextField("Player 2", true, true,null);
        whoMoves = new GameTextField(null, true, true,null);
        createGameFieldUI();
        game.updateField(true, false);
        setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setResizable(false);
        setMinimumSize(dimension);
        setTitle("Awesome tic-tac");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        displayPlayer = new DisplayPlayer();
        displayPlayer.display(true);
    }

    /**
     * Methods bounds buttons to matrix, creates bars of menu,labels, players name and buttons
     */
    private void createGameFieldUI() {
        JPanel topBar = createTopBar();
        JPanel headLabels = new HeadLabels().createHeadLabels();
        JPanel head = new Head().createHead();
        JPanel[] buttonsLines = new JPanel[fieldSize];
        int btnSize = GameButton.foundSize(fieldSize);

        for ( int string = 0; string < fieldSize; string++) {
            for ( int row = 0; row < fieldSize; row++) {
                button[string][row] = new GameButton(string, row, btnSize);
            }
            buttonsLines[string] = formLine(button[string]);
        }

        createLayout(buttonsLines, topBar, headLabels, head);
    }

    /**
     * Method creates a top bar which contains a menu and a combo box for fields size select
     *
     * @return top bar
     */
    private JPanel createTopBar() {
        JPanel topBar = new JPanel();
        JMenuBar menu = new Menu().menuCreator();
        String[] items = {"Classic 3x3","Big 4x4","Extra big 5x5","Crazy 6x6"};

        fieldSizeSetup = new JComboBox<>(items);
        fieldSizeSetup.setSelectedIndex(fieldSize - 3);
        fieldSizeSetup.addActionListener(a -> changeFieldSize());

        GroupLayout groupLayout = new GroupLayout(topBar);
        topBar.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(menu)
                .addComponent(fieldSizeSetup)

        );

        groupLayout.setVerticalGroup(groupLayout.createParallelGroup()
                .addComponent(menu)
                .addComponent(fieldSizeSetup)
        );

        return topBar;
    }

    /**
     * Methods form the string of buttons matrix
     * @return line of buttons
     */
    private JPanel formLine(GameButton[] buttons) {
        JPanel buttonsLine = new JPanel();
        GroupLayout groupLayout = new GroupLayout(buttonsLine);

        buttonsLine.setLayout(groupLayout);
        GroupLayout.ParallelGroup parallelGroup = groupLayout.createParallelGroup();
        GroupLayout.SequentialGroup sequentialGroup = groupLayout.createSequentialGroup();
        groupLayout.setAutoCreateGaps(true);

        for (int i = 0; i < fieldSize; i++) {
            GameButton button = buttons[i];
            sequentialGroup.addComponent(button.getButton());
            parallelGroup.addComponent(button.getButton());
        }
        groupLayout.setVerticalGroup(parallelGroup);
        groupLayout.setHorizontalGroup(sequentialGroup);
        return buttonsLine;
    }

    /**
     * Method creates layout compiling table of stings
     */
    private void createLayout(JComponent[] buttonsLines, JComponent... components) {
        Container pane = getContentPane();
        GroupLayout groupLayout = new GroupLayout(pane);
        pane.setLayout(groupLayout);
        GroupLayout.ParallelGroup parallelGroup = groupLayout.createParallelGroup();
        GroupLayout.SequentialGroup sequentialGroup = groupLayout.createSequentialGroup();
        groupLayout.setAutoCreateContainerGaps(true);
        groupLayout.setAutoCreateGaps(true);

        for (JComponent component : components) {
            parallelGroup.addComponent(component);
            sequentialGroup.addComponent(component);
        }

        for (JComponent line : buttonsLines) {
            parallelGroup.addComponent(line);
            sequentialGroup.addComponent(line);
        }

        groupLayout.setHorizontalGroup(parallelGroup);
        groupLayout.setVerticalGroup(sequentialGroup);
    }

    /**
     * This method runs new app with given game field size
     */
     private void changeFieldSize() {
         int newSize = fieldSizeSetup.getSelectedIndex() + 3;
         dispose();
         new UserInterface(newSize);
     }

    /**
     * This method updates a text inside a button after computer makes a move, and disable it for user
     *
     * @param string number in field matrix
     * @param row number in field matrix
     */
    public static void buttonsUpdate(int string, int row) {
        button[string][row].printFieldElement();
        button[string][row].setButtonEnabled(false);
        game.nextMove(string, row, game.getActiveFigure());
    }

    /**
     * Message showing message with given text
     * @param message text to show
     */
    public static void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    /**
     * Getters of class
     */
    public static GameButton getButton(int string, int row) {
        return button[string][row];
    }

    public static GameTextField getPlayer1Name() {
        return player1Name;
    }

    public static GameTextField getPlayer2Name() {
        return player2Name;
    }

    public static GameTextField getWhoMoves() {
        return whoMoves;
    }

    public static int getWindowWidth() {
        return WINDOW_WIDTH;
    }

}
