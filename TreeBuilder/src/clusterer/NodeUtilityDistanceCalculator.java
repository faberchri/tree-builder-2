package clusterer;

public class NodeUtilityDistanceCalculator implements INodeDistanceCalculator {

	
	public double calculateDistance(INode n1, INode n2) {
		// Calculates distance of two nodes based on Cobweb utility function
		
		double pClass = 0; //Probability of the class
		double pAttrInClass; //Probability of the attribute value, given membership in class
		double pAttInData; //Probability of the attribute value in the data set
		double k = 0; //Number of categories
		
		double utility = 0; //The utility metric to be calculated
		
		//1. Merge n1 and n2	
		//2. Calculate utility for the new node
		//3. Calculate distance from utility (Nodes with high utility should have low distance)
		
		return 0;
	}

}

