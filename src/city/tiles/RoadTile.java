package city.tiles;

import city.Coordinate;
import city.Upgradable;

/**
 * Silnice, která je políčkem mapy a lze ji vylepšovat.
 */
public class RoadTile extends Tile implements Upgradable {
    /** Pořizovací cena silnice. */
    public static final int PRICE = 100;
    /** Výchozí score silnice. */
    public static final int STARTING_SCORE = 100;

    protected int level;

    /**
     * Vytvoří silnici.
     *
     * @param coordinate umístění silnice
     */
    public RoadTile(Coordinate coordinate) {
        super(coordinate);
        this.level = 1;
    }

    /**
     * @return aktuální úroveň silnice
     */
    public int getLevel() {
        return level;
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public char getSymbol() {
        return 'R';
    }

    @Override
    public String getDescription() {
        return "Road " + getCoordinate() + " | Level: " + level + " | Score: " + getScore();
    }

    @Override
    public int getScore() {
        return STARTING_SCORE * level;
    }

    @Override
    public int getUpgradePrice() {
        return PRICE * level;
    }

    @Override
    public void upgrade() {
        level++;
    }
}
