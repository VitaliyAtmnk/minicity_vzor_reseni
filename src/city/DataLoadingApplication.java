package city;

import city.logic.CityManager;
import city.logic.CityMap;
import city.outputing.StatisticWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataLoadingApplication {

    public static CityLoader cityLoader;
    public static CityManager cityManager;
    public static StatisticWriter statisticWriter;
    public static final String basePath = "inputs/";
    public static void main(String[] args) {
        cityManager = new CityManager(new CityMap(5), 5000);
        cityLoader = new CityLoader(cityManager);
        statisticWriter = new StatisticWriter(cityManager);

        try {
            Files.walk(Paths.get(basePath))
                .filter(Files::isRegularFile)
                .filter(f->!Files.isDirectory(f))
                .filter(f-> f.toFile().length()<1_048_576)
                .filter(path->path.getFileName().toString().endsWith(".txt"))
                .filter(path->path.getFileName().toString().startsWith("city_"))
                .map(f->f.toFile().getPath())
                .forEach(path->cityLoader.load(path));
        } catch (IOException e) {
            System.out.println("Chyba při procházení "+basePath);
        }

        cityManager.printMap();

        statisticWriter.generateMiniCityReportFile();

        statisticWriter.generateTilesFile();
    }

}
