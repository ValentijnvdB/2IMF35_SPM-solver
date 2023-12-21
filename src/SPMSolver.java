
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

            GenericStrategy strategy = new InputOrder(game.getOrder());

            BitSet oddWins = Solver.solve(game, strategy);

            System.out.println(!oddWins.get(0));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
