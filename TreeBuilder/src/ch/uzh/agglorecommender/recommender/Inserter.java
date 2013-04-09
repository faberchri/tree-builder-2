package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class Inserter {
	
	/**
	 * Decides which insertion method to use and runs specific method
	 * 
	 * @param node this node is going to be inserted
	 * @param position 
	 * @return 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 * 
	 */
	public Boolean insert(INode node, TreePosition position) throws InterruptedException, ExecutionException {
		
		boolean result = false;
		
		if(position != null){
			
			System.out.println(position.getNode().getId());
		
			// According to Gennari, Langley, Fisher (1988), P.22
			if(position.getNode().isLeaf()){
				result = newNode(node,position.getNode());
			}
			else {
				result = incorporateNode(node,position.getNode());
			}
		}
		
		return result;
	}

	/**
	 * Insert node according to Cobweb New
	 * 
	 * @param node this node is going to be inserted
	 * 
	 */
	private static boolean newNode(INode node, INode position) {
		
		// Create a new child M of node position.
		// Initialize M's probabilities to those for position. 
		// Create a new child 0 of position.
		// Initialize O's probabilities using node's values.
		
		try {
			if(position != null){
				position.addChild(position);
				position.addChild(node);
			}
			
			// Move to next level
			INode parent = position.getParent();
			if(parent != null){
				updateTree(node,position);
			}
		}
		catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	/**
	 * Insert node according to Cobweb Incorporate
	 * 
	 * @param node this node is going to be incorporated
	 * 
	 */
	private static boolean incorporateNode(INode node, INode position) {
		
		// Update the probability of category 1. For each attribute A in instance I.
		// For each value V of A,
		// Update the probability of V given category 1.
		
		try {
			if(position != null){
				
				// Create new Node
				List<INode> nodesToMerge = new LinkedList<INode>();
				nodesToMerge.add(node);
				nodesToMerge.add(position);
				
				INode newNode = TreeComponentFactory.getInstance().createInternalNode(
						nodesToMerge,
						0.5); // FIXME Utility // FIXME Flexibility Type
				
				// Replace old node with new node
				newNode.setParent(position.getParent());
				Iterator<INode> children = position.getChildren();
				while(children.hasNext()){	
					INode child = children.next();
					child.setParent(newNode);
					newNode.addChild(child);
				}
				
				// Move to next level
				INode parent = position.getParent();
				if(parent != null){
					System.out.println(parent.getId());
					System.out.println(parent.getParent().getId());
					System.out.println(parent.getParent().getParent().getId());
					System.exit(1);
					updateTree(newNode,parent);
				}
			}
		}
		catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	public static void updateTree(INode node, INode position){
		
		System.out.println(position.getId());
		
		Set<INode> ratingsNew = node.getRatingAttributeKeys();
		Set<INode> ratingsPos = position.getRatingAttributeKeys();
		
		// Build Map
		Map<INode,IAttribute> attMap = new HashMap<>();
		for(INode rating : ratingsPos){
			attMap.put(rating, position.getNumericalAttributeValue(rating));
		}
		
		for(INode rating : ratingsNew){
			
			IAttribute att1 = position.getNumericalAttributeValue(rating);
			IAttribute att2 = node.getNumericalAttributeValue(rating);
			
			if(attMap.containsKey(rating)){
				
				int support 				= att1.getSupport() + att2.getSupport();
				double sumOfRatings 		= att1.getSumOfRatings() + att2.getSumOfRatings();
				double squaredSumOfRatings 	= att1.getSumOfSquaredRatings() + att2.getSumOfSquaredRatings();
				
				IAttribute newAtt = new ClassitAttribute(support,sumOfRatings,squaredSumOfRatings); // support, sumOfRating, squared
				attMap.put(rating, newAtt);
			}
			else {
				attMap.put(rating, att2);
			}
		}
		position.setRatingAttributes(attMap);
		
		// Move to next level
		INode parent = position.getParent();
		if(parent != null){
			System.out.println("move up");
			updateTree(node,parent);
		}
	}
}
