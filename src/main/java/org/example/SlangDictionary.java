package org.example;

import java.io.*;
import java.util.*;

//This class is used to create a dictionary.
public class SlangDictionary {
    public static HashMap<String, List<String>> dictionary = new HashMap<>();
    public static List<String> history = new ArrayList<>();

    //Get the data for the dictionary (words, definitions).
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

    //Search by word method.
    //searchType == 1: for Search by word function, the word searched will be added to the history.
    //searchType == 0: just search, not add to the history.
    public List<String> searchByWord(String word, int searchType){
        List<String> def = dictionary.get(word);
        if(def != null && searchType == 1){
            history.add(word);
        }
        return def;
    }

    //Search by definition method.
    //searchType == 1: for Search by definition function, the definition searched will be added to the history.
    //searchType == 0: just search, not add to the history.
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

    //Get history method.
    public List<String> getHistory(){
        return history;
    }

    //Save the dictionary to slangUpdated text file.
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
            String slangInfo;

            for (Map.Entry<String, List<String>> entry : dictionary.entrySet()) {
                String key = entry.getKey();
                StringBuilder sb = new StringBuilder();

                slangInfo = sb.append(key).append("`").toString();
                List<String> value = new ArrayList<>(entry.getValue());
                for (String def : value) {
                    slangInfo = sb.append(def).append("|").toString();
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

    //Add a slang method.
    //addType == 0: Add a new slang.
    //addType == 1: Overwrite a slang.
    //addType == 2: Duplicate a slang.
    public void addSlang(String word, List<String> def, int addType){
        if(addType == 0){
            dictionary.put(word, def);
        }
        else if(addType == 1 || addType == 2){
            dictionary.replace(word, def);
        }
        saveSlangDictionary();
    }

    //Edit a slang method.
    //def: New definition.
    //defs: List of old definitions.
    //editDefIndex: The definition of defs at this index will be changed to def.
    public void editSlang(String word, String def, List<String> defs, int editDefIndex){
        defs.set(editDefIndex, def);
        dictionary.replace(word, defs);
        saveSlangDictionary();
    }

    //Delete a slang method.
    public void deleteSlang(String word){
        dictionary.remove(word);
        saveSlangDictionary();
    }

    //Reset the dictionary method.
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

    //Generate a random slang method.
    public String randomSlang(){
        Object[] slangs = dictionary.keySet().toArray();
        Object randomSlang = slangs[new Random().nextInt(slangs.length)];
        return (String) randomSlang;
    }

    //Create slang quiz method, this method will return a list of 4 random slang words (with no duplication).
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
}
