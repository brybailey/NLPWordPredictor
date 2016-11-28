//Ranks Bigrams give word

import java.util.*;
import java.io.*;

public class RankBigrams{
    int number_of_docs =0;
    HashMap<String, HashMap<String,Integer>> index = new HashMap<String,HashMap<String,Integer>>();
    
    public void readModel( String filename ) {
        try {
            if ( filename == null )
                throw new FileNotFoundException();
            BufferedReader in = new BufferedReader( new FileReader( filename ));
            Scanner scan = new Scanner( in );
            
            while(scan.hasNext()){
                int prob= scan.nextInt();
                //break if the nextInt is -1, end of the file

                String first = scan.next();
                String second = scan.next();
                
                HashMap<String,Integer> currentMap = index.get(first);
                
                
               
                if(currentMap==null){ //New inner hashmap
                    HashMap<String,Integer> newMap = new HashMap<String,Integer>();
                    newMap.put(second,prob);
                    index.put(first, newMap);
                } else {//add to existing hashmap
                    currentMap.put(second,prob);
                }
            
            }
            
        }
        catch ( FileNotFoundException e ) {
            System.err.println( "Couldn't find bigram probabilities file " + filename );
            
        }
    }
    
    public String[] predict( String query){
        
        Map<String,Integer> map = index.get(query);
        
            PriorityQueue<Map.Entry<String,Integer>> pq =
            new PriorityQueue<Map.Entry<String,Integer>>(map.size(), new Comparator<Map.Entry<String, Integer>>(){
                
                public int compare(Map.Entry<String, Integer> arg0,
                                   Map.Entry<String, Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue());
                }
            });
            
            
            pq.addAll(map.entrySet());
            String[] result = new String[5];
            for(int x=0;x<5;x++){
                Map.Entry entry = pq.poll();
                
                result[x]=(String)entry.getKey();
            }
            
            
        
    return result;
    }
    
    public static void main( String[] args ){
        RankBigrams test = new RankBigrams();
        String file = args[0];
        test.readModel(file);
        System.out.println(Arrays.toString(test.predict("left")));
    
    }
}