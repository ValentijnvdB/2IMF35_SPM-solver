package Strategies;

import java.util.Iterator;

public class OddPathIterator implements Iterator<Integer> {

    private final OddPath path;

    private int index;

    public OddPathIterator(OddPath oddPath) {
        path = oddPath;
        index = 0;
    }
    @Override
    public boolean hasNext() {
        return index < path.length();
    }

    @Override
    public Integer next() {
        return path.get(index++);
    }
}
