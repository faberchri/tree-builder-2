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

import modules.ComplexNodeFactory;
import modules.SimpleAttributeFactory;
import visualization.VisualizationBuilder;
import client.IDataset;
import client.IDatasetItem;

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;


public final class TreeBuilder<T extends Number> extends Operator {
//public final class TreeBuilder<T extends Number> {	
	private IDataset<T> dataset; 
		
	private Set<INode> userNodes = new  HashSet<INode>(); //Collections.newSetFromMap(new ConcurrentHashMap<INode,Boolean>());
	private Set<INode> contentNodes = new HashSet<INode>(); //Collections.newSetFromMap(new ConcurrentHashMap<INode,Boolean>());
		
	private NodeFactory nodeFactory;
	private AttributeFactory attributeFactory;
		
	private INodeUpdater nodeUpdater;
	
	private INodeDistanceCalculator usersNodeDistanceCalculator;
	private INodeDistanceCalculator contentsNodeDistanceCalculator;
	private IClosestNodesSearcher closestNodesSearcher;
	
	
	public TreeBuilder(OperatorDescription rapidminerOperatorDescription, IDataset<T> dataset, INodeDistanceCalculator ndcUsers, INodeDistanceCalculator ndcContents, IClosestNodesSearcher cns, INodeUpdater nodeUpdater) {
		super(rapidminerOperatorDescription);
		this.dataset = dataset;
		//this.nodeFactory = SimpleNodeFactory.getInstance();
		this.nodeFactory = ComplexNodeFactory.getInstance();
		this.attributeFactory = SimpleAttributeFactory.getInstance();
		this.nodeUpdater = nodeUpdater;
		this.closestNodesSearcher = cns;
		this.contentsNodeDistanceCalculator = ndcContents;
		this.usersNodeDistanceCalculator = ndcUsers;
	}
	
	public void cluster() {
		
		// Build Leaf Nodes
		initLeafNodes(dataset);
		
		// Initialize control parameter
		int cycleCount = 0;
		long startTime = System.currentTimeMillis();
		
		// Initialze visualization frame
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Process Nodes
		while (userNodes.size() >= 2 || contentNodes.size() >= 2) {

			// Get closest User Nodes & Merge them
			INode newUserNode = null;
			if(userNodes.size() >= 2) {
				List<INode> cN = closestNodesSearcher.getClosestNodes(userNodes);
				newUserNode = mergeNodes(cN, userNodes);
				
				double elapsedTime = ((double)(System.currentTimeMillis() - startTime)) / 1000.0;
				System.out.println("cycle "+ cycleCount + "| number of open user nodes: " + userNodes.size() + "\t elapsed time [s]: "+ elapsedTime);
				printAllOpenUserNodes();
			}

			// Get closest Movie Nodes & Merge them
			INode newMovieNode = null;
			if(contentNodes.size() >= 2) {
				List<INode> cN = closestNodesSearcher.getClosestNodes(contentNodes);
				newMovieNode = mergeNodes(cN, contentNodes);
				
				double elapsedTime = ((double)(System.currentTimeMillis() - startTime)) / 1000.0;
				System.out.println("cycle "+ cycleCount + "| number of open movie nodes: " + contentNodes.size() + "\t elapsed time [s]: "+ elapsedTime);
				printAllOpenMovieNodes();
			}

			// Update Trees with info from other tree on current level - only if nodes merged
			if(newUserNode != null) {
				nodeUpdater.updateNodes(newUserNode,contentNodes); 
			}
			if(newMovieNode != null) {
				nodeUpdater.updateNodes(newMovieNode,userNodes);
			}
			
			// Create/Update Visualization
			visualize(frame,content);

			cycleCount++;
		} 
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
			usersNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.User, usersNodeDistanceCalculator));
		}		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, nodeFactory.createLeafNode(ENodeType.Content, contentsNodeDistanceCalculator));
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
		
	private INode mergeNodes(List<INode> nodesToMerge, Set<INode> openSet) {
		
		if (nodesToMerge.size() > 1) {
			
			// Create a new node (product of nodesToMerge)
			INode newNode;
			switch (nodesToMerge.get(0).getNodeType()) {
			case User:
				newNode = nodeFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge,
						usersNodeDistanceCalculator,
						attributeFactory);
				break;
			case Content:
				newNode = nodeFactory.createInternalNode(
						ENodeType.Content,
						nodesToMerge,
						contentsNodeDistanceCalculator,
						attributeFactory);
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
				nodeToMerge.setParent(newNode);
				newNode.addChild(nodeToMerge);
				
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
	
	// Print Functions
	private void printAllNodesInSet(Set<? extends IPrintableNode> set, String nodeNames){
		System.out.println("-----------------------");
		System.out.println(nodeNames);
		IPrintableNode[] setArr = set.toArray(new IPrintableNode[set.size()]);
		Arrays.sort(setArr);
		for (IPrintableNode node : setArr) {
			System.out.println(node.toString()+"|\t"+node.getAttributesString());
		}
		System.out.println("-----------------------");
	}
	
	public void printAllOpenUserNodes() {
		printAllNodesInSet((Set)userNodes, "User Nodes:");
	}
	
	public void printAllOpenMovieNodes() {
		printAllNodesInSet((Set)contentNodes, "MovieNodes:");
	}
	
}
