
/*
    progress measure: array
 */

import Strategies.GenericStrategy;
import Strategies.InputOrder;
import Strategies.RandomOrder;

import java.io.FileNotFoundException;
import java.util.BitSet;

public class SPMSolver {



    public static void main(String[] args) {
        String path = "C:\\Users\\tijnt\\OneDrive\\Documenten\\TUE\\YM2\\Q2 - 2IMF35 Algorithms for Model Checking\\A2\\dining_games\\";
        String file = "dining_2.invariantly_inevitably_eat.gm";

        try {
            StateSpace game = Parser.parse(path + file);

            GenericStrategy inOrder = new InputOrder(game.getOrder());
            GenericStrategy random = new RandomOrder(game.NROF_STATES);

            BitSet oddWins = Solver.solve(game, random);

            System.out.println("NrStates = " + game.NROF_STATES);
            System.out.println(oddWins);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
