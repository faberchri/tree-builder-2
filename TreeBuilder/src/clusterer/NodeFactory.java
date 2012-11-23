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
	
	private List<INode> createEmptyNodes(int numberOfNodes, Factory factory) {
		List<INode> nodes = new ArrayList<INode>();
		for (int i = 0; i < numberOfNodes; i++) {
			nodes.add(factory.createNode(null, null));
		}
		return nodes;
	}

}
