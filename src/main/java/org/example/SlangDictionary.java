package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SlangDictionary {
    public HashMap<String, List<String>> dictionary = new HashMap<>();
    public List<String> history = new ArrayList<>();
    public void getSlangData(){
        try{
            FileReader fr = new FileReader("slang.txt");
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

    }
    public void addSlang(String word, String [] def){

    }
    public void editSlang(String word){

    }
}
