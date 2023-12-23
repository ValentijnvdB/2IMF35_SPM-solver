package Strategies;

import java.util.Comparator;

public class OddPathComparator implements Comparator<OddPath> {
    @Override
    public int compare(OddPath o1, OddPath o2) {

        if (o1.isRemainder() && o2.isRemainder()) {
            return 0;
        } else if (o1.isRemainder()) { // o1 < o2
            return 1;
        } else if (o2.isRemainder()) { // o2 < o1
            return -1;
        }

        if (!o1.isLoop() || !o2.isLoop()) {
            if (o1.isLoop()) { // only o1 is a loop so o2 < o1
                return -1;

            } else if (o2.isLoop()) { // only o2 is a loop so o1 < o2
                return 1;

            } else { // neither is a loop
                if (o1.dependsOn(o2))  // o1 < o2
                    return 1;
                else if (o2.dependsOn(o1)) // o2 < o1
                    return -1;

            }
        }
        // o1 and o2 are not dependent on each other
        return Integer.compare(o1.length(), o2.length());
    }

}
