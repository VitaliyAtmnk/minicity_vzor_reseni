package city.tiles;

import city.Coordinate;
import city.Producing;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Továrna, která je políčkem mapy a umí v každém tahu produkovat energii.
 */
public class FactoryTile extends Tile implements Producing {
    /** Pořizovací cena továrny. */
    public static final int PRICE = 300;
    /** Výchozí bodová hodnota továrny. */
    public static final int STARTING_SCORE = 300;
    /** Nejnižší výroba energie v jednom tahu. */
    public static final int MIN_PRODUCTION = 25;
    /** Nejvyšší výroba energie v jednom tahu. */
    public static final int MAX_PRODUCTION = 75;

    private int production;

    /**
     * Vytvoří továrnu, která zatím neprovedla žádný výrobní tah.
     *
     * @param coordinate umístění továrny
     */
    public FactoryTile(Coordinate coordinate) {
        super(coordinate);
        this.production = 0;
    }

    /**
     * @return množství energie vyrobené při posledním tahu
     */
    public int getProduction() {
        return production;
    }

    @Override
    public int getPrice() {
        return PRICE;
    }

    @Override
    public char getSymbol() {
        return 'F';
    }

    @Override
    public String getDescription() {
        return "Factory " + getCoordinate() + " | Last production: " + production
                + " | Score: " + getScore();
    }

    @Override
    public int getScore() {
        return STARTING_SCORE;
    }

    @Override
    public int produce() {
        int produced = (int) ((Math.random() * (MAX_PRODUCTION - MIN_PRODUCTION)) + MIN_PRODUCTION);
        production += produced;
        return produced;
    }
}
