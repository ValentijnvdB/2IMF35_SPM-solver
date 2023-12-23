package Strategies;

public class SinglePhaseStrategy extends GenericStrategy {

    int[] order;
    boolean done;

    SinglePhaseStrategy() {
        done = false;
    }

    @Override
    protected int[] getPhase() {
        return order;
    }

    @Override
    public void nextPhase() {
        done = true;
    }

    @Override
    public boolean hasNextPhase() {
        return !done;
    }
}
