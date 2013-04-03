package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 * A node updater that removes the attributes of an
 * updated node that are children of the new node
 * and keeps the new node as attribute of the 
 * updated node.
 * The removed attributes of an node are stored in a map 
 * of maps for future use i.e. in the recommendation generation.
 */
public class SaveAttributeNodeUpdater implements INodeUpdater, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Stores for a node with removed attributes a map of all
	 * removed attributes together with the attribute value.
	 */
	private Map<INode, Map<INode, IAttribute>> removedAttributes = new HashMap<>();

	@Override
	public void updateNodes(INode newNode, Collection<INode> nodesToUpdate) {
		Logger log = TBLogger.getLogger(getClass().getName());
				
		for (INode attNode : newNode.getRatingAttributeKeys()) {			
			// Update Node if it is in collection of nodes that should be updated
			if (nodesToUpdate.contains(attNode)) {
				
				Map<INode, IAttribute> savedAtts = removedAttributes.get(attNode);
				if (savedAtts == null) {
					savedAtts = new HashMap<>();
				}
				
				attNode.addRatingAttribute(newNode, newNode.getNumericalAttributeValue(attNode));
				
				// Remove children of new node from nodes of the other tree
				Iterator<INode> mergedNodes = newNode.getChildren();
				while(mergedNodes.hasNext()){
					INode tmp = mergedNodes.next();
					savedAtts.put(tmp, attNode.getNumericalAttributeValue(tmp));
					attNode.removeAttribute(tmp);
				}
				
				removedAttributes.put(attNode, savedAtts);
				
				log.finest(attNode.toString() + " updated with " + newNode);
			}
		}

	}
	
	/**
	 * Gets for the passed node a map of all removed nodes together
	 * with the IAttribute object.
	 * @param attribute the node to look-up removed attributes
	 * @return map of removed attributes
	 */
	public Map<INode, IAttribute> getRemovedRatingAttributes(INode attribute) {
		return removedAttributes.get(attribute);

	}

}
