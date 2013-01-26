package ch.uzh.agglorecommender.visu;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;

import ch.uzh.agglorecommender.clusterer.Counter;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;


public class TreeVisualizer {

	/**
	 * JPanels which contains the JUNG items for cluster tree visualization.
	 */
	private VisualizationBuilder vbC;
	private VisualizationBuilder vbU;
	
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
		
		// Initialize Visualization Frame
		JFrame frame = new JFrame("Cluster trees");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setOneTouchExpandable(true);
		frame.getContentPane().add(split);
		
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
	
}
