package clusterer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ContentNodeFactory implements Factory{

	private NodeDistanceCalculator nodeDistanceCalculator;
	
	ContentNodeFactory(NodeDistanceCalculator ndc) {

		this.nodeDistanceCalculator = ndc;
	}

	@Override
	public Node createNode(List<Node> nodesToMerge, AttributeFactory attributeFactory) {
		Node newNode = new MovieNode(getNodeDistanceCalculator());
		// used at init
		if (nodesToMerge == null) {
			return newNode;
		}
		if (nodesToMerge.size() != 2) {
			System.err.println("Merge attempt with number of nodes != 2");
			System.exit(-1);
		}
		Node n1 = nodesToMerge.get(0);
		Node n2 = nodesToMerge.get(1);
		
		Set<Node> n1Keys = n1.getAttributeKeys();
		Set<Node> union = new HashSet<Node>(n1Keys);
		Set<Node> n2Keys = n2.getAttributeKeys();
		union.addAll(n2.getAttributeKeys());

		Map<Node, Attribute> attMap = new HashMap<Node, Attribute>();
		for (Node node : union) {
			List<Attribute> attArr = new ArrayList<Attribute>();
			if (n1Keys.contains(node) && n2Keys.contains(node)) {
				attArr.add(n1.getAttributeValue(node));
				attArr.add(n2.getAttributeValue(node));
			} else {
				if (n1Keys.contains(node)) {
					attArr.add(n1.getAttributeValue(node));
				} else {
					attArr.add(n2.getAttributeValue(node));
				}
			}
			Attribute newAttribute = attributeFactory.createAttribute(attArr);
			attMap.put(node, newAttribute);
		}
		newNode.setAttributes(attMap);		
		return newNode;
	}
	
	public NodeDistanceCalculator getNodeDistanceCalculator() {
		return nodeDistanceCalculator;
	}
		
}
