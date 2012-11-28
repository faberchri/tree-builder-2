package modules;

import java.util.ArrayList;
import java.util.List;

import clusterer.INode;
import clusterer.INodeDistanceCalculator;


public class NodeUtilityDistanceCalculator implements INodeDistanceCalculator {

	//TODO: When merging more than 2 nodes, utility can't be simply translated into distance, since utility
	// depends on which nodes are merged
	
	// Calculates distance of two nodes based on Cobweb utility function
	public double calculateDistance(INode n1, INode n2) {
		
		double pClass = 0; //Probability of the class
		double pAttrInClass = 0; //Probability of the attribute value, given membership in class
		double pAttrInData = 0; //Probability of the attribute value in the data set
		double k = 0; //Number of categories
		
		double utility = 0; //The utility metric to be calculated
		double distance = 0;
		
		// 1. Merge n1 and n2	
		List<INode> nodes = new ArrayList(); 
		nodes.add(n1);
		nodes.add(n2);
		ComplexNodeFactory fac = new ComplexNodeFactory();
		ComplexNode tempNode = (ComplexNode) fac.createCalculationNode(nodes);
		
		// 2. Calculate the 4 values for tempNode
		// pClass: 1/anzahl nodes der eigenen kategorie 
		// pAttrInClass: 1/anzahl gemergter attribute?
		// pAttrInData: 1/anzahl nodes der anderen kategorie?
		// k: 2 ?
		
		// 3. Calculate utility for the new node
		if(k != 0){
			utility = (pClass * pAttrInClass * pAttrInData) / k;	
		}
		
		//4. Calculate distance from utility (Nodes with high utility should have low distance)
		if(utility !=0)
			distance = 1/utility;
		else
			distance = Double.MAX_VALUE;
		
		return distance;
	}

}

