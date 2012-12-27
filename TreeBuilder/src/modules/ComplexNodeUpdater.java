package modules;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import utils.TBLogger;
import clusterer.INode;
import clusterer.INodeUpdater;

public class ComplexNodeUpdater implements INodeUpdater, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate) {		
		
		// Extract Group Attributes from newly created node
		 List<Set<INode>> attributeGroups = newNode.getAttributeGroups();
		 
		 if(attributeGroups != null) {
		 
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
						TBLogger.getLogger(getClass().getName()).info(
								"Added Attribute Group " + attributeGroup.toString()
								+ " to " + nodeToUpdate.toString());
					}
				}
			}
		 }
	}

}