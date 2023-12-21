package Strategies;

public class InputOrder extends GenericStrategy {

    public InputOrder(int[] inputOrder) {
        super();
        order = inputOrder.clone();
    }
}
