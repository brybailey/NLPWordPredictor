import java.io.*;
import java.util.*;

public class GetLetterBigrams {

    private HashMap<String,Integer> bigramsMap = new HashMap<String, Integer>();

    private int bigramCount;

    private HashMap<Character,Integer> letterMap = new HashMap<Character, Integer>();

    public GetLetterBigrams( String filename ) {
	mapLetters();
	try{
	    InputStreamReader read = new InputStreamReader( new FileInputStream( filename ) );
	    Scanner in = new Scanner( read );
	    char firstChar;
	    char secondChar;
	    bigramCount = 0;
	    StringTokenizer tokenize;
	    String word;
	    bigramCount = 0;

	    while( in.hasNext() ) {
		word = in.next();
		int i = 0;
		String bigram = "";
		for( int x = 0; x<word.length(); x++ ) {
		    char letter = word.charAt(x);

		    int count = 0;
		    if( bigram.length()>0 && bigram.length()%2==0 ) {

			if( bigramsMap.containsKey( bigram ) ) {
			    count = bigramsMap.get( bigram );
			} else{
			    count = 0;
			}
			
			bigramsMap.put( bigram, count+1 );

			bigramCount++;
			bigram = "";
		    } else bigram += letter;
		}
	    }

	} catch (Exception e ) {
	    e.printStackTrace();
	}
    }
    

    public HashMap<String,Integer> getGrams(){
	return bigramsMap;
    }

    public int getCount() {
	return bigramCount;
    }
    public void mapLetters() {
	letterMap = new HashMap<Character, Integer>();
	int i = 0;
	for( char alphabet = 'a'; alphabet <= 'z'; alphabet++ ) {
	    letterMap.put( alphabet, i );
	    i++;
	}

	for( char alphaUpper = 'A'; alphaUpper <= 'Z'; alphaUpper++ ) {
	    letterMap.put( alphaUpper, i );
	    i++;
	}

	for( char letter: letterMap.keySet() ) {
	    System.out.println( letter + " " + letterMap.get( letter ));
	}

    }

    public static void main( String[] args ) {
	GetLetterBigrams grams = new GetLetterBigrams( args[0].toString() );
	HashMap<String, Integer> map = grams.getGrams();
	System.out.println( grams.getCount() );
	for( String bigram: map.keySet() ) {
	    System.out.println( bigram + " " + Math.log((double)map.get(bigram)/grams.getCount()));
	}
    }
}