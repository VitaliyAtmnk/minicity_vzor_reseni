package city.logic;

import city.Producing;
import city.Upgradable;
import city.tiles.HouseTile;
import city.tiles.Tile;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Statistics {

    private final CityManager cityManager;

    public Statistics(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    public List<Tile> getTopThreeTiles() {
        return cityManager.getTiles().stream()
                .sorted(Comparator.comparingInt(Tile::getScore).reversed())
                .limit(3)
                .toList();
    }

    public double getProducingPercentage() {
        List<Tile> tiles = cityManager.getTiles();

        if (tiles.isEmpty()) {
            return 0;
        }

        long producingCount = tiles.stream()
                .filter(tile -> tile instanceof Producing)
                .count();

        return (double) producingCount * 100 / tiles.size();
    }

    public int getTotalPopulation() {
        return cityManager.getTiles().stream()
                .filter(tile -> tile instanceof HouseTile)
                .map(tile -> (HouseTile) tile)
                .mapToInt(HouseTile::getPopulation)
                .sum();
    }

    public double getOccupancyPercentage() {
        int occupied = cityManager.getTiles().size();
        int total = cityManager.getCityMap().getSize()
                * cityManager.getCityMap().getSize();

        return (double) occupied * 100 / total;
    }

    public Map<String, Long> getTileCountByType() {
        return cityManager.getTiles().stream()
                .collect(Collectors.groupingBy(
                        tile -> tile.getClass().getSimpleName(),
                        Collectors.counting()
                ));
    }

    public Map<String, Integer> getTotalPriceByType() {
        return cityManager.getTiles().stream()
                .collect(Collectors.groupingBy(
                        tile -> tile.getClass().getSimpleName(),
                        Collectors.summingInt(Tile::getPrice)
                ));
    }

    public Map<String, Double> getAverageScoreOfUpgradeableTiles() {
        return cityManager.getTiles().stream()
                .filter(tile -> tile instanceof Upgradable)
                .collect(Collectors.groupingBy(
                        tile -> tile.getClass().getSimpleName(),
                        Collectors.averagingInt(Tile::getScore)
                ));
    }

    public Map<String, Integer> getAffordableTiles() {
        int budget = cityManager.getBudget();

        return cityManager.getTiles().stream()
                .collect(Collectors.groupingBy(
                        tile -> tile.getClass().getSimpleName(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                list -> budget / list.get(0).getPrice()
                        )
                ));
    }

    public Map<String, Long> getDistanceCategories() {
        return cityManager.getTiles().stream()
                .collect(Collectors.groupingBy(
                        tile -> tile.getCoordinate()
                                .distanceTo(cityManager.getCityMap().getCenter()) <= 2
                                ? "near"
                                : "further",
                        Collectors.counting()
                ));
    }
}