package Strategies;

import java.util.Iterator;

public abstract class GenericStrategy implements Iterator<int[]>, Iterable<int[]> {

    @Override
    public Iterator<int[]> iterator() {
        return this;
    }
}
