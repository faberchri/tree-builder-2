package ch.uzh.agglorecommender.clusterer;
import gnu.trove.iterator.TIntDoubleIterator;
import gnu.trove.map.TIntDoubleMap;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treesearch.CachedMaxCUSearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.ClusterSetIndexed;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSetIndexed;
import ch.uzh.agglorecommender.clusterer.treesearch.IMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.IMergeResult;
import ch.uzh.agglorecommender.clusterer.treesearch.MergeResult;
import ch.uzh.agglorecommender.clusterer.treesearch.NoCommonRatingAttributeSkipMaxCUSearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.SharedMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.util.ToFileSerializer;
import ch.uzh.agglorecommender.visu.Observer;


/**
 * 
 * Implementation of COBWEB inspired hierarchical
 * agglomerative two-dimensional clustering algorithm 
 * for media recommendation generation.
 * 
 *  The model component of the M-V-C-pattern used for the clustering subsystem.
 *  (M:TreeBuilder-V:TreeVisualizer-C:ClusteringController)
 *
 */
public final class TreeBuilder implements Serializable, Observable, Runnable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The set of all root nodes of type user.
	 */
	private IClusterSet<INode> userNodes;
	
	/**
	 * The set of all root nodes of type content.
	 */
	private IClusterSet<INode> contentNodes;
		
	/**
	 * The result of the clustering process.
	 */
	private ClusterResult result;
			
	/**
	 * The tree component factory.
	 */
	private TreeComponentFactory treeComponentFactory;
		
	/**
	 * The updater which performs the introduction 
	 * of a node as attribute in the nodes of the other type.
	 */
	private INodeUpdater nodeUpdater;
		
	/**
	 * The searcher for the best user nodes merge.
	 */
	private IMaxCategoryUtilitySearcher userMCUSearcher;
	
	/**
	 * The searcher for the best content nodes merge.
	 */
	private IMaxCategoryUtilitySearcher contentMCUSearcher;
	
	/**
	 * Each cluster run gets a unique id, which is included in the filename of the
	 * serialized TreeBuilder object.
	 */
	private UUID runId = null;
	
	/**
	 * File system location for the serialized TreeBuilder.
	 */
	private String pathToWriteSerializedObject;
	
	/**
	 * The observers of the TreeBuilder, i.e. the views of the model.
	 */
	private transient List<Observer> observers = new LinkedList<Observer>();
	
	/**
	 * Causes the cluster method to exit after the current cycle completed if true.
	 */
	private boolean isInterrupted = false;
	
	/**
	 * Is true if clustering is completed, i.e. cluster method terminated.
	 */
	private boolean buildCompleted = false;
	
	/**
	 * The logger of this class.
	 */
	private transient Logger log = TBLogger.getLogger(getClass().getName());

	/**
	 * Instantiate a new TreeBuilder.
	 */
	protected TreeBuilder() {

		this.treeComponentFactory = TreeComponentFactory.getInstance();
		this.userMCUSearcher = new NoCommonRatingAttributeSkipMaxCUSearcher(
				new CachedMaxCUSearcher(
						new SharedMaxCategoryUtilitySearcher()));
		this.contentMCUSearcher = new NoCommonRatingAttributeSkipMaxCUSearcher(
				new CachedMaxCUSearcher(
						new SharedMaxCategoryUtilitySearcher()));
	}
			
	/**
	 * Performs the cluster tree creation of the data set.
	 * Upon completion of the clustering a new fully initialized
	 * {@code ClusterResult } object is created and assigned to
	 * {@code result}.
	 */
	private void cluster() {
		
		// Initialize the balancer for the clustering process.
		ClusteringBalancer<INode> balancer = new ClusteringBalancer<INode>(userNodes, contentNodes);

		int cycleCounter = 0;
		
		// Process Nodes
		while (! userNodes.clusteringDone() || ! contentNodes.clusteringDone()) {
			long time = System.nanoTime();
			log.info("------------------------------- start cycle " + cycleCounter + " ---------------------------------");
			
			// check if clustering was interrupted by user interaction
			if (isInterrupted) {
				isInterrupted = false;
				return;
			}
			
			performClusterCycle(balancer.getNextClusterSet());
			
			notifyObservers();
									
			log.info("---------------------- cycle " + cycleCounter + " completed (" + ( (double)(System.nanoTime() - time) / 1000000000.0) + " s) -------------------------");
		
			// serialize this TreeBuilder if necessary according to specified interval.
			// This writes current TreeBuilder state to disk and
			// allows to terminate clustering process and to resume later.
			// The frequency of serialization can be set with
			// ToFileSerializer.serializationTimeInterval
			ToFileSerializer.serializeConditionally(this, pathToWriteSerializedObject, runId);
			
			cycleCounter++;
		} 
				
		log.info("Clustering terminated!");
		// serialize this TreeBuilder if clustering is completed.
		ToFileSerializer.serialize(this, pathToWriteSerializedObject, runId);
		
		if (contentNodes.size() == 1 && userNodes.size() == 1) {
			result = new ClusterResult(
					result.getUserTreeLeavesMap(), result.getContentTreeLeavesMap(), 
					userNodes.getRoot(), contentNodes.getRoot(), runId);
		} else {
			log.severe("clustering terminated before the user or content cluster forests converged to trees");
			System.exit(-1);
		}
		
		// wake up thread that fetches cluster result object
        synchronized(this){
            this.buildCompleted = true;
            notifyAll();
       }
	}
		
	/**
	 * Searches and performs the best merge in the passed set and updates the other set with the new node.
	 * 
	 * @param openSet the set to cluster
	 */
	private void performClusterCycle(IClusterSet<INode> openSet) {
		
		if (openSet == userNodes) {
			log.info("Get closest user nodes & merge them");
			INode newUserNode = merge(searchBestMergeResultIndexed((IClusterSetIndexed<INode>)userNodes, userMCUSearcher), userNodes);

			// Update Trees with info from other tree on current level - only if nodes merged
			if(newUserNode != null) {
				nodeUpdater.updateNodes(newUserNode, contentNodes.getUnmodifiableView()); 
			}
		}
		
		if (openSet == contentNodes) {
			log.info("Get closest content nodes & merge them");
			INode newContentNode = merge(searchBestMergeResultIndexed((IClusterSetIndexed<INode>)contentNodes, contentMCUSearcher), contentNodes);		
			
			// Update Trees with info from other tree on current level - only if nodes merged
			if(newContentNode != null) {
				nodeUpdater.updateNodes(newContentNode, userNodes.getUnmodifiableView()); 
			}
		}
		log.info("number of remaining open nodes: " + openSet.size());
	}
			
	/**
	 * Gets the best merge from the passed set and returns the resulting new node.
	 * 
	 * @param nodes the indexed set of nodes in which the best merge is searched.
	 * @param mcus the max category searcher to use
	 * @return the merge result or null if no possible merge was found.
	 */
	private IMergeResult searchBestMergeResultIndexed(IClusterSetIndexed<INode> nodes, IMaxCategoryUtilitySearcher mcus) {

		if(nodes.size() < 2) return null;

		// get the IMergeResult with the highest utility value
		// from the obtained IMergeResults
		int bestCobinationId = -1;
		TIntDoubleMap shortList = mcus.getMaxCategoryUtilityMerges(nodes.getCombinationsIds(), nodes);
		double max = -Double.MAX_VALUE; // initialized with the smallest possible double value
		TIntDoubleIterator iterator = shortList.iterator();
		for ( int i = shortList.size(); i-- > 0; ) { // faster iteration by avoiding hasNext()
			iterator.advance();
			if (iterator.value() > max) {
				max = iterator.value();
				bestCobinationId = iterator.key();
			}
		}
		if (bestCobinationId == -1) {
			log.severe("Err: Best merge is == null; in: " + getClass().getSimpleName() );
			System.exit(-1);
		}
		
		IMergeResult best = new MergeResult(max, nodes.getCombination(bestCobinationId));

		log.info("Best node merge has category utility of "
				+best.getCategoryUtility() +" and includes: " + best.getNodes() + ", combination id: " + bestCobinationId);

		return best;
	}
	
	/**
	 * Creates a new node and initializes the nodes attributes based on a list of close nodes.
	 * The list of close nodes become the children of the new node.
	 * 
	 * @param mergeResult close nodes which are
	 * used to initialize a new node and will form the new nodes children. 
	 * These nodes are removed from the open set.
	 * 
	 * @param openSet the set of nodes to which the new node is added
	 * and from which the merged nodes are removed. 
	 * 
	 * @return a new node which has the {@code nodesToMerge} as children. 
	 */
	private INode merge(IMergeResult mergeResult, IClusterSet<INode> openSet) {
		Collection<INode> nodesToMerge = mergeResult.getNodes();
		if (nodesToMerge.size() < 2) {
			log.severe("Err: Merge attempt with 1 or less nodes, in: " + getClass().getSimpleName());
			System.exit(-1);
			return null;
		}

		// Create a new node (product of nodesToMerge)
		INode newNode = treeComponentFactory.createInternalNode(nodesToMerge, mergeResult.getCategoryUtility());
		
		// Add new node to open set
		openSet.add(newNode);
		log.fine("New node added to open set: " + newNode);

		// Remove merged nodes from cluster set
		for (INode nodeToMerge : nodesToMerge) {	
			if (!openSet.remove(nodeToMerge)) {
				log.severe("Err: Removal of merged node (" + nodeToMerge + ") from " +openSet +" failed, in: " + getClass().getSimpleName());
				System.exit(-1);
			}
		}	
		return newNode;
	}

	@Override
	public void addObserver(Observer observer) {
		if (observers == null) {
			observers = new LinkedList<Observer>();
		}
		if (observer != null) {
			observers.add(observer);					
		}
	}

	@Override
	public void removeObserver(Observer observer) {
		if (observers == null) return;
		if (observer != null) {
			observers.remove(observer);					
		}	
	}
	
	/**
	 * Update all observers of this observable.
	 */
	private void notifyObservers() {
		for (Observer o : observers) {
			o.update(userNodes, contentNodes);
		}
	}
	
	/**
	 * Configures the TreeBuilder for a new cluster run.
	 * This method needs to be called before the clustering process is started.
	 * 
	 * @param nodeUpdater the node updater to use in the clustering process
	 * @param leafNodes the initial clusters used as leaves in the cluster tree
	 * @param pathToWriteSerializedObject the file system location to write
	 * the serialized TreeBuilder
	 */
	protected void configTreeBuilderForNewRun(INodeUpdater nodeUpdater, InitialNodesCreator leafNodes, String pathToWriteSerializedObject) {
		if (nodeUpdater == null) throw new IllegalArgumentException("The passed node updater must not be null");
		if (leafNodes == null) throw new IllegalArgumentException("The passed initial nodes creator must not be null");
		
		setNodeUpdater(nodeUpdater);		
		setContentNodes(new ClusterSetIndexed<INode>(leafNodes.getContentLeaves().values()));
		setUserNodes(new ClusterSetIndexed<INode>(leafNodes.getUserLeaves().values()));
		setResult(new ClusterResult(
				leafNodes.getUserLeaves(),
				leafNodes.getContentLeaves(),
				null, null, UUID.randomUUID()));
		setPathToWriteSerializedObject(pathToWriteSerializedObject);
	}
	
	/**
	 * Set the user clusters.
	 * @param userNodes the user clusters that will be clustered.
	 */
	private void setUserNodes(IClusterSet<INode> userNodes) {
		this.userNodes = userNodes;
	}
	
	/**
	 * Set the content clusters.
	 * @param contentNodes the content clusters that will be clustered.
	 */
	private void setContentNodes(IClusterSet<INode> contentNodes) {
		this.contentNodes = contentNodes;
	}
	
	/**
	 * Set the run id of a new clustering process.
	 * 
	 * @param runId the new id
	 */
	private void setRunId(UUID runId) {
		this.runId = runId;
	}
	
	/**
	 * Sets the ClusterResult.
	 * @param result the new 
	 */
	private void setResult(ClusterResult result) {
		setRunId(result.getRunId());
		this.result = result;
	}
	
	/**
	 * Starts the thread that performs the clustering process.
	 */
	@Override
	public void run() {
		log = TBLogger.getLogger(getClass().getName());
		notifyObservers();
		cluster();		
	}
	
	/**
	 * Gets the cluster result as soon as the clustering process is completed.
	 * Calling this method is blocking since the thread only returns after the 
	 * clustering performing thread signals completion of clustering.
	 * 
	 * @return the result of the clustering process
	 */
	protected ClusterResult getResult() {
        synchronized(this){
            while(!buildCompleted){
                try {
					wait();
				} catch (InterruptedException e) {
					log.severe("Thread waiting for completion of " +
							"clustering process was interrupted.");
				}
            }
        }
		return result;
	}
	
	/**
	 * Sets the NodeUpdater to use for the clustering.
	 * @param nodeUpdater the new updater.
	 */
	protected void setNodeUpdater(INodeUpdater nodeUpdater) {
		this.nodeUpdater = nodeUpdater;
	}
	
	/**
	 * Sets the file system location for the serialized TreeBuilder object.
	 * @param pathToWriteSerializedObject file system location
	 */
	protected void setPathToWriteSerializedObject(
			String pathToWriteSerializedObject) {
		this.pathToWriteSerializedObject = pathToWriteSerializedObject;
	}
	
	/**
	 * Interrupts the clustering process.
	 */
	protected void interrupt() {
		isInterrupted = true;
	}
	
}
