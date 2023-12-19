package Strategies;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class GenericStrategy implements Iterator<Integer> {

    protected List<Integer> order;
    protected int index;

    public GenericStrategy() {
        reset();
    }

    public void reset() {
        index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < order.size();
    }

    @Override
    public Integer next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return order.get(index++);
    }
}
