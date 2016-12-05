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
    
    
    
    
    public GUI(){
	JFrame frame = new JFrame();
	
	Container contentPane = frame.getContentPane();
	input = new JTextField();
	output = new JTextField(10);
	input.addKeyListener(new Listener(input,output));
	

	contentPane.add(input, BorderLayout.NORTH);
	contentPane.add(output,BorderLayout.CENTER);
	frame.pack();
	frame.setSize(500,500);
	frame.setVisible(true);
    }
    public static void main(String[] args){
	GUI test = new GUI();
	System.out.println("Created GUI");
    }
   
}