package city.tiles;

import city.Coordinate;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Společný abstraktní základ všech políček, která lze umístit do mapy.
 */
public abstract class Tile {
    private final Coordinate coordinate;
    private final LocalDate createdAt;

    /**
     * Vytvoří políčko na zadané souřadnici.
     *
     * @param coordinate umístění políčka
     */
    protected Tile(Coordinate coordinate) {
        this.coordinate = coordinate;
        this.createdAt = LocalDate.now();
    }

    /**
     * @return souřadnice políčka
     */
    public Coordinate getCoordinate() {
        return coordinate;
    }

    /**
     * @return datum vytvoření políčka
     */
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    /**
     * @return pořizovací cena políčka
     */
    public abstract int getPrice();

    /**
     * @return znak používaný při vykreslení mapy
     */
    public abstract char getSymbol();

    /**
     * @return užitečný textový popis políčka
     */
    public abstract String getDescription();

    /**
     * @return aktuální skóre políčka
     */
    public abstract int getScore();
}
