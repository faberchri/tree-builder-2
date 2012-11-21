package clusterer;

import java.util.HashSet;
import java.util.Set;

public class SimpleNodeDistanceCalculator implements NodeDistanceCalculator {
	
	@Override
	public double calculateDistance(Node n1, Node n2) {
		if (! n1.getClass().equals(n2.getClass())) {
			System.err.println("Err: Calculating distance between two nodes of differnet type; in " + this.getClass().getSimpleName());
			System.exit(-1);
		}
		if (n1.equals(n2)) {
			System.err.println("Warning: Calculating distance between two nodes with n1 (" +n1+") == n2  ("+n2+"); in " + this.getClass().getSimpleName());
		}
		Set<Node> union = new HashSet<Node>(n1.getAttributeKeys());
		union.addAll(n2.getAttributeKeys());
		Set<Node> intersect = new HashSet<Node>(n1.getAttributeKeys());
		intersect.retainAll(n2.getAttributeKeys());
		double summedRatingDiffs = 0.0;
		for (Node userNode : intersect) {
			double n1Av = n1.getAttributeValue(userNode).getAverage();
			double n2Av = n2.getAttributeValue(userNode).getAverage();

			summedRatingDiffs = Math.abs(n1Av - n2Av);
		}
		return 1.0 / (double)union.size() * summedRatingDiffs + ((double)union.size() - (double)intersect.size()) / (double)union.size();

	}

}
