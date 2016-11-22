/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.io.*;
import java.util.*;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.pdmodel.PDDocument;


public class BigramTrainer {

    /** Maximum number of words we can handle. */
    static final int MAX_WORDS = 200000;

     /** The mapping from words to identifiers. */
    HashMap<String,Integer> index = new HashMap<String,Integer>();

    /** The mapping from identifiers to words. */
    String[] word = new String[MAX_WORDS];

    /** An array holding the unigram counts. */
    int[] unigram_count = new int[MAX_WORDS];

    /** The bigram counts. Since most of these are zero (why?), we store these
     *	in a hashmap rather than an array to save space (and since it is impossible
     *  to create such a big array anyway).  */
    HashMap<Integer,HashMap<Integer,Integer>> bigram_count = new HashMap<Integer,HashMap<Integer,Integer>>();

    /** The identifier of the previous word processed. */
    int last_index = -1;

    /** Number of unique words (word forms) in the training corpus. */
    int unique_words = 0;

    /** The total number of words in the training corpus. */
    int total_words = 0;


    /**
     *  Processes the file @code{f}. If @code{f} is a directory,
     *  all its files and subdirectories are recursively processed.
     */
    public void processFiles( File f ) {
	// do not try to index fs that cannot be read
	if ( f.canRead() ) {
	    if ( f.isDirectory() ) {
		String[] fs = f.list();
		// an IO error could occur
		if ( fs != null ) {
		    for ( int i=0; i<fs.length; i++ ) {
			processFiles( new File( f, fs[i] ));
		    }
		}
	    } else {
		try {
		    Reader reader = new FileReader( f );
		    //  Read the first few bytes of the file to see if it is 
		    // likely to be a PDF 
		    char[] buf = new char[4];
		    reader.read( buf, 0, 4 );
		    reader.close();
		    if ( buf[0] == '%' && buf[1]=='P' && buf[2]=='D' && buf[3]=='F' ) {
			// We assume this is a PDF file
			try {
			    String contents = extractPDFContents( f );
			    reader = new StringReader( contents );
			}
			catch ( IOException e ) {
			    // Perhaps it wasn't a PDF file after all
			    reader = new FileReader( f );
			}
		    }
		    else {
			// We hope this is ordinary text
			reader = new FileReader( f );
		    }
		    Tokenizer tok = new Tokenizer( reader, false, false, null );
		    while ( tok.hasMoreTokens() ) {
			processToken( tok.nextToken() );
		    }
		    reader.close();
		}
		catch ( IOException e ) {
		    e.printStackTrace();
		}
	    }
	}
    }
 

    /**
     *  Extracts the textual contents from a PDF file as one long string.
     */
    public String extractPDFContents( File f ) throws IOException {
	FileInputStream fi = new FileInputStream( f );
	PDFParser parser = new PDFParser( fi );   
	parser.parse();   
	fi.close();
	COSDocument cd = parser.getDocument();   
	PDFTextStripper stripper = new PDFTextStripper();   
	String result = stripper.getText( new PDDocument( cd ));  
	cd.close();
	return result;
    }


    /**
     *  Processes one word in the training corpus, and adjusts the unigram and
     *  bigram counts. 
     *
     *  @param token The current word to be processed.
     */ 
    void processToken( String token ) {

	// YOUR CODE HERE

    }


    /**
     *  Prints the language model.
     */
    void printStats() {

	// YOUR CODE HERE

    }


    /** Prints usage information. */
    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( "  -f <filename> : file from which to build the language model");
    }


    /** 
     *  <p>Constructor. Processes the file <code>f</code> and builds a language model
     *  from it.</p>
     *
     *  @param f The training file.
     */
    public BigramTrainer( String f ) {
	if ( f == null ) {
	    System.err.println( "No filename given\n" );
	    printHelpMessage();
	    return;
	}
	processFiles( new File( f ));
	printStats();
    }


    public static void main( String[] args ) {
	// Parse command line arguments 
	String training_file = null;
	int i=0; 
	while ( i<args.length ) {
	    if ( args[i].equals( "-f" )) {
		i++;
		if ( i<args.length ) {
		    training_file = args[i++];
		}
		else {
		    printHelpMessage();
		    return;
		}
	    }
	}
	new BigramTrainer( training_file );
    }
}
