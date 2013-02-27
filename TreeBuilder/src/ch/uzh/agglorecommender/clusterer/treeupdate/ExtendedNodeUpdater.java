package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
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
		
		
		for (INode attNode : newNode.getNumericalAttributeKeys()) {			
			// Update Node if it is in collection of nodes that should be updated
			if (nodesToUpdate.contains(attNode)) {
				attNode.addNumericalAttribute(newNode, newNode.getNumericalAttributeValue(attNode));
				
				// Remove children of new node from nodes of the other tree
				Iterator<INode> mergedNodes = newNode.getChildren();
				while(mergedNodes.hasNext()){
					attNode.removeNumericalAttribute(mergedNodes.next());
				}
				log.finest(attNode.toString() + " updated with " + newNode);
			}
		}
		
		for (Object attNode : newNode.getNominalAttributeKeys()) {			
			// Update Node if it is in collection of nodes that should be updated
			if (nodesToUpdate.contains(attNode)) {
				((INode) attNode).addNominalAttribute(newNode, newNode.getNominalAttributeValue(attNode));
				
				// Remove children of new node from nodes of the other tree
				Iterator<INode> mergedNodes = newNode.getChildren();
				while(mergedNodes.hasNext()){
					((INode) attNode).removeNominalAttribute(mergedNodes.next());
				}
				log.finest(attNode.toString() + " updated with " + newNode);
			}
		}
		
		
	}

}
