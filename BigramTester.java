/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.io.*;
import java.util.*;

public class BigramTester {

    /** The mapping from words to identifiers. */
    HashMap<String,Integer> index = new HashMap<String,Integer>();

    /** The mapping from identifiers to words. */
    String[] word;

    /** An array holding the unigram counts. */
    int[] unigram_count;

    /** The bigram log-probabilities. */
    HashMap<Integer,HashMap<Integer,Double>> bigram_prob = new HashMap<Integer,HashMap<Integer,Double>>();

    /** Number of unique words (word forms) in the training corpus. */
    int unique_words = 0;

    /** The total number of words in the training corpus. */
    int total_words = 0;

    /** The average log-probability (= the estimation of the entropy) of the test corpus. */
    double logProb = 0;

    /** The identifier of the previous word processed in the test corpus. Is -1 if the last
     *  word was unknown. */
    Integer last_index = -1;

    /** The fraction of the probability mass given to unknown words. */
    double lambda3 = 0.000001;

    /** The fraction of the probability mass given to unigram probabilities. */
    double lambda2 = 0.01-lambda3;

    /** The fraction of the probability mass given to bigram probabilities. */
    double lambda1 = 0.99;

    /** The number of words processed in the test corpus. */
    int test_words_processed = 0;


    /**
     *  Reads the contents of the language model file into the appropriate data structures.
     *
     *  @param filename The name of the language model file.
     *  @return <code>true</code> if the entire file could be processed, false otherwise.
     */
    boolean readModel( String filename ) {
	try {
	    if ( filename == null )
		throw new FileNotFoundException();
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    Scanner scan = new Scanner( in );
	    // The next line is important for reading doubles correctly
	    scan.useLocale( Locale.forLanguageTag( "en_US" ));
	    unique_words = scan.nextInt();
	    total_words = scan.nextInt();

	    // YOUR CODE HERE

	    return true;
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "Couldn't find bigram probabilities file " + filename );
	    return false;
	}
    }

    
    /**
     *  
     */
    void computeEntropyCumulatively( String word ) {

	// YOUR CODE HERE

    }

	    

    /**
     *  <p>Reads and processes the test file one word at a time. </p>
     *
     *  @param test_filename The name of the test corpus file.
     *  @return <code>true</code> if the entire file could be processed, false otherwise.
     */
    boolean processTestFile( String test_filename ) {
	try {
	    Reader reader = new FileReader( test_filename );
	    Tokenizer tok = new Tokenizer( reader, false, false, null );
	    while ( tok.hasMoreTokens() ) {
		computeEntropyCumulatively( tok.nextToken() );
	    }
	    reader.close();
	    return true;
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading testfile" );
	    return false;
	}
    }


    /** 
     *  <p>Constructor. Reads bigram statistics and test file, and computes 
     *  the entropy of the latter. </p>
     *
     *  @param statsfile The file containing the language model.
     *  @param testfile  The file containing the test corpus.
     */
    public BigramTester( String statsfile, String testfile ) {
	if ( statsfile == null || testfile == null) {
	    System.err.println( "Please give the name of the bigram statistics file and the test file\n" );
	    printHelpMessage();
	    return;
	}
	boolean success = readModel( statsfile );
	if ( !success ) 
	    return;
	success = processTestFile( testfile );
	if ( !success ) 
	    return;
	System.out.format( "Read %d words. Estimated entropy: %.2f\n", test_words_processed, logProb );
    }


    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -f <filename> : file with language model");
	System.err.println( "  -t <filename> : test corpus" );
    }


    public static void main( String[] args ) {
	// Parse command line arguments 
	String model_file = null;
	String test_file = null;
	int i=0; 
	while ( i<args.length ) {
	    if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length ) {
		    model_file = args[i++];
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    if ( args[i].equals( "-t" )) {
		i++;
		if ( i<args.length ) {
		    test_file = args[i++];
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
	new BigramTester( model_file, test_file );
    }
}
