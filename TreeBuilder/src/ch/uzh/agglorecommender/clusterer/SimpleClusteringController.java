package ch.uzh.agglorecommender.clusterer;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;

/**
 * 
 * A simple clustering controller that does not allow any user interaction during
 * clustering. The M-V-C pattern with this context does not have a view.
 *
 */
public class SimpleClusteringController extends ClusteringController {

	/**
	 * Instantiates a new SimpleClusteringController that controls the passed TreeBuilder.
	 * 
	 * @param treeBuilder the TreeBuilder to control.
	 */
	public SimpleClusteringController(TreeBuilder treeBuilder) {
		super(treeBuilder);
	}
	
	/**
	 * Instantiates a new SimpleClusteringController and a new TreeBuilder.
	 */
	public SimpleClusteringController() {
		super();
	}

	@Override
	public ClusterResult startNewClusteringRun(INodeUpdater nodeUpdater,
			InitialNodesCreator leafNodes, String pathToWriteSerializedObject) {
		getTreeBuilder().configTreeBuilderForNewRun(nodeUpdater, leafNodes, pathToWriteSerializedObject);
		startClustering();
		return terminateClustering();
	}

	@Override
	public ClusterResult resumeClusteringRun(String pathToWriteSerializedObject) {
		getTreeBuilder().setPathToWriteSerializedObject(pathToWriteSerializedObject);
		startClustering();
		return terminateClustering();
	}

}
