package Solver;

import java.util.Arrays;
import java.util.BitSet;

public class GameProgressMeasure {

    private final int[][] ro;

    private final StateSpace game;

    private final int SIZE;

    private final int TOP;

    private final int[] MAX_PM;

    public GameProgressMeasure(StateSpace game) {
        this.game = game;
        SIZE = (1 + game.MAX_PRIORITY)/2;
        ro = new int[game.NROF_STATES][SIZE];
        TOP = Integer.MAX_VALUE;

        MAX_PM = new int[SIZE];
        int[] pr = game.countPriorities();
        for (int i = 1; i < pr.length; i += 2) {
            MAX_PM[toIP(i)] = pr[i];
        }
    }

    /**
     * Lifts progress measure of state i once
     * @param i state
     * @return true iff pm is stable
     */
    public boolean lift(int i) {
        SPMSolver.lifted++;

        if (isTop(i)) return true;

        int[] old = ro[i];
        ro[i] = computeLift(i);
        return Arrays.equals(old, ro[i]);
    }

    /**
     * Tests whether the pm of state i is stable
     * @param i state
     * @return true iff the pm of state i is stable
     */
    public boolean isStable(int i) {
        return isTop(i) || Arrays.equals(ro[i], computeLift(i));
    }

    /**
     * Compute on iteration of lift
     * @param i index of progress measure
     * @return the lifted progress measure
     */
    private int[] computeLift(int i) {

        return computeLift(i, game.isOwnedByEven(i));
    }

    /**
     * Compute on iteration of lift
     * @param i index of progress measure
     * @param minimize whether to take the minimize or max
     * @return the lifted progress measure
     */
    private int[] computeLift(int i, boolean minimize) {
        int v = minimize ? TOP : Integer.MIN_VALUE;
        int[] out = new int[SIZE];
        Arrays.fill(out, v);

        boolean maximize = !minimize;

        int[] edges = game.getOutgoingEdges(i);
        int[] m;
        int ep = game.getPriority(i);

        for (int w : edges) {

            m = prog(w, ep);

            if (    (minimize   &&  smaller(m, out, ep)) ||
                    (maximize   &&  larger (m, out, ep))  ) {
                out = m;
            }
        }

        return out;
    }

    /**
     * prog function from the definitions
     * @param w the target state
     * @param epv the external priority of state v
     * @return prog(ro, v, w)
     */
    private int[] prog(int w, int epv) {
        if (isTop(w)) return ro[w].clone();

        int[] out = ro[w].clone();

        int ip = toIP(epv);

        for (int i = ip + 1; i < out.length; i++) {
            out[i] = 0;
        }

        // if odd, we increase
        if (epv % 2 == 1) {
            out = increase(out, ip);
        }

        return out;
    }


    /**
     * Increase the progress measure by one
     * @param m the progress measure
     * @param ip the (internal) position to start
     */
    private int[] increase(int[] m, int ip) {

        if (m[ip] != TOP) {

            if (m[ip] >= MAX_PM[ip]) {
                if (ip == 0) {
                    Arrays.fill(m, TOP);
                } else {
                    m[ip] = 0;
                    m = increase(m, ip - 1);
                }
            } else {
                m[ip] += 1;
            }
        }

        return m;
    }


    /**
     * Get winning set of player odd of the current progress measures
     * @return the winning set of odd
     */
    public BitSet get() {
        BitSet out = new BitSet(ro.length);
        for (int i = 0; i < ro.length; i++) {
            out.set(i, isTop(i));
        }
        return out;
    }

    /**
     * Test if progress measure i is a top
     * @param i the progress measure
     * @return true iff progress measure i is a top
     */
    private boolean isTop(int i) {
        for (int j : ro[i]) {
            if (j != TOP) return false;
        }
        return true;
    }

    /**
     * Convert external priority to an internal priority
     * @param ep external priority
     * @return internal priority
     */
    private int toIP(int ep) {
        return (ep-1)/2;
    }

    /**
     * Convert internal priority to an external priority
     * @param ip internal priority
     * @return external priority
     */
    private int toEP(int ip) {
        return ip*2 + 1;
    }

    //region compare

    /**
     * Compare two progress measures upto and including external position ep
     * @param m1 first progress measure
     * @param m2 second progress measure
     * @param ep compare upto position
     * @return 0 if m1 =ep m2, less than 0 if m1 <=ep m2 and greater than 0 if m1 >=ep m2
     */
    private int compare(int[] m1, int[] m2, int ep) {
        if (ep == 0) return 0;

        int ip = toIP(ep);

        int c;
        for (int k = 0; k <= ip; k++) {
            c = Integer.compare(m1[k], m2[k]);
            if (c != 0) return c;
        }
        return 0;
    }

    private int compare(int i, int j, int ep) {
        return this.compare(ro[i], ro[j], ep);
    }

    private int compare(int i, int[] m2, int ep) {
        return this.compare(ro[i], m2, ep);
    }

    private boolean larger(int[] m1, int[] m2, int ep) {
        return compare(m1, m2, ep) >= 1;
    }

    private boolean equal(int[] m1, int[] m2, int ep) {
        return compare(m1, m2, ep) == 0;
    }

    private boolean smaller(int[] m1, int[] m2, int ep) {
        return compare(m1, m2, ep) <= -1;
    }

    private boolean larger(int i, int[] m2, int ep) {
        return compare(i, m2, ep) >= 1;
    }

    private boolean equal(int i, int[] m2, int ep) {
        return compare(i, m2, ep) == 0;
    }

    private boolean smaller(int i, int[] m2, int ep) {
        return compare(i, m2, ep) <= -1;
    }

    private boolean larger(int i, int j, int ep) {
        return compare(i, j, ep) >= 1;
    }

    private boolean equal(int i, int j, int ep) {
        return compare(i, j, ep) == 0;
    }

    private boolean smaller(int i, int j, int ep) {
        return compare(i, j, ep) <= -1;
    }

    //endregion


}
