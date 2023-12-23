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
 *  2. paths in descending order w.r.t depends on relation
 *  3. remainder in order from small to large identifier
 */
public class LassoStrategy extends GenericStrategy {

    private final PriorityQueue<OddPath> queue;

    private final StateSpace game;
    public LassoStrategy(StateSpace game) {
        this.game = game;
        queue = new PriorityQueue<>(new OddPathComparator());
        init();
    }


    /**
     * Initializes the order array by finding lassos.
     * The order will be
     * 1. loops
     * 2. paths
     * 3. remainder
     */
    private void init() {
        BitSet seen = new BitSet(game.NROF_STATES);
        BitSet partOfLasso = new BitSet(game.NROF_STATES);
        HashMap<Integer, OddPath> containedIn = new HashMap<>();

        int i = -1;
        while (seen.cardinality() < game.NROF_STATES) {
            i = seen.nextClearBit(i+1);
            queue.addAll(findOddPaths(new ArrayList<>(), i, seen, partOfLasso, containedIn));
        }

        int r = game.NROF_STATES - partOfLasso.cardinality();
        if (r > 0) {
            int[] remainder = new int[r];
            int s = -1;
            for (int j = 0; j < r; j++) {
                s = partOfLasso.nextClearBit(s+1);
                remainder[j] = s;
            }
            queue.add(new OddPath(remainder, false, true, null));
        }
    }

    /**
     * Finds a lasso starting in state v with prefix, if one exists
     * @param prefix prefix
     * @param v the starting state
     * @param seen Which states we should ignore
     * @param partOfLasso Which states are already part of lasso
     * @return An ArrayList of found lassos
     */
    private ArrayList<OddPath> findOddPaths(ArrayList<Integer> prefix, int v, BitSet seen, BitSet partOfLasso,
                                            HashMap<Integer, OddPath> containedIn) {


        if (game.isOwnedByEven(v)) {
            seen.set(v);
            return new ArrayList<>();
        }

        if (prefix.contains(v)) { // Base case, we have found a loop
            int i = prefix.indexOf(v);
            int s = minPriority(prefix, i, prefix.size());

            if (s % 2 == 1) { // least priority is odd

                // Construct OddPaths
                ArrayList<OddPath> out = new ArrayList<>();

                // loop
                int[] loop = listToRevArray(prefix, i, prefix.size(), partOfLasso);
                OddPath lPath = new OddPath(loop, true, false, null);
                out.add(lPath);
                for(int k : lPath) containedIn.put(k, lPath);

                // prefix
                int[] beforeLoop = listToRevArray(prefix, 0, i, partOfLasso);
                if (beforeLoop.length > 0) {
                    OddPath pPath = new OddPath(beforeLoop, false, false, lPath);
                    out.add(pPath);
                    for(int k : pPath) containedIn.put(k, pPath);
                }

                return out;
            } else {
                return new ArrayList<>();
            }

        } else if (seen.get(v)) {
            ArrayList<OddPath> out = new ArrayList<>();

            if (partOfLasso.get(v)) {
                int[] beforeLoop = listToRevArray(prefix, 0, prefix.size(), partOfLasso);

                if (beforeLoop.length > 0) {
                    OddPath pPath = new OddPath(beforeLoop, false, false, containedIn.get(v));
                    out.add( pPath );
                    for (int k : pPath) containedIn.put(k, pPath);
                }
            }

            return out;
        } else {
            seen.set(v);

            int[] outEdges = game.getEdges(v);
            ArrayList<OddPath> out = new ArrayList<>();
            prefix.add(v);

            // Go over all edges of (v, w)
            for (int w : outEdges) {
                if (game.isOwnedByOdd(w)) {
                    out.addAll(findOddPaths(prefix, w, seen, partOfLasso, containedIn));
                    if (!out.isEmpty()) break;
                }
            }
            prefix.remove(prefix.size()-1);
            return out;
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
     * Converts a ArrayList to array where the order is reversed
     * and for every element e in the new array sets partOfLasso[e] to true
     * @param in the ArrayList
     * @param lb lower bound (inclusive)
     * @param up upper bound (exclusive)
     * @param partOfLasso partOfLasso bitset
     * @return the array
     */
    private int[] listToRevArray(ArrayList<Integer> in, int lb, int up, BitSet partOfLasso) {
        int[] out = new int[up-lb];
        for (int j = lb; j < up; j++) {
            out[up-j-1] = in.get(j);
            partOfLasso.set(in.get(j));
        }
        return out;
    }


    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    @Override
    public int[] next() {
        return Objects.requireNonNull(queue.poll()).path();
    }
}
