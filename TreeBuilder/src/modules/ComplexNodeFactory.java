package modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clusterer.AttributeFactory;
import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;
import clusterer.NodeFactory;

public class ComplexNodeFactory extends NodeFactory {

	private static NodeFactory factory = new ComplexNodeFactory();

	public ComplexNodeFactory() {
		// singleton
	}

	public static NodeFactory getInstance() {
		return factory;
	}

	@Override
	public INode createLeafNode(ENodeType typeOfNewNode,
			INodeDistanceCalculator nodeDistanceCalculator) {

		return new ComplexNode(typeOfNewNode, nodeDistanceCalculator);
	}

	@Override
	public INode createInternalNode(ENodeType typeOfNewNode,
			List<INode> nodesToMerge,
			INodeDistanceCalculator nodeDistanceCalculator,
			AttributeFactory attributeFactory) {

		// Error Prevention
		if (nodesToMerge.size() < 2) {
			System.err.println("Merge attempt with number of nodes < 2");
			System.exit(-1);
		}

		Map<INode, IAttribute> attMap = createAttMap(nodesToMerge, attributeFactory);
		ArrayList<INode> attGroup = null; // To be implemented

		return new ComplexNode(typeOfNewNode, nodeDistanceCalculator, new HashSet<INode>(nodesToMerge), attMap, attGroup);
	}

	@Override
	public INode createCalculationNode(List<INode> nodesToMerge) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<INode,IAttribute> createAttMap(List<INode> nodesToMerge, AttributeFactory attributeFactory) {

		// Go through every given Node and process their attributes into a combined list of attributes
		HashMap<INode,ArrayList<INode>> union = new HashMap<INode,ArrayList<INode>>(); // HashMap of keys with multiple occurrence; key: id, value: array of nodes
		for (int i = 0;i<nodesToMerge.size();i++) {

			INode tempNode = nodesToMerge.get(i);
			Set<INode> tempKeys = tempNode.getAttributeKeys();

			// Process every single attribute of current Node
			for(INode tempKey : tempKeys) {

				ArrayList<INode> tempValue = new ArrayList();

				// look for match of key in union -> match: Load already inserted nodes first and create new key,value pair after
				if(union.containsKey(tempKey)) {
					tempValue = union.get(tempKey);
					union.remove(tempKey);
				}
				tempValue.add(tempNode); // Adds current Node to Set
				union.put(tempKey, tempValue); // Insert a not yet inserted key to union 
			}
		}

		// Build Attribute Map for new Node; calculate new value when multiple nodes have same key, simply insert when not
		Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();
		ArrayList<INode> attGroup = new ArrayList<INode>();

		for (INode tempKey : union.keySet()) {

			//System.out.println("Key: " + tempKey.toString());

			List<IAttribute> attArr = new ArrayList<IAttribute>();
			ArrayList<INode> involvedNodeList = union.get(tempKey);
			//System.out.println("involvedNodeList: " + involvedNodeList.toString());

			// Process all involved Nodes and add their value of the specific key to the calculation
			for(INode involvedNode : involvedNodeList) {
				attArr.add(involvedNode.getAttributeValue(tempKey));
			}

			// Create a new Attribute calculation with all added values, add to attribute Map
			IAttribute newAttribute = attributeFactory.createAttribute(attArr);

			// Add Attribute to Attribute Map
			attMap.put(tempKey, newAttribute);

			// If involvedNodeList size = size of nodes to merge -> all nodes have this item in common; add to list of potential attribute group 
			// -> absolutely not sure about this.. if multiple 
			if(involvedNodeList.size() == nodesToMerge.size()) {
				attGroup.add(tempKey);
			}
		}

		// Process List of potential groups to definitive list if size bigger than 1
		if(attGroup.size() < 2) {
			attGroup = null;
		}
		else {
			System.out.println("Attribute Group Created " + attGroup.toString());
		}

		return attMap;
	}

	public ArrayList<INode> attGroup() {
		return null;
		// Problem: Sehr stark verwoben mit attMap
	}
}
