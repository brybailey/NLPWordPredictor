import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class WordPredictor extends Frame {
	
    private Label userLabel;
    private JTextArea userInput;    
    public static JTextArea predictedWords;
    
    public WordPredictor() {
	setLayout( new FlowLayout() );
	setExtendedState(Frame.MAXIMIZED_BOTH);
	userLabel = new Label("Type here:");
	add(userLabel);


	userInput = new JTextArea(46,20);
	userInput.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "predict" );
	userInput.getActionMap().put("predict", new Predict() );


	predictedWords = new JTextArea(5,15);
	add( userInput );
	add( predictedWords );
	setVisible( true );
	toFront();
	addWindowListener( new WindowAdapter() {
		public void windowClosing(WindowEvent we) {
		    dispose();
		}
		
	    });
    }
    
    public static void main( String[] args ) {
	WordPredictor predictWords = new WordPredictor();
    }
    
}
