/*
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

public class GUI extends JFrame{
    private JTextField input;
    private JTextField output;
    
    
    
    
    public GUI(int level, int viterbi){
	JFrame frame = new JFrame();
	
	Container contentPane = frame.getContentPane();
	input = new JTextField();
	output = new JTextField(10);
	input.addKeyListener(new Listener(input,output,level,viterbi));
	

	contentPane.add(input, BorderLayout.NORTH);
	contentPane.add(output,BorderLayout.CENTER);
	frame.pack();
	frame.setSize(500,500);
	frame.setVisible(true);
    }

    static void printHelpMessage() {
	System.err.println( "The following parameters are available: " );
	System.err.println( " -n <number> : either 2, 3 or 4 (=bi-, tri-, or quad-gram) word prediction " );
	System.err.println( " -v <number> : either 2 (=bigram) or 3 (=trigram) letter-level viterbi decoding" );
    }

    public static void main(String[] args){
	int level = 2;
	int viterbi = 2;
	int i = 0;
	while( i<args.length ) {
	    if( args[i].equals( "-n" ) ) {
		i++;    
		if( i <args.length ) {
		    level = new Integer( args[i++] );
		} else {
		    printHelpMessage();
		    return;
		}
	    } else if( args[i].equals( "-v" ) ) {
		i++;
		if ( i<args.length ) {
		    viterbi = new Integer( args[i++] );
		} else {
		    printHelpMessage();
		    return;
		}
	    }
	}

	GUI test = new GUI(level, viterbi);
	System.out.println("Created GUI");
    }
   
}