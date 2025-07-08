package kz.goldstar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {
    //List of commands
    public static final String newFileOutput = "-o";
    public static final String createFilePrefix = "-p";
    public static final String rewrite = "-a";
    public static final String shortStatistic = "-s";
    public static final String fullStatistic = "-f";
    public static final String [] toCheck = {newFileOutput,createFilePrefix,rewrite,shortStatistic,fullStatistic};
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Аргументы командной строки не переданы");
        }else {
            List<Sorter> sorterList = new ArrayList<>();
            boolean commandOWasCalled = false;
            boolean commandPWasCalled= false;
            String newPathToOutput = null;
            String prefix = null;
            boolean commandRewriteWasCalled = false;
            boolean commandFullStatistic = false;
            boolean commandShortStatistic = false;

            for(int i = 0; i < args.length; i ++){
                if(args[i].endsWith(".txt")){
                   Sorter sorter = new Sorter();
                   sorter.readFile(args[i]);
                   sorterList.add(sorter);
                }
                if(args[i].startsWith("-") && args[i].length() == 2){
                    switch (args[i]){
                        case newFileOutput:
                            if(!commandOWasCalled) {
                                try {
                                    commandOWasCalled = true;
                                    newPathToOutput = args[i + 1];
                                    File file = new File(newPathToOutput);
                                    if(!file.exists()){
                                        System.out.println("Не верно указан путь к директории в команде -O");
                                    }
                                } catch (ArrayIndexOutOfBoundsException ar) {
                                    System.out.println("После команды -o необходимо передать путь к файлу!");
                                }
                            }
                            break;
                        case createFilePrefix:
                            if(!commandPWasCalled){
                                try {
                                    commandPWasCalled = true;
                                    for(String prefixNotCommand: toCheck){
                                        if(args[i+1].equals(prefixNotCommand)){
                                            throw  new IllegalArgumentException("После команды -p необходимо добавить префикс");
                                        }
                                    }
                                    if(!checkPrefixSymbols(args[i+1])) {
                                        prefix = args[i + 1];
                                    }else System.out.println("Вы не можете указывать в префиксе имени файла следующие символы:" +
                                            "\\\\/:*?\\\"<>|");
                                } catch (ArrayIndexOutOfBoundsException ar){
                                    System.out.println("После команды -p необходимо добавить префикс");
                                }
                            }else {
                                throw new IllegalArgumentException("Вы вызвали команду " + createFilePrefix + " несколько раз");
                            }
                            break;
                        case rewrite:
                            if(!commandRewriteWasCalled){
                                commandRewriteWasCalled = true;
                                System.out.println("Выбрана добавление записи в старые файлы");
                            }else throw new IllegalArgumentException("Вы вызвали команду " + rewrite + " несколько раз");
                            break;
                        case shortStatistic:
                            if(!commandShortStatistic && !commandFullStatistic){
                                commandShortStatistic = true;
                                System.out.println("Выбран короткий вид статистики");
                            }else throw new IllegalArgumentException("Команду показа статистики можно вызвать лишь раз и это " +
                                    "либо " + commandFullStatistic + " или " +commandShortStatistic);
                            break;
                        case fullStatistic:
                            if(!commandShortStatistic && !commandFullStatistic){
                                commandFullStatistic = true;
                                System.out.println("Выбран полный вид статистики");
                            }else throw new IllegalArgumentException("Команду показа статистики можно вызвать лишь раз и это " +
                                    "либо " + shortStatistic + " или " +fullStatistic);
                            break;
                        default:
                            System.out.println("Команда "+ args[i]+ " не найдена ");
                    }
                }
            }
            writeToFile(commandOWasCalled,commandPWasCalled,commandRewriteWasCalled,newPathToOutput,prefix,sorterList,commandShortStatistic,commandFullStatistic);
        }


    }
    public static boolean checkPrefixSymbols(String prefix){
        return prefix.matches(".*[\\\\/:*?\"<>|].*");
    }
    public static void  writeToFile(boolean commandOWasCalled, boolean commandPWasCalled,boolean commandReWrite ,String newPathToOutput,String prefix,List<Sorter> sorterList,boolean commandShortStatistic, boolean commandFullStatistic){
        String outputDirectory = System.getProperty("src/main/resources/filesForTest");
        String prefixToWrite = "";
        List<Double> mainDoubles = new ArrayList<>();
        List<String> mainStrings = new ArrayList<>();
        List<Long> mainLongs = new ArrayList<>();
        File fileStrings =null;
        File fileDoubles = null;
        File fileLongs = null;
        if(commandOWasCalled) {
            if (!newPathToOutput.isEmpty()) {
                outputDirectory = newPathToOutput;
            }
        }
            if(commandPWasCalled){
                if(!prefix.isEmpty()) {
                    prefixToWrite = prefix;
                }
            }
            if(!sorterList.isEmpty()){
                for(Sorter sorter: sorterList){
                    if(!sorter.getStrings().isEmpty()){
                        mainStrings.addAll(sorter.getStrings());
                    }
                    if(!sorter.getDoubles().isEmpty()){
                        mainDoubles.addAll(sorter.getDoubles());
                    }
                    if(!sorter.getLongs().isEmpty()){
                        mainLongs.addAll(sorter.getLongs());
                    }
                }
            }
            if(!mainStrings.isEmpty()){
                fileStrings = new File(outputDirectory,prefixToWrite+"strings.txt");
                fillingData(commandReWrite,mainStrings,fileStrings);
            }
            if(!mainDoubles.isEmpty()){
                fileDoubles = new File(outputDirectory,prefixToWrite+"floats.txt");
                fillingData(commandReWrite,mainDoubles,fileDoubles);
            }
            if(!mainLongs.isEmpty()){
                fileLongs = new File(outputDirectory,prefixToWrite+"longs.txt");
                fillingData(commandReWrite,mainLongs,fileLongs);
            }
            if(commandShortStatistic){
                if(fileStrings != null) {
                    if(!mainStrings.isEmpty()) {
                        shortStatisticView(fileStrings,mainStrings);
                    }
                }if(fileDoubles != null) {
                    if(!mainDoubles.isEmpty()) {
                        shortStatisticView(fileDoubles,mainDoubles);
                    }
                }
                if(fileLongs != null) {
                    if (!mainLongs.isEmpty()) {
                        shortStatisticView(fileLongs, mainLongs);
                    }
                }
            }
            if(commandFullStatistic){
                if(fileStrings != null) {
                    if(!mainStrings.isEmpty()) {
                        fullStatisticView(fileStrings,mainStrings);
                    }
                }if(fileDoubles != null) {
                    if(!mainDoubles.isEmpty()) {
                        fullStatisticView(fileDoubles,mainDoubles);
                    }
                }
                if(fileLongs != null) {
                    if (!mainLongs.isEmpty()) {
                        fullStatisticView(fileLongs, mainLongs);
                    }
                }
            }
    }
    private static void fillingData(boolean commandReWrite,List<?> list,File file){
        try(FileWriter fw = new FileWriter(file,commandReWrite)) {
                for(var s : list) {
                    fw.write(s + "\r\n");
                }
        } catch (IOException e) {
            System.out.println("Не возможно записать в файл " + e.getMessage());
        }
    }
    public static void shortStatisticView(File file,List<?> list){
        System.out.println("В файл " + file.getName() + " записано : " + list.size() + " строк");
    }
    public static void fullStatisticView(File file,List<?> list){
        List<Number> numbers = list.stream().filter(item-> item instanceof Number).map(item->(Number) item).toList();
        if(!numbers.isEmpty()){
            System.out.println("Отчет по цифрам Файла: " + file.getName());
            System.out.println("В файл записано : " + numbers.size() + " строк с цифрами");
            Optional<BigDecimal> min = numbers.stream().map(i -> new BigDecimal(i.toString())).min(Comparator.naturalOrder());
            Optional<BigDecimal> max = numbers.stream().map(i -> new BigDecimal(i.toString())).max(Comparator.naturalOrder());
            BigDecimal sum = numbers.stream().map(i-> new BigDecimal(i.toString())).reduce(BigDecimal.ZERO,BigDecimal::add);
            BigDecimal avg = sum.divide(BigDecimal.valueOf(numbers.size()), RoundingMode.HALF_UP);
            System.out.println("мин = " + min.get());
            System.out.println("макс = " + max.get());
            System.out.println("сумма = " + sum);
            System.out.println("среднее = " + avg);
        }
        List<String> strings = list.stream().filter(item-> item instanceof String).map(item->(String) item).toList();
        if(!strings.isEmpty()){
            System.out.println("Отчет по строкам файла: " + file.getName());
            System.out.println("В файл записано : " + strings.size() + " строк с текстом");
            int maxLength = strings.stream().max(Comparator.comparing(String::length)).stream().findFirst().orElse(null).length();
            int minLength =strings.stream().max(Comparator.comparing(String::length).reversed()).stream().findFirst().orElse(null).length();
            System.out.println("мин длина строки = " + minLength);
            System.out.println("макс длина строки = " + maxLength);
        }
    }
}