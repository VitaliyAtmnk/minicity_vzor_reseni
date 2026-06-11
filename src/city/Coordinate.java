package city;

import java.util.Objects;

/**
 * Neměnná dvojice souřadnic určující pozici v mapě města.
 */
public final class Coordinate {
    private final int x;
    private final int y;

    /**
     * Vytvoří souřadnici.
     *
     * @param x vodorovná souřadnice
     * @param y svislá souřadnice
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return vodorovná souřadnice
     */
    public int getX() {
        return x;
    }

    /**
     * @return svislá souřadnice
     */
    public int getY() {
        return y;
    }

    /**
     * Spočítá manhattanskou vzdálenost od jiné souřadnice.
     *
     * @param other druhá souřadnice
     * @return manhattanská vzdálenost
     */
    public int distanceTo(Coordinate other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Coordinate other)) {
            return false;
        }
        return x == other.x && y == other.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
