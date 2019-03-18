package ticTacToe;

import ticTacToe.ui.UserInterface;

public class Main {
    private static final int DEFAULT_FIELD_SIZE = 3;
    public static void main(String[] args) {
        new UserInterface(DEFAULT_FIELD_SIZE);
    }
}
