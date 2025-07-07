package kz.goldstar;

import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    //List of commands
    public static final String newFileOutput = "-o";
    public static final String createFilePrefix = "-p";
    public static final String rewrite = "-a";
    public static final String shortStatistic = "-s";
    public static final String fullStatictic = "-f";
    public static final String [] toCheck = {newFileOutput,createFilePrefix,rewrite,shortStatistic,fullStatictic};
    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("Аргументы командной строки не переданы");
        }else {
            List<Sorter> sorterList = new ArrayList<>();
            boolean commandOWasCalled = false;
            boolean commandPWasCalled= false;
            String newPathToOutput = null;
            String prefix = null;
            boolean commandRewrite = false;
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
                                    if(!file.isDirectory()){
                                        throw new IllegalArgumentException("Не верно указан путь к директории в команде -O");
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
                            if(!commandRewrite){
                                commandRewrite = true;
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
                        case fullStatictic:
                            if(!commandShortStatistic && !commandFullStatistic){
                                commandFullStatistic = true;
                                System.out.println("Выбран короткий вид статистики");
                            }else throw new IllegalArgumentException("Команду показа статистики можно вызвать лишь раз и это " +
                                    "либо " + commandFullStatistic + " или " +commandShortStatistic);
                    }
                }
            }
        }

    }
    public static boolean checkPrefixSymbols(String prefix){
        return prefix.matches(".*[\\\\/:*?\"<>|].*");
    }
    public static boolean writeToFile(){

    }
}