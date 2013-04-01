package ch.uzh.agglorecommender.recommender.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;

public class NodeInserter {
	
	ClusterResult clusterResult = null;
	TreeComponentFactory userTreeComponentFactory = null;
	static PositionFinder positionFinder  = new PositionFinder(null);
	
	public NodeInserter(ClusterResult clusterResult, TreeComponentFactory treeComponentFactory){
		this.clusterResult = clusterResult;
		this.userTreeComponentFactory = treeComponentFactory;
	}
	
	/**
	 * Decides which insertion method to use and runs specific method
	 * 
	 * @param node this node is going to be inserted
	 * @return 
	 * 
	 */
	public static Boolean insert(INode node) {
		
		// Find fitting method of insertion
		// FIXME Implement
		
		newNode(node);
		
		return true;
	}

	/**
	 * Insert node according to Cobweb New
	 * 
	 * @param node this node is going to be inserted
	 * 
	 */
	public static void newNode(INode node) {
		
		TreePosition position = positionFinder.findPosition(node, null);
		if(position != null){
			position.getNode().addChild(node);
		}
	}
	
	/**
	 * Insert node according to Cobweb Incorporate
	 * 
	 * @param node this node is going to be incorporated
	 * 
	 */
	public void incorporateNode(INode node) {
			
		// Find position to insert node
		TreePosition position = positionFinder.findPosition(node, null);
			
		if(position != null){
			
			// Create new Node
			List<INode> nodesToMerge = new LinkedList<INode>();
			nodesToMerge.add(node);
			nodesToMerge.add(position.getNode());
			
			INode newNode = userTreeComponentFactory.createInternalNode(
					ENodeType.User,
					nodesToMerge,0); // FIXME: Wie/auf welchem Weg muss die CU berechnet werden?
			
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

}
