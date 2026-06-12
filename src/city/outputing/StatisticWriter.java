package city.outputing;

import city.Coordinate;
import city.Producing;
import city.Upgradable;
import city.logic.CityManager;
import city.tiles.HouseTile;
import city.tiles.Tile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StatisticWriter {


    public static final String outputReportFile = "outputs/report.txt";
    public static final String outputExportFile = "outputs/tiles.csv";

    public CityManager cityManager;

    public StatisticWriter(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    public void generateTilesFile(){
        List<Tile> tiles = cityManager.getTiles();

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputExportFile))) {
            writer.println("x;y;symbol;score;distanceFromCenter;price");
            tiles.sort(Comparator.comparingDouble(tile->tile.getCoordinate().distanceTo(cityManager.getCityMap().getCenter())));
            tiles.forEach((tile)->{
                Coordinate cords = tile.getCoordinate();
                writer.println(
                    cords.getX()+";"+
                        cords.getY()+";"+
                        tile.getSymbol()+";"+
                        tile.getScore()+";"+
                        cords.distanceTo(cityManager.getCityMap().getCenter())+";"+
                        tile.getPrice()
                );
            });
        }catch (IOException e) {
            System.out.println("Chyba při zapisování do souboru "+ outputExportFile);
        }
        System.out.println("Úspěšně zapsán soubor do "+outputExportFile);
    }

    public void generateMiniCityReportFile() {
        List<Tile> tiles = cityManager.getTiles();

        List<Tile> best3 = tiles.stream()
            .sorted(Comparator.comparingInt(Tile::getScore).reversed())
            .limit(3).toList();

        double factoryPercent = tiles.stream().filter(tile -> tile instanceof Producing).count() / (double) tiles.size();
        double upgradablePercent = tiles.stream().filter(tile -> tile instanceof Upgradable).count() / (double) tiles.size();

        long population = tiles.stream()
            .filter(tile->tile instanceof HouseTile)
            .mapToInt(tile -> ((HouseTile) tile).getPopulation())
            .sum();

        double filledPostions = (double) (cityManager.getCityMap().getSize() * cityManager.getCityMap().getSize()) / tiles.size();

        Map<String, List<Tile>> groupedMap = tiles.stream().collect(Collectors.groupingBy(t->t.getClass().getSimpleName()));

        Map<String, Long> typesCount = tiles.stream().collect(Collectors.groupingBy(t->t.getClass().getSimpleName(),Collectors.counting()));

        Map<String, Long> typesPrice = tiles.stream().collect(Collectors.groupingBy(t->t.getClass().getSimpleName(),Collectors.summingLong(Tile::getPrice)));

        Map<String, Double> upgradableAvgScore = tiles.stream()
            .filter(tile -> tile instanceof Upgradable)
            .collect(Collectors.groupingBy(t->t.getClass().getSimpleName(),
                Collectors.averagingInt(Tile::getScore)));

        Map<String, Integer> affordable = new HashMap<>();
        groupedMap.forEach((k,v)->{
            affordable.put(k,(int)Math.round(cityManager.getBudget() / (double) v.getFirst().getPrice()));
        });

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputReportFile))){
            writer.println("MINICITY REPORT");
            writer.println();
            writer.println("Map size: "+cityManager.getCityMap().getSize());
            writer.println("Budget: "+cityManager.getBudget());
            writer.println("Total score: "+cityManager.getTotalScore());
            writer.println("Total tiles: "+tiles.size());
            writer.println();
            writer.println("Tiles by type:");
            typesCount.forEach((k,v)->{
                writer.println(k+": "+v);
            });
            writer.println();
            writer.println("Special tiles:");
            writer.println("Producing tiles: "+(factoryPercent*100)+"%");
            writer.println("Upgradable tiles: "+(upgradablePercent*100)+"%");
            writer.println("Population: "+population);
            writer.println();
            writer.println("Filled: "+filledPostions);
            writer.println();
            writer.println("Price per type:");
            typesPrice.forEach((k,v)->
                writer.println(k+" "+v));
            writer.println();
            writer.println("Avg score per upgradable:");
            upgradableAvgScore.forEach((k,v)->
                writer.println(k+" "+v));
            writer.println();
            writer.println("Able to buy from type:");
            affordable.forEach((k,v)->
                writer.println(k+" "+v));

            writer.println();
            writer.println("Top 3 tiles by score:");

            for (int i = 0; i < 3; i++) {
                writer.println((i+1)+". "+best3.get(i).getDescription());
            }


        } catch (IOException e) {
            System.out.println("Chyba při zapisování do souboru "+ outputReportFile);
        }

        System.out.println("Úspěšně zapsán soubor do "+outputReportFile);

    }
}
