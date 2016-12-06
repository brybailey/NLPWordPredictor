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
    
    
    PriorityQueue<Map.Entry<String,Integer>> pq;
    
    public Listener( JTextField input, JTextField output, int level ){
        super();
        this.level = level;
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
        System.out.println(s);
        //Spacebar pressed
        if(code == KeyEvent.VK_PERIOD){
            wordCount++;
            //currentWord="";
            //Space pressed for the second time, grab best match from pq, update sentence, proceed with next prediction
            if(code == lastCode){
                if(pq!=null){
                sentence = sentence.substring(0,sentence.length()-currentWord.length());
                input.setText(sentence+pq.peek().getKey());
                sentence = input.getText();
                }
            }
            System.out.println("WC: "+wordCount);
            String[] words = new String[level-1];
            //One word in sentence, use bigram model
            if(wordCount>0 && (wordCount<2||level==2) ){
                fillWords( sentence, words );
                System.out.println(Arrays.toString(words));

                if( bigramPredictor.canPredict(first)) {
                    pq = bigramPredictor.predict(first);
                } else pq = null;
                
                //At least two words in sentence, use trigram model
            } else if(wordCount>1 && (wordCount<3||level==3)){
                 fillWords( sentence, words );
                System.out.println(Arrays.toString(words));

                if( trigramPredictor.canPredict(second,first) ) {
                    pq = trigramPredictor.predict(second,first);
                } else if ( bigramPredictor.canPredict(first) ){
                    pq = bigramPredictor.predict(first);
                } else pq = null;
                
            }else if (wordCount>2 && (wordCount<4||level==4) ){
                fillWords( sentence, words );
                System.out.println(Arrays.toString(words));
                if ( quadgramPredictor.canPredict(third,second,first ) ) {
                    pq = quadgramPredictor.predict(third,second,first);
                } else if( trigramPredictor.canPredict(second,first) ) {
                    pq = trigramPredictor.predict(second,first);
                } else if ( bigramPredictor.canPredict(first) ){
                    pq = bigramPredictor.predict(first);
                } else pq = null;
                
            } else {
                first = sentence;
                pq = null;
            }
            printTopResults(pq);
            //Delete
        } else if(code == KeyEvent.VK_BACK_SPACE){
            System.out.println("Back Space");
            
            //All normal alphanumerics
        } else if( wordCount > 0 ) {
            sentence += s;
            updateList(pq);
                        printTopResults( pq );
        }
        lastCode = code;
    }
    
    public void fillWords( String curSentence, String[] array ) {
        
        int wc = wordCount;
        for( int i = array.length-1; i>-1; i-- ) {
            array[i]=getLastWord(curSentence);
            wc--;
            if(wc>1){
            curSentence = removeLastWord(curSentence);
                System.out.println("CUR: "+curSentence);
            }
        }
    }
    
    // Still needs a little work
    public void regressionPrediction(String[] words, int wordCount, int gramLevel, String currentWord) {
	if( gramLevel == 4 && wordCount > 3 ) {
	    if( quadgramPredictor.canPredict( words[gramLevel-1], words[gramLevel-2], words[gramLevel-3] )  ){
		pq = quadgramPredictor.predict(  words[gramLevel-1], words[gramLevel-2], words[gramLevel-3] );
	    } else if( trigramPredictor.canPredict( words[gramLevel-1], words[gramLevel-2] ) ) {
		pq = trigramPredictor.predict(  words[gramLevel-1], words[gramLevel-2] );
	    } else if( bigramPredictor.canPredict( words[gramLevel-1] ) ) {
		pq = bigramPredictor.predict( words[gramLevel-1] );
	    } else pq = null;
	} else if( gramLevel == 3 && wordCount > 2 ) {
	    if( trigramPredictor.canPredict( words[gramLevel-1], words[gramLevel-2] ) ) {
		pq = trigramPredictor.predict(  words[gramLevel-1], words[gramLevel-2] );
	    } else if( bigramPredictor.canPredict( words[gramLevel-1] ) ) {
		pq = bigramPredictor.predict( words[gramLevel-1] );
	    } else pq = null;
	} else if( gramLevel == 2 && wordCount > 1 ) {
	    if( bigramPredictor.canPredict( words[gramLevel-1] ) ) {
		pq = bigramPredictor.predict( words[gramLevel-1] );
	    } else pq = null;
	}
	printTopResults( pq );
    }
	
    public String getLastWord(String s){
        String w = s.substring(s.lastIndexOf(".")+1,s.length());
        return w;
    }
    
    public String getFirstWord(String s) {
        String first = s.substring(0, s.indexOf(" "));
        return first;
    }
    public String removeLastWord(String s){
        String ns = s.substring(0,s.lastIndexOf("."));
        return ns;
    }
    public String removeTrailingSpace(String s){
        String ns=s.substring(s.lastIndexOf("."));
        return ns;
    }
    public void updateList(PriorityQueue<Map.Entry<String,Integer>> oldpq){
        if( pq!= null ) {
            PriorityQueue<Map.Entry<String,Integer>> newpq = new PriorityQueue<Map.Entry<String,Integer>>(200000, new Comparator<Map.Entry<String, Integer>>() {
                public int compare( Map.Entry<String,Integer> arg0,
                                   Map.Entry<String,Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue() );
                }
            });
            
            //	    System.out.println( "CHECK FIRST: " + pq.peek() );
            for( Map.Entry<String,Integer> element: oldpq ) {
                if( element.getKey().startsWith( currentWord ) ) {
                    newpq.add( element );
                }
            }
            oldpq = newpq;
            
        }

    }
    public void printTopResults( PriorityQueue<Map.Entry<String,Integer>> predictions ) {
        if( predictions != null ) {
            PriorityQueue<Map.Entry<String,Integer>> temp = new PriorityQueue<Map.Entry<String,Integer>>(5, new Comparator<Map.Entry<String, Integer>>() {
                public int compare( Map.Entry<String,Integer> arg0,
                                   Map.Entry<String,Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue() );
                }
            });
            String outputText = "";
            for( int i=0; i<predictions.size()&&i<5; i++ ) {
                outputText += predictions.peek().getKey()+" ";
                temp.add(predictions.poll());
            }
            for( Map.Entry<String,Integer> reverse: temp ) {
                predictions.add( reverse );
            }
            //	System.out.println( predictions.peek() + " AND " + temp.peek() );
            output.setText(outputText);
        } else {
            output.setText( "Wow! Cool new word dude!" );
        }
        
    }
}