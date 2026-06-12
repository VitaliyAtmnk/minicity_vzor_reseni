package reseni;

import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Main {


    public void processFiles(CityManager cityManager, String directoryPath) {
        File folder = new File(directoryPath);
        File[] files = folder.listFiles((dir, name) ->
            name.startsWith("city_") && name.endsWith(".txt")
        );

        if (files == null) return;

        for (File file : files) {
            if (file.isFile() && file.length() <= 1024 * 1024) {
                try (java.util.Scanner scanner = new java.util.Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine().trim();
                        if (line.isEmpty()) continue;
                        String[] parts = line.split(" ");
                        String command = parts[0];

                        switch (command) {
                            case "Add":
                                String type = parts[1];
                                int addX = Integer.parseInt(parts[2]);
                                int addY = Integer.parseInt(parts[3]);
                                cityManager.addTile(type, new city.Coordinate(addX, addY));
                                break;
                            case "Remove":
                                int remX = Integer.parseInt(parts[1]);
                                int remY = Integer.parseInt(parts[2]);
                                cityManager.removeTile(new city.Coordinate(remX, remY));
                                break;
                            case "Upgrade":
                                int upX = Integer.parseInt(parts[1]);
                                int upY = Integer.parseInt(parts[2]);
                                cityManager.upgradeTile(new city.Coordinate(upX, upY));
                                break;
                            case "Next":
                                cityManager.nextTurn();
                                break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + file.getName());
                }
            }
        }
    }

    public void tileExport(CityManager cityManager, String tilesPath){
        List<Tile> tiles = cityManager.getTiles();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tilesPath))) {
            writer.write("x;y;type;symbol;score;distanceFromCenter;createdAt");
            writer.newLine();
            for (Tile tile : tiles) {
                writer.write(tile.getCoordinate().getX() + ";" + tile.getCoordinate().getY() + ";" + tile.getClass() + ";" + tile.getSymbol() + ";" + tile.getScore() + ";" + tile.getCoordinate().distanceTo(cityManager.getCityMap().getCenter())
            ;}
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void cityReport(CityManager cityManager, String reportPath){
        List<Tile> tiles = cityManager.getTiles();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportPath))) {
            writer.write("MINICITY REPORT");
            writer.newLine();
            writer.write("Map size: " + cityManager.getCityMap().getSize());
            writer.newLine();
            writer.write("Budget: " + cityManager.getBudget());
            writer.newLine();
            writer.write("Total score: " + cityManager.getTotalScore());
            writer.newLine();
            writer.write("Total tiles: " + tiles.size());
            writer.newLine();
            writer.write("Tiles by type:" + tiles.getClass());
            writer.newLine();
            writer.write("Top 3 tiles by score: " + tiles.getClass());


    } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);

    }


