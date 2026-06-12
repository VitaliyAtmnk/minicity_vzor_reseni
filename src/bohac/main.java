package bohac;

import city.Coordinate;
import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.sort;
import static java.util.Arrays.stream;

public class main {
    public static List<File> dataLoader(File path){
        File[] files = path.listFiles();
        List<File> inputs = stream(files)
                .filter(f -> f.isFile())
                .filter(f -> f.getName().toString().endsWith(".txt"))
                .filter(f -> f.getName().toString().startsWith("city_")).toList();

        return inputs;
    }
    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);
        String dataInput = "inputs";
        List<File> inputs = dataLoader(Path.of(dataInput).toFile());
        inputs.forEach(f -> {
            try {
                Files.lines(f.toPath())
                        .forEach(l -> {
                            String[] parts = l.split(" ");
                            if (parts[0].startsWith("Remove")){cityManager.removeTile(new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));}
                            else if (parts[0].startsWith("Upgrade")){cityManager.upgradeTile(new Coordinate(Integer.parseInt(parts[1]), Integer.parseInt(parts[2])));}
                            else if (parts[0].startsWith("Next")){cityManager.nextTurn();}
                            else if (parts[0].startsWith("Add")) {cityManager.addTile(parts[1], new Coordinate(Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));}
                        });

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        List<Tile> tiles = cityManager.getTiles();

        List<Tile> top3 = tiles.stream()
                .sorted(Comparator.comparingInt(Tile::getScore)).limit(3).toList().reversed();
        for (int i = 0; i < 3; i++) {
            System.out.println(top3.get(i).getDescription());
        }
        int population = tiles.stream()
                .filter(t -> t.getSymbol() == 'H')
                .mapToInt(t->Integer.parseInt(t.getDescription().split(" ")[5])).sum();
        System.out.println("populace:" + population);

        int factori = Math.toIntExact(tiles.stream()
                .filter(t -> t.getSymbol() == 'F')
                .count());
        System.out.println("energie: " + (factori*100) / tiles.toArray().length + " %");

        System.out.println("obsazeni: " + tiles.toArray().length + " / " + cityMap.getSize()*cityMap.getSize());
        //mapy

        Map<Character, Long> types= tiles.stream().collect(Collectors.groupingBy(t -> t.getSymbol(), Collectors.counting()));
        for(Character key : types.keySet()){
            System.out.println(key + " : " +types.get(key));
        }try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("report.txt")))){
        out.println("MINICITY REPORT");
        out.println(types);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
