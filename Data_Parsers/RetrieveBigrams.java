import java.util.*;
import java.io.*;

public class RetrieveBigrams {
    

    private static final int MAX_PROBS = 10000000;
    private HashMap<String, HashMap<String, Double>> bigrams = new HashMap<String, HashMap<String, Double>> (MAX_PROBS);
    private Vector<Double> biProbs = new Vector<Double>(MAX_PROBS);
    private int totalGrams;

    private void makeBigrams( String filename ) {
	try {
	    InputStreamReader read = new InputStreamReader( new FileInputStream( filename ) );
	    Scanner in = new Scanner( read );
	    String nextLine;
	    String firstWord;
	    String secondWord;
	    double prob;
	    totalGrams = 0;
	    HashMap<String,Double> interior;
	    StringTokenizer tokenize;
	    while( in.hasNext() ) {
		nextLine = in.nextLine();
		tokenize = new StringTokenizer( nextLine );
		prob =  Math.log( Double.parseDouble( tokenize.nextToken() ) );
		biProbs.add(prob);
		firstWord = tokenize.nextToken();
		secondWord = tokenize.nextToken();

		interior = new HashMap<String, Double>();
		interior.put( secondWord, prob);
		bigrams.put(firstWord, interior );
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
	return biProbs;
    }

    private int howManyGrams(){
	return totalGrams;
    }

    private HashMap<String, HashMap<String,Double>> returnGrams() {
	return bigrams;
    }

    private RetrieveBigrams( String input ) {
	makeBigrams( input );
    }

    public static void main( String[] args ) {
	RetrieveBigrams grams = new RetrieveBigrams( "w2_.txt" );
	System.out.println( grams.howManyGrams() );
	Vector<Double> probs = grams.getProbs();
	HashMap<String, HashMap<String, Double>> bigrams = grams.returnGrams();
	for( String word: bigrams.keySet() ) {
	    HashMap<String, Double> inside =  bigrams.get(word);
	    for( String word2: inside.keySet() ) {
		System.out.println( word + " " + word2 + " " + inside.get( word2 ) );
	    }
	}
    }
}
