package ch.uzh.agglorecommender.clusterer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.visu.ClusteringStatusPanel;
import ch.uzh.agglorecommender.visu.TreeVisualizer;

/**
 * 
 * A clustering controller with a GUI as view.
 *
 */
public class GUIClusteringController extends ClusteringController {
	
	/**
	* The graphical representation of the tree structure.
	*/
	TreeVisualizer visualizer;
	
	/**
	 * Instantiates a new SimpleClusteringController that controls the passed TreeBuilder.
	 * 
	 * @param treeBuilder the TreeBuilder to control.
	 */
	public GUIClusteringController(TreeBuilder treeBuilder) {
		super(treeBuilder);
	}
	
	/**
	 * Instantiates a new SimpleClusteringController and a new TreeBuilder.
	 */
	public GUIClusteringController() {
		super();
	}
	
	@Override
	public ClusterResult startNewClusteringRun(INodeUpdater nodeUpdater, InitialNodesCreator leafNodes, String pathToWriteSerializedObject) {
		getTreeBuilder().configTreeBuilderForNewRun(nodeUpdater, leafNodes, pathToWriteSerializedObject);
		addTreeBuilderObservers();
		startClustering();
		return terminateClustering();
	}
	
	@Override
	public ClusterResult resumeClusteringRun(String pathToWriteSerializedObject) {
		getTreeBuilder().setPathToWriteSerializedObject(pathToWriteSerializedObject);
		addTreeBuilderObservers();
		startClustering();
		return terminateClustering();
	}
	
	/**
	 * Interrupts the clustering process.
	 */
	private void interruptClustering() {
		getTreeBuilder().interrupt();
	}
	
	/**
	 * Attach the observers (Views) to the TreeBuilder (Model/Observable). 
	 */
	private void addTreeBuilderObservers() {
		ClusteringStatusPanel monitor = new ClusteringStatusPanel();
		getTreeBuilder().addObserver(monitor);
		visualizer = new TreeVisualizer(monitor);
		visualizer.setPlayButtonActionListener(new PlayButtonActionListener());
		getTreeBuilder().addObserver(visualizer);
	}
	
	@Override
	protected ClusterResult terminateClustering() {
		ClusterResult r = getTreeBuilder().getResult();
		visualizer.deactivatePlayButton();
		return r;
	}
	
	/**
	 * 
	 * ActionListener for the play button of the TreeVisualizer that 
	 * starts and stops the clustering process, 
	 *
	 */
	private class PlayButtonActionListener implements ActionListener{
		
		/**
		 * True while clustering is interrupted.
		 */
		private boolean isPaused = false;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton button;
			if (e.getSource() instanceof JButton) {
				button = (JButton) e.getSource();
			} else {
				return;
			}
			
			if (isPaused) {
				button.setText("Interrupt");
				isPaused = false;
				startClustering();
			} else {
				button.setText("Continue");
				isPaused = true;
				interruptClustering();
			}				
		}
		
	}

}
