package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.tiles.Tile;

import java.util.Scanner;

/**
 * Konzolová aplikace umožňující uživateli interagovat s městem pomocí příkazů.
 */
public class ConsoleApplication {
    private static final int DEFAULT_MAP_SIZE = 5;
    private static final int DEFAULT_BUDGET = 1_000;

    private final Scanner scanner;
    private final CityManager cityManager;
    private final CommandProcessor processCommand;
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
        processCommand = new CommandProcessor(cityManager);
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
                running = processCommand.processCommand(command);
            } catch (RuntimeException exception) {
                System.out.println("Error: " + exception.getMessage());
            }
        }
    }

}
