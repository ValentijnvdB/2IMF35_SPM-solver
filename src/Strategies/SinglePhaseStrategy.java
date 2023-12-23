package Strategies;

public class SinglePhaseStrategy extends GenericStrategy {

    int[] order;
    boolean done;

    SinglePhaseStrategy() {
        done = false;
    }

    @Override
    public boolean hasNext() {
        return !done;
    }

    @Override
    public int[] next() {
        done = true;
        return order;
    }
}
