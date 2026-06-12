package city;

import city.logic.CityManager;
import city.logic.CityMap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CityLoader {
    CityManager cityManager;
    CommandProcessor commandProcessor;
    CityMap cityMap;

    public CityLoader(CityManager cityManager) {
        this.cityManager = cityManager;
        this.cityMap = cityManager.getCityMap();
        commandProcessor = new CommandProcessor(cityManager);
    }

    public CityManager load(String path) {
        try {
            Files.lines(Paths.get(path))
                .forEach(line -> commandProcessor.processCommand(line));
        } catch (IOException e) {
            System.out.println("Chyba v souboru "+path+" "+e.getMessage());
        }
        return cityManager;
    }
}
