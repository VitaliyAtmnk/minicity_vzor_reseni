package city.tiles;

import city.Coordinate;

/**
 * Park, který je zvláštním typem silnice a přidává bonus atraktivity.
 */
public class ParkTile extends RoadTile {
    /** Pořizovací cena parku. */
    public static final int PRICE = 150;
    /** Výchozí bodová hodnota parku. */
    public static final int STARTING_SCORE = 200;
    /** Výchozí bonus atraktivity parku. */
    public static final int STARTING_ATTRACTIVENESS_BONUS = 50;
    /** Zvýšení bonusu atraktivity při jednom vylepšení. */
    public static final int ATTRACTIVENESS_BONUS_STEP = 25;

    private int attractivenessBonus;

    /**
     * Vytvoří park první úrovně.
     *
     * @param coordinate umístění parku
     */
    public ParkTile(Coordinate coordinate) {
        super(coordinate);
        this.attractivenessBonus = STARTING_ATTRACTIVENESS_BONUS;
    }

    /**
     * @return aktuální bonus atraktivity
     */
    public int getAttractivenessBonus() {
        return attractivenessBonus;
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public char getSymbol() {
        return 'P';
    }

    @Override
    public String getDescription() {
        return "Park " + getCoordinate() + " | Level: " + level
                + " | Attractiveness bonus: " + attractivenessBonus
                + " | Score: " + getScore();
    }

    @Override
    public int getScore() {
        return STARTING_SCORE * level + attractivenessBonus;
    }

    @Override
    public int getUpgradePrice() {
        return PRICE * level;
    }

    @Override
    public void upgrade() {
        super.upgrade(); // level++
        attractivenessBonus += ATTRACTIVENESS_BONUS_STEP;
    }
}
