package city;

import city.logic.CityManager;
import city.tiles.Tile;

public class CommandProcessor {

    CityManager cityManager;
    CommandProcessor(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    /**
     * Zpracuje jeden příkaz uživatele. Očekává správný počet a typ argumentů za platným příkazem.
     *
     * @param command text příkazu
     */
    public boolean processCommand(String command) {
        String trimmedCommand = command.trim();
        if (trimmedCommand.isEmpty()) {
            return true;
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
                return false;
            }
            default -> System.out.println("Unknown command: " + parts[0]);
        }
        return true;
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
