package city.logic;

import city.Coordinate;
import city.tiles.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Čtvercová mapa města, která spravuje umístění jednotlivých políček.
 */
public class CityMap {
    private final Tile[][] tiles;
    private final Coordinate center;
    private final int size;

    /**
     * Vytvoří prázdnou čtvercovou mapu. Souřadnice mapy začínají hodnotou 1.
     *
     * @param size délka jedné strany mapy
     */
    public CityMap(int size) {
        this.size = size;
        this.tiles = new Tile[size][size];
        this.center = new Coordinate((size + 1) / 2, (size + 1) / 2);
    }

    /**
     * Přidá nebo přepíše políčko na souřadnici uložené v objektu.
     *
     * @param tile přidávané políčko
     */
    public void add(Tile tile) {
        Coordinate coordinate = tile.getCoordinate();
        validateCoordinate(coordinate);
        tiles[toIndex(coordinate.getX())][toIndex(coordinate.getY())] = tile;
    }

    /**
     * Najde políčko na souřadnici.
     *
     * @param coordinate hledaná souřadnice
     * @return políčko nebo {@code null}, pokud je pozice prázdná
     */
    public Tile getTile(Coordinate coordinate) {
        validateCoordinate(coordinate);
        return tiles[toIndex(coordinate.getX())][toIndex(coordinate.getY())];
    }

    /**
     * Odebere políčko ze souřadnice. Prázdná pozice zůstane beze změny.
     *
     * @param coordinate souřadnice odebíraného políčka
     */
    public void remove(Coordinate coordinate) {
        validateCoordinate(coordinate);
        tiles[toIndex(coordinate.getX())][toIndex(coordinate.getY())] = null;
    }

    /**
     * Vrátí všechna umístěná políčka.
     *
     * @return nový seznam políček
     */
    public List<Tile> getTiles() {
        List<Tile> result = new ArrayList<>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Tile tile = tiles[x][y];
                if (tile != null) {
                    result.add(tile);
                }
            }
        }
        // result.sort(new TileDistanceFromCenterComparator(center));
        return result;
    }

    /**
     * @return souřadnice středu mapy
     */
    public Coordinate getCenter() {
        return center;
    }

    /**
     * @return délka jedné strany mapy
     */
    public int getSize() {
        return size;
    }

    /**
     * Vykreslí mapu do standardního výstupu.
     */
    public void printMap() {
        System.out.print("y\\x");
        for (int x = 1; x <= size; x++) {
            System.out.printf(" %2d", x);
        }
        System.out.println();

        for (int y = 1; y <= size; y++) {
            System.out.printf("%3d", y);
            for (int x = 1; x <= size; x++) {
                Tile tile = tiles[toIndex(x)][toIndex(y)];
                char symbol = tile == null ? ' ' : tile.getSymbol();
                System.out.printf(" %2s", symbol);
            }
            System.out.println();
        }
    }

    private void validateCoordinate(Coordinate coordinate) {
        if (coordinate.getX() < 1 || coordinate.getX() > size
                || coordinate.getY() < 1 || coordinate.getY() > size) {
            throw new IndexOutOfBoundsException("Coordinate outside map: " + coordinate);
        }
    }

    private int toIndex(int coordinatePart) {
        return coordinatePart - 1;
    }
}
