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

        int s;
        BitSet notStable;

        do {
            notStable = new BitSet(game.NROF_STATES);

            // iterate over all states according to the strategy
            while (strategy.hasNext()) {
                s = strategy.next();

                // repeat lift until progress measure s is stable
                while (!ro.lift(s)) {

                    // ASSERT progress measure s was not stable after one lift
                    notStable.set(s, true);

                }
            }

        // keep iterating over all states until all states are stable
        } while (notStable.isEmpty());

        return ro.get();
    }


}
