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
    String lastWord="";
    String secondLastWord="";
    int lastSpaceIndex=0;
    
    
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
        
        // System.out.println(code);
        System.out.println(s);
        
        //Spacebar pressed
        if(code == KeyEvent.VK_PERIOD){
            wordCount++;
            
            //Space pressed for the second time
            if(code == lastCode){
                input.setText(sentence+output.getText());
                sentence = input.getText();
                
            }
            System.out.println("WC: "+wordCount);
            
            //One word in sentence, use bigram model
            if(wordCount>0 && wordCount<2){
                System.out.println("BIGRAM parsing for: ("+sentence+")");
                //sentence=sentence.substring(sentence.lastIndexOf("."));
               System.out.println("Remove trailing space: ("+sentence+")");
                lastWord = sentence.substring(sentence.lastIndexOf(".")+1,sentence.length());
               
              System.out.println("Last word: (" + lastWord+")");
                
                pq = bigramPredictor.predict(lastWord);
                output.setText(pq.poll().getKey());

            //At least two words in sentence, use trigram model
            } else if(wordCount>1){
                System.out.println("TRIGRAM parsing for (" +sentence+")");
                String removeLastWord = sentence.substring(0,sentence.lastIndexOf("."));
                System.out.println("Remove last word: ("+removeLastWord+")");
                secondLastWord = removeLastWord.substring(removeLastWord.lastIndexOf(".")+1,removeLastWord.length());
                System.out.println("Second to last word: (" +secondLastWord+")");
                
                lastWord=sentence.substring(sentence.lastIndexOf(".")+1);
                
                System.out.println("Last word: (" +lastWord+")");
                
                pq = trigramPredictor.predict(secondLastWord,lastWord);
                output.setText(pq.poll().getKey());
                
            }else{
                
                lastWord = sentence;
            }
          
        
        // sentence +=s;
        //Delete
    } else if(code == KeyEvent.VK_BACK_SPACE){
        System.out.println("Back Space");
        
        //All normal alphanumerics
    }else {
        sentence +=s;
        
    }
    lastCode = code;
    
}

}