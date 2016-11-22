/*  
 *  This file is part of the computer assignment for the
 *  Natural Language Processing course at Williams.
 * 
 *  Author: Johan Boye
 */  

package nlp;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


/**
 *   A graphical interface to the information retrieval system.
 */
public class SearchGUI extends JFrame {

    /**  The indexer creating the search index. */
    Indexer indexer;

    /**  The returned documents. */
    private PostingsList results; 
	
    /**  Directories that should be indexed. */
    LinkedList<String> dirNames = new LinkedList<String>();

    /**  The query type (either intersection, phrase, or ranked). */
    int queryType = Index.INTERSECTION_QUERY;

    /**  The ranking type (either tf-idf, pagerank, or combination). */
    int rankingType = Index.TF_IDF;

    /** The patterns matching non-standard words (e-mail addresses, etc.) */
    String patterns_file = null;
		
    /** The file containing the logo. */
    String pic_file = "";
		
    /**  Lock to prevent simultaneous access to the index. */
    Object indexLock = new Object();

    /*  
     *   Common GUI resources
     */
    public JTextField queryWindow = new JTextField( "", 28 );
    public JTextArea resultWindow = new JTextArea( "", 23, 28 );
    private JScrollPane resultPane = new JScrollPane( resultWindow );
    private Font queryFont = new Font( "Arial", Font.BOLD, 24 );
    private Font resultFont = new Font( "Arial", Font.BOLD, 16 );
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu( "File" );
    JMenu optionsMenu = new JMenu( "Search options" );
    JMenu rankingMenu = new JMenu( "Ranking score" ); 
    JMenuItem saveItem = new JMenuItem( "Save index and exit" );
    JMenuItem quitItem = new JMenuItem( "Quit" );
    JRadioButtonMenuItem intersectionItem = new JRadioButtonMenuItem( "Intersection query" );
    JRadioButtonMenuItem phraseItem = new JRadioButtonMenuItem( "Phrase query" );
    JRadioButtonMenuItem rankedItem = new JRadioButtonMenuItem( "Ranked retrieval" );
    JRadioButtonMenuItem tfidfItem = new JRadioButtonMenuItem( "tf-idf" );
    JRadioButtonMenuItem pagerankItem = new JRadioButtonMenuItem( "PageRank" );
    JRadioButtonMenuItem combinationItem = new JRadioButtonMenuItem( "Combination" );
    ButtonGroup queries = new ButtonGroup();
    ButtonGroup ranking = new ButtonGroup(); 
    public JPanel feedbackBar = new JPanel(); 
    JCheckBox[] feedbackButton = new JCheckBox[10];
    JToggleButton feedbackExecutor = new JToggleButton("New search"); 


    /* ----------------------------------------------- */


    /*
     *   Create the GUI.
     */
    private void createGUI() {
	// GUI definition
	setSize( 600, 650 );
	setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	JPanel p = new JPanel();
	p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
	getContentPane().add(p, BorderLayout.CENTER);
	// Top menu
	menuBar.add( fileMenu );
	menuBar.add( optionsMenu );
	menuBar.add( rankingMenu );
	fileMenu.add( quitItem );
	optionsMenu.add( intersectionItem );
	optionsMenu.add( phraseItem );
	optionsMenu.add( rankedItem );
	rankingMenu.add( tfidfItem ); 
	rankingMenu.add( pagerankItem ); 
	rankingMenu.add( combinationItem ); 
	queries.add( intersectionItem );
	queries.add( phraseItem );
	queries.add( rankedItem );
	ranking.add( tfidfItem ); 
	ranking.add( pagerankItem );
	ranking.add( combinationItem ); 
	intersectionItem.setSelected( true );
	tfidfItem.setSelected( true );
	p.add( menuBar );
	// Logo
	JPanel p1 = new JPanel();
	p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));
        p1.add( new JLabel( new ImageIcon( pic_file )));
	p.add( p1 );
	JPanel p3 = new JPanel();
	// Search box
	p3.setLayout(new BoxLayout(p3, BoxLayout.X_AXIS));
	p3.add( queryWindow );
	queryWindow.setFont( queryFont );
	p.add( p3 );
	// Display area for search results
	p.add( resultPane );
	resultWindow.setFont( resultFont );
	// Show the interface
	setVisible( true );
		
	Action search = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
		    // Turn the search string into a linked list
		    StringTokenizer tok = new StringTokenizer( queryWindow.getText(), " " );
		    ArrayList<String> searchterms = new ArrayList<String>();
		    while ( tok.hasMoreTokens() ) {
			searchterms.add( tok.nextToken() );
		    }
		    // Search and print results. Access to the index is synchronized since
		    // we don't want to search at the same time we're indexing new files
		    // (this might corrupt the index).
		    synchronized ( indexLock ) {
			results = indexer.index.search( searchterms, queryType, rankingType ); 
		    }
		    StringBuffer buf = new StringBuffer();
		    if ( results != null ) {
			buf.append( "\nFound " + results.size() + " matching document(s)\n\n" );
			for ( int i=0; i<results.size(); i++ ) {
			    buf.append( " " + i + ". " );
			    String filename = indexer.index.docIDs.get( "" + results.get(i).docID );
			    if ( filename == null ) {
				buf.append( "" + results.get(i).docID );
			    }
			    else {
				buf.append( filename );
			    }
			    if ( queryType == Index.RANKED_QUERY ) {
				buf.append( "   " + String.format( "%.5f", results.get(i).score )); 
			    }
			    buf.append( "\n" );
			}
		    }
		    else {
			buf.append( "\nFound 0 matching document(s)\n\n" );
		    }
		    resultWindow.setText( buf.toString() );
		    resultWindow.setCaretPosition( 0 );
		}
	    };
	queryWindow.registerKeyboardAction( search,
					    "",
					    KeyStroke.getKeyStroke( "ENTER" ),
					    JComponent.WHEN_FOCUSED );
	
	Action quit = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
		    System.exit( 0 );
		}
	    };
	quitItem.addActionListener( quit );

	
	Action setIntersectionQuery = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
		    queryType = Index.INTERSECTION_QUERY;
		}
	    };
	intersectionItem.addActionListener( setIntersectionQuery );
		
	Action setPhraseQuery = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
		    queryType = Index.PHRASE_QUERY;
		}
	    };
	phraseItem.addActionListener( setPhraseQuery );
		
	Action setRankedQuery = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
		    queryType = Index.RANKED_QUERY;
		}
	    };
	rankedItem.addActionListener( setRankedQuery );

	Action setTfidfRanking = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			rankingType = Index.TF_IDF;
		}
		};
	tfidfItem.addActionListener( setTfidfRanking );
		
	Action setPagerankRanking = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			rankingType = Index.PAGERANK;
		}
		};
	pagerankItem.addActionListener( setPagerankRanking );
		
	Action setCombinationRanking = new AbstractAction() {
		public void actionPerformed( ActionEvent e ) {
			rankingType = Index.COMBINATION;
		}
		};
	combinationItem.addActionListener( setCombinationRanking );
    }

 
    /* ----------------------------------------------- */
   

    /**
     *   Calls the indexer to index the chosen directory structure.
     *   Access to the index is synchronized since we don't want to 
     *   search at the same time we're indexing new files (this might 
     *   corrupt the index).
     */
    private void index() {
	indexer = new Indexer( patterns_file );
	synchronized ( indexLock ) {
	    resultWindow.setText( "\n  Indexing, please wait..." );
	    for ( int i=0; i<dirNames.size(); i++ ) {
		File dokDir = new File( dirNames.get( i ));
		indexer.processFiles( dokDir );
	    }
	    resultWindow.setText( "\n  Done!" );
	}
    };


    /* ----------------------------------------------- */


    /**
     *   Decodes the command line arguments.
     */
    private void decodeArgs( String[] args ) {
	int i=0, j=0;
	while ( i < args.length ) {
	    if ( "-d".equals( args[i] )) {
		i++;
		if ( i < args.length ) {
		    dirNames.add( args[i++] );
		}
	    }
	    else if ( "-p".equals( args[i] )) {
		i++;
		if ( i < args.length ) {
		    patterns_file = args[i++];
		}
	    }
	    else if ( "-l".equals( args[i] )) {
		i++;
		if ( i < args.length ) {
		    pic_file = args[i++];
		}
	    }
	    else {
		System.err.println( "Unknown option: " + args[i] );
		break;
	    }
	}
    }				    


    /* ----------------------------------------------- */


    public static void main( String[] args ) {
	SearchGUI s = new SearchGUI();
	s.decodeArgs( args );
	s.createGUI();
	s.index();
    }
    
}
