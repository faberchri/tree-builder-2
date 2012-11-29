package modules;

import java.util.Map;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;

//Empty class for distance calculation based on utility
public class UtilityNode extends SimpleNode {

	private int attributeCount = 0; 
	private double probability = 0;
	private Map<INode, IAttribute> attributes;
	
	public UtilityNode(ENodeType typeOfNewNode,
			INodeDistanceCalculator nodeDistanceCalculator) {
		super(typeOfNewNode, nodeDistanceCalculator);
		//TODO update probability
	}

	public void setProbability(double probability){
		this.probability = probability;
	}
	
	public int getNumberOfAttributes(){
		return attributeCount;
	}
	
	@Override
	public void setAttributes(Map<INode, IAttribute> attributes) {
		// TODO Auto-generated method stub
		this.attributes = attributes;
		attributeCount++;
	}


}
