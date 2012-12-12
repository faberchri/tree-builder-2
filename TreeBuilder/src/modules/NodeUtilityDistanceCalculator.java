package modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import clusterer.Counter;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;


public class NodeUtilityDistanceCalculator implements INodeDistanceCalculator {

	//TODO: When merging more than 2 nodes, utility can't be simply translated into distance, since utility
	// depends on which nodes are merged
	
	float pClass = 0; //Probability of the class
	double pAttrInData = 0; //Sum of Probabilities of the attribute value in the data set
	double pAttrInClass = 0; //Sum of Probabilities of the attribute values, given membership in class
	double k = 0; //Number of categories, e.g. all nodes in this tree	
	
	double utility = 0; //The utility metric to be calculated
	double distance = 0;
		
	// Calculates distance of two nodes based on Cobweb utility function from Gennari, Langley and Fischer "Models 
	// of Incremental Concept Formation", p. 26
	public double calculateDistance(INode n1, INode n2, Counter counter, Set<INode> openNodes) {
		
		// Should already be a list in the method definition
		List<INode> nodesToMerge = new ArrayList(); 
		nodesToMerge.add(n1);
		nodesToMerge.add(n2);
		
		// Merge attributes of all nodes
		ComplexNodeFactory nodeFactory = new ComplexNodeFactory();
		ComplexAttributeFactory attFactory = new ComplexAttributeFactory();
		Map<INode,IAttribute> mergedAttributes = nodeFactory.createAttMap(nodesToMerge, attFactory);
		
		// Count total children of all nodes
		int totalChildren = 0;
		for (INode nodeToMerge : nodesToMerge) {
			totalChildren += nodeToMerge.getChildrenCount();
		}
		
		// Retrieve Data from Counter
		long numberOfAllNodes = 0;
		long nodeCountOnCurrentLevel = 0;
		switch(n1.getNodeType()) {
			case User: 
				numberOfAllNodes = counter.getUserNodeCount();
				nodeCountOnCurrentLevel = counter.getOpenUserNodeCount();
				break;
			case Content: 
				numberOfAllNodes = counter.getUserNodeCount();
				nodeCountOnCurrentLevel = counter.getOpenMovieNodeCount();
		}
		
		//2. Calculate variables necessary for utility calculation
		//2.1 Calculate probability of the class (pClass). This is calculated from number of children / total number of nodes on current level
		if(totalChildren < 1) {
			pClass = (float) 1/nodeCountOnCurrentLevel; // At the first level were all nodes are leaf nodes
		}
		else {
			pClass = (float) totalChildren / (numberOfAllNodes - 1); // All further levels
		}
		//System.out.println("pClass:" + pClass);
		
		//2.2 Calculate Sum of Probabilities of the attribute value in the data set (pAttrInData).
		//Can be translated to: 
		//For each attribute value in tempNode, calculate how often it occurs in dataset compared to size of dataset
		//and then calculate multiplicative inverse (Kehrwert)
		
		// Calculate all Nodes with the same attributes
//		double pAttrInData = 0;
//		for(INode attribute : tempNode.getAttributeKeys()) {
//			int numberOfAllNodesWithAttribute = 0;
//			for(INode testNode : openNodes) {
//				if(testNode.hasAttribute(attribute)) {
//					numberOfAllNodesWithAttribute += testNode.getAttributeValue(attribute).getSupport(); // Adds the number of all subnodes with this attribute
//				}
//			}
//			pAttrInData += numberOfAllNodes / numberOfAllNodesWithAttribute;
//		}
//		System.out.println("pAttrInData:" + pAttrInData);
		
		//2.3 Calculate sum of Probabilities of the attribute values, given membership in class (pAttrInClass)
		//i.e. the expected number of attribute values that one can correctly guess for an arbitrary member of tempNode (= probability of occurring)

		
		// Decide if nominal or scalar
		double pAttrInClass = 0;
		if(1==1)
			pAttrInClass = attrInClassNominal(mergedAttributes);
		else
			pAttrInClass = attrInClassScalar(mergedAttributes);
			
//		double pAttrInClass = 0;
//		for(Entry<INode, IAttribute> attribute : mergedAttributes.entrySet()) {
//			pAttrInClass += attribute.getValue().getAverage();
//		}
		//System.out.println("pAttrInClass:" + pAttrInClass);
		
		//2.4 Calculate number of categories (k)
		//k = numberOfAllNodes;
		//System.out.println("k:" + k);
		
		//3. TODO: Calculate utility for the new node
		if(pAttrInClass != 0){
			//utility = (pClass * pAttrInClass - pAttrInData) / k;
			utility = (pClass * pAttrInClass);
		}
		
		//4. Calculate distance from utility (Nodes with high utility should have low distance)
		if(utility !=0)
			distance = 1/utility;
		else
			distance = Double.MAX_VALUE;
		
		System.out.println("utility/distance: " + distance);
		
		return distance;
	}

	@Override
	public double calculateDistance(INode n1, INode n2) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private double attrInClassNominal(Map<INode,IAttribute> mergedAttributes) {
		double pAttrInClass = 0;
		for(Entry<INode, IAttribute> attribute : mergedAttributes.entrySet()) {
			pAttrInClass += attribute.getValue().getAverage();
		}
		return pAttrInClass;
	}
	
	private double attrInClassScalar(Map<INode, IAttribute> mergedAttributes) {
		return 0.0;
	}

}
