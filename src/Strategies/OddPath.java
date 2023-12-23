package Strategies;

import java.util.Iterator;

public record OddPath(int[] path, boolean isLoop, boolean isRemainder, OddPath dependsOn) implements Iterable<Integer> {

    public int length() {
        return path.length;
    }

    public int get(int i) {
        return path[i];
    }

    /**
     * Check whether this depends on other directly or indirectly
     * @param other the other Lasso
     * @return whether this depends on other directly or indirectly
     */
    public boolean dependsOn(OddPath other) {
        return recDependsOn(this, other);
    }

    /**
     * Recursively check whether l1 is dependent on l2 by checking if l1.dependsOn equals l2
     * if not then recursing with (l1.dependsOn, l2)
     * @param l1 Lasso 1
     * @param l2 Lasso 2
     * @return whether l1 depends on l2
     */
    private static boolean recDependsOn(OddPath l1, OddPath l2) {
        if (l1.dependsOn == null) {
            return false;
        }
        return l1.dependsOn.equals(l2) || recDependsOn(l1.dependsOn, l2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof OddPath other) {

            if (this.isLoop() == other.isLoop() && this.isRemainder == other.isRemainder && this.path.length == other.path.length) {

                for (int i = 0; i < path.length; i++) {
                    if (this.path[i] != other.path[i]) return false;
                }

                return this.dependsOn == null || this.dependsOn.equals(other.dependsOn);

            }
        }
        return false;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new OddPathIterator(this);
    }
}
