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
 *  This class computes the minimum-cost alignment of two strings.
 */
public class Aligner {

    /**
     *  When printing the results, only print BREAKOFF characters per line.
     */ 
    public static final int BREAKOFF = 60;


    /**
     *  <p>Computes and returns the backpointer array (see Jurafsky and Martin, Fig 3.27)
     *  arising from the calculation of the minimal edit distance of two strings
     *  <code>s0,/code> and <code>s1</code>.</p>
     *
     *  <p>The backpointer array has three dimensions. The first two are the row and
     *  column indices of the table in Fig 3.27. The third dimension either has 
     *  the value 0 (in which case the value is the row index of the cell the backpointer 
     *  is pointing to), or the value 1 (the value is the column index). For example, if
     *  the backpointer from cell (5,5) is to cell (5,4), then 
     *  <code>backptr[5][5][0]=5</code> and <code>backptr[5][5][1]=4</code>.</p>
     *
     *  <p>Note that the first dimension in the backpointer array has size 1 more than
     *  the length of the string <code>s0</code>, and the second dimension has size 1 more 
     *  than the length of <code>s1</code>. This is because index 0 represents the "start-of
     *  -string" symbol '#'.</p>
     *
     *  @param s0 The first string.
     *  @param s1 The second string.
     *  @return backptr The backpointer array.
     */
    public static int[][][] computeBackpointers( String s0, String s1 ) throws NullPointerException {
	if ( s0==null || s1==null )
	    throw new NullPointerException();
	int[][][] backptr = new int[s0.length()+1][s1.length()+1][2];

	// YOUR CODE HERE

	return backptr;
    }


    /**
     *  <p>Finds the best alignment of two different strings <code>s0</code> 
     *  and <code>s1</code>, given an array of backpointers.</p>
     *
     *  <p>The alignment is made by padding the input strings with spaces. If, for 
     *  instance, the strings are <code>around</code> and <code>rounded</code>,
     *  then the padded strings should be <code>around  </code> and 
     *  <code> rounded</code>.</p>
     *
     *  @param s0 The first string.
     *  @param s1 The second string.
     *  @param backptr A three-dimensional matrix of backpointers, as returned by
     *         the <code>diff</code> method above.
     *  @return An array containing exactly two strings. The first string (index 0 
     *         in the array) contains the string <code>s0</code> padded with spaces
     *         as described above, the second string (index 1 in the array) contains
     *         the string <code>s1</code> padded with spaces.
     */
    public static String[] align( String s0, String s1, int[][][] backptr ) { 
	String[] result = new String[2];

	// YOUR CODE HERE

	return result;
    }

	
    /**
     *  <p>Prints two aligned strings (= strings padded with spaces). 
     *  Note that this printing method assumes that the padded strings
     *  are in the reverse order, compared to the original strings
     *  (because we are following backpointers from the end of the 
     *  original strings).</p> 
     * 
     *   @param s An array of two equally long strings, representing 
     *           the alignment of the two original strings. 
     */
    public static void printAlignment( String[] s ) {
	if ( s[0]==null || s[1]==null )
	    return;
	int startindex = s[0].length()-1;
	while ( startindex > 0 ) {
	    int endindex = Math.max( 0, startindex-BREAKOFF+1 );
	    for ( int i=startindex; i>=endindex; i-- ) {
		System.out.print( s[0].charAt( i ));
	    }
	    System.out.println();
	    for ( int i=startindex; i>=endindex; i-- ) {
		if ( s[0].charAt(i) == s[1].charAt(i) ) {
		    System.out.print( '|' );
		}
		else {
		    System.out.print( ' ' );
		}
	    }
	    System.out.println();
	    for ( int i=startindex; i>=endindex; i-- ) {
		System.out.print( s[1].charAt( i ));
	    }
	    System.out.println();
	    System.out.println();
	    startindex -= BREAKOFF;
	}
    } 


    /**
     *  Reads a textfile and returns the contents as a string.
     *  @param filename The name of the file.
     *  @return The contents of the file.
     */
    private static String readFile( String filename ) {
	try {
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    StringBuffer buf = new StringBuffer();
	    String line;
	    while (( line = in.readLine()) != null ) {
		buf.append( line );
	    }
	    return buf.toString();
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "File " + filename + " not found.");
	    return null;
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + filename );
	    return null;
	}
    }


    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -s <string1> <string2>     : align two strings" );
	System.err.println( "  -f <filename1> <filename2> : align the contents of two files" );
    }

		
    public static void main( String[] args ) {
	// Parse command line arguments 
	int i=0; 
	while ( i<args.length ) {
	    if ( args[i].equals( "-s" )) {
		i++;
		if ( i<args.length+1 ) {
		    String s1 = args[i++];
		    String s2 = args[i++];
		    printAlignment( align(s1, s2, computeBackpointers(s1, s2))); 
		    return;
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	    else if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length+1 ) {
		    String f1 = args[i++];
		    String f2 = args[i++];
		    String s1 = readFile( f1 );
		    String s2 = readFile( f2 );
		    if ( s1!=null && s2!=null ) {
			printAlignment( align(s1, s2, computeBackpointers(s1, s2))); 
		    }
		    return;
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
    }
}
