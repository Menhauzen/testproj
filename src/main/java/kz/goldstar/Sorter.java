package kz.goldstar;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//Этот класс сортирует и считает статистику
public class Sorter {
    private String filePath;
    private double doubleAverage;
    private long longAverage;
    private long min;
    private long max;
    private int countDigits;
    private int countStringLines;
    private int minLineLength;
    private int maxLineLength;
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
        }catch (FileNotFoundException f){
            System.out.println("Файла по пути: " + path + " не существует" + f.getMessage());
        } catch (IOException e){
            System.out.println("Ошибка при чтении файла: " + path + e.getMessage());
        }
    }
    public  boolean isLong(String str){
        try {
            Long.parseLong(str);
            return true;
        }catch (NumberFormatException ex){
            return false;
        }
    }
    public  boolean isFloat(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    public String fullStatistic(){
        return null;
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
            String resultStrings = getStrings().stream().collect(Collectors.joining(", "));
            System.out.println("String results: " + resultStrings);
        }
    }

    public static void main(String[] args) {
        Sorter sorter = new Sorter();
        sorter.readFile("src/main/resources/filesForTest/in1.txt");
        System.out.println();
        String resultDoubles = sorter.getDoubles().stream().map(String::valueOf).collect(Collectors.joining(", "));
        System.out.println("Float results: " + resultDoubles);
        String resultIntegers = sorter.getLongs().stream().map(String::valueOf).collect(Collectors.joining(", "));
        System.out.println("Long results: " + resultIntegers);
        String resultStrings = sorter.getStrings().stream().collect(Collectors.joining(", "));
        System.out.println("String results: " + resultStrings);
    }

}
