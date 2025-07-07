package kz.goldstar;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//Этот класс сортирует
public class Sorter {
    private String filePath;
    private List<Double> doubles = new ArrayList<>();
    private List<Long> longs = new ArrayList<>();
    private List<String> strings = new ArrayList<>();
    public void readFile(String path){
        filePath = path;
            try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null){
                    if(isLong(line)){
                        longs.add(Long.parseLong(line));
                    }else if(isFloat(line)){
                        doubles.add(Double.parseDouble(line));
                    }else {
                        strings.add(line);
                    }
                }
            } catch (FileNotFoundException e){
                System.out.println(e.getMessage());
            }catch (IOException e){
                System.out.println("Ошибка при чтении файла: " + path + e.getMessage());
            }
        }

    public  boolean isLong(String str){
        if(Pattern.matches("^-?\\d+$",str)) {
            try {
                Long.parseLong(str);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }else return false;
    }
    public  boolean isFloat(String str) {
        if(str.contains(".") || str.toLowerCase().contains("e")) {
            try {
                Double.parseDouble(str);
                return true;
            } catch (NumberFormatException ex) {
                return false;
            }
        }else return false;
    }

    public List<Double> getDoubles() {
        return doubles;
    }

    public List<Long> getLongs() {
        return longs;
    }

    public List<String> getStrings() {
        return strings;
    }
    public void printResults(){
        if(!getDoubles().isEmpty()) {
            String resultDoubles = getDoubles().stream().map(String::valueOf).collect(Collectors.joining(", "));
            System.out.println("Float results: " + resultDoubles);
        }

        if(!getLongs().isEmpty()) {
            String resultIntegers = getLongs().stream().map(String::valueOf).collect(Collectors.joining(", "));
            System.out.println("Long results: " + resultIntegers);
        }
        if(!strings.isEmpty()) {
            String resultStrings = String.join(", ", getStrings());
            System.out.println("String results: " + resultStrings);
        }
    }

}
