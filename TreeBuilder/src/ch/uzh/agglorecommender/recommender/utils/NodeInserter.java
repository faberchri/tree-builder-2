package ch.uzh.agglorecommender.recommender.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;

public class NodeInserter {
	
	static TreeComponentFactory userTreeComponentFactory = null;
	private static RecommendationBuilder rb;
	
	public NodeInserter(RecommendationBuilder rb, TreeComponentFactory treeComponentFactory){
		this.userTreeComponentFactory = treeComponentFactory;
		this.rb = rb;
	}
	
	/**
	 * Decides which insertion method to use and runs specific method
	 * 
	 * @param node this node is going to be inserted
	 * @return 
	 * 
	 */
	public static Boolean insert(INode node) {
		
		boolean result = false;
		
		TreePosition position = rb.findMostSimilar(node);
		
		if(position != null){
		
			if(position.getUtility() > 0.2){
				result = incorporateNode(node,position);
			}
			else {
				result = newNode(node,position);
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
	public static boolean newNode(INode node, TreePosition position) {
		
		try {
			if(position != null){
				position.getNode().addChild(node);
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
	public static boolean incorporateNode(INode node, TreePosition position) {
		
		try {
			if(position != null){
				
				// Create new Node
				List<INode> nodesToMerge = new LinkedList<INode>();
				nodesToMerge.add(node);
				nodesToMerge.add(position.getNode());
				
				INode newNode = userTreeComponentFactory.createInternalNode(
						ENodeType.User,
						nodesToMerge,0);
				
				// Insert new Node into tree
				newNode.setParent(position.getNode().getParent());
				Iterator<INode> children = position.getNode().getChildren();
				while(children.hasNext()){	
					INode child = children.next();
					child.setParent(newNode);
					newNode.addChild(child);
				}
			}
		}
		catch (Exception e){
			return false;
		}
		
		return true;
	}

}
