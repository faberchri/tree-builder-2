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

public class NodeUnpacker {
	
	private Searcher searcher;

	public NodeUnpacker(Searcher searcher){
		this.searcher = searcher;
	}

	public INode unpack(INode tempPosition) {
		
		// Determine leaves map
		ImmutableMap<String, INode> leavesMap = null;
		if(tempPosition.getNodeType() == ENodeType.Content){
			leavesMap = searcher.getLeavesMapU();
		}
		else if (tempPosition.getNodeType() == ENodeType.User){
			leavesMap = searcher.getLeavesMapC();
		}
		
		// Build new rating map
		Map<INode,IAttribute> newRatingMap = new HashMap<>();
		Set<INode> ratings = tempPosition.getRatingAttributeKeys();
		for(INode rating : ratings){
			List<String> datasetIds = rating.getDataSetIds();
			for(String datasetId : datasetIds){
				INode originalNode = findOriginalNode(datasetId,leavesMap); // Attribute Node
				newRatingMap.put(originalNode, tempPosition.getNumericalAttributeValue(rating));
			}
		}
		
		tempPosition.setRatingAttributes(newRatingMap);
		
		return tempPosition;
	}
	
	public INode findOriginalNode(String datasetId, ImmutableMap<String,INode> leavesMap){
		
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
