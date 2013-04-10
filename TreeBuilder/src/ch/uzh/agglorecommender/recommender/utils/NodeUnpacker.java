package ch.uzh.agglorecommender.recommender.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.Searcher;

import com.google.common.collect.ImmutableMap;

/**
 * Provides methods to retrieve a complete node with all
 * original attributes
 *
 */
public class NodeUnpacker {
	
	private Searcher searcher;

	/**
	 * Needs to be instantiated with a reference to a searcher instance,
	 * which provides connections to the trees
	 * 
	 * @param searcher
	 */
	public NodeUnpacker(Searcher searcher){
		this.searcher = searcher;
	}

	/**
	 * Retrieves all original attributes contained in a node and
	 * builds an unpacked node for search of most similar node
	 * 
	 * @param node the node that should be unpacked
	 * @return INode unpacked node
	 */
	public INode unpack(INode node) {
		
		// Determine leaves map
		ImmutableMap<String, INode> leavesMap = null;
		if(node.getNodeType() == ENodeType.Content){
			leavesMap = searcher.getLeavesMapU();
		}
		else if (node.getNodeType() == ENodeType.User){
			leavesMap = searcher.getLeavesMapC();
		}
		
		// Build new rating map
		Map<INode,IAttribute> newRatingMap = new HashMap<>();
		Set<INode> ratings = node.getRatingAttributeKeys();
		for(INode rating : ratings){
			List<String> datasetIds = rating.getDataSetIds();
			for(String datasetId : datasetIds){
				INode originalNode = findOriginalNode(datasetId,leavesMap); // Attribute Node
				newRatingMap.put(originalNode, node.getNumericalAttributeValue(rating));
			}
		}
		
		node.setRatingAttributes(newRatingMap);
		
		return node;
	}
	
	/**
	 * Helper method to find the original node based on the given dataset ID
	 * 
	 * @param datasetId identifying id of original attribute
	 * @param leavesMap map of leaf nodes to find original attribute
	 * @return INode original attribute
	 */
	private INode findOriginalNode(String datasetId, ImmutableMap<String,INode> leavesMap){
		
		if(leavesMap != null){
			for(String key : leavesMap.keySet()){
				if(key.equals(datasetId)){
					return leavesMap.get(key);
				}
			}
		}
		
		return null;
		
	}
}
