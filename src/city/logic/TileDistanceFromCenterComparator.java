package city.logic;

import city.Coordinate;
import city.tiles.Tile;

import java.util.Comparator;
import java.util.Objects;

/**
 * Porovnává políčka podle manhattanské vzdálenosti od zadaného středu mapy.
 */
public class TileDistanceFromCenterComparator implements Comparator<Tile> {
    private final Coordinate center;

    /**
     * Vytvoří komparátor používající konkrétní střed mapy.
     *
     * @param center střed mapy
     */
    public TileDistanceFromCenterComparator(Coordinate center) {
        this.center = center;
    }

    @Override
    public int compare(Tile first, Tile second) {
        return Integer.compare(
                first.getCoordinate().distanceTo(center),
                second.getCoordinate().distanceTo(center));
    }
}
