package ch.uzh.agglorecommender.recommender.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

import com.google.common.collect.ImmutableMap;

public class UnpackerTool {
	
	private ImmutableMap<String, INode> leavesMap;

	public UnpackerTool(ImmutableMap<String, INode> leavesMapC){
		this.leavesMap = leavesMapC;
	}

	public INode unpack(INode tempPosition) {
		
		Map<INode,IAttribute> newRatingMap = new HashMap<>();
		
		Set<INode> ratings = tempPosition.getRatingAttributeKeys();
		for(INode rating : ratings){
			List<String> datasetIds = rating.getDataSetIds();
			int i= 0;
			for(String datasetId : datasetIds){
				INode originalNode = findOriginalNode(datasetId); // Attribute Node
				newRatingMap.put(originalNode, tempPosition.getNumericalAttributeValue(rating));
				i++;
			}
		}
		
		tempPosition.setRatingAttributes(newRatingMap);
		
		return tempPosition;
	}
	
	public INode findOriginalNode(String datasetId){
		
		for(String key : leavesMap.keySet()){
			if(key.equals(datasetId)){
				return leavesMap.get(key);
			}
		}
		
		return null;
		
		
		
		// Get all children of position
//		Iterator<INode> children = position.getChildren();
		
		// Determine the one child that has the datasetID in one of its attributes
//		while(children.hasNext()){
//			INode child = children.next();
//			for(INode attributeKey : child.getRatingAttributeKeys()){
//				
//				List<String> datasetIds = attributeKey.getDataSetIds();
//				
//				// Catch condition
//				if(datasetIds.size() == 1 && datasetIds.get(0).equals(datasetId)){
//					System.out.println("datasetID: " + datasetId + "/" + attributeKey.getDataSetIds().toString());
//					return attributeKey;
//				}
//				else {
//					for(String tempDatasetId : datasetIds){
//						if(tempDatasetId.equals(datasetId)){
//							
//							// Search on level deeper 
////							System.out.println("search one level deeper");
//							INode originalNode = findOriginalNode(child,datasetId);
//							if(originalNode != null){
//								return originalNode;
//							}
//							else return null;
//						}
//					}
//				}
//			}
//		}
//		return null;
		
	}
	
//	public IAttribute findOriginalAttribute(INode position, String datasetId) {
//		
//		// Get all children of position
//		Iterator<INode> children = position.getChildren();
//		
//		// Determine the one child that has the datasetID in one of its attributes
//		while(children.hasNext()){
//			INode child = children.next();
//			for(INode attributeKey : child.getRatingAttributeKeys()){
//				
//				List<String> datasetIds = attributeKey.getDataSetIds();
//				
//				// Catch condition
//				if(datasetIds.size() == 1 && datasetIds.get(0).equals(datasetId)){
//					return child.getNumericalAttributeValue(attributeKey);
//				}
//				else {
//					for(String tempDatasetId : datasetIds){
//						if(tempDatasetId.equals(datasetId)){
//							
//							// Search on level deeper 
////							System.out.println("search one level deeper");
//							IAttribute originalAttribute = findOriginalAttribute(child,datasetId);
//							if(originalAttribute != null){
//								return originalAttribute;
//							}
//							else return null;
//						}
//					}
//				}
//			}
//		}
//		return null;
//	}

}
