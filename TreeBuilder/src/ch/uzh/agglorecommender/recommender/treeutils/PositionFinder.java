package ch.uzh.agglorecommender.recommender.treeutils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;

public class PositionFinder {
	
	private double highestUtility = 0;
	
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
			
			// Prepare nodes array for utility calculation
			List<INode> nodesToCalculate = new LinkedList<INode>();
			nodesToCalculate.add(inputNode);
			nodesToCalculate.add(position);
			
			// Define utility searcher -> FIXME sollte uebergeben werden!!!
			ClassitMaxCategoryUtilitySearcher classitUS = null;
			CobwebMaxCategoryUtilitySearcher cobwebUS = null;
			if(1==1){
				classitUS = new ClassitMaxCategoryUtilitySearcher();
			}
			else{
				cobwebUS = new CobwebMaxCategoryUtilitySearcher();
			}
			
			
			// Establish cut off value when 0, ie. when position is on root
			if(cutoff == 0) {
				if(position != null){
					if(classitUS != null){
						cutoff = classitUS.calculateCategoryUtility(nodesToCalculate);
					}
					else if(cobwebUS != null){
						cutoff = cobwebUS.calculateCategoryUtility(nodesToCalculate);	
					}
				}
				else {
					System.out.println("Root node is null");
				}
			}
			
			if(position != null) {
				if(position.getChildrenCount() > 0){
			
					Iterator<INode> compareSet = position.getChildren();
					INode nextPosition = null;
					while(compareSet.hasNext()) {
						  
						INode tempPosition = compareSet.next();
						nodesToCalculate.set(0, tempPosition);
						double utility = 0.0;
						if(classitUS != null){
							utility = classitUS.calculateCategoryUtility(nodesToCalculate);
						}
						else if(cobwebUS != null){
							utility = cobwebUS.calculateCategoryUtility(nodesToCalculate);	
						}
						
						// Find child with highest utility of all children and higher utility than previously found
						if(utility >= highestUtility){
//							System.out.println("Found higher utility: " + utility + ">" + highestUtility);
							highestUtility = utility;
							nextPosition = tempPosition;
						}
					}
					
					// Make decision based on calculated highestUtility
					if(highestUtility >= cutoff) {
//						System.out.println("Continue one level down");
						INode finalPosition = findPosition(inputNode,nextPosition,cutoff);
						if (finalPosition != null){
//							System.out.println("found final position");
							return finalPosition;
						}
						else {
//							System.out.println("received null position");
							return position;
						}
					}
					else {
						System.out.println("Best position was found in tree: " + position.toString());
						return position;
					}
				}
				else {
					System.out.println("Best position was found in leaf: " + position.toString());
					return position;
				}
			}
		}
		
//		System.out.println("Error: inputNode is null");
		return null;
	}
}
