package ch.uzh.agglorecommender.recommender.utils;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class TreePosition {
	
	private INode node;
	private double utility;
	private double level;

	public TreePosition(INode position, double utility, double level){
		this.node = position;
		this.utility = utility;
		this.level = level;
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
	
	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}
	
	public String toString() {
		return node.toString() + " / Utility: " + utility + " / Level: " + level;
	}

}
