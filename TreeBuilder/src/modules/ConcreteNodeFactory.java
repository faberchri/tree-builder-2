package modules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import clusterer.AttributeFactory;
import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.NodeFactory;

import com.google.common.collect.ImmutableMap;

public class ConcreteNodeFactory extends NodeFactory {

	private static NodeFactory factory = new ConcreteNodeFactory();

	private ConcreteNodeFactory() {
		// singleton
	}

	public static NodeFactory getInstance() {
		return factory;
	}

	@Override
	public INode createLeafNode(ENodeType typeOfNewNode) {

		return new Node(typeOfNewNode);
	}

	@Override
	public INode createInternalNode(
			ENodeType typeOfNewNode,
			List<INode> nodesToMerge,
			AttributeFactory attributeFactory) {

		if (nodesToMerge.size() < 2) {
			System.err.println("Merge attempt with number of nodes < 2, in: "+getClass().getSimpleName());
			System.exit(-1);
		}

		Map<INode, IAttribute> attMap = createAttMap(nodesToMerge, attributeFactory);
		INode newN = new Node(typeOfNewNode, nodesToMerge, attMap);
				
		return newN;
	}

	private  Map<INode,IAttribute> createAttMap(List<INode> nodesToMerge, AttributeFactory attributeFactory) {
		Map<INode, IAttribute> map = new HashMap<INode, IAttribute>();
		for (INode node : nodesToMerge) {
			for (INode attNodes : node.getAttributeKeys()) {
				map.put(attNodes, null);
			}			
		}
		for (Map.Entry<INode, IAttribute> entry : map.entrySet()) {
			IAttribute newAtt = attributeFactory.createAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (map.containsValue(null)) {
			System.err.println("Attribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return ImmutableMap.copyOf(map);		
	}


// ----------------------------------  For deletion ---------------------------------------------------	

//	public Map<INode,IAttribute> createAttMap(List<INode> nodesToMerge, AttributeFactory attributeFactory) {
//
//		// Go through every given Node and process their attributes into a combined list of attributes
//		HashMap<INode,List<INode>> union = new HashMap<INode,List<INode>>(); // HashMap of keys with multiple occurrence; key: id, value: array of nodes
//		for (int i = 0;i<nodesToMerge.size();i++) {
//
//			INode tempNode = nodesToMerge.get(i);
//			Set<INode> tempKeys = tempNode.getAttributeKeys();
//
//			// Process every single attribute of current Node
//			for(INode tempKey : tempKeys) {
//
//				List<INode> tempValue = new ArrayList<INode>();
//
//				// look for match of key in union -> match: Load already inserted nodes first and create new key,value pair after
//				if(union.containsKey(tempKey)) {
//					tempValue = union.get(tempKey);
//					union.remove(tempKey);
//				}
//				tempValue.add(tempNode); // Adds current Node to Set
//				union.put(tempKey, tempValue); // Insert a not yet inserted key to union 
//			}
//		}
//
//		// Build Attribute Map for new Node; calculate new value when multiple nodes have same key, simply insert when not
//		Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();
//		ArrayList<INode> attGroup = new ArrayList<INode>();
//
//		for (INode tempKey : union.keySet()) {
//
//			//System.out.println("Key: " + tempKey.toString());
//
//			List<IAttribute> attArr = new ArrayList<IAttribute>();
//			List<INode> involvedNodeList = union.get(tempKey);
//			//System.out.println("involvedNodeList: " + involvedNodeList.toString());
//
//			// Process all involved Nodes and add their value of the specific key to the calculation
//			for(INode involvedNode : involvedNodeList) {
//				attArr.add(involvedNode.getAttributeValue(tempKey));
//			}
//
//			// Create a new Attribute calculation with all added values, add to attribute Map
//			IAttribute newAttribute = attributeFactory.createAttribute(attArr);
//
//			// Add Attribute to Attribute Map
//			attMap.put(tempKey, newAttribute);
//
//			// If involvedNodeList size = size of nodes to merge -> all nodes have this item in common; add to list of potential attribute group 
//			// -> absolutely not sure about this.. if multiple 
//			if(involvedNodeList.size() == nodesToMerge.size()) {
//				attGroup.add(tempKey);
//			}
//		}
//
//		// Process List of potential groups to definitive list if size bigger than 1
//		if(attGroup.size() < 2) {
//			attGroup = null;
//		}
//		else {
//			System.out.println("Attribute Group Created " + attGroup.toString());
//		}
//
//		return attMap;
//	}

}
