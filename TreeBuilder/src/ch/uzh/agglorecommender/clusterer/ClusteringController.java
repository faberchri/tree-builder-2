package ch.uzh.agglorecommender.clusterer;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;

/**
 *  The controller component of the M-V-C-pattern used for the clustering subsystem.
 *  (M:TreeBuilder-V:TreeVisualizer-C:ClusteringController)
 */
public abstract class ClusteringController {
	
	/**
	* The data model.
	*/
	private TreeBuilder treeBuilder;

	/**
	 * Instantiates a new ClusteringController that controls the passed TreeBuilder.
	 * 
	 * @param treeBuilder the TreeBuilder to control.
	 */
	public ClusteringController(TreeBuilder treeBuilder) {
		if (treeBuilder == null) {
			treeBuilder = new TreeBuilder();
		}
		this.treeBuilder = treeBuilder;
	}
	
	/**
	 * Instantiates a new ClusteringController and a new TreeBuilder.
	 */
	public ClusteringController() {
		treeBuilder = new TreeBuilder();
	}

	/**
	 * Configurates the TreeBuilder of this controller, starts the clustering
	 * process and returns the cluster tree as a ClusterResult.
	 * 
	 * @param nodeUpdater the node updater to use in the clustering process
	 * @param leafNodes the initial clusters used as leaves in the cluster tree
	 * @param pathToWriteSerializedObject the file system location to write
	 * the serialized TreeBuilder
	 * @return the cluster result as ClusterResult object.
	 */
	public abstract ClusterResult startNewClusteringRun(INodeUpdater nodeUpdater,
			InitialNodesCreator leafNodes, String pathToWriteSerializedObject);

	/**
	 * Resumes a previously started clustering process.
	 * 
	 * @param pathToWriteSerializedObject location for the new serialization file.
	 * If null no file is created.
	 * @return the result of the clustering process
	 */
	public abstract ClusterResult resumeClusteringRun(String pathToWriteSerializedObject);

	/**
	 * Fetches the ClusteringResult object from the TreeBuilder.
	 * The thread calling this method does only return after
	 * the clustering process running in a different thread started with
	 * startClustering is completed. (Blocking)
	 * @return the clustering result
	 */
	protected ClusterResult terminateClustering() {
		return getTreeBuilder().getResult();
	}
	
	/**
	 * Gets the controlled TreeBuilder.
	 * @return the TreeBuilder of this controller.
	 */
	protected final TreeBuilder getTreeBuilder() {
		return treeBuilder;
	}
	
	/**
	 * Starts the clustering process of the TreeBuilder in a new thread.
	 */
	protected final void startClustering() {
		Thread t = new Thread(treeBuilder);
		t.start();
	}

}