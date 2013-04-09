package ch.uzh.agglorecommender.recommender.utils;

import java.util.Comparator;
import java.util.Map;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class RatingComparator implements Comparator<INode> {

	private Map<INode, IAttribute> base;
	
	public RatingComparator(Map<INode, IAttribute> base) {
		this.base = base;
	}

	// Note: this comparator imposes orderings that are inconsistent with equals.    
	public int compare(INode n1, INode n2) {

		IAttribute a1 = base.get(n1);
		IAttribute a2 = base.get(n2);

		double mean1 = a1.getSumOfRatings() / a1.getSupport();
		double mean2 = a2.getSumOfRatings() / a2.getSupport();

		if (mean1 >= mean2) {
			return -1;
		} 
		else {
			return 1;
		}
	}
}

