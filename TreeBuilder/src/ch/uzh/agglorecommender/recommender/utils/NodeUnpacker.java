package ch.uzh.agglorecommender.recommender.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.Recommender;

import com.google.common.collect.ImmutableMap;

public class NodeUnpacker {
	
	private ImmutableMap<String, INode> leavesMapU;
	private ImmutableMap<String, INode> leavesMapC;

	public NodeUnpacker(Recommender rm){
		this.leavesMapU = rm.getLeavesMapU();
		this.leavesMapC = rm.getLeavesMapC();
	}

	public INode unpack(INode tempPosition) {
		
		// Determine leaves map
		ImmutableMap<String, INode> leavesMap = null;
		if(tempPosition.getNodeType() == ENodeType.Content){
			leavesMap = leavesMapU;
		}
		else if (tempPosition.getNodeType() == ENodeType.User){
			leavesMap = leavesMapC;
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
