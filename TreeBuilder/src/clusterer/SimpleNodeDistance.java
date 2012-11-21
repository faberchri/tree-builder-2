package clusterer;

import java.util.ArrayList;
import java.util.List;

public class SimpleNodeDistance implements NodeDistance {
	
	private final double distance;
	private final Node n1;
	private final Node n2;
	
	public Node getOtherNode(Node n) {
		if (n.equals(n1)) {
		 return n2;	
		}
		if (n.equals(n2)) {
			return n1;
		}
		return null;
	}
	
	public double getDistance() {
		return distance;
	}
	
	@Override
	public List<Node> getBothNode() {
		List<Node> li = new ArrayList<Node>(2);
		li.add(n1);
		li.add(n2);
		return li;
	}
	
	public SimpleNodeDistance(double distance, Node n1, Node n2) {
		this.distance = distance;
		this.n1 = n1;
		this.n2 = n2;
	}

	@Override
	public int compareTo(NodeDistance o) {
		double d1 = this.getDistance();
		double d2 = o.getDistance();
		if (d1 < d2) return -1;
		if (d1 > d2) return 1;
		return 0;
	}
	
	@Override
	public String toString() {
		return n1.toString().concat(" - ").concat(n2.toString()).concat(" d: "+ String.valueOf(distance));
	}
	
}
