package city.tests;

import city.Coordinate;
import city.logic.CityManager;
import city.logic.CityMap;
import city.logic.TileDistanceFromCenterComparator;
import city.tiles.FactoryTile;
import city.tiles.HouseTile;
import city.tiles.ParkTile;
import city.tiles.RoadTile;
import city.tiles.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Jednoduchý spustitelný test základních požadavků bez externí testovací knihovny.
 */
public final class CityManagerSmokeTest {
    private CityManagerSmokeTest() {}

    /**
     * Ověří přidávání, rozpočet, přepis políčka, vylepšení, produkci a řazení.
     *
     * @param args argumenty se nepoužívají
     */
    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 2_000);

        cityManager.addTile("Road", new Coordinate(1, 1));
        cityManager.addTile("Park", new Coordinate(2, 2));
        cityManager.addTile("House", new Coordinate(3, 3));
        cityManager.addTile("Factory", new Coordinate(4, 4));

        require(cityManager.getBudget() == 1_250, "Budget must decrease after adding tiles.");
        require(cityMap.getTile(new Coordinate(1, 1)) instanceof RoadTile, "Road must be added.");
        require(cityMap.getTile(new Coordinate(2, 2)) instanceof ParkTile, "Park must be added.");
        require(cityMap.getTile(new Coordinate(3, 3)) instanceof HouseTile, "House must be added.");
        require(cityMap.getTile(new Coordinate(4, 4)) instanceof FactoryTile, "Factory must be added.");

        RoadTile road = (RoadTile) cityMap.getTile(new Coordinate(1, 1));
        int budgetBeforeRoadUpgrade = cityManager.getBudget();
        int roadUpgradePrice = road.getUpgradePrice();
        cityManager.upgradeTile(new Coordinate(1, 1));
        require(road.getLevel() == 2, "Upgradable tile must increase its level.");
        require(cityManager.getBudget() == budgetBeforeRoadUpgrade - roadUpgradePrice,
                "Upgrade price must decrease the budget.");

        int budgetBeforeFactoryUpgrade = cityManager.getBudget();
        cityManager.upgradeTile(new Coordinate(4, 4));
        require(cityManager.getBudget() == budgetBeforeFactoryUpgrade,
                "Factory must not be upgraded.");

        int budgetBeforeTurn = cityManager.getBudget();
        cityManager.nextTurn();
        int budgetIncrease = cityManager.getBudget() - budgetBeforeTurn;
        require(budgetIncrease >= 25 * 4 && budgetIncrease <= 75 * 4,
                "Factory production must increase budget by energy multiplied by four.");

        List<Tile> sortedTiles = new ArrayList<>(cityMap.getTiles());
        sortedTiles.sort(new TileDistanceFromCenterComparator(cityMap.getCenter()));
        require(sortedTiles.get(0).getCoordinate().equals(new Coordinate(3, 3)),
                "The center tile must be first after sorting.");

        int budgetBeforeOverwrite = cityManager.getBudget();
        cityManager.addTile("Road", new Coordinate(4, 4));
        require(cityMap.getTile(new Coordinate(4, 4)) instanceof RoadTile,
                "Adding to an occupied coordinate must overwrite the tile.");
        require(cityManager.getBudget() == budgetBeforeOverwrite - RoadTile.PRICE,
                "Overwriting a tile must still cost its purchase price.");

        require(cityManager.getTotalScore() > 0, "Total score must be calculated.");
        System.out.println("Smoke test passed.");
    }

    private static void require(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
