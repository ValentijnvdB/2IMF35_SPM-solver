package Strategies;

import Solver.StateSpace;

import java.util.*;

public class ReverseEdgeStrategy extends SinglePhaseStrategy {


    private final StateSpace game;
    public ReverseEdgeStrategy(StateSpace game) {
        super();
        this.game = game;
        init();
    }

    private void init() {
        HashMap<Integer, ArrayList<Integer>> map = new HashMap<>();
        PriorityQueue<Integer> lengths = new PriorityQueue<>();

        for (int i = 0; i < game.NROF_STATES; i++) {
            int length = game.getOutgoingEdges(i).length;

            ArrayList<Integer> list = map.getOrDefault(length, new ArrayList<>());
            list .add(i);
            map.put(length, list);

            if (!lengths.contains(length)) lengths.add(length);
        }


        // Compute order
        order = new int[game.NROF_STATES];
        BitSet processed = new BitSet(game.NROF_STATES);
        int i = 0;

        while (processed.cardinality() < game.NROF_STATES) {
            if (lengths.isEmpty()) throw new IllegalStateException("Some states are not added to 'order', but lengths queue is empty. This should never happen!");

            int next = lengths.poll();
            i = walkReversePathsBFS(map.get(next), processed, i);
        }

    }

    /**
     * Walks reverse edges and adds each vertex it sees to the end of this.order
     * @param startStates the first states in the queue
     * @param processed BitSet where index i is true iff state i has been processed already
     * @param i first empty spot in this.order
     * @return the new first empty spot in this.order
     */
    private int walkReversePathsBFS(ArrayList<Integer> startStates, BitSet processed, int i) {
        if (startStates == null) return i;


        ArrayDeque<Integer> queue = new ArrayDeque<>(startStates);

        int curState;

        while (!queue.isEmpty()) {

            curState = queue.poll();
            if (processed.get(curState)) {
                continue;
            }

            processed.set(curState);
            order[i] = curState;
            int[] incoming = game.getIncomingEdges(curState);

            for (int in : incoming) {
                queue.add(in);
            }
            i++;
        }

        return i;
    }

}
