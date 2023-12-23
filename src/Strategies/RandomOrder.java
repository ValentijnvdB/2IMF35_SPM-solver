package Strategies;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomOrder extends SinglePhaseStrategy {

    public RandomOrder(int nrStates) {
        super();
        order = IntStream.rangeClosed(0, nrStates-1).toArray();

        Random random = new Random();
        int j, old;
        for (int i = nrStates-1; i > 0; i--) {
            j = random.nextInt(i+1);
            // swap indices i and j
            old = order[i];
            order[i] = order[j];
            order[j] = old;
        }
    }

}
