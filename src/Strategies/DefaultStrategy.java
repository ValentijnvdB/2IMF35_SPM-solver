package Strategies;

import java.util.stream.IntStream;

public class DefaultStrategy extends GenericStrategy {

    public DefaultStrategy(int nrStates) {
        super(1);
        order[0] = IntStream.rangeClosed(0, nrStates-1).toArray();
    }
}
