package ch.uzh.agglorecommender.recommender.utils;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class TreePosition {
	
	private INode node;
	private Double utility;

	public TreePosition(INode position, Double utility){
		this.node = position;
		this.utility = utility;
	}

	public INode getNode() {
		return node;
	}

	public void setNode(INode position) {
		this.node = position;
	}

	public Double getUtility() {
		return utility;
	}

	public void setUtility(Double utility) {
		this.utility = utility;
	}
	
	public String toString() {
		return node.toString() + " / Utility: " + utility;
	}

}
