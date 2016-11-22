/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.util.*;
import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 *  This class attempts to correct keystroke errors by HMM decoding. 
 */
public class CorrectKeystrokeErrors {

    /**
     *  Reads the test file with mistyped sentences, and prints out the corrected
     *  results.
     */
    static void processTestFile( String filename, String a_file, int n ) {
	if ( filename == null ) 
	    return;
	ViterbiDecoder v;
	if ( n==2 ) {
	    v = new ViterbiBigramDecoder( a_file );
	}
	else if ( n==3 ) {
	    v = new ViterbiTrigramDecoder( a_file );
	}
	else {
	    return;
	}
	try {
	    InputStreamReader in = new InputStreamReader( new FileInputStream(filename), StandardCharsets.UTF_8 );
	    Scanner scan = new Scanner( in );
	    scan.useLocale( Locale.forLanguageTag( "en-US" ));
	    while ( scan.hasNext() ) {
		// Read next line, turn everything to lowercase, remove initial and 
		// trailing whitespace, add an "END" symbol at the end of the string.
		String s = scan.nextLine().toLowerCase().trim() + Key.indexToChar( Key.START_END );
		System.out.println( v.viterbi( s ));
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "Couldn't find " + filename );
	}
    }



    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -f <filename> : file with mistyped sentences to correct" );
	System.err.println( "  -p <filename> : file with n-gram probabilities" );
	System.err.println( "  -n <number> : either 2 (=bigram model) or 3 (trigram model)" );
    }


    public static void main( String[] args ) {
	// Parse command line arguments 
	String test_file = null;
	String a_file = null;
	ViterbiDecoder v = null;
	int i=0; 
	int n=2;
	while ( i<args.length ) {
	    if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length ) {
		    test_file = args[i++];
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    else if ( args[i].equals( "-p" )) {
		i++;
		if ( i<args.length ) {
		    a_file = args[i++];
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    else if ( args[i].equals( "-n" )) {
		i++;
		if ( i<args.length ) {
		    try {
			n = new Integer( args[i] );
			i++;
		    }
		    catch ( Exception e ) {
			printHelpMessage();
			return;
		    }
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    else {
		printHelpMessage();
		return;
	    }
	}
	if ( test_file != null && a_file != null ) {
	    processTestFile( test_file, a_file, n );
	}
    }
}