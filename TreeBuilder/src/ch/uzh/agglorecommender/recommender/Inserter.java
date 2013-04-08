package ch.uzh.agglorecommender.recommender;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
		
			if(position.getUtility() > 0.2){ // FIXME needs to have a theoretical basis
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
	private static boolean newNode(INode node, TreePosition position) {
		
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
	private static boolean incorporateNode(INode node, TreePosition position) {
		
		try {
			if(position != null){
				
				// Create new Node
				List<INode> nodesToMerge = new LinkedList<INode>();
				nodesToMerge.add(node);
				nodesToMerge.add(position.getNode());
				
				INode newNode = TreeComponentFactory.getInstance().createInternalNode(
//						ENodeType.User,
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
	
	private void updateTree(INode node){
		
	}

}
