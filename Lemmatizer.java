/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.io.*;
import java.util.*;

/**
 *  This class lemmatizes words based on simple heuristics.
 */
public class Lemmatizer {

    /**
     *  <p>Lemmatizes a word based on heuristics.</p>
     *
     *  @param word The word.
     *  @param tag The part-of-speech tag of the word.
     *  @return The lemmatized form of <code>word</code>.
     */
    public static String lemmatize( String word, String tag ) {

	//  MODIFY THIS METHOD AS YOU SEE FIT

	if ( tag.equals("NOUN") && word.endsWith("s") ) {
	    // Remove plural "s"
	    return word.substring( 0, word.length()-1 );
	}
	if ( tag.equals("VERB") && word.endsWith("s") ) {
	    // Remove present-tense "s"
	    return word.substring( 0, word.length()-1 );
	}
	if ( tag.equals("ADJ") && word.endsWith("er")) {
	    // Remove comparative "er"
	    return word.substring( 0, word.length()-2 );
	}
	if ( tag.equals("ADJ") && word.endsWith("est")) {
	    // Remove superlative "est"
	    return word.substring( 0, word.length()-3 );
	}
	return word;
    }


    /**
     *  <p>Reads a file containing one word and one part-of-speech tag per line,
     *  tokenizes each word, and prints the result.</p>
     *
     *  @param filename The name of the file.
     */
    private static void lemmatizeFile( String filename ) {
	try {
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    StringBuffer buf = new StringBuffer();
	    String line;
	    while (( line = in.readLine()) != null ) {
		buf.append( line );
		StringTokenizer tok = new StringTokenizer( line );
		String word, tag;
		if ( tok.hasMoreTokens() ) {
		    word = tok.nextToken();
		}
		else {
		    System.err.println( "Warning: Problem on line \"" + line + "\"" );
		    continue;
		}
		if ( tok.hasMoreTokens() ) {
		    tag = tok.nextToken();
		}
		else {
		    System.err.println( "Warning: Problem on line \"" + line + "\"" );
		    continue;
		}
		System.out.format( "%s %s %s\n", word, tag, lemmatize(word, tag));
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "File " + filename + " not found.");
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + filename );
	}
    }


    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -f <filename> : name of the file to be lemmatized" );
    }



    /** Main */
    public static void main( String[] args ) {
	String filename = null;
	// Parse command line arguments 
	int i=0; 
	while ( i<args.length ) {
	    if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length ) {
		    filename = args[i];
		    i++;
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    else {
		System.err.println( "Unrecognized parameter: " + args[i] );
		printHelpMessage();
		return;
	    }
	}
	if ( filename != null ) {
	    lemmatizeFile( filename );
	}
	else {
	    printHelpMessage();
	}
    }
}
