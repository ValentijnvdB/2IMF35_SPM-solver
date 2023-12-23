package Strategies;

import java.util.Iterator;

public class PhaseIterator implements Iterator<Integer> {

    private final int[] phase;
    private int index;

    protected PhaseIterator(int[] phase) {
        this.phase = phase;
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < phase.length;
    }

    @Override
    public Integer next() {
        return phase[index++];
    }
}
