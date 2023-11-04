package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SlangDictionary {
    public void getSlangData(){
        try{
            FileReader fr = new FileReader("slang.txt");
            BufferedReader br = new BufferedReader(fr);
            String buffer;

            while ((buffer = br.readLine()) != null) {
                System.out.println(buffer);
            }

//            while((buffer=br.readLine())!=null) {
//                if (buffer.contains("`")) {
//                    String[] str = buffer.split("`");
//                    String[] temp = str[1].split("\\|");
//                }
//            }

            br.close();
            fr.close();
        }
        catch (IOException e){
            System.out.println("An error occurred.");
            return;
        }
    }
}
