package org.example;

import java.io.*;
import java.util.*;

public class SlangDictionary {
    public static HashMap<String, List<String>> dictionary = new HashMap<>();
    public static List<String> history = new ArrayList<>();
    public void getSlangData(){
        try{
            FileReader fr = new FileReader("slangUpdated.txt");
            BufferedReader br = new BufferedReader(fr);
            String prevKey = "";
            String buffer;

            while((buffer = br.readLine()) != null) {
                if (buffer.contains("`")) {
                    String[] str = buffer.split("`");
                    String[] def = str[1].split("\\|");
                    for(int i = 0; i < def.length; i++){
                        def[i] = def[i].stripLeading();
                    }
                    List<String> defs = Arrays.asList(def);
                    dictionary.put(str[0], defs);
                    prevKey = str[0];
                }
                else{
                    List<String> defs = new ArrayList<>(dictionary.get(prevKey));
                    defs.add(buffer);
                    dictionary.replace(prevKey, defs);
                }
            }

            br.close();
            fr.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
        }
    }
    public List<String> searchByWord(String word, int searchType){
        List<String> def = dictionary.get(word);
        if(def != null && searchType == 1){
            history.add(word);
        }
        return def;
    }
    public List<String> searchByDefinition(String def, int searchType){
        List<String> words = new ArrayList<>();
        boolean foundWord = false;

        for(String word : dictionary.keySet()){
            if(dictionary.get(word).contains(def)){
                words.add(word);
                foundWord = true;
            }
        }

        if(foundWord && searchType == 1){
            history.add(def);
        }
        return words;
    }
    public List<String> getHistory(){
        return history;
    }
    public void saveSlangDictionary(){
        try {
            File file = new File( "slangUpdated.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            return;
        }

        try {
            FileWriter fw = new FileWriter("slangUpdated.txt");
            String slangInfo = "";

            for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
                String key = entry.getKey();
                slangInfo += key + "`";

                List<String> value = new ArrayList<>(entry.getValue());
                for (String def : value) {
                    slangInfo += def + "|";
                }

                fw.write(slangInfo.substring(0, slangInfo.length() - 1));
                fw.write("\n");
                slangInfo = "";
            }
            fw.close();
            System.out.println("SAVED.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }
    public void addSlang(String word, List<String> def, int addType){
        if(addType == 0){
            dictionary.put(word, def);
        }
        else if(addType == 1 || addType == 2){
            dictionary.replace(word, def);
        }
        saveSlangDictionary();
    }
    public void editSlang(String word, String def, List<String> defs, int editDefIndex){
        defs.set(editDefIndex, def);
        dictionary.replace(word, defs);
        saveSlangDictionary();
    }
    public void deleteSlang(String word){
        dictionary.remove(word);
        saveSlangDictionary();
    }

    public void resetSlangData(){
        try{
            FileReader fr = new FileReader("slang.txt");
            BufferedReader br = new BufferedReader(fr);
            String prevKey = "";
            String buffer;

            dictionary.clear();
            history.clear();

            while((buffer = br.readLine()) != null) {
                if (buffer.contains("`")) {
                    String[] str = buffer.split("`");
                    String[] def = str[1].split("\\|");
                    for(int i = 0; i < def.length; i++){
                        def[i] = def[i].stripLeading();
                    }
                    List<String> defs = Arrays.asList(def);
                    dictionary.put(str[0], defs);
                    prevKey = str[0];
                }
                else{
                    List<String> defs = new ArrayList<>(dictionary.get(prevKey));
                    defs.add(buffer);
                    dictionary.replace(prevKey, defs);
                }
            }

            saveSlangDictionary();
            br.close();
            fr.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
        }
    }

    public String randomSlang(){
        Object[] slangs = dictionary.keySet().toArray();
        Object randomSlang = slangs[new Random().nextInt(slangs.length)];
        return (String) randomSlang;
    }

    public List<String> slangQuiz(){
        List<String> slangQuiz = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            String randomSlang = randomSlang();
            if(slangQuiz.contains(randomSlang)){
                i--;
                continue;
            }
            slangQuiz.add(randomSlang);
        }
        return slangQuiz;
    }

    public List<String> definitionQuiz(){
        List<String> defQuiz = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            String randomSlang = randomSlang();
            if(defQuiz.contains(randomSlang)){
                i--;
                continue;
            }
            defQuiz.add(randomSlang);
        }
        return defQuiz;
    }
}
