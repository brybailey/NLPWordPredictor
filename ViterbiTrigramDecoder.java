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
 *  This class implements Viterbi decoding using trigram probabilities in order
 *  to correct keystroke errors.
 */
public class ViterbiTrigramDecoder implements ViterbiDecoder {

    /**
     * The trellis used for Viterbi decoding. The first index is the time step.
     */
    double[][][] v;
    
    /** The trigram stats. */
    double[][][] a = new double[Key.NUMBER_OF_CHARS][Key.NUMBER_OF_CHARS][Key.NUMBER_OF_CHARS];
    
    /** The observation matrix. */
    double[][] b = new double[Key.NUMBER_OF_CHARS][Key.NUMBER_OF_CHARS];
    
    /** Pointers to retrieve the topmost hypothesis. */
    int[][][] backptr;
    

    /**  
     *  Reads the bigram probabilities (the 'A' matrix) from a file. 
     */
    public void init_a(String filename) {
	try {
	    InputStreamReader in = new InputStreamReader(new FileInputStream(filename), StandardCharsets.UTF_8);
	    Scanner scan = new Scanner(in).useLocale(Locale.US);
	    while (scan.hasNext()) {
		int i = scan.nextInt();
		int j = scan.nextInt();
		int k = scan.nextInt();
		double d = scan.nextDouble();
		a[i][j][k] = d;
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
    
    
    // ------------------------------------------------------


    /** 
     *  Initializes the observation probabilities (the 'B' matrix).
     */
    public void init_b() {
	for ( int i=0; i<Key.NUMBER_OF_CHARS; i++ ) {
	    char[] cs = Key.neighbour[i];
	    // Initialize all log-probabilities to some small value.
	    for ( int j=0; j<Key.NUMBER_OF_CHARS; j++ ) {
		b[i][j] = Double.NEGATIVE_INFINITY;
	    }
	    // All neighbouring keys are assigned the probability 0.1
	    for ( int j=0; j<cs.length; j++ ) {
		b[i][Key.charToIndex(cs[j])] = Math.log(0.1);
	    }
	    // The remainder of the probability mass is given to the correct
	    // key.
	    b[i][i] = Math.log((10 - cs.length) / 10.0);
	}
    }
    

    // ------------------------------------------------------


    /**
     * Performs the Viterbi decoding and returns the most likely string.
     */
    public String viterbi(String s) {
	

	// REPLACE THE STATEMENT BELOW WITH YOUR CODE

	return s;
    }
    

    // ------------------------------------------------------


    /**
     *  Constructor: Initializes the A and B matrices.
     */
    public ViterbiTrigramDecoder( String filename ) {
	init_a( filename );
	init_b();
    }
}
    

