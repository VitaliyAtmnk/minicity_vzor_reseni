package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.logic.Statistics;
import city.tiles.Tile;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Konzolová aplikace umožňující uživateli interagovat s městem pomocí příkazů.
 */
public class ConsoleApplication {
    private static final int DEFAULT_MAP_SIZE = 5;
    private static final int DEFAULT_BUDGET = 1_000;

    private final Scanner scanner;
    private final CityManager cityManager;
    private boolean running;

    /**
     * Vytvoří konzolovou aplikaci.
     *
     * @param scanner zdroj uživatelského vstupu
     * @param cityManager správce města
     */
    public ConsoleApplication(Scanner scanner, CityManager cityManager) {
        this.scanner = scanner;
        this.cityManager = cityManager;
        this.running = true;
    }

    /**
     * Vytvoří výchozí objekty aplikace a spustí hlavní cyklus.
     *
     * @param args argumenty příkazové řádky se nepoužívají
     */
    public static void main(String[] args) {

        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);

        String path = "inputs";

        List<String> commands = new ArrayList<>();
        List<File> validFiles = nactiSoubory(path);

        for (File file : validFiles) {
            try {
                commands.addAll(Files.readAllLines(file.toPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        ConsoleApplication app =
                new ConsoleApplication(new Scanner(System.in), cityManager);

        for (String command : commands) {
            app.processCommand(command);
        }
        System.out.println(commands);

        List<String> export = new ArrayList<>();
        export.add("MINICITY REPORT");
        Statistics statistics = new Statistics(cityManager);

        export.add("\nTOP 3 Tiles: \n");

        for (Tile tile : statistics.getTopThreeTiles()) {
            export.add(tile.getDescription());
        }


        export.add("\nProducing tiles: \n");
        export.add("Producing tiles: "
                + statistics.getProducingPercentage() + "%");


        export.add("\nTotal population: \n");
        export.add(String.valueOf(statistics.getTotalPopulation()));
        export.add("\nOccupancy: \n");
        export.add(statistics.getOccupancyPercentage() + "%");

        export.add("\nTiles by type:\n");
        statistics.getTileCountByType()
                .forEach((type, count) ->
                        export.add(type + ": " + count));

        System.out.println(export);


            try {
                FileWriter writer = new FileWriter("./output/report.txt");
                for(String str: export) {
                    writer.write(str + System.lineSeparator());
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("Chyba při zapisování: " + e.getMessage());
            }

        List<String> csv = new ArrayList<>();

        csv.add("x;y;type;symbol;score;distanceFromCenter;createdAt");

        List<Tile> tiles = cityManager.getTiles();

        for (Tile tile : tiles) {

            int distance = tile.getCoordinate()
                    .distanceTo(cityMap.getCenter());

            csv.add(
                    tile.getCoordinate().getX() + ";" +
                            tile.getCoordinate().getY() + ";" +
                            tile.getClass().getSimpleName() + ";" +
                            tile.getSymbol() + ";" +
                            tile.getScore() + ";" +
                            distance + ";" +
                            tile.getCreatedAt()
            );
        }

        try {
            FileWriter writer = new FileWriter("./output/tiles.csv");

            for (String line : csv) {
                writer.write(line + System.lineSeparator());
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Chyba při zapisování CSV: "
                    + e.getMessage());
        }





    }

    public static List<File> nactiSoubory(String path) {
        File dir = new File(path);
        File[] soubory = dir.listFiles();

        List<File> vysledek = new ArrayList<>();

        if (soubory == null) {
            return vysledek;
        }

        for (File f : soubory) {
            if (f.isFile()
                    && f.getName().startsWith("city_")
                    && f.getName().endsWith(".txt")
                    && f.length() <= 1024) {

                vysledek.add(f);
            }
        }

        return vysledek;
    }

    /**
     * Načítá a zpracovává příkazy, dokud uživatel aplikaci neukončí.
     */
    public void run() {
        while (running && scanner.hasNextLine()) {
            String command = scanner.nextLine();
            try {
                processCommand(command);
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

    /**
     * Zpracuje jeden příkaz uživatele. Očekává správný počet a typ argumentů za platným příkazem.
     *
     * @param command text příkazu
     */
    public void processCommand(String command) {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            return;
        }

        String[] parts = trimmedCommand.split(" ");
        switch (parts[0].toLowerCase()) {
            case "add" -> addTile(parts);
            case "remove" -> removeTile(parts);
            case "info" -> printInfo(parts);
            case "upgrade" -> upgradeTile(parts);
            case "next" -> cityManager.nextTurn();
            case "show" -> cityManager.printMap();
            case "budget" -> System.out.println(cityManager.getBudget());
            case "exit" -> {
                System.out.println("Score: " + cityManager.getTotalScore());
                running = false;
            }
            default -> System.out.println("Unknown command: " + parts[0]);
        }
    }

    private void addTile(String[] parts) {
        cityManager.addTile(parts[1], coordinate(parts[2], parts[3]));
    }

    private void removeTile(String[] parts) {
        cityManager.removeTile(coordinate(parts[1], parts[2]));
    }

    private void printInfo(String[] parts) {
        Tile tile = cityManager.getCityMap().getTile(coordinate(parts[1], parts[2]));
        System.out.println(tile == null ? "Empty tile" : tile.getDescription());
    }

    private void upgradeTile(String[] parts) {
        cityManager.upgradeTile(coordinate(parts[1], parts[2]));
    }

    private Coordinate coordinate(String x, String y) {
        return new Coordinate(Integer.parseInt(x), Integer.parseInt(y));
    }
}