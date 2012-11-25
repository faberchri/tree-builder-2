package modules;

import java.util.Set;

import clusterer.INode;
import clusterer.INodeUpdater;

public class ComplexNodeUpdater implements INodeUpdater {

	@Override
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate) {		
		
		// 1. Extract Group Attributes from newly created node -> wie identifizieren? braucht es neue klasse?
		// newNode.getAttributeKeys() nur grouping
		
		// 2. Remove all corresponding attributes from all nodes of other tree on current level, Add Group Attribute to All nodes of other tree on current level
		// for(nodeToUpdate : nodesToUpdate)
			// attNode.removeAttribute();
			// attNode.addAttribute();
	}

}