package modules;

import java.util.ArrayList;
import java.util.List;

import clusterer.INode;
import clusterer.INodeDistanceCalculator;


public class NodeUtilityDistanceCalculator implements INodeDistanceCalculator {

	//TODO: When merging more than 2 nodes, utility can't be simply translated into distance, since utility
	// depends on which nodes are merged
	
	double pClass = 0; //Probability of the class
	double pAttrInClass = 0; //Sum of Probabilities of the attribute values, given membership in class
	double pAttrInData = 0; //Sum of Probabilities of the attribute value in the data set
	double k = 0; //Number of categories
	
	double utility = 0; //The utility metric to be calculated
	double distance = 0;
		
	// Calculates distance of two nodes based on Cobweb utility function from Gennari, Langley and Fischer "Models 
	// of Incremental Concept Formation", p. 26
	public double calculateDistance(INode n1, INode n2) {
		List<INode> nodes = new ArrayList(); 
		nodes.add(n1);
		nodes.add(n2);
		
		UtilityNode tempNode = new UtilityNode(n1.getNodeType(), this); //Temporary node for utility calculation 
		
		//1. Merge n1 and n2	
		
		UtilityNodeFactory fac = new UtilityNodeFactory();
		tempNode = (UtilityNode) fac.createCalculationNode(nodes);
		
		//2. Calculate variables necessary for utility calculation
		//2.1 Calculate probability of the class (pClass). This corresponds to: Total number of attributes/attributes in this node
		double noAttributes = 0; //TODO: How to count the total number of attributes
		double noAttributesInNode = tempNode.getNumberOfAttributes();
		
		switch(tempNode.getNodeType()){
		case User:
			noAttributes = UtilityNodeFactory.getNumberOfMovies();
		case Content:
			noAttributes = UtilityNodeFactory.getNumberOfUsers();
		}
		
		if (noAttributes == 0){
			System.out.println("Can't calculate utility: No Attributes found"); //Anm: try/catch-clause einbauen?
		}
		else{
			pClass = noAttributes/noAttributesInNode;
		}
		
		//2.2 Calculate sum of Probabilities of the attribute values, given membership in class (pAttrInClass)
		
		//2.3 Calculate Sum of Probabilities of the attribute value in the data set (pAttrInData).
		//Can be translated to: 
		//For each attribute value in tempNode, calculate how often it occurs in dataset and then calculate multiplicative inverse (Kehrwert)
		
		
		
		//2.4 Calculate number of categories (k)
		switch(tempNode.getNodeType()){
		case User:
			k = UtilityNodeFactory.getNumberOfUsers();
		case Content:
			k = UtilityNodeFactory.getNumberOfMovies();
		}
		
		
		//3. TODO: Calculate utility for the new node
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


