package ch.uzh.agglorecommender.recommender.utils;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

/**
 * Class combines helpful information for search, recommendation,
 * evaluation and insertion
 *
 */
public class TreePosition {
	
	private INode node;
	private double utility;
	private double level;

	/**
	 * Needs to be instantiated with node on position, utility of
	 * the node with a given input node and the level of the node
	 * 
	 * @param position the node on the position
	 * @param utility utility of the position node and the input node
	 * @param level the level of the position
	 */
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
