package Strategies;

import java.util.ArrayList;

public record Lasso(int[] path, int[] loop, Lasso dependsOn) {

    public int length() {
        return path.length + loop.length;
    }

    public int loopLength() {
        return loop.length;
    }

    public int pathLength() {
        return path.length;
    }

    public boolean fullLasso() {
        return loop.length > 0;
    }

    /**
     * Check whether this depends on other directly or indirectly
     * @param other the other Lasso
     * @return whether this depends on other directly or indirectly
     */
    public boolean dependsOn(Lasso other) {
        return recDependsOn(this, other);
    }

    /**
     * Recursively check whether l1 is dependent on l2 by checking if l1.dependsOn equals l2
     * if not then recursing with (l1.dependsOn, l2)
     * @param l1 Lasso 1
     * @param l2 Lasso 2
     * @return whether l1 depends on l2
     */
    private static boolean recDependsOn(Lasso l1, Lasso l2) {
        if (l1.dependsOn == null) {
            return false;
        }
        return l1.dependsOn.equals(l2) || recDependsOn(l1.dependsOn, l2);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof Lasso other) {
            if (this.path.length == other.path.length && this.loop.length == other.loop.length) {

                for (int i = 0; i < path.length; i++) {
                    if (this.path[i] != other.path[i]) return false;
                }

                for (int i = 0; i < loop.length; i++) {
                    if (this.loop[i] != other.loop[i]) return false;
                }

                return true;

            }
        }
        return false;
    }

}
