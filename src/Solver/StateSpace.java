package Solver;

import java.util.BitSet;
import java.util.HashMap;

public class StateSpace {

    private final int[][] adj;

    private final int[][] revAdj;

    private final int[] priority;

    private final int[] order;

    private final String[] names;

    private final BitSet ownedByEven;

    public final int NROF_STATES;
    public final int MAX_PRIORITY;

    public final String NAME;

    public StateSpace(int nrStates, int maxPriority, int[][] adj, int[][] revAdj, int[] priority, BitSet ownedByEven, int[] order, String name, String[] names) {
        this.NROF_STATES = nrStates;
        this.MAX_PRIORITY = maxPriority;
        this.adj = adj;
        this.revAdj = revAdj;
        this.priority = priority;
        this.ownedByEven = ownedByEven;
        this.order = order;
        this.NAME = name;
        this.names = names;
    }


    public int getPriority(int i) {
        return priority[i];
    }

    public boolean isOwnedByEven(int i) {
        return ownedByEven.get(i);
    }

    public boolean isOwnedByOdd(int i) {
        return !isOwnedByEven(i);
    }

    public int getOwner(int i) {
        if (isOwnedByEven(i)) return 0;
        return 1;
    }

    public int[] getOutgoingEdges(int i) {
        return adj[i];
    }

    public int[] getIncomingEdges(int i) {
        return revAdj[i];
    }

    public int[] countPriorities() {
        HashMap<Integer, Integer> frequencyMap = new HashMap<>();
        for (int i : priority) {
            frequencyMap.put(i, frequencyMap.getOrDefault(i, 0) + 1);
        }
        int[] out = new int[MAX_PRIORITY+1];
        for (int k : frequencyMap.keySet()) {
            out[k] = frequencyMap.get(k);
        }
        return out;
    }

    public int[] getOrder() {
        return order;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("parity ").append(NROF_STATES-1).append(";\n");

        for (int i = 0; i < NROF_STATES; i++) {
            sb.append(i).append(' ').append(getPriority(i)).append(' ').append(getOwner(i)).append(' ');
            for (int j : adj[i]) {
                sb.append(j).append(',');
            }
            sb.deleteCharAt(sb.length()-1);

            sb.append(";\n");
        }

        return sb.toString();
    }

}
