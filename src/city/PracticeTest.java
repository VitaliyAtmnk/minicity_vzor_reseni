package city;

import city.Coordinate;
import city.logic.CityManager;
import city.logic.CityMap;
import city.logic.TileDistanceFromCenterComparator;
import city.tiles.FactoryTile;
import city.tiles.HouseTile;
import city.tiles.Tile;

import java.io.*;
import java.util.List;

public class PracticeTest {

    public static void lineWorking(File file, CityManager cityManager) {
        BufferedReader br;
        PrintWriter pw;
        String line;

        if (!file.exists() && file.listFiles() == null) {
            System.out.println("No file or directory found.");
        }

        if (!file.isDirectory()) {
            try {
                br = new BufferedReader(new FileReader(file));
                pw = new PrintWriter(new BufferedWriter(new FileWriter("outputs/tiles.csv", true)));
                List<Tile> tiles = cityManager.getTiles();

                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(" ");

                    switch (tokens[0]) {
                        case "Add":
                            cityManager.addTile(tokens[1], new Coordinate(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
                            break;
                        case "Remove":
                            cityManager.removeTile(new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                            break;
                        case "Upgrade":
                            cityManager.upgradeTile(new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                            break;
                        case "Next":
                            cityManager.nextTurn();
                            break;
                        default:
                            System.out.println("Not a valid operation.");
                            break;
                    }
                }

                tiles.stream()
                        .mapToInt(Tile::getScore)
                        .sorted()
                        .limit(3)
                        .forEach(System.out::println);

                long factoryCount = tiles.stream()
                        .filter(tile -> tile.getSymbol() == 'F')
                        .count();

                long houseCount = tiles.stream()
                        .filter(tile -> tile.getSymbol() == 'H')
                        .count();

                long roadCount = tiles.stream()
                        .filter(tile -> tile.getSymbol() == 'R')
                        .count();

                long parkCount = tiles.stream()
                        .filter(tile -> tile.getSymbol() == 'P')
                        .count();


                if (!tiles.isEmpty()) {
                    System.out.println("Factories in percent: ");
                    System.out.println((factoryCount / tiles.size()) * 100 + "%");
                }

                System.out.println("Factory tiles: " + factoryCount);
                System.out.println("House tiles: " + houseCount);
                System.out.println("Road tiles: " + roadCount);
                System.out.println("Park tiles: " + parkCount);

                long occupiedTiles = tiles.stream()
                        .filter(tile -> tile.getSymbol() != ' ')
                        .count();

                System.out.println("Occupied tiles: " + occupiedTiles);

                pw.println("x;y;symbol;score;distanceFromCenter;createdAt");
                for (Tile t : tiles) {
                    pw.println(t.getCoordinate().getX() + ";" + t.getCoordinate().getY() + ";" + t.getSymbol() + ";" + t.getScore() + ";" + t.getCoordinate().distanceTo(cityManager.getCityMap().getCenter()) + ";" + t.getPrice());
                }

                br.close();
                pw.close();
            } catch (IOException e) {
                System.out.println("Error when working with file.");
            }
        }
    }

    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);
        File path = new File("inputs/");
        File[] content = path.listFiles();

        if (content != null) {
            for (File f : content) {
                if (!f.isDirectory() && f.length() <= 1_000_000) {
                    String[] fileBack = f.getName().split("\\.");
                    String[] fileFront = fileBack[0].split("_");
                    if (fileFront[0].equals("city") && fileBack[1].equals("txt")) {
                        lineWorking(f, cityManager);
                    }
                }
            }
        }
    }
}
