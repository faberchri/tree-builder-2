package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;



public class ExtendedNodeUpdater implements INodeUpdater, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void updateNodes(INode newNode, Collection<INode> nodesToUpdate) {
		Logger log = TBLogger.getLogger(getClass().getName());
		
		
		for (INode attNode : newNode.getAttributeKeys()) {
			
			// Update Node if it is in collection of nodes that should be updated
			if (nodesToUpdate.contains(attNode)) {
				
				attNode.addAttribute(newNode, newNode.getAttributeValue(attNode));
				
				// Determine nodes that are included in newNode.getAttributeValue(attNode)
				// Implement 
				INode includedNode = null;
				
				attNode.removeAttribute(includedNode);
				
				log.finest(attNode.toString() + " updated with " + newNode);
			}
			
		}
		
		
	}

}
