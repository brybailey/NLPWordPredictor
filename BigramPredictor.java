/*
 *
 *
 */

import java.io.*;
import java.util.*;

public class BigramPredictor {
    private final String FILENAME = "bigram_hashmap.ser";
    HashMap<String, HashMap<String,Integer>> index;
    
    public BigramPredictor(){
        try{
            System.out.print("Initializing Bigram HashMap... ");
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            index = (HashMap) ois.readObject();
            ois.close();
            fis.close();
            System.out.println("Done.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public PriorityQueue<Map.Entry<String,Integer>> predict(String query){
        Map<String,Integer> map = index.get(query);
        
        PriorityQueue<Map.Entry<String,Integer>> pq =
        new PriorityQueue<Map.Entry<String,Integer>>(map.size(), new Comparator<Map.Entry<String, Integer>>(){
            
            public int compare(Map.Entry<String, Integer> arg0,
                               Map.Entry<String, Integer> arg1) {
                return arg1.getValue().compareTo(arg0.getValue());
            }
        });
        
        
        pq.addAll(map.entrySet());
 
    
    return pq;
    }
    public static void main(String[] args){
       
        BigramPredictor bi = new BigramPredictor();
        System.out.println(bi.index.get("the").get("dog"));
    }
}