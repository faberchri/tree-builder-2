package modules;

import java.util.ArrayList;
import java.util.Set;

import clusterer.INode;
import clusterer.INodeUpdater;

public class ComplexNodeUpdater implements INodeUpdater {

	@Override
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate) {		
		
		// Extract Group Attributes from newly created node -> wie identifizieren? braucht es neue klasse?
		 ArrayList<Set> attributeGroups = newNode.getAttributeGroups();
		 
		// Process Every Group: 
		 for(Set<INode> attributeGroup : attributeGroups) { 
			 
			// Process Every Node of the other tree
			for(INode nodeToUpdate : nodesToUpdate) {
				
				// Count occurrence of Attributes
				int attCount = 0;
				for(INode attribute : attributeGroup) {
					if(nodeToUpdate.hasAttribute(attribute)) {
						attCount++;
					}
				}
				
				// Add Attribute Group to current node if all attributes where found
				if(attCount == attributeGroup.size()){
					nodeToUpdate.addAttributeGroup(attributeGroup);
				}
			}
		}
	}

}