/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

/** 
 *  This interface specifies the requirements on required methods of a Viterbi decoder. 
 */ 
public interface ViterbiDecoder {

    /** Reads the n-gram probabilities from a file. */
    public void init_a( String filename );

    /** Initializes the observation matrix. */
    public void init_b();

     /**
     *  Performs the Viterbi decoding and returns the most likely
     *  string. 
     */
    public String viterbi( String s );

}