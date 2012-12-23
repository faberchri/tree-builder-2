package clusterer;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;

import modules.CobwebAttributeFactory;
import modules.ConcreteNodeFactory;
import storing.DBHandling;
import visualization.Display;
import visualization.VisualizationBuilder;
import client.IDataset;
import client.IDatasetItem;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;

/**
 * 
 * Implementation of COBWEB inspired hierarchical
 * agglomerative two-dimensional clustering algorithm 
 * for media recommendation generation.
 *
 * @param <T> the data type of the media ratings.
 */
public final class TreeBuilder<T extends Number> extends Operator {

	/**
	 * The data set to cluster.
	 */
	private IDataset<T> dataset; 
	
	/**
	 * The set of all root nodes of type user.
	 */
	private Set<INode> userNodes = new  HashSet<INode>(); 
	
	/**
	 * The set of all root nodes of type content.
	 */
	private Set<INode> contentNodes = new HashSet<INode>();
	
	/**
	 * The node factory of the clusterer.
	 */
	private NodeFactory nodeFactory;
	
	/**
	 * The factory that creates the attribute object of the created nodes.
	 */
	private AttributeFactory attributeFactory;
	
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
	 * Handles storing of nodes to db
	 */
	private DBHandling dbHandling;
	
	/**
	 * Instantiates a new tree builder which can create a cluster tree based on the passed data set.
	 * 
	 * @param rapidminerOperatorDescription Data container for name, class, short name,
	 * path and the description of an operator. 
	 * @param dataset the data set to cluster.
	 * @param searcherUsers the best merge searcher for nodes of type user.
	 * @param searcherContent the best merge searcher for nodes of type content.
	 * @param nodeUpdater the node updater used in the clustering process.
	 */
	public TreeBuilder(
			OperatorDescription rapidminerOperatorDescription,
			IDataset<T> dataset,
			IMaxCategoryUtilitySearcher searcherContent,
			IMaxCategoryUtilitySearcher searcherUsers,
			INodeUpdater nodeUpdater) {
		
		super(rapidminerOperatorDescription);
		this.dataset = dataset;
		this.nodeFactory = ConcreteNodeFactory.getInstance();
		this.attributeFactory = CobwebAttributeFactory.getInstance();
		this.nodeUpdater = nodeUpdater;
		this.userMCUSearcher = searcherUsers;
		this.contentMCUSearcher = searcherContent;
	}
	
	/**
	 * Performs the cluster tree creation of the data set.
	 */
	public void cluster() {
		
		// Instantiate DB
		//this.dbHandling = new DBHandling();
		//dbHandling.connect();
		
		// Build Leaf Nodes
		initLeafNodes(dataset);
		
		// Initialize Counter
		Counter counter = Counter.getInstance();
		counter.setInitialCounts(contentNodes.size(), userNodes.size());
		counter.setOpenUserNodeCount(userNodes.size());
		counter.setOpenMovieNode(contentNodes.size());
		counter.setStartTime(System.currentTimeMillis());
		
		// Start Display of control Data and hang in to counter
		Display display = new Display();
		display.start(counter);
		counter.setDisplay(display);
		
		// Initialize Visualization Frame
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
		// Process Nodes
		while (userNodes.size() >= 2 || contentNodes.size() >= 2) {

			// Get closest User Nodes & Merge them
			INode newUserNode = null;
			if(userNodes.size() >= 2) {
				IMergeResult cN = userMCUSearcher.getMaxCategoryUtilityMerge(userNodes);
				System.out.println("Best user node merge has category utility of "
							+cN.getCategoryUtility() +" and includes: " + cN.getNodes());
				newUserNode = mergeNodes(cN.getNodes(), userNodes,counter);
				System.out.println("cycle "+ counter.getCycleCount() + "| number of open user nodes: " + 
				userNodes.size() + "\t elapsed time [s]: "+ counter.getElapsedTime());
//				printAllOpenUserNodes();
			}

			// Get closest Movie Nodes & Merge them
			INode newMovieNode = null;
			if(contentNodes.size() >= 2) {
				IMergeResult cN = contentMCUSearcher.getMaxCategoryUtilityMerge(contentNodes);
				System.out.println("Best content node merge has category utility of "
						+cN.getCategoryUtility() +" and includes: " + cN.getNodes());
				newMovieNode = mergeNodes(cN.getNodes(), contentNodes,counter);				
				System.out.println("cycle "+ counter.getCycleCount() + "| number of open movie nodes: " + 
				contentNodes.size() + "\t elapsed time [s]: "+ counter.getElapsedTime());
//				printAllOpenMovieNodes();
			}

			// Update Trees with info from other tree on current level - only if nodes merged
			if(newUserNode != null && contentNodes.size() > 1) {
				nodeUpdater.updateNodes(newUserNode,contentNodes); 
			}
			if(newMovieNode != null && userNodes.size() > 1) {
				nodeUpdater.updateNodes(newMovieNode,userNodes);
			}
			
			// Create/Update Visualization
			visualize(frame,content);
			
			// Update counter
			counter.setOpenMovieNode(contentNodes.size());
			counter.setOpenUserNodeCount(userNodes.size());
			counter.addCycle();
		} 
		
		// Close Database
		//dbHandling.shutdown();
	}
	
	/**
	 * Initializes the leaf nodes of the tree at the start of the clustering process.
	 * For each user and each content item one node is created.
	 * 
	 * @param dataset the data set to cluster.
	 */
	private void initLeafNodes(IDataset<T> dataset) {

		Map<Integer, List<IDatasetItem<T>>> usersMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		Map<Integer, List<IDatasetItem<T>>> contentsMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		
		// sort data items according to user id and content id
		Iterator<IDatasetItem<T>> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			IDatasetItem<T> datasetItem = it.next();
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<IDatasetItem<T>> li = new ArrayList<IDatasetItem<T>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<IDatasetItem<T>> li = new ArrayList<IDatasetItem<T>>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
		}
		
		// create for each user and content id one node
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer i : usersMap.keySet()) {
			usersNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.User));
		}		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.Content));
		}
		
		// attach to each node its attributes map
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
				double normalizedRating = dataset.getNormalizer().normalizeRating( di.getValue());
				attributes.put(contentsNodeMap.get(di.getContentId()), attributeFactory.createAttribute(normalizedRating));
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
				double normalizedRating = dataset.getNormalizer().normalizeRating( di.getValue());
				attributes.put(usersNodeMap.get(di.getUserId()), attributeFactory.createAttribute(normalizedRating));
			}
			contentsNodeMap.get(entry.getKey()).setAttributes(attributes);
			contentNodes.add(contentsNodeMap.get(entry.getKey()));
		}
						
	}
	
	/**
	 * Creates a new node and initializes the nodes attributes based on a list of close nodes.
	 * The list of close nodes become the children of the new node.
	 * 
	 * @param nodesToMerge close nodes which are
	 * used to initialize a new node and will form the new nodes children. 
	 * These nodes are removed from the open set.
	 * 
	 * @param openSet the set of nodes to which the new node is added
	 * and from which the merged nodes are removed. 
	 * 
	 * @return a new node which has the {@code nodesToMerge} as children. 
	 */
	private INode mergeNodes(List<INode> nodesToMerge, Set<INode> openSet, Counter counter) {
		
		if (nodesToMerge.size() > 1) {
			
			// Create a new node (product of nodesToMerge)
			INode newNode;
			switch (nodesToMerge.get(0).getNodeType()) {
			case User:
				newNode = nodeFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge,
						attributeFactory);
				counter.addUserNode();
				break;
			case Content:
				newNode = nodeFactory.createInternalNode(
						ENodeType.Content,
						nodesToMerge,
						attributeFactory);
				counter.addMovieNode();
				break;
			default:
				newNode = null;
				System.err.println("Err: Not supported node encountered in: " + getClass().getSimpleName());
				System.exit(-1);
				break;
			}
			
			// Add new node to openset
			openSet.add(newNode);
			
			
			// Updating relationships and remove
			for (INode nodeToMerge : nodesToMerge) {	
				
				// Create parent/child relationships
// is done on instantiation of node can be removed from here				
//				nodeToMerge.setParent(newNode);
//				newNode.addChild(nodeToMerge);
	
				// Remove merged Nodes
				if (!openSet.remove(nodeToMerge)) {
					System.err.println("Err: Removal of merged node (" + nodeToMerge + ") from " +openSet +" failed, in: " + getClass().getSimpleName());
				}
			}
			

			return newNode;
			
		} 
		else {
			System.err.println("Err: Merge attempt with 1 or less nodes, in: " + getClass().getSimpleName());
			System.exit(-1);
		}
		return null;
	}
	
	/**
	 * Creates a graphical representation of the current state of the cluster tree.
	 * @param frame
	 * @param content
	 */
	public void visualize(JFrame frame, Container content) {
		
		// Instantiate VisualizationBuilder
		VisualizationBuilder vb = new VisualizationBuilder((Set)contentNodes,(Set)userNodes);
		
		// Add Content to Swing Panel
        content.removeAll();
        content.add(vb);
        
        // Repack Frame
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
		System.out.println("-----------------------");
		System.out.println(nodeNames);
		INode[] setArr = set.toArray(new INode[set.size()]);
		Arrays.sort(setArr);
		for (INode node : setArr) {
			System.out.println(node.toString()+"|\t"+node.getAttributesString());
		}
		System.out.println("-----------------------");
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
