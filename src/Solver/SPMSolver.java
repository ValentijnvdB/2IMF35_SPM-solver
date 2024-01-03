package Solver;

import Strategies.*;

import java.util.BitSet;

public class SPMSolver {



    public static void main(String[] args) {
        String path = "C:\\Users\\tijnt\\OneDrive\\Documenten\\TUE\\YM2\\Q2 - 2IMF35 Algorithms for Model Checking\\A2\\Simple Tests\\Lasso\\";
        String file = "test1.txt";

        try {
            StateSpace game = Parser.parse(path + file);
            System.out.println(game);

            GenericStrategy inOrder = new InputOrder(game.getOrder());
            GenericStrategy random = new RandomOrder(game.NROF_STATES);
            GenericStrategy lasso = new LassoStrategy(game);
            GenericStrategy reverseEdge = new ReverseEdgeStrategy(game);

            BitSet oddWins = Solver.solve(game, lasso);

            System.out.println("NrStates = " + game.NROF_STATES);
            System.out.println(oddWins);
        } catch (Exception e) {
            SimpleLogger.error(e);
        }
    }


}
