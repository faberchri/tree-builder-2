package modules;

import java.util.List;

import clusterer.AttributeFactory;
import clusterer.ENodeType;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;
import clusterer.NodeFactory;

public class UtilityNodeFactory extends NodeFactory{

	private static long movieNodeCount = 0;
	private static long userNodeCount = 0;
	

	@Override
	public INode createInternalNode(ENodeType typeOfNewNode,
			List<INode> nodesToMerge,
			INodeDistanceCalculator nodeDistanceCalculator,
			AttributeFactory attributeFactory) {
		// create calculation node and add it to tree
		return null;
	}

	@Override
	public INode createCalculationNode(List<INode> nodesToMerge) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode createLeafNode(ENodeType typeOfNewNode,
			INodeDistanceCalculator nodeDistanceCalculator) {
		try{
			switch(typeOfNewNode){
			case User:
				userNodeCount++;
			case Content:
				movieNodeCount++;
			}
		}
			catch (NumberFormatException bigNumber){
			System.out.println("The Data contains too many items!");
			}	
		//return new UtilityNode(typeOfNewNode, nodeDistanceCalculator);
		return null;
	}
	
	public static long getNumberOfUsers(){
		return userNodeCount;
	}
	public static long getNumberOfMovies(){
		return movieNodeCount;
	}
}
