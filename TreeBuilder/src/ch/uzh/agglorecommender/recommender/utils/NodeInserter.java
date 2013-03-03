package ch.uzh.agglorecommender.recommender.treeutils;

import java.io.File;
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
	PositionFinder positionFinder  = new PositionFinder();
	
	public NodeInserter(ClusterResult clusterResult, TreeComponentFactory userTreeComponentFactory){
		this.clusterResult = clusterResult;
		this.userTreeComponentFactory = userTreeComponentFactory;
	}
	
	/**
	 * Read Insertion File, create Nodes and insert
	 * 
	 * @param file this file is going to be read
	 * 
	 */
	public void processList(File file) {
		// FIXME Implement
		
		for (INode node : nodes){
			insert(node);
		}
	}
	
	/**
	 * Decides which insertion method to use and runs specific method
	 * 
	 * @param node this node is going to be inserted
	 * 
	 */
	public void insert(INode node) {
		
		// Find fitting method of insertion
		// FIXME Implement
		
		newNode(node);
		
	}

	/**
	 * Insert node according to Cobweb New
	 * 
	 * @param node this node is going to be inserted
	 * 
	 */
	public void newNode(INode node) {
		
		INode position = positionFinder.findPosition(node, null, 0);
		if(position != null){
			position.addChild(node);
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
		INode position = positionFinder.findPosition(node, null, 0);
			
		if(position != null){
			
			// Create new Node
			List<INode> nodesToMerge = new LinkedList<INode>();
			nodesToMerge.add(node);
			nodesToMerge.add(position);
			
			INode newNode = userTreeComponentFactory.createInternalNode(
					ENodeType.User,
					nodesToMerge,0); // FIXME: Wie/auf welchem Weg muss die CU berechnet werden?
			
			// Insert new Node into tree
			newNode.setParent(position.getParent());
			Iterator<INode> children = position.getChildren();
			while(children.hasNext()){	
				INode child = children.next();
				child.setParent(newNode);
				newNode.addChild(child);
			}
		}
	}

}
