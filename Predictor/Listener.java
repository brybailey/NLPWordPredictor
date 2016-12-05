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
    BigramPredictor bigramPredictor = new BigramPredictor();
    TrigramPredictor trigramPredictor = new TrigramPredictor();
    
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
    
    PriorityQueue<Map.Entry<String,Integer>> pq;
    
    public Listener( JTextField input, JTextField output){
        super();
        this. input = input;
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
            currentWord="";
            //Space pressed for the second time
            if(code == lastCode){
                input.setText(sentence+output.getText());
                sentence = input.getText();
                
            }
            System.out.println("WC: "+wordCount);
            
            //One word in sentence, use bigram model
            if(wordCount>0 && wordCount<2){
                first = getLastWord(sentence);
                pq = bigramPredictor.predict(first);
                output.setText(pq.poll().getKey());
                //At least two words in sentence, use trigram model
            } else if(wordCount>1){
                first = getLastWord(sentence);
                sentence = removeLastWord(sentence);
                second = getLastWord(sentence);
                pq = trigramPredictor.predict(second,first);
                output.setText(pq.poll().getKey());
                
            }else{
                first = sentence;
            }
            //Delete
        } else if(code == KeyEvent.VK_BACK_SPACE){
            System.out.println("Back Space");
            
            //All normal alphanumerics
        } else if( wordCount > 0 ) {
            sentence += s;
            
            
            PriorityQueue<Map.Entry<String,Integer>> newpq = new PriorityQueue<Map.Entry<String,Integer>>(200000, new Comparator<Map.Entry<String, Integer>>() {
                public int compare( Map.Entry<String,Integer> arg0,
                                   Map.Entry<String,Integer> arg1) {
                    return arg1.getValue().compareTo(arg0.getValue() );
                }
            });
            System.out.println("CW("+currentWord+")");
            for( Map.Entry<String,Integer> element: pq ) {
                if( element.getKey().startsWith( currentWord ) ) {
                    newpq.add( element );
                }
            }
            pq =newpq;
            if(pq!=null &&pq.peek()!=null ){
                output.setText( pq.poll().getKey() );
                System.out.println(" WORD COUNT CHECK " );
            } else{
                System.out.println("Failed to match word: " + currentWord);
            }
            
        }
        lastCode = code;
    }
    
    public String getLastWord(String s){
        String w = s.substring(s.lastIndexOf(".")+1,s.length());
        return w;
    }
    public String removeLastWord(String s){
        String ns = s.substring(0,s.lastIndexOf("."));
        return ns;
    }
    public String removeTrailingSpace(String s){
        String ns=s.substring(s.lastIndexOf("."));
        return ns;
    }
}






























