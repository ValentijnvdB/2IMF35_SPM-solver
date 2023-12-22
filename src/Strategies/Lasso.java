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

    public boolean dependsOn(Lasso other) {
        return this.dependsOn.equals(other);
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
