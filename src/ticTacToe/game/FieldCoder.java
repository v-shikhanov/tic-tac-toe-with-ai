package ticTacToe.game;

/**
 *  This class using to convert two-level array to a long number (for moves knowledge base size optimisation)
 *
 * @see ticTacToe.ai.LearningAlgorithm
 */
public class FieldCoder {
    /**
     * Method converts two-level array to a long number
     * @param field current field that must be converted to code
     * @return field in long number equivalent
     */
    public long getCode(Game.Figure[][] field) {
        long code = 0;
        for (int s = 0; s < field.length; s++) {
            for (int r = 0; r < field.length; r++) {
                switch (field[s][r]) {
                    case ZERO: {
                        code += 1 * (Math.pow(3, (s*field.length + r)));
                        break;
                    }
                    case CROSS: {
                        code += 2 * (Math.pow(3, (s*field.length + r)));
                        break;
                    }
                    case EMPTY: {
                        code += 3 * (Math.pow(3, (s*field.length + r)));
                        break;
                    }
                }
            }
        }
        return code;
    }
}
