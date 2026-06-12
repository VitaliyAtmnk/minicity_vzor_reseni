package src.city;

import src.city.logic.CityManager;
import src.city.logic.CityMap;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Idk {
        CityMap cityMap = new CityMap(5);
        CityManager cityManager = new CityManager(cityMap, 5000);

    public Idk() throws FileNotFoundException, UnsupportedEncodingException {
    }


    public void filterFiles(){
        ArrayList<File> files;
        File input = new File("inputs");

        FileFilter filter = file -> {
            boolean isJavaFile = file.getName().endsWith(".txt");
            boolean isJavaFile1 = file.getName().startsWith("city_");
            boolean isLargeEnough = file.length() > 1024;
            return isJavaFile && isJavaFile1 && isLargeEnough ;
        };

        File[] filteredFiles = input.listFiles(filter);

    }
    public void parseLine(String[] tokens) {

        return new nevim(tokens[0],
                tokens[1],
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]);
    }


    public void prikaz(String slovo){
        try {
            List<nevim> shimpents = Files.lines(Path.of(filterFiles())
                    .map(line -> line.trim().split(" "))
                    .map(Idk::parseLine)
                    .toList();
        }catch (
            IOException e) {
                throw new RuntimeException(e);
            }

        if (slovo.equals("Add")){
            cityManager.addTile(tokens[1], new Coordinate(tokens[2], tokens[3]));
            return;
        }

        if (slovo.equals("Remove")){
            cityManager.removeTile(tokens[2], tokens[3]);
            return;
        }

        if (slovo.equals("Upgrade")){
            cityManager.upgradeTile(tokens[2], tokens[3]);
            cityManager.nextTurn();
            return;
        }
        if (slovo.equals("Next")){
            cityManager.nextTurn();
        }
    }



    PrintWriter writer = new PrintWriter("report.txt");
    writer.println("MINICITY REPORT");
    writer.println("Map size" + getCityMap.size);
    writer.println("Budget ");
    writer.println("Total score");
    writer.println("Total tiles");
    writer.println("");
    writer.println("Tiles by type:");
    writer.println("RoadTile" + );
    writer.println("ParkTile");
    writer.println("HouseTile");
    writer.println("FactoryTile");
    writer.println("");
    writer.println("Special tiles:");
    writer.println("Upgradable tiles:");
    writer.println("Producing tiles: ");
    writer.println("Total house population:");
    writer.println("");
    writer.println("Top 3 tiles by score:");
    writer.println("");
    writer.println("");
    writer.println("");
    writer.close();
}
class nevim{
    String podminka;
    String stavba;
    int x;
    int y;
}
