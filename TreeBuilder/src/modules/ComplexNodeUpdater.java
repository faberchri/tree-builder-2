package modules;

import java.util.List;
import java.util.Set;

import clusterer.INode;
import clusterer.INodeUpdater;

public class ComplexNodeUpdater implements INodeUpdater {

	@Override
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate) {		
		
		// Extract Group Attributes from newly created node
		 List<Set<INode>> attributeGroups = newNode.getAttributeGroups();
		 
		// Process Every Group
		 for(Set<INode> attributeGroup : attributeGroups) { 
			 
			// Process Every Node of the other tree
			for(INode nodeToUpdate : nodesToUpdate) {
				
				// Count occurrence of Attributes
				int attOccurenceCount = 0;
				for(INode attribute : attributeGroup) {
					if(nodeToUpdate.hasAttribute(attribute)) {
						attOccurenceCount++;
					}
				}
				
				// Add Attribute Group to current node if all attributes where found
				if(attOccurenceCount == attributeGroup.size()){
					nodeToUpdate.addAttributeGroup(attributeGroup);
					System.out.println("Added Attribute Group " + attributeGroup.toString() + " to " + nodeToUpdate.toString());
				}
			}
		}
	}

}