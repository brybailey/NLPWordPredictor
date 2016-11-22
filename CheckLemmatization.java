/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.io.*;
import java.util.*;

public class CheckLemmatization {

    /**
     *  <p>Checks a file containing lemmatizations against
     *  lemmatizations read from System.in.</p>
     *
     *  @param filename The name of the file.
     */
    private static void checkLemmatization( String f ) {
	int all=0, match=0;
	try {
	    BufferedReader in1 = new BufferedReader( new InputStreamReader( System.in ));
	    BufferedReader in2 = new BufferedReader( new FileReader( f ));
	    StringBuffer buf = new StringBuffer();
	    String line1, line2;
	    while (( line1 = in1.readLine()) != null &&  (line2 = in2.readLine())!= null ) {
		all++;
		StringTokenizer tok1 = new StringTokenizer( line1 );
		StringTokenizer tok2 = new StringTokenizer( line2 );
		if ( tok1.hasMoreTokens() ) tok1.nextToken(); else continue;
		if ( tok1.hasMoreTokens() ) tok1.nextToken(); else continue;
		if ( tok2.hasMoreTokens() ) tok2.nextToken(); else continue;
		if ( tok2.hasMoreTokens() ) tok2.nextToken(); else continue;
		if ( tok1.hasMoreTokens() && tok2.hasMoreTokens() ) {
		    if ( tok1.nextToken().equals( tok2.nextToken() )) {
			match++;
		    }
		    else {
			System.err.println( line1 );
			System.err.println( line2 );
			System.err.println( "-------" );
		    }
		}
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "Could not find file " + f );
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + f );
	}
	System.out.format( "%d comparisons, %d matches. Correctness: %.2f\n", all, match, 1.0*match/all );

    }


    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -f <filename> : name of the file to to be compared with stdin" );
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
		    String f = args[i++];
		    checkLemmatization( f );
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