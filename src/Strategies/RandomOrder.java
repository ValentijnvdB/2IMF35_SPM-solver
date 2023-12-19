package Strategies;

import java.util.Collections;
import java.util.stream.IntStream;

public class RandomOrder extends GenericStrategy {

    public RandomOrder(int nrStates) {
        super();
        this.order = IntStream.rangeClosed(0, nrStates-1).boxed().toList();
        Collections.shuffle(order);
    }

}
