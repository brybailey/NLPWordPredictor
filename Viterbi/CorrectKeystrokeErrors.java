/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  


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
    static String processMistypedWord( String mistyped, String a_file, int n ) {
	ViterbiDecoder v;
	if ( n==2 ) {
	    v = new ViterbiBigramDecoder( a_file );
	}
	else if ( n==3 ) {
	    v = new ViterbiTrigramDecoder( a_file );
	}
	else {
	    return "n should be 2 or 3";
	}
	String s = mistyped +  Key.indexToChar( Key.START_END );
	String correct = v.viterbi( s );
	return correct;
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
	String word = null;
	String a_file = null;
	ViterbiDecoder v = null;
	int i=0; 
	int n=2;
	while ( i<args.length ) {
	    if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length ) {
		    word = args[i++];
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
	if ( word != null && a_file != null ) {
	    System.out.println( processMistypedWord( word, a_file, n ) );
	}
    }
}