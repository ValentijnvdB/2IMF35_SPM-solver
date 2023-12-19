package Strategies;

import java.util.stream.IntStream;

public class DefaultStrategy extends GenericStrategy {

    public DefaultStrategy(int nrStates) {
        super();
        order = IntStream.rangeClosed(0, nrStates-1).boxed().toList();
    }
}
