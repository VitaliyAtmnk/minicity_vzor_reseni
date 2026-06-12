package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RunByFiles {
    public static void writeCSV(CityManager cityManager) {
        String outFile = "outputs\\tiles.csv";
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
            out.println("x;y;type;symbol;score;distanceFromCenter;createdAt");
            for (Tile tile : cityManager.getTiles()) {
                out.println(tile.getCoordinate().getX() 
                        + ";" + tile.getCoordinate().getY() 
                        + ";" + tile.getClass().getSimpleName() 
                        + ";" + tile.getSymbol() 
                        + ";" + tile.getScore() 
                        + ";" 
                        + "gg" 
                        + tile.getCreatedAt());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        System.out.println("file " + outFile + " created");
    }

    public static void writeReport(CityManager cityManager) {
        String outFile = "outputs\\report.txt";
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile)))) {
            out.println("MINICITY REPORT");
            out.println("\nMap size: " + cityManager.getCityMap().getSize());
            out.println("Budget: " + cityManager.getBudget());
            out.println("Total score: " + cityManager.getTotalScore());
            out.println("Total tiles: " + cityManager.getTiles().size());
            out.println("\nTiles by type:");
            out.println("RoadTile: " + cityManager
                    .getCityMap()
                    .getTiles()
                    .stream()
                    .filter(tile -> tile
                            .getClass()
                            .getSimpleName()
                            .equals("RoadTile")
                    )
                    .count()
            );
            out.println("ParkTile: " + cityManager
                    .getCityMap()
                    .getTiles()
                    .stream()
                    .filter(tile -> tile
                            .getClass()
                            .getSimpleName()
                            .equals("ParkTile")
                    )
                    .count()
            );
            out.println("HouseTile: " + cityManager
                    .getCityMap()
                    .getTiles()
                    .stream()
                    .filter(tile -> tile
                            .getClass()
                            .getSimpleName()
                            .equals("HouseTile")
                    )
                    .count()
            );
            out.println("FactoryTile: " + cityManager
                    .getCityMap()
                    .getTiles()
                    .stream()
                    .filter(tile -> tile
                            .getClass()
                            .getSimpleName()
                            .equals("FactoryTile")
                    )
                    .count()
            );
            out.println("\nSpecial tiles:");

            out.println("Upgradable tiles: ");
            out.println("Producing tiles: ");
            out.println("Total house population: ");

            out.println("\nTop 3 tiles by score");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("file " + outFile + " created");

    }

    public static void executeFile(String filePath, CityManager cityManager) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] command = line.split(" ");
                switch (command[0]) {
                    case "Add" -> {
                        switch (command[1]) {
                            case "Road" ->
                                    cityManager.addTile("Road", new Coordinate(Integer.parseInt(command[2]), Integer.parseInt(command[3])));
                            case "Park" ->
                                    cityManager.addTile("Park", new Coordinate(Integer.parseInt(command[2]), Integer.parseInt(command[3])));
                            case "House" ->
                                    cityManager.addTile("House", new Coordinate(Integer.parseInt(command[2]), Integer.parseInt(command[3])));
                            case "Factory" ->
                                    cityManager.addTile("Factory", new Coordinate(Integer.parseInt(command[2]), Integer.parseInt(command[3])));
                        }
                    }
                    case "Remove" -> {
                        cityManager.removeTile(new Coordinate(Integer.parseInt(command[1]), Integer.parseInt(command[2])));
                    }
                    case "Upgrade" -> {
                        cityManager.upgradeTile(new Coordinate(Integer.parseInt(command[1]), Integer.parseInt(command[2])));
                    }
                    case "Next" -> {
                        cityManager.nextTurn();
                    }
                }

            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Executed file " + filePath);
    }

    public static void getValidFilesPaths(String folder, CityManager cityManager) {
        try (Stream<Path> paths = Files.walk(Paths.get(folder))) {
            paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .filter(path -> path.toString().startsWith(folder + "\\city_"))
                    .filter(path -> {
                        try {
                            return Files.size(path) <= 1024 * 1024;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .forEach(path -> executeFile(path.toString(), cityManager))
//                    .forEach(System.out::println)
            ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);

        String folder = "inputs";
        getValidFilesPaths(folder, cityManager);
        System.out.println("Executed all files");

        writeReport(cityManager);

        writeCSV(cityManager);
    }
}
