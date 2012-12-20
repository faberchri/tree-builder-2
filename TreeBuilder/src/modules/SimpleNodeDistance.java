package modules;

import java.util.ArrayList;
import java.util.List;

import clusterer.INode;
import clusterer.IMergeResult;

public class SimpleNodeDistance implements IMergeResult {
	
	private final double distance;
	private final INode n1;
	private final INode n2;
	
	public SimpleNodeDistance(double distance, INode n1, INode n2) {
		this.distance = distance;
		this.n1 = n1;
		this.n2 = n2;
	}
	
	public INode getOtherNode(INode n) {
		if (n.equals(n1)) {
		 return n2;	
		}
		if (n.equals(n2)) {
			return n1;
		}
		return null;
	}
	
	public double getCategoryUtility() {
		return distance;
	}
	
	@Override
	public List<INode> getBothNode() {
		List<INode> li = new ArrayList<INode>(2);
		li.add(n1);
		li.add(n2);
		return li;
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
		return n1.toString().concat(" - ").concat(n2.toString()).concat(" d: "+ String.valueOf(distance));
	}
	
}
