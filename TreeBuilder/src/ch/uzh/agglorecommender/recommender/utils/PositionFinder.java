package ch.uzh.agglorecommender.recommender.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.SharedMaxCategoryUtilitySearcher;

import com.google.common.collect.ImmutableMap;

public class PositionFinder {
	
	SharedMaxCategoryUtilitySearcher cuSearcher;
	UnpackerTool unpacker;
	
	public PositionFinder(ImmutableMap<String,INode> leavesMap){
		cuSearcher = new SharedMaxCategoryUtilitySearcher();
		unpacker = new UnpackerTool(leavesMap);
	}
	
	/**
	 * Finds the best position (most similar node) in the tree for a given node
	 * Calculations are based on category utility. If the previously calculated
	 * utility value is higher than the best utility value on the current level
	 * then the previous position is the best. If the search does not stop in
	 * the tree, it ends on a leaf node.
	 * 
	 * @param inputNodeID this node is the base of the search
	 * @param position this is the current starting point of the search
	 */
	public TreePosition findPosition(INode inputNode,INode position) {
		
		if(inputNode != null && position != null) {
			
			// Calculate Utility of Position
			List<INode> nodesToCalculate = new LinkedList<INode>();
			INode unpackedNode = unpacker.unpack(position);
			nodesToCalculate.add(inputNode);
			nodesToCalculate.add(unpackedNode);
			double highestUtility = cuSearcher.calculateCategoryUtility(nodesToCalculate);
			
			TreePosition bestPosition = new TreePosition(position,highestUtility);
			
			// Collect Best Position from Children
			if(position.getChildrenCount() > 0){
			
				Iterator<INode> children = position.getChildren();
					
				while(children.hasNext()) {
						  
					INode child = children.next();
					
					// Skip on child if no rated movie is included
					boolean relevant = checkRelevance(inputNode,child);
					
					if(relevant){
						// Find child with highest utility of all children and higher utility than previously found
						TreePosition tempPosition = findPosition(inputNode,child);
						if(tempPosition != null){
							if(tempPosition.getUtility() > bestPosition.getUtility()){
								bestPosition = tempPosition;
							}
						}
					}
				}
				return bestPosition;
			}
			return bestPosition;
		}
		return null;
	}
	
	private boolean checkRelevance (INode input, INode test){
		
		int count = 0;
		for(INode rating : input.getRatingAttributeKeys()){
			for(String id : rating.getDataSetIds()){
				for(INode ratingChild : test.getRatingAttributeKeys()){
					if(ratingChild.getDataSetIds().contains(id)){
						return true;
					}
				}
			
			}
		}
		
		return false;
	}	
}
