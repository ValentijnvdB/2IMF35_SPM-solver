package Strategies;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class GenericStrategy implements Iterator<Integer> {

    protected int[][] order;

    protected int NROF_PHASES;

    protected int phase;
    protected int index;

    public GenericStrategy() {
        reset();
        phase = 0;
    }

    public GenericStrategy(int nrPhases) {
        this();
        NROF_PHASES = nrPhases;
        order = new int[NROF_PHASES][];
    }

    public void reset() {
        index = 0;
    }

    public boolean hasNextPhase() {
        return phase < NROF_PHASES;
    }

    public void nextPhase() {
        phase++;
    }

    public int getSizeOfPhase() {
        return order[phase].length;
    }

    @Override
    public boolean hasNext() {
        return index < order[phase].length;
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return order[phase][index++];
    }
}
