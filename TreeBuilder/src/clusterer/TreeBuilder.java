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

import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorDescription;

import visualization.VisualizationBuilder;

import client.IDataset;
import client.IDatasetItem;


public final class TreeBuilder<T extends Number> extends Operator {
	
	private IDataset<T> dataset; 
		
	private Set<INode> userNodes = new  HashSet<INode>(); //Collections.newSetFromMap(new ConcurrentHashMap<INode,Boolean>());
	private Set<INode> movieNodes = new HashSet<INode>(); //Collections.newSetFromMap(new ConcurrentHashMap<INode,Boolean>());
		
	private NodeFactory factory;
	private AttributeFactory<T> attributeFactory;
		
	private NodeUpdater nodeUpdater;
	
	private IClosestNodesSearcher closestNodesSearcher;
	
	public TreeBuilder(OperatorDescription rapidminerOperatorDescription, IDataset<T> dataset, NodeDistanceCalculator ndcUsers, NodeDistanceCalculator ndcContents, NodeUpdater nodeUpdater, IClosestNodesSearcher cns) {
		super(rapidminerOperatorDescription);
		this.dataset = dataset;
		this.factory = new NodeFactory(ndcUsers, ndcContents);
		this.attributeFactory = SimpleAttributeFactory.getInstance(dataset.getNormalizer());
		this.nodeUpdater = nodeUpdater;
		this.closestNodesSearcher = cns;
	}
	
	public INode cluster() {
		initLeafNodes(dataset);
		int cycleCount = 0;
		long startTime = System.currentTimeMillis();
		while (userNodes.size() > 2 && movieNodes.size() > 2) {
			List<INode> cN = getClosestOpenUserNodes();
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
	
		
	private void initLeafNodes(IDataset<T> dataset) {

		Map<Integer, List<IDatasetItem<T>>> usersMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		Map<Integer, List<IDatasetItem<T>>> contentsMap = new HashMap<Integer, List<IDatasetItem<T>>>();
		
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
		
		Map<Integer, INode> usersNodeMap = new HashMap<Integer, INode>();
		for (Integer i : usersMap.keySet()) {
			usersNodeMap.put(i, UserNode.getFactory().createNode(null, null));
		}
		
		Map<Integer, INode> contentsNodeMap = new HashMap<Integer, INode>();
		for (Integer i : contentsMap.keySet()) {
			contentsNodeMap.put(i, MovieNode.getFactory().createNode(null, null));
		}
		
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : usersMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
				attributes.put(contentsNodeMap.get(di.getContentId()), attributeFactory.createAttribute(di.getValue()));
			}
			usersNodeMap.get(entry.getKey()).setAttributes(attributes);
			userNodes.add(usersNodeMap.get(entry.getKey()));
		}
		for (Map.Entry<Integer, List<IDatasetItem<T>>> entry : contentsMap.entrySet()) {
			Map<INode, IAttribute> attributes = new HashMap<INode, IAttribute>();
			for (IDatasetItem<T> di : entry.getValue()) {
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
	
	public List<INode> getClosestOpenUserNodes() {
		return closestNodesSearcher.getClosestNodes(userNodes);
	}
		
	public List<INode> getClosestOpenMovieNodes() {
		return closestNodesSearcher.getClosestNodes(movieNodes);
	}
		
	private void mergeNodes(List<INode> nodes, Set<INode> openSet) {
		if (nodes.size() > 1) {
			INode newNode = nodes.get(0).getNodeFactory().createNode(nodes, attributeFactory);
			openSet.add(newNode);
			for (INode node : nodes) {
				node.setParent(newNode);
				newNode.addChild(node);
				if (!openSet.remove(node)) {
					System.err.println("removal of merged node (" + node + ") from " +openSet +" failed, " + getClass().getSimpleName());
				}
			}

			nodeUpdater.updateNodes(newNode);
		} else {
			System.err.println("merge attempt with 1 or less nodes, " + getClass().getSimpleName());
			System.exit(-1);
		}
	}
	
}
