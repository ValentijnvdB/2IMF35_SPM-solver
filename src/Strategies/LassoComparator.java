package Strategies;

import java.util.Comparator;

public class LassoComparator implements Comparator<Lasso> {
    @Override
    public int compare(Lasso o1, Lasso o2) {
        // if both are loops, compare length of loops
        if (o1.fullLasso() && o2.fullLasso()) {
            return Integer.compare(o1.loopLength(), o2.loopLength());
        }
        // otherwise
        else
        {
            if (o1.fullLasso()) {
                return -1;
            }
            else if (o2.fullLasso()) {
                return 1;
            }
            else
            {
                if (o1.dependsOn(o2))
                    return 1;
                else if (o2.dependsOn(o1))
                    return -1;
                else
                    return Integer.compare(o1.pathLength(), o2.pathLength());
            }

        }

    }
}
