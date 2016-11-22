/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.util.*;

public interface Index {

    /* Index types */
    public static final int PERSISTENT_HASHED_INDEX = 1;
    public static final int HASHED_INDEX = 0;

    /* Query types */
    public static final int INTERSECTION_QUERY = 0;
    public static final int PHRASE_QUERY = 1;
    public static final int RANKED_QUERY = 2;
	
    /* Ranking types */
    public static final int TF_IDF = 0; 
    public static final int PAGERANK = 1; 
    public static final int COMBINATION = 2; 

    public HashMap<String, String> docIDs = new HashMap<String,String>();
    public HashMap<String,Integer> docLengths = new HashMap<String,Integer>();

    public void insert( String token, int docID, int offset );
    public Iterator<String> getDictionary();
    public PostingsList getPostings( String token );
    public PostingsList search( ArrayList<String> query, int queryType, int rankingType );

}
		    
