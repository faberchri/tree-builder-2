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

import visualization.VisualizationBuilder;

import client.Dataset;
import client.DatasetItem;


public final class TreeBuilder<T extends Number> {
	
	private Dataset<T> dataset; 
		
	private Set<Node> userNodes = new  HashSet<Node>(); //Collections.newSetFromMap(new ConcurrentHashMap<Node,Boolean>());
	private Set<Node> movieNodes = new HashSet<Node>(); //Collections.newSetFromMap(new ConcurrentHashMap<Node,Boolean>());
		
	private NodeFactory factory;
	private AttributeFactory<T> attributeFactory;
		
	private NodeUpdater nodeUpdater;
	
	private ClosestNodesSearcher closestNodesSearcher;
	
	public TreeBuilder(Dataset<T> dataset, NodeDistanceCalculator ndcUsers, NodeDistanceCalculator ndcContents, NodeUpdater nodeUpdater, ClosestNodesSearcher cns) {
		this.dataset = dataset;
		this.factory = new NodeFactory(ndcUsers, ndcContents);
		this.attributeFactory = SimpleAttributeFactory.getInstance(dataset.getNormalizer());
		this.nodeUpdater = nodeUpdater;
		this.closestNodesSearcher = cns;
	}
	
	public Set<Node> cluster() {
		initLeafNodes(dataset);
		int cycleCount = 0;
		long startTime = System.currentTimeMillis();
		while (userNodes.size() > 2 && movieNodes.size() > 2) {
			List<Node> cN = getClosestOpenUserNodes();
//			getClosestOpenUserNodes();
//			System.out.println("ClosestOpenUserNodes: "+ cN);
			mergeNodes(cN, userNodes);
			double elapsedTime = ((double)(System.currentTimeMillis() - startTime)) / 1000.0;
			System.out.println("cycle "+ cycleCount + "| number of open user nodes: " + userNodes.size() + "\t elapsed time [s]: "+ elapsedTime);
//			printAllOpenUserNodes();
			cN = getClosestOpenMovieNodes();
//			getClosestOpenMovieNodes();
//			System.out.println("ClosestOpenMovieNodes: "+ cN);
			mergeNodes(cN, movieNodes);
			elapsedTime = ((double)(System.currentTimeMillis() - startTime)) / 1000.0;
			System.out.println("cycle "+ cycleCount + "| number of open movie nodes: " + movieNodes.size() + "\t elapsed time [s]: "+ elapsedTime);
//			printAllOpenMovieNodes();
			cycleCount++;
		} 
		
		return null; // FIXME
	}

	public void visualize() {
		
		// Instantiate VisualizationBuilder
		VisualizationBuilder vb = new VisualizationBuilder(movieNodes,userNodes);
		
		// Swing Visualization
        JFrame frame = new JFrame();
        Container content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.add(vb);
        frame.pack();
        frame.setVisible(true);
	}
	
		
	private void initLeafNodes(Dataset<T> dataset) {

		Map<Integer, List<DatasetItem<T>>> usersMap = new HashMap<Integer, List<DatasetItem<T>>>();
		Map<Integer, List<DatasetItem<T>>> contentsMap = new HashMap<Integer, List<DatasetItem<T>>>();
		
		Iterator<DatasetItem<T>> it = dataset.iterateOverDatasetItems();
		while(it.hasNext()) {
			DatasetItem<T> datasetItem = it.next();
			if (usersMap.containsKey(datasetItem.getUserId())) {
				usersMap.get(datasetItem.getUserId()).add(datasetItem);
			} else {
				List<DatasetItem<T>> li = new ArrayList<DatasetItem<T>>();
				li.add(datasetItem);
				usersMap.put(datasetItem.getUserId(), li);
			}
			if (contentsMap.containsKey(datasetItem.getContentId())) {
				contentsMap.get(datasetItem.getContentId()).add(datasetItem);
			} else {
				List<DatasetItem<T>> li = new ArrayList<DatasetItem<T>>();
				li.add(datasetItem);
				contentsMap.put(datasetItem.getContentId(), li);
			}
		}
		
		Map<Integer, Node> usersNodeMap = new HashMap<Integer, Node>();
		for (Integer i : usersMap.keySet()) {
			usersNodeMap.put(i, UserNode.getFactory().createNode(null, null));
		}
		
		Map<Integer, Node> contentsNodeMap = new HashMap<Integer, Node>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, MovieNode.getFactory().createNode(null, null));
		}
		
		for (Map.Entry<Integer, List<DatasetItem<T>>> entry : usersMap.entrySet()) {
			Map<Node, Attribute> attributes = new HashMap<Node, Attribute>();
			for (DatasetItem<T> di : entry.getValue()) {
				attributes.put(contentsNodeMap.get(di.getContentId()), attributeFactory.createAttribute(di.getValue()));
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<DatasetItem<T>>> entry : contentsMap.entrySet()) {
			Map<Node, Attribute> attributes = new HashMap<Node, Attribute>();
			for (DatasetItem<T> di : entry.getValue()) {
				attributes.put(usersNodeMap.get(di.getUserId()), attributeFactory.createAttribute(di.getValue()));
			}
			contentsNodeMap.get(entry.getKey()).setAttributes(attributes);
			movieNodes.add(contentsNodeMap.get(entry.getKey()));
		}		
				
	}
		
	private void printAllNodesInSet(Set<? extends PrintableNode> set, String nodeNames){
		System.out.println("-----------------------");
		System.out.println(nodeNames);
		PrintableNode[] setArr = set.toArray(new PrintableNode[set.size()]);
		Arrays.sort(setArr);
		for (PrintableNode node : setArr) {
			System.out.println(node.toString()+"|\t"+node.getAttributesString());
		}
		System.out.println("-----------------------");
	}
	
	public void printAllOpenUserNodes() {
		printAllNodesInSet((Set)userNodes, "User Nodes:");
	}
	
	public void printAllOpenMovieNodes() {
		printAllNodesInSet((Set)movieNodes, "MovieNodes:");
	}
	
	public List<Node> getClosestOpenUserNodes() {
		return closestNodesSearcher.getClosestNodes(userNodes);
	}
		
	public List<Node> getClosestOpenMovieNodes() {
		return closestNodesSearcher.getClosestNodes(movieNodes);
	}
		
	private void mergeNodes(List<Node> nodes, Set<Node> openSet) {
		if (nodes.size() > 1) {
			Node newNode = nodes.get(0).getNodeFactory().createNode(nodes, attributeFactory);
			openSet.add(newNode);
			for (Node node : nodes) {
				node.setParent(newNode);
				newNode.addChild(node);
				if (!openSet.remove(node)) {
					System.err.println("removal of merged node (" + node + ") from " +openSet +" failed, " + getClass().getSimpleName());
				}
			}
			closestNodesSearcher.setNodeOfLastMerge(newNode);
			nodeUpdater.updateNodes(newNode);
		} else {
			System.err.println("merge attempt with 1 or less nodes, " + getClass().getSimpleName());
			System.exit(-1);
		}
	}
	
}
