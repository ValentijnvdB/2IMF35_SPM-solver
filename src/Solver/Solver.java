package Solver;

import Strategies.GenericStrategy;

import java.util.BitSet;

public class Solver {

    /**
     * Returns the winning set of odd for a given parity game
     * @param game the parity game
     * @param strategy the order in which to lift the progress measure
     * @return the winning set of player odd
     */
    public static BitSet solve(StateSpace game, GenericStrategy strategy) {

        GameProgressMeasure ro = new GameProgressMeasure(game);

        BitSet notStable;


        while (strategy.hasNextPhase()) {

            strategy.nextPhase();

            do {

                notStable = new BitSet(game.NROF_STATES);

                // iterate over all states according to the strategy
                for (int s : strategy) {

                    // repeat lift until progress measure s is stable
                    while (!ro.lift(s)) {

                        // ASSERT progress measure s was not stable after one lift
                        notStable.set(s, true);

                    }
                }

                // keep iterating over all states in the current phase until all states are stable
                // we leave the loop iff notStable only contains 0's, since 0 is the default value,
                // this is only if no bit is set to 1.
            } while (!notStable.isEmpty());


        }

        return ro.get();
    }


}
