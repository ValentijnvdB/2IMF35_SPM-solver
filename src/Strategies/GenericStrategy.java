package Strategies;

import java.util.Iterator;

public abstract class GenericStrategy implements Iterable<Integer> {

    protected abstract int[] getPhase();

    public abstract void nextPhase();

    public abstract boolean hasNextPhase();

    @Override
    public Iterator<Integer> iterator() {
        return new PhaseIterator(this.getPhase());
    }
}
