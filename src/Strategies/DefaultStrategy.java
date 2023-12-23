package Strategies;

import java.util.stream.IntStream;

public class DefaultStrategy extends SinglePhaseStrategy {

    public DefaultStrategy(int nrStates) {
        super();
        order = IntStream.rangeClosed(0, nrStates-1).toArray();
    }
}
