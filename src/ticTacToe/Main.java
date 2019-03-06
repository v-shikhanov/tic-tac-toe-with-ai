package ticTacToe;

import ticTacToe.ui.UserInterface;

public class Main {
    private static final int defaultFieldSize = 3;
    public static void main(String[] args) {
        new UserInterface(defaultFieldSize);
    }
}
