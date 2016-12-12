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
    ViterbiDecoder vit;
    Dictionary dictionary;
    String[] words;
    int lettersSaved = 0;
    
    
    PriorityQueue<Map.Entry<String,Integer>> pq;
    PriorityQueue<Map.Entry<String,Integer>> pastpq;
    
    public Listener( JTextField input, JTextField output, int level, int viterbi ){
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

	if( viterbi == 2 ) {
	    vit = new ViterbiBigramDecoder( "bigram_probs.txt" ); 
	} else if ( viterbi == 3 ) {
	    vit = new ViterbiTrigramDecoder( "trigram_probs.txt" );
	}

        this.input = input;
        this.output = output;
	words = new String[level-1];

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

	/* Check if the last typed word is valid,
         * run viterbi x times if it is not,
         * then update pq based on valid word,
         * then reset current word
         */
        //Spacebar pressed
        if(code == KeyEvent.VK_SPACE) {
	    wordCount++;
            currentWord = getLastWord(sentence);
	    System.out.println( currentWord + "AND" );
	    if( !dictionary.contains( currentWord ) && currentWord != "." && !currentWord.equals("") ) {
		currentWord += Key.START_END;
		for( int i = 0; i < 100 && !dictionary.contains( currentWord ); i++ ) {
		    currentWord = vit.viterbi( currentWord );
		}
		sentence = removeLastWord(sentence);
		//		sentence += " " + currentWord;
		if( wordCount < 2 ) {
		    sentence += currentWord;
		} else sentence += " " + currentWord;
	    }

	    input.setText(sentence);
            System.out.println("Last Typed Word: /" +currentWord+"/");
            //Attempt to decode the last typed word until a valid word is found
            //Update pq predictions based on the last typed word
            fillWords( sentence, words );
            System.out.println("SPACE: Words: " + Arrays.toString(words));
            regressionPrediction( words, wordCount, level );
            printTopResults(pq);
            pastpq = pq;
            currentWord ="";

	    /* Add current top word in pq to sentence,
             * print out message if pq is empty
             * update pq based on word just added
             */ 
	} else if (code == KeyEvent.VK_ENTER ) {
	    if(pq != null&&pq.size()!=0){
                sentence = removeChars(sentence, currentWord.length()-1);
                
		lettersSaved += 1+ pq.peek().getKey().length()-currentWord.length();
                System.out.println("Letters Saved: " + lettersSaved);
                
                
                input.setText(sentence+pq.peek().getKey()+" ");
                wordCount++;
                sentence = removeChars(input.getText(),1);
                System.out.println("ENTER: Sentence: /"+sentence+"/");
                
                fillWords( sentence, words );
                System.out.println("ENTER: Words: " + Arrays.toString(words));
                regressionPrediction( words, wordCount, level );
                printTopResults(pq);
                pastpq = pq;
	    } else{
		System.out.println("No current prediction, please enter more words");
	    }
	    currentWord="";
	    /* New sentence if a space follows,
	     * reset current word and revert to smallest gram
	     */
		
	} else if (code == KeyEvent.VK_PERIOD ) {
	    // END OF SENTENCE
	    wordCount = 0;
	    
	} else if (code == KeyEvent.VK_BACK_SPACE){
            System.out.println("Back Space");
            if(input.getText().equals(""))
		wordCount=0;
            //All normal alphanumerics
	    /*else if ( code == KeyEvent.VK_SHIFT && lastCode == KeyEvent.VK_SPACE) {
		if( pastpq != null ) {
		sentence = removeChars( sentence, currentWord.length() );

		input.setText( sentence+ " "+ pastpq.peek().getKey() + " ");
		sentence = input.getText();
		currentWord = "";
		wordCount++;
		code = KeyEvent.VK_SPACE;
	    }    
	      */
	    /* New character typed, update pq
             * revert to smaller gram if no ngram exists,
             * print out error if pq is null
             */
	} else if ( wordCount > 0 ) {
	    /*	    
		    if( lastCode == KeyEvent.VK_SPACE ) {
		currentWord = s;
	    }
            sentence += s;
	    String tempSentence = removeLastWord( sentence );
	    
	    String[] words = new String[level-1];
	    fillWords( tempSentence, words );

	    pq = updateList();
	    //	    System.out.println( pq );
	    

	    int recount = level;
	    if( pq == null ) {
		while( pq == null && recount > 1) {
		    regressionPrediction( words, wordCount, level-1 );
		    pq = updateList();
		    System.out.println( "AFTER: " + pq );
		    recount--;
		}
	    }
	    printTopResults();
	    pastpq = pq;
        }
        lastCode = code;*/
	    System.out.println("LETTER: currentWord: /"+currentWord+"/");
            
            regressionPredictionLetter(words, wordCount,level);
            printTopResults(pq);
	}
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
    
    public void regressionPrediction(String[] words, int wCount, int gramLevel) {
	if( wCount >= gramLevel-1 ) {
	    levelPrediction( gramLevel, words );
	} else {
	    levelPrediction( wCount+1, words );
	}
    }
	
    public void regressionPredictionLetter(String[] words, int wCount, int gramLevel) {
        if( wCount >= gramLevel-1 ) {
            levelPredictionLetter( gramLevel, words );
        } else {
            levelPredictionLetter( wCount+1, words );
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

    public void levelPredictionLetter( int gramLevel, String[] words ) {
        if( gramLevel == 4 ) {
            if( quadgramPredictor.canPredict( words[gramLevel-2], words[gramLevel-3], words[gramLevel-4] )  ){
		pq = quadgramPredictor.predict(  words[gramLevel-2], words[gramLevel-3], words[gramLevel-4] );
                updateList();
                if(pq.size()==0)
                    levelPredictionLetter(gramLevel-1,words);
            } else {
                levelPredictionLetter( gramLevel - 1, words );
            }
        } else if( gramLevel == 3 ) {
            if ( trigramPredictor.canPredict( words[gramLevel-2], words[gramLevel-3] ) ) {
                pq = trigramPredictor.predict(  words[gramLevel-2], words[gramLevel-3] );
                updateList();
                if(pq.size()==0)
                    levelPredictionLetter(gramLevel-1,words);
            } else {
                levelPredictionLetter( gramLevel - 1, words  );
            }
        } else if( gramLevel == 2 ) {
            if( bigramPredictor.canPredict( words[gramLevel-2] ) ) {
                pq = bigramPredictor.predict( words[gramLevel-2] );
                updateList();
                if(pq.size()==0)
                    levelPredictionLetter(gramLevel-1,words);
            } else {
                levelPredictionLetter( gramLevel - 1, words );
            }
        } else if( gramLevel == 1 ){
            pq = null;
        }
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
	if( s.contains( " " ) ) {
	String ns = s.substring(0,s.lastIndexOf(" "));
	return ns;
	} else return "";
    }
    public String removeTrailingSpace(String s){
        String ns=s.substring(s.lastIndexOf(" "));
        return ns;
    }
    public void updateList(){

        if( pq!= null ) {

            PriorityQueue<Map.Entry<String,Integer>> updatedpq = new PriorityQueue<Map.Entry<String,Integer>>(20000, new pqComparator() );
            
	    System.out.println( "PQ: " + pq );
	    //System.out.println( "cur2: " + currentWord );
	    for( Map.Entry<String,Integer> element: pq ) {
		if( element.getKey().startsWith( currentWord ) ) {
                    updatedpq.add( element );
		}
	    }
	    
	    pq = updatedpq;
	} else pq = null;

    }
    public void printTopResults( PriorityQueue<Map.Entry<String,Integer>> predictions ) {
        if( predictions != null ) {
	    int size = predictions.size();
            PriorityQueue<Map.Entry<String,Integer>> temp = new PriorityQueue<Map.Entry<String,Integer>>(5, new pqComparator() );
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
		output.setText( "No predictions" );
	    }
        }
        
    }
}

