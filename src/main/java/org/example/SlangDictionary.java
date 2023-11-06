package org.example;

import java.io.*;
import java.util.*;

public class SlangDictionary {
    public HashMap<String, List<String>> dictionary = new HashMap<>();
    public List<String> history = new ArrayList<>();
    public void getSlangData(){
        try{
            FileReader fr = new FileReader("slangUpdated.txt");
            BufferedReader br = new BufferedReader(fr);
            String buffer;

            while((buffer = br.readLine()) != null) {
                if (buffer.contains("`")) {
                    String[] str = buffer.split("`");
                    String[] def = str[1].split("\\|");
                    for(int i = 0; i < def.length; i++){
                        def[i] = def[i].stripLeading();
                    }
                    List<String> temp = Arrays.asList(def);
                    dictionary.put(str[0], temp);
                }
            }

            br.close();
            fr.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
        }
    }
    public List<String> searchByWord(String word){
        List<String> def = dictionary.get(word);
        history.add(word);
        return def;
    }
    public List<String> searchByDefinition(String def){
        List<String> words = new ArrayList<>();
        history.add(def);
        for(String word: dictionary.keySet()){
            if(dictionary.get(word).contains(def)){
                words.add(word);
            }
        }
        return words;
    }
    public List<String> getHistory(){
        return this.history;
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
    public void addSlang(String word, List<String> def){
        if(searchByWord(word) != null){
            System.out.println("Add another def or override");
        }
        else{
            dictionary.put(word, def);
            saveSlangDictionary();
        }
    }
    public void editSlang(String word){
        System.out.println("Edit");
    }
    public void deleteSlang(String word){
        if(searchByWord(word) == null){
            System.out.println("Word not found");
        }
        else{
            dictionary.remove(word);
            saveSlangDictionary();
            System.out.println("Deleted");
        }
    }
}
