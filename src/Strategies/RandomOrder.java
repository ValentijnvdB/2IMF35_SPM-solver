package Strategies;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomOrder extends GenericStrategy {

    public RandomOrder(int nrStates) {
        super(1);
        order[0] = IntStream.rangeClosed(0, nrStates-1).toArray();

        Random random = new Random();
        int j, old;
        for (int i = nrStates-1; i > 0; i--) {
            j = random.nextInt(i+1);
            // swap indices i and j
            old = order[0][i];
            order[0][i] = order[0][j];
            order[0][j] = old;
        }
    }

}
