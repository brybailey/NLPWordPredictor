import java.io.*;
import java.util.*;

public class GetLetterTrigrams {

    private HashMap<String,Integer> trigramsMap = new HashMap<String, Integer>();

    private int trigramCount;

    private HashMap<Character,Integer> letterMap = new HashMap<Character, Integer>();

    public GetLetterTrigrams( String filename ) {
	mapLetters();
	try{
	    InputStreamReader read = new InputStreamReader( new FileInputStream( filename ) );
	    Scanner in = new Scanner( read );
	    char firstChar;
	    char secondChar;
	    trigramCount = 0;
	    StringTokenizer tokenize;
	    String word;
	    trigramCount = 0;

	    while( in.hasNext() ) {
		word = in.next();
		int i = 0;
		String trigram = "";
		for( int x = 0; x<word.length(); x++ ) {
		    char letter = word.charAt(x);

		    int count = 0;
		    if( trigram.length()>0 && trigram.length()%3==0 ) {

			if( trigramsMap.containsKey( trigram ) ) {
			    count = trigramsMap.get( trigram );
			} else{
			    count = 0;
			}
			
			trigramsMap.put( trigram, count+1 );

			trigramCount++;
			trigram = "";
		    } else trigram += letter;
		}
	    }

	} catch (Exception e ) {
	    e.printStackTrace();
	}
    }
    

    public HashMap<String,Integer> getGrams(){
	return trigramsMap;
    }

    public int getCount() {
	return trigramCount;
    }

    public HashMap<Character,Integer> getLetterMappings() {
	return letterMap;
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
	String hyphen = "-";
	letterMap.put( hyphen.charAt(0), i );

    }

    public static void main( String[] args ) {
	GetLetterTrigrams grams = new GetLetterTrigrams( args[0].toString() );
	HashMap<String, Integer> map = grams.getGrams();
	HashMap<Character, Integer> letters = grams.getLetterMappings();
	System.out.println( grams.getCount() );
	for( String trigram: map.keySet() ) {
		int firstLetter = letters.get(trigram.charAt(0));
		int secondLetter = letters.get(trigram.charAt(1));
		int thirdLetter = letters.get(trigram.charAt(2));
		System.out.println( firstLetter+ " " + secondLetter  + " " + thirdLetter + " " + Math.log((double)map.get(trigram)/grams.getCount()));
	}
    }
}