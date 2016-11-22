/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.util.*;
import java.io.*;

public class PageRank {

    /**  
     *   Maximal number of documents. We're assuming here that we
     *   don't have more docs than we can keep in main memory.
     */
    final static int MAX_NUMBER_OF_DOCS = 200000;

    /**
     *   Mapping from document names to document numbers.
     */
    Hashtable<String,Integer> docNumber = new Hashtable<String,Integer>();

    /**
     *   Mapping from document numbers to document names
     */
    String[] docName = new String[MAX_NUMBER_OF_DOCS];

    /**  
     *   A memory-efficient representation of the transition matrix.
     *   The outlinks are represented as a Hashtable, whose keys are 
     *   the numbers of the documents linked from.<p>
     *
     *   The value corresponding to key i is a Hashtable whose keys are 
     *   all the numbers of documents j that i links to.<p>
     *
     *   If there are no outlinks from i, then the value corresponding 
     *   key i is null.
     */
    Hashtable<Integer,Hashtable<Integer,Boolean>> link = new Hashtable<Integer,Hashtable<Integer,Boolean>>();

    // the initial distribution over the states
    double[] xVector, xPrimeVector;
    /**
     *   The number of outlinks from each node.
     */
    int[] out = new int[MAX_NUMBER_OF_DOCS];

    /**
     *   The probability that the surfer will be bored, stop
     *   following links, and take a random jump somewhere.
     */
    final static double BORED = 0.15;

    /**
     *   Convergence criterion: Transition probabilities do not 
     *   change more that EPSILON from one iteration to another.
     */
    final static double EPSILON = 0.0001;

    
    /* --------------------------------------------- */


    public PageRank( String filename ) {
	int noOfDocs = readDocs( filename );
	iterate( noOfDocs, 1000 );
    }


    /* --------------------------------------------- */


    /**
     *   <p>Reads the documents and creates the docs table. When this method 
     *   finishes executing then the @code{out} vector of outlinks is 
     *   initialised for each doc. </p>
     *
     *   @return the number of documents read.
     */
    int readDocs( String filename ) {
	int fileIndex = 0;
	try {
	    System.err.print( "Reading file... " );
	    BufferedReader in = new BufferedReader( new FileReader( filename ));
	    String line;
	    while ((line = in.readLine()) != null && fileIndex<MAX_NUMBER_OF_DOCS ) {
		int index = line.indexOf( ";" );
		String title = line.substring( 0, index );
		Integer fromdoc = docNumber.get( title );
		//  Have we seen this document before?
		if ( fromdoc == null ) {	
		    // This is a previously unseen doc, so add it to the table.
		    fromdoc = fileIndex++;
		    docNumber.put( title, fromdoc );
		    docName[fromdoc] = title;
		}
		// Check all outlinks.
		StringTokenizer tok = new StringTokenizer( line.substring(index+1), "," );
		while ( tok.hasMoreTokens() && fileIndex<MAX_NUMBER_OF_DOCS ) {
		    String otherTitle = tok.nextToken();
		    Integer otherDoc = docNumber.get( otherTitle );
		    if ( otherDoc == null ) {
			// This is a previousy unseen doc, so add it to the table.
			otherDoc = fileIndex++;
			docNumber.put( otherTitle, otherDoc );
			docName[otherDoc] = otherTitle;
		    }
		    // Set the probability to 0 for now, to indicate that there is
		    // a link from fromdoc to otherDoc.
		    if ( link.get(fromdoc) == null ) {
			link.put(fromdoc, new Hashtable<Integer,Boolean>());
		    }
		    if ( link.get(fromdoc).get(otherDoc) == null ) {
			link.get(fromdoc).put( otherDoc, true );
			out[fromdoc]++;
		    }
		}
	    }
	    if ( fileIndex >= MAX_NUMBER_OF_DOCS ) {
		System.err.print( "stopped reading since documents table is full. " );
	    }
	    else {
		System.err.print( "done. " );
	    }
	}
	catch ( FileNotFoundException e ) {
	    System.err.println( "File " + filename + " not found!" );
	}
	catch ( IOException e ) {
	    System.err.println( "Error reading file " + filename );
	}
	System.err.println( "Read " + fileIndex + " number of documents" );
	return fileIndex;
    }


    /* --------------------------------------------- */


    /**
     *   Chooses an initial probability vector x, and repeatedly
     *   computes xG, xG^2, xG^3... until xG^i = xG^(i+1).
     */
    void iterate( int numberOfDocs, int maxIterations ) {
	
	 xVector = new double[numberOfDocs];
	 xVector[0] = 1;
	 // Initialization of x vector
	 for( int i = 1; i < xVector.length; i++ ) {
	     xVector[i] = 0;
	 }

	 // Output vector
	 xPrimeVector = new double[numberOfDocs];
	 
	 int x;
	 for( x = 0; x < maxIterations; x++ ) {
	     // position of output & column of matrix
	     for( int i = 0; i<numberOfDocs; i++ ) {
		 //row of matrix
		 for( int j = 0; j<numberOfDocs; j++ ) {
		     Hashtable<Integer, Boolean> tableCheck = link.get(j);

		     double xPrime;
		     // If it's a sink
		     if( tableCheck == null ) {

			 xPrime= ( (1.0)/numberOfDocs );
		     // If we have outlinks to this page
		     } else if( tableCheck.get(i) != null ) {
			 xPrime = ((1.0*BORED/numberOfDocs) + ((1.0-BORED)*1/tableCheck.size()));
			 // If this page has an outlink, but not to this page
		     } else {
			 xPrime= ((1.0*BORED)/numberOfDocs);
			 
		     }

		     // The next value in the output vector
		     xPrimeVector[i] += xVector[j]*xPrime;
		     
		 }
		 
		 
	     }

	     // Check for convergence
	     if(  epsilonCheck( xVector, xPrimeVector) < EPSILON ) {
		 break;

		 // Otherwise change vectors, iterate again
	     } else {
		 for( int y = 0; y < numberOfDocs; y++ ){
		     xVector[y] = xPrimeVector[y];
		     xPrimeVector[y] = 0.0;
		 }
	     }
	     
	 }

	 // Convergence! Or reached max iterations
	 for( int i = 0; i < numberOfDocs; i++ ) {
	     System.out.format( "%s: %.5f\n", docName[i], xPrimeVector[i]);
	 }	 
	 System.out.format( "Solution found after %d iterations\n", x );
    }
    
    
    private PostingsList getPostingsList() {
	PostingsList list = new PostingsList();
	for( int i = 0; i < numberOfDocs; i++ ) {
	    PostingsEntry newEntry = new PostingsEntry();
	    newEntry.docID = docName[i];
	    newEntry.score = xPrimeVector[i];
	}
	return list;
    }


    /* --------------------------------------------- */
    double epsilonCheck( double[] xVec, double[] xPrimeVec ) {
	double epCheck = 0.0;
	for( int i = 0; i< xVec.length; i++ ) {
	    epCheck += Math.abs(xVec[i]-xPrimeVec[i]);
	}
	return epCheck;
    }

    public static void main( String[] args ) {
	if ( args.length != 1 ) {
	    System.err.println( "Please give the name of the link file" );
	}
	else {
	    new PageRank( args[0] );
	}
    }
}