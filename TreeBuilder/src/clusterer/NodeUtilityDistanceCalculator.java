package clusterer;

public class NodeUtilityDistanceCalculator implements INodeDistanceCalculator {

	//TODO: When merging more than 2 nodes, utility can't be simply translated into distance, since utility
	// depends on the merged node itself
	
	public double calculateDistance(INode n1, INode n2) {
		// Calculates distance of two nodes based on Cobweb utility function
		
		double pClass = 0; //Probability of the class
		double pAttrInClass = 0; //Probability of the attribute value, given membership in class
		double pAttrInData = 0; //Probability of the attribute value in the data set
		double k = 0; //Number of categories
		
		double utility = 0; //The utility metric to be calculated
		double distance = 0;
		//1. Merge n1 and n2	-> New merge function needed?
		//2. Calculate utility for the new node
		utility = (pClass * pAttrInClass * pAttrInData) / k;
		//3. Calculate distance from utility (Nodes with high utility should have low distance)
		distance = 1/utility;
		
		return distance;
	}

}

