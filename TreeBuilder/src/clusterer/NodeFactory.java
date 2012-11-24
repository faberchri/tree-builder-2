package clusterer;

import java.util.List;

abstract class NodeFactory {
		
	public abstract INode createLeafNode(ENodeType typeOfNewNode, INodeDistanceCalculator nodeDistanceCalculator);
	
	/**
	 * for now we assume nodesToMerge.size() == 2 or nodesToMerge == null
	 * @param List nodesToMerge
	 * @return a new node already initialized with parameters
	 */
	public abstract INode createInternalNode(ENodeType typeOfNewNode, List<INode> nodesToMerge, INodeDistanceCalculator nodeDistanceCalculator, AttributeFactory attributeFactory);

}
