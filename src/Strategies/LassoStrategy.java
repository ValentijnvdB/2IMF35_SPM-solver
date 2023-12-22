package Strategies;

import Solver.StateSpace;

import java.util.*;

/**
 * Lasso strategy
 * <p>
 * Finds lassos of states that
 *  - are all owned by odd
 *  - where the least priority in the loop is odd
 * <p>
 *  Then lifts in the order
 *  1. smallest to largest loop
 *  2. paths that are part of a lasso
 *  3. paths of odd owned states that end in a lasso
 *  4. remainder in order from small to large
 */
public class LassoStrategy extends GenericStrategy {

    private static final LassoComparator comparator =  new LassoComparator();

    private final StateSpace game;
    public LassoStrategy(StateSpace game) {
        NROF_PHASES = 0;
        this.game = game;
        init();
    }

    /**
     * Initializes the order array by finding lassos.
     * The order will be
     * 1. loops
     * 2. paths to loops
     * 3. paths to lassos
     * 4. remainder
     */
    private void init() {
        BitSet seen = new BitSet(game.NROF_STATES);
        BitSet partOfLasso = new BitSet(game.NROF_STATES);
        HashMap<Integer, Lasso> containedIn = new HashMap<>();

        PriorityQueue<Lasso> queue = new PriorityQueue<>(comparator);
        int i = -1;
        while (seen.cardinality() < game.NROF_STATES) {
            i = seen.nextClearBit(i+1);
            queue.addAll(findLasso(new ArrayList<>(), i, seen, partOfLasso, containedIn));
        }

        if (partOfLasso.cardinality() < game.NROF_STATES) NROF_PHASES++;
        // Setup phases
        order = new int[NROF_PHASES][];

        // loops
        ArrayList<int[]> paths = new ArrayList<>();
        Lasso lasso = queue.poll();
        i = 0;
        while(lasso != null) {

            if (lasso.loopLength() != 0) {
                order[i] = lasso.loop();
                i++;
            }

            if (lasso.pathLength() != 0) {
                paths.add(lasso.path());
            }

            lasso = queue.poll();
        }

        // paths
        for (int[] p : paths) {
            order[i] = p;
            i++;
        }

        // remainder
        int r = game.NROF_STATES - partOfLasso.cardinality();
        if (r > 0) {
            int[] remainder = new int[r];
            int s = -1;
            for (int j = 0; j < r; j++) {
                s = partOfLasso.nextClearBit(s + 1);
                remainder[j] = s;
            }
            order[i] = remainder;
        }
    }

    /**
     * Finds a lasso starting in state v with prefix path, if one exists
     * @param path prefix
     * @param v the starting state
     * @param seen Which states we should ignore
     * @param partOfLasso Which states are already part of lasso
     * @return An ArrayList of found lassos
     */
    private ArrayList<Lasso> findLasso(ArrayList<Integer> path, int v, BitSet seen, BitSet partOfLasso, HashMap<Integer, Lasso> containedIn) {


        if (game.isOwnedByEven(v)) {
            seen.set(v);
            return new ArrayList<>();
        }

        if (path.contains(v)) { // Base case, we have found a loop
            int i = path.indexOf(v);
            int s = minPriority(path, i, path.size());

            if (s % 2 == 1) { // least priority is odd

                // Construct Lasso
                int[] beforeLoop = listToArray(path, 0, i, partOfLasso);
                int[] loop = listToArray(path, i, path.size(), partOfLasso);

                if (beforeLoop.length > 0) NROF_PHASES++;
                if (loop.length > 0) NROF_PHASES++;

                ArrayList<Lasso> out = new ArrayList<>();
                Lasso lasso = new Lasso(beforeLoop, loop, null);
                out.add(lasso);
                for(int k : lasso.loop()) containedIn.put(k, lasso);
                for(int k : lasso.path()) containedIn.put(k, lasso);
                return out;
            } else {
                return new ArrayList<>();
            }

        } else if (seen.get(v)) {
            ArrayList<Lasso> out = new ArrayList<>();

            if (partOfLasso.get(v)) {
                int[] beforeLoop = listToArray(path, 0, path.size(), partOfLasso);

                if (beforeLoop.length > 0) {
                    NROF_PHASES++;
                    Lasso lasso = new Lasso(beforeLoop, new int[]{}, containedIn.get(v));
                    out.add( lasso );
                    for (int k : lasso.path()) containedIn.put(k, lasso);
                }
            }

            return out;
        } else {
            seen.set(v);

            int[] outEdges = game.getEdges(v);
            ArrayList<Lasso> lassos = new ArrayList<>();
            path.add(v);

            // Go over all edges of (v, w)
            for (int w : outEdges) {
                if (game.isOwnedByOdd(w)) {
                    lassos.addAll(findLasso(path, w, seen, partOfLasso, containedIn));
                    break;
                }
            }
            path.remove(path.size()-1);
            return lassos;

        }
    }

    /**
     * Returns the minimum int in the list in the range [lb, up)
     * @param path the list
     * @param lb lower bound (inclusive)
     * @param ub upper bound (exclusive)
     * @return the minimum element
     */
    private int minPriority(List<Integer> path, int lb, int ub) {
        int min = Integer.MAX_VALUE;

        for (int i = lb; i < ub; i++) {
            min = Integer.min(min, game.getPriority( path.get(i) ));
        }
        return min;
    }

    /**
     * Converts a ArrayList to array and for every element e in the new array sets partOfLasso[e] to true
     * @param in the ArrayList
     * @param lb lower bound (inclusive)
     * @param up upper bound (exclusive)
     * @param partOfLasso partOfLasso bitset
     * @return the array
     */
    private int[] listToArray(ArrayList<Integer> in, int lb, int up, BitSet partOfLasso) {
        int[] out = new int[up-lb];
        for (int j = lb; j < up; j++) {
            out[j-lb] = in.get(j);
            partOfLasso.set(in.get(j));
        }
        return out;
    }

}
