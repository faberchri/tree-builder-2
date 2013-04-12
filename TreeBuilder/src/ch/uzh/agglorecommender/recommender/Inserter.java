package ch.uzh.agglorecommender.recommender;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

/**
 * Instantiates a new Inserter, which offers the possibility to insert
 * a new node into the respective tree
 *
 */
public class Inserter {

	private static TreeComponentFactory treeComponentFactory = TreeComponentFactory.getInstance();

	/**
	 * Inserts a node into a certain position and decides on method
	 * based on the node attributes
	 * 
	 * @param node the node to insert
	 * @param position the position where the node should be inserted
	 * @return boolean value about success of insertion
	 */
	public Boolean insert(INode node, TreePosition position){

		if(position != null){

			if(position.getNode().isLeaf()){
				newNode(node,position.getNode());
			}
			else {
				incorporateNode(node,position.getNode());
			}
		}

		return true;
	}

	/**
	 * Inserts a new node if position to insert is a leaf
	 * 
	 * @param node the node to insert
	 * @param position the position where the node should be inserted
	 */
	private void newNode(INode node, INode position) {
		
		// Create NodesToMerge
		Set<INode> nodesToMerge = new HashSet<>();
		nodesToMerge.add(node);
		nodesToMerge.add(position);
		
		// Create new Node
		INode newNode = treeComponentFactory.createInternalNode(nodesToMerge, position.getCategoryUtility());
		newNode.addChild(position);
		newNode.addChild(node);
		newNode.setParent(position.getParent());
		
		// Redefine nodes
		position.setParent(newNode);
		node.setParent(newNode);
		
		incorporateNode(node,newNode.getParent());
	}

	/**
	 * Incorporates the new node into the position node if position 
	 * to insert is a not a leaf
	 * 
	 * @param node the node to insert
	 * @param position the position where the node should be inserted
	 */
	private static void incorporateNode(INode node, INode position){

		if(node != null && position != null){

			if(!position.isRoot()){
				INode parent = position.getParent();

				// Create NodesToMerge
				Set<INode> nodesToMerge = new HashSet<>();
				nodesToMerge.add(node);
				nodesToMerge.add(position);
				
				// Create new Node
				INode newNode = treeComponentFactory.createInternalNode(nodesToMerge, position.getCategoryUtility());
				
				// Switch to new Node
				newNode.setParent(position.getParent());
				position.setParent(null);

				Iterator<INode> it = position.getChildren();
				Set<INode> children = new HashSet<>();
				while(it.hasNext()){
					INode child = it.next();
					children.add(child);
					child.setParent(newNode);
				}

				for(INode child : children){
					position.removeChild(child);
				}

				// Next Level
				incorporateNode(node, parent);
			}
		}
	}
}
