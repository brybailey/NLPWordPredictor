import java.awt.event.*;
import javax.swing.*;
public class Predict extends AbstractAction {

    JTextArea userArea;
    JTextArea predictArea;
    RankBigrams rankBigrams;
    String lastWord = "";
    
    public Predict( JTextArea userArea, JTextArea predictArea ) {
        super();
	this.userArea = userArea;
	this.predictArea = predictArea;
	rankBigrams = new RankBigrams();
	rankBigrams.readModel( "w2_.txt" );
    }
    public void actionPerformed( ActionEvent e ) {
	String text = userArea.getText();
	//	lastWord = text.substring( text.length()-lastWord.length()-1, text.indexOf( " " ) );

	
	String bestWord = rankBigrams.predict( text )[0];
	predictArea.append(bestWord);
    }
}
