package ch.uzh.agglorecommender.visu;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;


public class TreeVisualizer {

	/**
	 * JPanels which contains the JUNG items for cluster tree visualization.
	 */
	private VisualizationBuilder vbC;
	private VisualizationBuilder vbU;
	
	private boolean isPaused = false;
	
	/**
	 * References to open node sets.
	 */
	private Set<INode> userNodes, contentNodes;
	
	/**
	 * Set up the facilities for the tree structure representation.
	 * 
	 * @param counter
	 */
	public void initVisualization(
			Set<INode> userNodes,
			Set<INode> contentNodes) {
		this.contentNodes = contentNodes;
		this.userNodes = userNodes;

		// Set System L&F
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Initialize Visualization Frame		
		JFrame frame = new JFrame("Cluster trees");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
				
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setOneTouchExpandable(true);
        split.setResizeWeight(0.5);
        frame.getContentPane().add(split, BorderLayout.CENTER);
       
        JPanel controlPanel = new JPanel();
        JButton pB = new PlayButton();
        controlPanel.add(pB);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Clustering Control", TitledBorder.CENTER, TitledBorder.CENTER)); 
        frame.getContentPane().add(controlPanel, BorderLayout.PAGE_END);

		
		// Instantiate VisualizationBuilder
		vbC = new VisualizationBuilder(contentNodes);
		vbC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Content Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
		vbU = new VisualizationBuilder(userNodes);
		vbU.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "User Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
	
		split.setLeftComponent(vbU);
		split.setRightComponent(vbC);
		
		frame.pack();
		frame.setVisible(true);
		visualize();		
	}
		
	/**
	 * Creates a graphical representation of the current state of the cluster tree.
	 * @param frame
	 * @param content
	 */
	public void visualize() {
			vbC.updateGraph(contentNodes);
			vbU.updateGraph(userNodes);
	}
	
	/**
	 * Prints a textual representation of all nodes
	 * (including its attributes) contained in the passed set on stdout.
	 * 
	 * @param set the set of nodes to print.
	 * @param nodeNames the description of the set to print.
	 */
	private void printAllNodesInSet(Set<INode> set, String nodeNames){
		Logger log = TBLogger.getLogger(getClass().getName());
		log.info("-----------------------");
		log.info(nodeNames);
		INode[] setArr = set.toArray(new INode[set.size()]);
		Arrays.sort(setArr);
		for (INode node : setArr) {
			log.info(node.toString()+"|\t"+node.getAttributesString());
		}
		log.info("-----------------------");
	}
	
	/**
	 * Prints all textual representation  of all open user nodes on stdout.
	 */
	public void printAllOpenUserNodes() {
		printAllNodesInSet((Set)userNodes, "User Nodes:");
	}
	
	/**
	 * Prints all textual representation  of all open content nodes on stdout.
	 */
	public void printAllOpenMovieNodes() {
		printAllNodesInSet((Set)contentNodes, "ContentNodes:");
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	private class PlayButton extends JButton implements MouseListener {
		
		public PlayButton() {
			addMouseListener(this);
			this.setText("Interrupt");
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// nothing to do			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// nothing to do			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (! e.getSource().equals(this)) return;
			if (isPaused) {
				this.setText("Interrupt");
				isPaused = false;
			} else {
				this.setText("Continue");
				isPaused = true;
			}			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// nothing to do			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// nothing to do			
		}
		
	}
	
}
