package ch.uzh.agglorecommender.visu;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collection;
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

import ch.uzh.agglorecommender.clusterer.Monitor;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;


public class TreeVisualizer {
	
	/**
	 * JPanels which contains the JUNG items for cluster tree visualization.
	 */
	private TreePanel vbC;
	private TreePanel vbU;
	
	private MonitorPanel monitorPanel;
	
	private boolean isPaused = false;
	
	/**
	 * References to open node sets.
	 */
	private Collection<INode> userNodes, contentNodes;
	private Monitor monitor;
	
	/**
	 * Set up the facilities for the tree structure representation.
	 * 
	 * @param counter
	 */
	public void initVisualization(
			Collection<INode> userNodes,
			Collection<INode> contentNodes,
			Monitor monitor) {
		this.contentNodes = contentNodes;
		this.userNodes = userNodes;
		this.monitor = monitor;

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
		
		// Tree Panes
		JSplitPane treeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		treeSplit.setOneTouchExpandable(true);
        treeSplit.setResizeWeight(0.5);
        frame.getContentPane().add(treeSplit, BorderLayout.NORTH);
        
        // Control & Monitor Panes
        JSplitPane controlSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        treeSplit.setOneTouchExpandable(true);
        treeSplit.setResizeWeight(0.5);
        frame.getContentPane().add(controlSplit, BorderLayout.SOUTH);
        
		// Instantiate TreePanel
		vbC = new TreePanel(contentNodes);
		vbC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Content Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
		vbU = new TreePanel(userNodes);
		vbU.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "User Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
	
		treeSplit.setLeftComponent(vbU);
		treeSplit.setRightComponent(vbC);
       
        // Control Panel
        JPanel controlPanel = new JPanel();
        JButton pB = new PlayButton();
        controlPanel.add(pB);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Clustering Control", TitledBorder.CENTER, TitledBorder.CENTER)); 
//        frame.getContentPane().add(controlPanel, BorderLayout.PAGE_END);
        
        // Monitor Panel
        monitorPanel = new MonitorPanel(monitor);
		monitorPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Monitor", TitledBorder.CENTER, TitledBorder.CENTER));
//        frame.getContentPane().add(monitorPanel, BorderLayout.PAGE_START);
        
        controlSplit.setLeftComponent(controlPanel);
        controlSplit.setRightComponent(monitorPanel);
		
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
		monitorPanel.update();
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
			log.info(node.toString() + "|\t" + node.getNumericalAttributesString()
					+ "|\t" + node.getNominalAttributesString());
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
