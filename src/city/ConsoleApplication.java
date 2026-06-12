package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.nio.file.Files;
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
        try (Scanner scanner = new Scanner(System.in)) {
            CityManager cityManager = new CityManager(new CityMap(DEFAULT_MAP_SIZE), DEFAULT_BUDGET);
            new ConsoleApplication(scanner, cityManager).run();
        }
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
            case "add" ->       addTile(parts);
            case "remove" ->    removeTile(parts);
            case "info" ->      printInfo(parts);
            case "upgrade" ->   upgradeTile(parts);
            case "next" ->      cityManager.nextTurn();
            case "show" ->      cityManager.printMap();
            case "budget" ->    System.out.println(cityManager.getBudget());
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
