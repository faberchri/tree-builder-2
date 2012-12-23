package modules;

import java.util.Arrays;
import java.util.List;

import clusterer.IMergeResult;
import clusterer.INode;

public class MergeResult implements IMergeResult {
	
	private final double utility;
	private final INode[] mergeNodes;
	
	public MergeResult(double utility, INode[] nodesOfThisMerge) {
		this.mergeNodes = nodesOfThisMerge;		
		this.utility = utility;
	}
		
	public double getCategoryUtility() {
		return utility;
	}
	
	@Override
	public List<INode> getNodes() {
		return Arrays.asList(mergeNodes);
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
		for (INode n : mergeNodes) {
			sb.append(n.toString());
			sb.append("\n");
		}
		sb.append("is equal ");
		sb.append(utility);
		return sb.toString();
	}
	
}
