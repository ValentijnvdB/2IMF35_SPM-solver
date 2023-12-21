package Strategies;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class GenericStrategy implements Iterator<Integer> {

    protected int[] order;
    protected int index;

    public GenericStrategy() {
        reset();
    }

    public void reset() {
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < order.length;
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return order[index++];
    }
}
