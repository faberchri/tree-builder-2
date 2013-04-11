package ch.uzh.agglorecommender.visu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 *  The view component of the M-V-C-pattern used for the clustering subsystem.
 *  (M:TreeBuilder-V:TreeVisualizer-C:GUIClusteringController)
 */
public class TreeVisualizer implements Observer{
	
	/**
	 * JPanels which contains the JUNG items for cluster tree visualization.
	 */
	private TreePanel vbC;
	private TreePanel vbU;
		
	private JButton playButton = new JButton("Interrupt");
	
	private boolean isPaused = false;
		
	public TreeVisualizer(ClusteringStatusPanel clusteringStatusPanel) {
		initVisualization(clusteringStatusPanel);
	}
	
	/**
	 * Set up the facilities for the tree structure representation.
	 * 
	 * @param clusteringStatusPanel the status panel to add to the gui
	 */
	private void initVisualization(
			JPanel clusteringStatusPanel) {

		// Set System L&F
		try {
			UIManager.setLookAndFeel(
					UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		// Initialize Visualization Frame		
		JFrame frame = new JFrame("Agglomerative Clustering");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		
		// Tree Panes
		JSplitPane treeSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		treeSplit.setOneTouchExpandable(true);
        treeSplit.setResizeWeight(0.5);
        frame.getContentPane().add(treeSplit, BorderLayout.CENTER);
                
		// Instantiate TreePanel
		vbC = new TreePanel();
		vbC.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Content Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
		vbU = new TreePanel();
		vbU.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "User Data Cluster Tree", TitledBorder.CENTER, TitledBorder.CENTER));
	
		treeSplit.setLeftComponent(vbU);
		treeSplit.setRightComponent(vbC);
       
        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 0));
        controlPanel.add(playButton);
        controlPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Control", TitledBorder.CENTER, TitledBorder.CENTER)); 
//        frame.getContentPane().add(controlPanel, BorderLayout.PAGE_END);
        
        // ClusteringStatusPanel
        clusteringStatusPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Progress", TitledBorder.CENTER, TitledBorder.CENTER));
//        frame.getContentPane().add(monitorPanel, BorderLayout.PAGE_START);
        
		JPanel controlAndMonitorPanel = new JPanel(new BorderLayout());
		controlAndMonitorPanel.add(new JScrollPane(controlPanel),BorderLayout.WEST);
		controlAndMonitorPanel.add(new JScrollPane(clusteringStatusPanel),BorderLayout.CENTER);
		
        frame.getContentPane().add(controlAndMonitorPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
		
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
		
	public boolean isPaused() {
		return isPaused;
	}
	
	public void setPlayButtonActionListener(ActionListener listener) {
		for (ActionListener l : playButton.getActionListeners()) {
			playButton.removeActionListener(l);
		}
		playButton.addActionListener(listener);
	}
	
	public void deactivatePlayButton() {
		playButton.setEnabled(false);
	}

//	private class PlayButton extends JButton implements MouseListener {
//		
//		public PlayButton() {
//			addMouseListener(this);
//			this.setText("Interrupt");
//		}
//
//		@Override
//		public void mouseClicked(MouseEvent e) {
//			// nothing to do			
//		}
//
//		@Override
//		public void mousePressed(MouseEvent e) {
//			// nothing to do			
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			if (! e.getSource().equals(this)) return;
//			if (isPaused) {
//				this.setText("Interrupt");
//				isPaused = false;
//			} else {
//				this.setText("Continue");
//				isPaused = true;
//			}			
//		}
//
//		@Override
//		public void mouseEntered(MouseEvent e) {
//			// nothing to do			
//		}
//
//		@Override
//		public void mouseExited(MouseEvent e) {
//			// nothing to do			
//		}
//	}

	@Override
	public void update(IClusterSet<INode> userClusterSet,
			IClusterSet<INode> contentClusterSet) {
		vbC.updateGraph(contentClusterSet.getUnmodifiableView());
		vbU.updateGraph(userClusterSet.getUnmodifiableView());
		
	}
	
}
