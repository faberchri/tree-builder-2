package ch.uzh.agglorecommender.clusterer.treesearch;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;


/**
 * 
 * Immutable parameter object for merge candidates.
 *
 */
public class MergeResult implements IMergeResult {
	
	private final double utility;
	private final INode[] mergeNodesArr;
	private final List<INode> mergeNodesLi;
	
	public MergeResult(double utility, INode[] nodesOfThisMerge) {
		if (nodesOfThisMerge == null) throw new InvalidParameterException("passed merge array is null");
 		this.mergeNodesArr = nodesOfThisMerge;	
		this.mergeNodesLi = null;
		this.utility = utility;
	}
	
	public MergeResult(double utility, List<INode> nodesOfThisMerge) {
		if (nodesOfThisMerge == null) throw new InvalidParameterException("passed merge list is null");
		this.mergeNodesLi = nodesOfThisMerge;
		this.mergeNodesArr = null;
		this.utility = utility;
	}
		
	public double getCategoryUtility() {
		return utility;
	}
	
	@Override
	public List<INode> getNodes() {
		if (mergeNodesLi != null) return mergeNodesLi;
		return Arrays.asList(mergeNodesArr);
	}

	
	@Override
	public int compareTo(IMergeResult o) {
		double d1 = this.getCategoryUtility();
		double d2 = o.getCategoryUtility();
		if (d1 < d2) return -1;
		if (d1 > d2) return 1;
		return 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Category Utility of merging\n");
		for (INode n : getNodes()) {
			sb.append(n.toString());
			sb.append("\n");
		}
		sb.append("is equal ");
		sb.append(utility);
		return sb.toString();
	}
	
}
