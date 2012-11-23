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
	public INode createNode(List<INode> nodesToMerge, AttributeFactory attributeFactory) {
		INode newNode = new MovieNode(getNodeDistanceCalculator());
		// used at init
		if (nodesToMerge == null) {
			return newNode;
		}
		if (nodesToMerge.size() != 2) {
			System.err.println("Merge attempt with number of nodes != 2");
			System.exit(-1);
		}
		INode n1 = nodesToMerge.get(0);
		INode n2 = nodesToMerge.get(1);
		
		Set<INode> n1Keys = n1.getAttributeKeys();
		Set<INode> union = new HashSet<INode>(n1Keys);
		Set<INode> n2Keys = n2.getAttributeKeys();
		union.addAll(n2.getAttributeKeys());

		Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();
		for (INode node : union) {
			List<IAttribute> attArr = new ArrayList<IAttribute>();
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
			IAttribute newAttribute = attributeFactory.createAttribute(attArr);
			attMap.put(node, newAttribute);
		}
		newNode.setAttributes(attMap);		
		return newNode;
	}
	
	public NodeDistanceCalculator getNodeDistanceCalculator() {
		return nodeDistanceCalculator;
	}
		
}
