import java.util.*;
import java.io.*;

public class RetrieveTrigrams {
    

    private static final int MAX_PROBS = 10000000;
    private HashMap<String, HashMap<String, HashMap<String, Double>>> trigrams = new HashMap<String, HashMap<String, HashMap<String, Double>>> (MAX_PROBS);
    private Vector<Double> triProbs = new Vector<Double>(MAX_PROBS);
    private int totalGrams;

    private void makeTrigrams( String filename ) {
	try {
	    InputStreamReader read = new InputStreamReader( new FileInputStream( filename ) );
	    Scanner in = new Scanner( read );
	    String nextLine;
	    String firstWord;
	    String secondWord;
	    String thirdWord;
	    double prob;
	    totalGrams = 0;
	    HashMap<String, HashMap<String, Double>> interior;
	    HashMap<String, Double> mostInterior;
	    StringTokenizer tokenize;

	    while( in.hasNext() ) {
		nextLine = in.nextLine();
		tokenize = new StringTokenizer( nextLine );
		prob =  Math.log( Double.parseDouble( tokenize.nextToken() ) );
		triProbs.add(prob);
		firstWord = tokenize.nextToken();
		secondWord = tokenize.nextToken();
		thirdWord = tokenize.nextToken();
		interior = new HashMap<String, HashMap<String,Double>>();
		mostInterior = new HashMap<String, Double>();
		mostInterior.put( thirdWord, prob );
		interior.put( secondWord, mostInterior);
		trigrams.put(firstWord, interior );
		totalGrams++;
	    }
	} catch (Exception e ) {
	    e.printStackTrace();
	}
	
    }

    /*    private String parseWord( String line) {
	start = end;
	end++;
	String spaces = line.substring( start, end );
	while( spaces.substring(start, end).equals(" " ) ){
	    start = end;
	    end++;
	}
	end = line.indexOf( " ", start );
	return line.substring( start, end );
	} */

    private Vector<Double> getProbs() {
	return triProbs;
    }

    private int howManyGrams(){
	return totalGrams;
    }

    private HashMap<String, HashMap<String, HashMap<String, Double>>> returnGrams() {
	return trigrams;
    }

    private RetrieveTrigrams( String input ) {
	makeTrigrams( input );
    }

    public static void main( String[] args ) {
	RetrieveTrigrams grams = new RetrieveTrigrams( "w3_.txt" );
	System.out.println( grams.howManyGrams() );
	Vector<Double> probs = grams.getProbs();
	HashMap<String, HashMap<String, HashMap<String,Double>>> trigrams = grams.returnGrams();
	for( String word: trigrams.keySet() ) {
	    HashMap<String, HashMap<String, Double>> inside =  trigrams.get(word);
	    for( String word2: inside.keySet() ) {
		HashMap<String,Double> doubleInside = inside.get(word2);
		for( String word3: doubleInside.keySet() ) {
		    System.out.println( word + " " + word2 + " " + " " + doubleInside.get( word3 ) );
		}
	    }
	}
    }
}
