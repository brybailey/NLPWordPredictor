import java.awt.event.*;
import javax.swing.*;
public class Predict extends AbstractAction {
    public Predict() {
        super();
    }
    public void actionPerformed( ActionEvent e ) {
        WordPredictor.predictedWords.append("Hello World");
    }
}
