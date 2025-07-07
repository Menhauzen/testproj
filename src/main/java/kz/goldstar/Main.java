package kz.goldstar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                   sorter.printResults();
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
                                    System.out.println(prefix);
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
                                System.out.println("Выбран короткий вид статистики");
                            }else throw new IllegalArgumentException("Команду показа статистики можно вызвать лишь раз и это " +
                                    "либо " + commandFullStatistic + " или " +commandShortStatistic);
                            break;
                        default:
                            System.out.println("Команда "+ args[i]+ " не найдена ");
                    }
                }
            }
            writeToFile(commandOWasCalled,commandPWasCalled,commandRewriteWasCalled,newPathToOutput,prefix,sorterList);
        }


    }
    public static boolean checkPrefixSymbols(String prefix){
        return prefix.matches(".*[\\\\/:*?\"<>|].*");
    }
    public static boolean writeToFile(boolean commandOWasCalled, boolean commandPWasCalled,boolean commandReWrite ,String newPathToOutput,String prefix,List<Sorter> sorterList){
        String outputDirectory = System.getProperty("src/main/resources/filesForTest");
        String prefixToWrite = "";
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
                       File file = new File(outputDirectory,prefixToWrite+"strings.txt");
                        fillingData(commandReWrite,sorter,file,Type.String);
                    }
                    if(!sorter.getDoubles().isEmpty()){
                        File file = new File(outputDirectory,prefixToWrite+"floats.txt");
                        fillingData(commandReWrite,sorter,file,Type.Float);
                    }
                    if(!sorter.getLongs().isEmpty()){
                        File file = new File(outputDirectory,prefixToWrite+"longs.txt");
                        fillingData(commandReWrite,sorter,file,Type.Long);
                    }
                }
            }

return true;
    }
    private static void fillingData(boolean commandReWrite,Sorter sorter,File file,Type type){
        try(FileWriter fw = new FileWriter(file)) {
            if(type.equals(Type.String)){
                for(String s : sorter.getStrings()) {
                    if(commandReWrite) {
                        fw.append(s).append("\r\n");
                    }else fw.write(s + "\r\n");
                }
            }
            if(type.equals(Type.Long)) {
                for (Long s : sorter.getLongs()) {
                    if (commandReWrite) {
                        fw.append(String.valueOf(s)).append("\r\n");
                    } else fw.write(s + "\r\n");
                }
            }
            if(type.equals(Type.Float)) {
                for (Double s : sorter.getDoubles()) {
                    if (commandReWrite) {
                        fw.append(String.valueOf(s)).append("\r\n");
                    } else fw.write(s + "\r\n");
                }

            }
        } catch (IOException e) {
            System.out.println("Не возможно записать в файл " + e.getMessage());
        }
    }
}