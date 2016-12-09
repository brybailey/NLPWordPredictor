/*
 * 2016 Bryan Bailey & Braden Becker
 *
 *
 */

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Listener implements KeyListener{
    //Textfields
    JTextField input;
    JTextField output;
    //Word level prediciton tables
    BigramPredictor bigramPredictor;
    TrigramPredictor trigramPredictor;
    QuadgramPredictor quadgramPredictor;
    int numberOfWords=0;
    int wordLength;
    int lastCode;
    int wordCount=0;
    String sentence="";
    String first="";
    String second="";
    String third="";
    String secondLastWord="";
    int lastSpaceIndex=0;
    String currentWord="";
    int level;
    ViterbiDecoder biVit;
    ViterbiDecoder triVit;
    Dictionary dictionary;
    
    
    PriorityQueue<Map.Entry<String,Integer>> pq;
    PriorityQueue<Map.Entry<String,Integer>> pastpq;
    
    public Listener( JTextField input, JTextField output, int level ){
        super();
        this.level = level;
	dictionary = new Dictionary();
        if( level == 2 ) {
            bigramPredictor = new BigramPredictor();
        } else if (level == 3 ) {
            bigramPredictor = new BigramPredictor();
            trigramPredictor = new TrigramPredictor();
        } else if (level == 4 ) {
            bigramPredictor = new BigramPredictor();
            trigramPredictor = new TrigramPredictor();
            quadgramPredictor = new QuadgramPredictor();
        }
        this.input = input;
        this.output = output;
	biVit = new ViterbiBigramDecoder( "bigram_probs.txt" ); 
	triVit = new ViterbiTrigramDecoder( "trigram_probs.txt" );

    }
    //Overwrite
    public void keyTyped(KeyEvent e) {
    }
    //Overwrite
    public void keyReleased(KeyEvent e) {
    }
    //Process updates
    public void keyPressed(KeyEvent e) {
        //KeyEvent code
        int code = e.getKeyCode();
        //Typed String
        String s = Character.toString(e.getKeyChar());
        //Current sentence
        sentence = input.getText();
	currentWord+=s;

       // System.out.println(code);
        //Spacebar pressed
        if(code == KeyEvent.VK_SPACE) {
	    wordCount++;
            //currentWord="";
            //Space pressed for the second time, grab best match from pq, update sentence, proceed with next prediction
            if(code == lastCode){
		wordCount--;
                if(pq!=null){
		    input.setText(sentence+pq.peek().getKey());
		    sentence = input.getText();
		    currentWord = "";
		    wordCount++;
                } else {
		    // Remove the extra space, there is nothing to predict
		    input.setText( removeChars( sentence, 1 ) );
		    sentence = input.getText();
		    currentWord = getLastWord( sentence );
		    if( !dictionary.contains( currentWord ) ) {
			currentWord = biVit.viterbi( currentWord );
		    }    
		    sentence = removeLastWord( sentence );
		    sentence = sentence + currentWord;
		    input.setText( sentence );
		    //System.out.println( "Error: no predictions exist" );
		}
	    } else if ( lastCode == KeyEvent.VK_PERIOD ) {
		output.setText("");
	    }
	    System.out.println("WC: "+wordCount);
	    String[] words = new String[level-1];
	    fillWords( sentence, words );
	    regressionPrediction( words, wordCount, level );
	    System.out.println( "MIDDLE1: " + pq);
	    printTopResults( pq );	    
	    pastpq = pq;
	    //System.out.println( "AFTER1: " + pq );
	    //	    currentWord = currentWord.substring(0, currentWord.indexOf( " " ) );
	} else if (code == KeyEvent.VK_PERIOD ) {
	    // END OF SENTENCE
	    wordCount = 0;
	    
	} else if (code == KeyEvent.VK_BACK_SPACE){
            System.out.println("Back Space");
            if(input.getText().equals(""))
	       wordCount=0;
            //All normal alphanumerics
        } else if ( code == KeyEvent.VK_SHIFT && lastCode == KeyEvent.VK_SPACE) {
	    if( pastpq != null ) {
		sentence = removeChars( sentence, currentWord.length() );

		input.setText( sentence+ " "+ pastpq.peek().getKey() + " ");
		sentence = input.getText();
		currentWord = "";
		wordCount++;
		code = KeyEvent.VK_SPACE;
	    }    
	    
	} else if ( wordCount > 0 ) {
	    
	    if( lastCode == KeyEvent.VK_SPACE ) {
		currentWord = s;
	    }
            sentence += s;
	    String tempSentence = removeLastWord( sentence );

	    String[] words = new String[level-1];
	    fillWords( tempSentence, words );

	    updateList();

	    int recount = level;
	    if( pq != null ) {
		while( pq.size() == 0 && pq != null && recount > 1) {
		    regressionPrediction( words, wordCount, level-1 );
		    updateList();
		    recount--;
		}
	    }
	    printTopResults(pq);
	    pastpq = pq;
        }
        lastCode = code;
    }
    
    public void fillWords( String curSentence, String[] array ) {
        int wc = wordCount;
	fillLoop:
        for( int i = 0; i<array.length; i++ ) {
	    if( wc > 1 ) {
		array[i]=getLastWord(curSentence);
		wc--;
		removeChars( curSentence, 1 );
		curSentence = removeLastWord(curSentence);
	    } else {
		array[i] = curSentence;
		break fillLoop;
	    }
	}
    }
    
    // Still needs a little work
    public void regressionPrediction(String[] words, int wCount, int gramLevel) {
	if( wCount >= gramLevel-1 ) {
	    levelPrediction( gramLevel, words );
	} else {
	    levelPrediction( wCount+1, words );
	}
    }
	

    public void levelPrediction( int gramLevel, String[] words ) {
	if( gramLevel == 4 ) {
	    if( quadgramPredictor.canPredict( words[gramLevel-2], words[gramLevel-3], words[gramLevel-4] )  ){
		pq = quadgramPredictor.predict(  words[gramLevel-2], words[gramLevel-3], words[gramLevel-4] );
	    } else {
		levelPrediction( gramLevel - 1, words );
	    }
	} else if( gramLevel == 3 ) {
	    if ( trigramPredictor.canPredict( words[gramLevel-2], words[gramLevel-3] ) ) {
		pq = trigramPredictor.predict(  words[gramLevel-2], words[gramLevel-3] );	
	    } else {
		levelPrediction( gramLevel - 1, words  );
	    }
	} else if( gramLevel == 2 ) {
	    if( bigramPredictor.canPredict( words[gramLevel-2] ) ) {
		pq = bigramPredictor.predict( words[gramLevel-2] );	    
	    } else {
		levelPrediction( gramLevel - 1, words );
	    }
	} else if( gramLevel == 1 ) pq = null;
    }

    public String getLastWord(String s){
        String w = s.substring(s.lastIndexOf(" ")+1,s.length());
        return w;
    }

    public String removeChars(String s, int number){
	return s.substring( 0, s.length()-number);
    }
    
    public String getFirstWord(String s) {
        String first = s.substring(0, s.indexOf(" "));
        return first;
    }
    public String removeLastWord(String s){
        String ns = s.substring(0,s.lastIndexOf(" "));
        return ns;
    }
    public String removeTrailingSpace(String s){
        String ns=s.substring(s.lastIndexOf(" "));
        return ns;
    }
    public void updateList() {//PriorityQueue<Map.Entry<String,Integer>> oldpq){
        if( pq!= null ) {
            PriorityQueue<Map.Entry<String,Integer>> newpq = new PriorityQueue<Map.Entry<String,Integer>>(200000, new Comparator<Map.Entry<String, Integer>>() {
                public int compare( Map.Entry<String,Integer> arg0,
                                   Map.Entry<String,Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue() );
                }
            });
            
            //	    System.out.println( "CHECK FIRST: " + pq.peek() );
            for( Map.Entry<String,Integer> element: pq ) {
                if( element.getKey().startsWith( currentWord ) ) {
                    newpq.add( element );
                }
            }
            pq = newpq;
            
        } else pq = null;

    }
    public void printTopResults( PriorityQueue<Map.Entry<String,Integer>> predictions ) {
        if( predictions != null ) {
	    int size = predictions.size();
            PriorityQueue<Map.Entry<String,Integer>> temp = new PriorityQueue<Map.Entry<String,Integer>>(5, new Comparator<Map.Entry<String, Integer>>() {
                public int compare( Map.Entry<String,Integer> arg0,
                                   Map.Entry<String,Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue() );
                }
            });
            String outputText = "";
            for( int i=0; i<size&&i<5; i++ ) {
                outputText += predictions.peek().getKey()+" ";
                temp.add(predictions.poll());
            }
            for( int i=0; i<temp.size(); i++ ){
                predictions.add( temp.poll() );
            }
            //	System.out.println( predictions.peek() + " AND " + temp.peek() );
            output.setText(outputText);
        } else {
	    if ( !currentWord.contains(".") ) {
		output.setText( "Wow! Cool new word dude!" );
	    }
        }
        
    }
}