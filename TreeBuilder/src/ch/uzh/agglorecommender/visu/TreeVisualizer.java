package ch.uzh.agglorecommender.visu;

import java.awt.Container;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.JFrame;


import ch.uzh.agglorecommender.clusterer.Counter;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.visu.Display;
import ch.uzh.agglorecommender.visu.VisualizationBuilder;



public class TreeVisualizer {

	/**
	 * Frame for tree structure ch.uzh.agglorecommender.visu.
	 */
	private JFrame visuFrame;
	
	/**
	 * References to open node sets.
	 */
	private Set<INode> userNodes, contentNodes;
	
	/**
	 * Set up the facilities for the tree structure representation.
	 * 
	 * @param counter
	 */
	public void initVisualization(Counter counter,
			Set<INode> userNodes,
			Set<INode> contentNodes) {
		this.contentNodes = contentNodes;
		this.userNodes = userNodes;
		// Start Display of control Data and hang in to counter
		Display display = new Display();
		display.start(counter);
		counter.setDisplay(display);
		
		// Initialize Visualization Frame
        visuFrame = new JFrame();
        visuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	/**
	 * Creates a graphical representation of the current state of the cluster tree.
	 * @param frame
	 * @param content
	 */
	public void visualize() {
		
		Container content = visuFrame.getContentPane();
		
		// Instantiate VisualizationBuilder
		VisualizationBuilder vb = new VisualizationBuilder(contentNodes, userNodes);
		
		// Add Content to Swing Panel
        content.removeAll();
        content.add(vb);
        
        // Repack Frame
        visuFrame.pack();
        visuFrame.setVisible(true);
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
	
}
