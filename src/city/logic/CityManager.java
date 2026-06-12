package city.logic;

import city.Coordinate;
import city.Producing;
import city.Upgradable;
import city.tiles.FactoryTile;
import city.tiles.HouseTile;
import city.tiles.ParkTile;
import city.tiles.RoadTile;
import city.tiles.Tile;

import java.io.FileWriter;
import java.util.*;

/**
 * Koordinuje práci s mapou města, rozpočtem, skóre a průběhem tahů.
 */
public class CityManager {
    /** Násobitel převádějící vyrobenou energii na přírůstek rozpočtu. */
    public static final int ENERGY_BUDGET_MULTIPLIER = 4;

    private final CityMap cityMap;
    private int budget;

    /**
     * Vytvoří správce města.
     *
     * @param cityMap spravovaná mapa
     * @param budget počáteční rozpočet
     */
    public CityManager(CityMap cityMap, int budget) {
        this.cityMap = cityMap;
        this.budget = budget;
    }

    /**
     * Přidá vybraný typ políčka, pokud rozpočet stačí na jeho pořízení.
     * Obsazená souřadnice je v takovém případě přepsána.
     *
     * @param tileType typ políčka: Road, Park, House nebo Factory
     * @param coordinate cílová souřadnice
     */
    public void addTile(String tileType, Coordinate coordinate) {
        Tile tile = createTile(tileType, coordinate);
        if (budget < tile.getPrice()) {
            return;
        }
        cityMap.add(tile);
        budget -= tile.getPrice();
    }

    /**
     * Odebere políčko ze souřadnice.
     *
     * @param coordinate souřadnice odebíraného políčka
     */
    public void removeTile(Coordinate coordinate) {
        cityMap.remove(coordinate);
    }

    /**
     * Vylepší políčko, pokud to políčko umí a rozpočet pokrývá cenu vylepšení.
     *
     * @param coordinate souřadnice vylepšovaného políčka
     */
    public void upgradeTile(Coordinate coordinate) {
        Tile tile = cityMap.getTile(coordinate);
        if (!(tile instanceof Upgradable)) {
            return;
        }
        Upgradable upgradable = (Upgradable) tile;

        int price = upgradable.getUpgradePrice();
        if (budget < price) {
            return;
        }

        upgradable.upgrade();
        budget -= price;
    }

    /**
     * Provede další tah a připíše do rozpočtu hodnotu energie z produkujících políček.
     */
    public void nextTurn() {
        for (Tile tile : cityMap.getTiles()) {
            if (tile instanceof Producing) {
                budget += ((Producing) tile).produce() * ENERGY_BUDGET_MULTIPLIER;
            }
        }
    }

    /**
     * @return spravovaná mapa
     */
    public CityMap getCityMap() {
        return cityMap;
    }

    /**
     * @return aktuální rozpočet
     */
    public int getBudget() {
        return budget;
    }

    /**
     * Spočítá aktuální celkové skóre všech umístěných políček.
     *
     * @return celkové skóre
     */
    public int getTotalScore() {
        return cityMap.getTiles().stream()
                .mapToInt(Tile::getScore)
                .sum();
    }

    /**
     * Vrátí všechna políčka.
     *
     * @return nový seznam políček
     */
    public List<Tile> getTiles() {
        return cityMap.getTiles();
    }

    /**
     * Vykreslí mapu města.
     */
    public void printMap() {
        cityMap.printMap();
    }

    private Tile createTile(String tileType, Coordinate coordinate) {
        return switch (tileType.toLowerCase()) {
            case "road" -> new RoadTile(coordinate);
            case "park" -> new ParkTile(coordinate);
            case "house" -> new HouseTile(coordinate);
            case "factory" -> new FactoryTile(coordinate);
            default -> throw new IllegalArgumentException("Unknown tile type: " + tileType);
        };
    }

    public void exportReport(){
        try {
            FileWriter exportCity = new FileWriter("report.txt");
            exportCity.write("MINICITY REPORT");
            exportCity.write("\n");
            exportCity.write("\n");
            exportCity.write("Map size: " + cityMap.getSize());
            exportCity.write("\n");
            exportCity.write("Budget: " + budget);
            exportCity.write("\n");
            exportCity.write("Total score: " + getTotalScore());
            exportCity.write("\n");
            exportCity.write("Tiles by type: ");
            exportCity.write("\n");
            int roadTilesCount = 0;
            int parkTilesCount = 0;
            int houseTilesCount = 0;
            int factoryTilesCount = 0;
            for (Tile tile : cityMap.getTiles()) {
                if (tile.getClass().getSimpleName().equals("RoadTile")) {
                    roadTilesCount++;
                }
                if (tile.getClass().getSimpleName().equals("ParkTile")) {
                    parkTilesCount++;
                }
                if (tile.getClass().getSimpleName().equals("HouseTile")) {
                    houseTilesCount++;
                }
                if (tile.getClass().getSimpleName().equals("FactoryTile")) {
                    factoryTilesCount++;
                }

            }
            exportCity.write("RoadTiles: " + roadTilesCount + "\n ParkTiles: " + parkTilesCount + "\n HouseTiles: " + houseTilesCount + "\n FactoryTiles: " + factoryTilesCount);
            exportCity.write("\n");
            exportCity.write("Special tiles:");
            exportCity.write("\n");
            int upgradeableTilesCount = 0;
            int productionTilesCount = 0;
            for (Tile tile : cityMap.getTiles()) {
                if (tile instanceof Upgradable) {
                    upgradeableTilesCount++;
                }
                if (tile instanceof Producing) {
                    productionTilesCount++;
                }
            }

            exportCity.write("Upgradable tiles: " + upgradeableTilesCount);
            exportCity.write("\n");
            exportCity.write("Producing tiles: " + productionTilesCount);
            exportCity.write("\n");

            int totalHousePopulation = 0;
            for (Tile tile : cityMap.getTiles()) {
                if (tile instanceof HouseTile) {
                    totalHousePopulation += ((HouseTile) tile).getPopulation();
                }
            }
            exportCity.write("Total house population: " + totalHousePopulation);

            exportCity.write("\n");
            exportCity.write("Top 3 tiles by score:");
            exportCity.write("\n");
            int bestRoadTileScore = 0;
            int bestParkTileScore = 0;
            int bestHouseTileScore = 0;
            int bestFactoryTileScore = 0;
            for (Tile tile : cityMap.getTiles()) {
                if (tile instanceof RoadTile && tile.getScore() > bestRoadTileScore) {
                    bestRoadTileScore = tile.getScore();
                }
                if (tile instanceof ParkTile && tile.getScore() > bestParkTileScore) {
                    bestParkTileScore = tile.getScore();
                }
                if (tile instanceof HouseTile && tile.getScore() > bestHouseTileScore) {
                    bestHouseTileScore = tile.getScore();
                }
                if (tile instanceof FactoryTile && tile.getScore() > bestFactoryTileScore) {
                    bestFactoryTileScore = tile.getScore();
                }
            }
            SortedSet<Integer> bestTiles = new TreeSet<>();
            bestTiles.add(bestFactoryTileScore);
            bestTiles.add(bestRoadTileScore);
            bestTiles.add(bestParkTileScore);
            bestTiles.add(bestHouseTileScore);
            //dosly mi znalosti na dokonceni
            exportCity.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
