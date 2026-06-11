package city.tiles;

import city.Coordinate;
import city.Upgradable;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Dům, který je políčkem mapy, má populaci a lze jej vylepšovat.
 */
public class HouseTile extends Tile implements Upgradable {
    /** Pořizovací cena domu. */
    public static final int PRICE = 200;
    /** Výchozí bodová hodnota domu. */
    public static final int STARTING_SCORE = 250;

    private final int population;
    private int level;

    /**
     * Vytvoří dům první úrovně s náhodnou populací od 1 do 8.
     *
     * @param coordinate umístění domu
     */
    public HouseTile(Coordinate coordinate) {
        super(coordinate);
        this.population = new Random().nextInt(1, 9);
        this.level = 1;
    }

    /**
     * @return populace domu
     */
    public int getPopulation() {
        return population;
    }

    /**
     * @return aktuální úroveň domu
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
        return 'H';
    }

    @Override
    public String getDescription() {
        return "House " + getCoordinate() + " | Population: " + population
                + " | Level: " + level + " | Score: " + getScore();
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
