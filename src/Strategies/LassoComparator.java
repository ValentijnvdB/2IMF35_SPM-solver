package Strategies;

import java.util.Comparator;

public class LassoComparator implements Comparator<Lasso> {
    @Override
    public int compare(Lasso o1, Lasso o2) {
        if (o1.loopLength() == 0) {

            if (o2.loopLength() == 0) {
                return Integer.compare(o1.pathLength(), o2.pathLength());
            } else {
                return 1;
            }

        } else if (o2.loopLength() == 0) {
            return -1;
        } else {
            return Integer.compare(o1.loopLength(), o2.loopLength());
        }
    }
}
