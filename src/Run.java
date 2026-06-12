import city.Coordinate;
import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class Run {

    static ArrayList<File> files = new ArrayList<>();
    static List<String> lines;
    static ArrayList<String> allLines = new ArrayList<>();


    public static void printDir(String dirPath){

        File directory = new File(dirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("You did not provide directory!");
            return;
        }

        for (File f : directory.listFiles()) {
            String[] tokens = f.getName().split("\\.");
            if (tokens[tokens.length - 1].equals("txt")) {
                if (tokens[0].length() > 4) {
                    if (tokens[0].substring(0,5).equals("city_")) {
                        if (f.length() < 1_000_000) {
                            files.add(f);
                        }
                    }
                }
            }
        }

        for (File f : files){
            System.out.println(f.getName());
        }
    }

    public static void getLines() {
        for (File f : files){
            try {
                lines = Files.lines(Path.of(f.getPath()))
                        .toList();

                allLines.addAll(lines);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }



        for (String line : allLines){
            System.out.println(line);
        }
    }

    public static void enterCommand(CityManager cityManager) {
        for (String line : allLines){
            String[] tokens = line.split(" ");
            switch (tokens.length){
                case 4:
                    cityManager.addTile(tokens[1], new Coordinate(Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3])));
                case 3:
                    if (tokens[0].equals("Remove")){
                        cityManager.removeTile(new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                    } else if (tokens[0].equals("Upgrade")){
                        cityManager.upgradeTile(new Coordinate(Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2])));
                    }
                case 1:
                    cityManager.nextTurn();
                break;
            }
        }
    }

    public static void statistics(CityManager cityManager) {
        List<Tile> tiles = cityManager.getTiles();

        List<Integer> sortedScore = tiles.stream()
                .map(Tile::getScore)
                .sorted()
                .toList().reversed();

        for (int i = 0; i < 3; i++) {
            System.out.println(sortedScore.get(i));
        }

        long factoryTiles = tiles.stream()
                .map(Tile::getSymbol)
                .filter(symbol -> symbol.equals('F'))
                .count();

        for (Tile tile : tiles){
            System.out.println(tile.getSymbol());

        }
        System.out.println(((double)factoryTiles / (double)tiles.size()) * 100  + "%");
    }

    public static void main(String[] args) {


        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);

        printDir("inputs");
        getLines();
        enterCommand(cityManager);
        statistics(cityManager);
    }
}

