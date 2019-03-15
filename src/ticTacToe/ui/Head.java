package ticTacToe.ui;

import javax.swing.*;

import static ticTacToe.ui.UserInterface.*;

/**
 *  Class for head User interface element creation
 * @see UserInterface
 */
class Head {
    JPanel createHead() {
        JPanel head = new JPanel();
        GroupLayout groupLayout = new GroupLayout(head);
        head.setLayout(groupLayout);
        groupLayout.setAutoCreateGaps(true);

        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(getPlayer1Name().getTextField())
                .addComponent(getWhoMoves().getTextField())
                .addComponent(getPlayer2Name().getTextField())

        );

        groupLayout.setVerticalGroup(groupLayout.createParallelGroup()
                .addComponent(getPlayer1Name().getTextField())
                .addComponent(getWhoMoves().getTextField())
                .addComponent(getPlayer2Name().getTextField())
        );
        return head;
    }
}
