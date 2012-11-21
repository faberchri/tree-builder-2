package clusterer;

import java.util.ArrayList;
import java.util.List;

public class NodeFactory {
		
	public NodeFactory(NodeDistanceCalculator ndcUsers, NodeDistanceCalculator ndcContent) {
		
		UserNode.setFactory(new UserNodeFactory(ndcUsers));
		MovieNode.setFactory(new ContentNodeFactory(ndcContent));		
	}
			
	public List createEmptyContentNodes(int numberOfNodes) {
		return createEmptyNodes(numberOfNodes, MovieNode.getFactory());
	}
	
	public List createEmptyUserNodes(int numberOfNodes) {
		return createEmptyNodes(numberOfNodes, UserNode.getFactory());
	}
	
	private List<Node> createEmptyNodes(int numberOfNodes, Factory factory) {
		List<Node> nodes = new ArrayList<Node>();
		for (int i = 0; i < numberOfNodes; i++) {
			nodes.add(factory.createNode(null, null));
		}
		return nodes;
	}

}
