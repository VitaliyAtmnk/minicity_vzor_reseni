package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    
    public static List<Command> loadInput(String path){
        List<Command> toReturn = new ArrayList<>();
        try {
            List<String[]> help = Files.lines(Path.of(path))
                    .map(line -> line.split(" "))
                    .toList();

            for (String[] t : help){
                if (t.length == 1) toReturn.add(new Command(t[0]));
                if (t.length == 3) toReturn.add( new Command(t[0], new Coordinate(Integer.parseInt(t[1]), Integer.parseInt(t[2]))));
                if (t.length == 4) toReturn.add(new Command(t[0], t[1], new Coordinate(Integer.parseInt(t[2]), Integer.parseInt(t[3]))));
            }
        } catch (IOException e) {
            System.out.println("Chyba při načítání dat" + e);
        }
        return toReturn;
    }

    public static void executeCmd(List<Command> cmd, CityManager cityManager){
       for (Command c : cmd){
           String cmdType = c.getCmd();

           switch (cmdType){
               case "Add" -> cityManager.addTile(c.getType(), c.getCoordinate());
               case "Upgrade" -> cityManager.upgradeTile(c.getCoordinate());
               case "Next" -> cityManager.nextTurn();
               case "Remove" -> cityManager.removeTile(c.getCoordinate());
               default -> System.out.println("Neplatný příkaz");
           }
       }

    }

    public static void threeBest(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        List<Tile> sorted = tiles.stream()
                .sorted(Comparator.comparingInt(Tile::getScore))
                .toList();
        System.out.println(sorted);

        System.out.println("Tři nejlepší Tile:");
        for (int i = 0; i < 3; i++) {
            System.out.println(sorted.get(i));
        }
    }

    public static void producing(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        int produce = 0;
        int all = 0;
        for (Tile tile : tiles){
            all += 1;
            if (tile instanceof Producing){
                produce += 1;
            }
        }

        double precent = ((double) produce /all) * 100;
        System.out.println(precent + "% " + " produkují energii");

    }

    public static int population(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        int sum = tiles.stream()
                .filter(tile -> tile instanceof HouseTile)
                .mapToInt(tile -> ((HouseTile) tile).getPopulation())
                .sum();
        return sum;
    }

    public static Map<String, Long> sumTypes(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        Map<String, Long> byType = tiles.stream()
                .collect(Collectors.groupingBy(t -> {
                    if(t instanceof HouseTile) return "House";
                    if (t instanceof RoadTile) return "Road";
                    if (t instanceof FactoryTile) return "Factory";
                    return "Park";
                }, Collectors.counting()));

        return byType;
    }

    public static Map<String, Long> priceByType(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        Map<String, Long> byType = tiles.stream()
                .collect(Collectors.groupingBy(t -> {
                    if(t instanceof HouseTile) return "House";
                    if (t instanceof RoadTile) return "Road";
                    if (t instanceof FactoryTile) return "Factory";
                    return "Park";
                }, Collectors.summingLong(Tile::getPrice)));

        return byType;
    }

    public static Map<String, Double> scoreByType(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        Map<String, Double> byType = tiles.stream()
                .filter(tile -> tile instanceof Upgradable)
                .collect(Collectors.groupingBy(t -> {
                    if(t instanceof HouseTile) return "House";
                    if (t instanceof RoadTile) return "Road";
                    return "Park";
                }, Collectors.averagingDouble(Tile::getScore)));

        return byType;

    }

    public static Map<String, Integer> howMuchCanIBuild(CityManager cityManager){
        List<Tile> tiles = cityManager.getTiles();
        Map<String, Integer> byType = tiles.stream()
                .collect(Collectors.groupingBy(t -> {
                    if(t instanceof HouseTile) return "House";
                    if (t instanceof RoadTile) return "Road";
                    if (t instanceof FactoryTile) return "Factory";
                    return "Park";
                }, Collectors.summingInt(t -> {
                    if (t.getPrice() < cityManager.getBudget()) return 1;
                    return 0;
                })));

        return byType;
    }
    
    public static void main(String[] args) {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);
        
        executeCmd(loadInput("inputs/city_center.txt"), cityManager);
        executeCmd(loadInput("inputs/city_industry.txt"), cityManager);
        executeCmd(loadInput("inputs/city_residential.txt"), cityManager);

        threeBest(cityManager);
        producing(cityManager);
        System.out.println(population(cityManager));
        System.out.println(sumTypes(cityManager));

        try (PrintWriter pr = new PrintWriter(new BufferedWriter(new FileWriter(new File("report.txt"))));){
            pr.println("Map size: ");
            //nestíhám více bohužel :(

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class Command{
    String cmd;
    String type;
    Coordinate coordinate;

    public Command(String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return "Command{" +
                "cmd='" + cmd + '\'' +
                ", type='" + type + '\'' +
                ", coordinate=" + coordinate +
                '}';
    }

    public Command(String cmd, String type, Coordinate coordinate) {
        this.cmd = cmd;
        this.type = type;
        this.coordinate = coordinate;
    }

    public Command(String cmd, Coordinate coordinate) {
        this.cmd = cmd;
        this.coordinate = coordinate;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
