package ch.uzh.agglorecommender.recommender.treeutils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;

import com.google.common.collect.ImmutableMap;

public class PositionFinder {
	
	private double highestUtility = 0;
	ImmutableMap<Integer,INode> leavesMapU;
	
	/**
	 * Finds the best position (most similar node) in the tree for a given node
	 * Calculations are based on category utility. If the previously calculated
	 * utility value is higher than the best utility value on the current level
	 * then the previous position is the best. If the search does not stop in
	 * the tree, it ends on a leaf node.
	 * 
	 * @param inputNodeID this node is the base of the search
	 * @param position this is the current starting point of the search
	 * @param cutoff this is the previously calculated utility value
	 */
	public INode findPosition(INode inputNode,INode position,double cutoff) {
		
		if(inputNode != null) {
			
			// Prepare nodes array
			List<INode> nodesToCalculate = new LinkedList<INode>();
			nodesToCalculate.add(inputNode);
			nodesToCalculate.add(position);
			
			// Error TESTS
			// Beide Sets haben die Werte, mann kann aber nicht mit dem attKey von input den value holen?!
//			// Tests because of error -> keys sind nicht vergleichbar!!!!!!!!!!!!
			for(INode attKey : inputNode.getAttributeKeys()){
				for(INode attKey2 : position.getAttributeKeys()){
					if(attKey == attKey2){
						System.out.println("Found same key");
					}
				}
			}
			
			
//			if(1==1){ // FIX Need type of clustering that was used	
				ClassitMaxCategoryUtilitySearcher helper = new ClassitMaxCategoryUtilitySearcher();
//			}
//			else if(1==0){
//				CobwebMaxCategoryUtilitySearcher helper = new CobwebMaxCategoryUtilitySearcher();
//			}
			
			// Establish cut off value when 0, ie. when position is on root
			if(cutoff == 0) {
				if(position != null){
//					System.out.println("Compare: " + inputNode.toString() + "(" + inputNode.getAttributeKeys().toString() + ")/" + position.toString() + "(" + position.getAttributeKeys().toString());
					cutoff = helper.calculateCategoryUtility(nodesToCalculate); // FIXME Utility Berechnung liefert 0
				}
				else {
					System.out.println("Root node is null");
				}
				System.out.println("Established cut off: " + cutoff);
			}
			
			if(position != null) {
				if(position.getChildrenCount() > 0){
			
					Iterator<INode> compareSet = position.getChildren();
					INode nextPosition = null;
					while(compareSet.hasNext()) {
						  
						INode tempPosition = compareSet.next();
						nodesToCalculate.set(0, tempPosition);
						double utility = helper.calculateCategoryUtility(nodesToCalculate); // FIXME Utility Berechnung liefert 0
						
						if(utility >= highestUtility){
							//System.out.println("Found higher utility: " + utility + ">" + highestUtility);
							highestUtility = utility;
							nextPosition = tempPosition;
						}
					}
			
					if(highestUtility >= cutoff) {
						//System.out.println("Continue one level down");
						INode finalPosition = findPosition(inputNode,nextPosition,cutoff);
						if (finalPosition != null){
							return finalPosition;
						}
					}
					else {
						System.out.println("Best position was found " + position.toString() + "; better utility than all children");
						return position;
					}
				}
				else {
					System.out.println("Best position was found " + position.toString() + "; is leaf node");
					return position;
				}
				
				return null;
			}
//			else {
//				System.out.println("Parent Node is null");
//			}
		}
		
		System.out.println("Position Finder has not received correct values");
		return null;
	}
}
