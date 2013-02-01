package ch.uzh.agglorecommender.recommender.treeutils;

import java.util.Iterator;

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
	 * @param parent this is the current starting point of the search
	 * @param cutoff this is the previously calculated utility value
	 */
	public INode findPosition(INode inputNode,INode parent,double cutoff) {
		
		if(inputNode != null) {
		
			INode[] nodesToCalculate = new INode[2];
			nodesToCalculate[0] = inputNode;
			nodesToCalculate[1] = parent;
			ClassitMaxCategoryUtilitySearcher helper = new ClassitMaxCategoryUtilitySearcher(); // <--------- unschšn, sollte je nach Typ anderst sein
			
			// Establish cut off value when 0
			if(cutoff == 0) {
			    cutoff = helper.calculateCategoryUtility(nodesToCalculate); //unschšn
				//System.out.println("Established cut off: " + cutoff);
			}
			
			if(parent != null) {
				if(parent.getChildrenCount() > 0){
			
					Iterator<INode> compareSet = parent.getChildren();
					INode nextPosition = null;
					while(compareSet.hasNext()) {
						  
						INode tempPosition = compareSet.next();
						nodesToCalculate[1] = tempPosition;
						double utility = helper.calculateCategoryUtility(nodesToCalculate);
						
						if(utility > highestUtility){
							//System.out.println("Found higher utility: " + utility + ">" + highestUtility);
							highestUtility = utility;
							nextPosition = tempPosition;
						}
					}
			
					if(highestUtility > cutoff) {
						//System.out.println("Continue one level down");
						INode position = findPosition(inputNode,nextPosition,cutoff);
						if (position != null){
							return position;
						}
					}
					else {
						System.out.println("Best position was found " + parent.toString() + "; better utility than all children");
						return parent;
					}
				}
				else {
					System.out.println("Best position was found " + parent.toString() + "; is leaf node");
					return parent;
				}
				
				return null;
			}
			else {
				System.out.println("Parent Node is null");
			}
		}
		
		System.out.println("Position Finder has not received correct values");
		return null;
	}
}
