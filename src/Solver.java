import Strategies.GenericStrategy;

import java.util.BitSet;

public class Solver {

    public static BitSet solve(StateSpace game, GenericStrategy strategy) {

        GameProgressMeasure ro = new GameProgressMeasure(game);

        do {
            while (strategy.hasNext()) {
                int s = strategy.next();
                while (!ro.lift(s));
            }
        } while (isStable(game.NROF_STATES, ro));

        return ro.get();
    }

    private static boolean isStable(int nrStates, GameProgressMeasure ro) {
        for (int i = 0; i < nrStates; i++) {
            if (!ro.isStable(i)) return false;
        }
        return true;
    }


}
