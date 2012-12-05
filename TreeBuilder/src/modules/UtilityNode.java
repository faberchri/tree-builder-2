package modules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;

//Empty class for distance calculation based on utility
public class UtilityNode extends SimpleNode {

	private int MovieAttributeCount = 0; 
	private int UserAttributeCount = 0; 
	private double probability = 0;
	private Map<INode, IAttribute> attributes;
	
	public UtilityNode(ENodeType typeOfNewNode,
			INodeDistanceCalculator nodeDistanceCalculator, HashSet<INode> hashSet, Map<INode, IAttribute> attMap, ArrayList<INode> attGroup) {
		super(typeOfNewNode, nodeDistanceCalculator);
		//TODO update probability
	}

	public void setProbability(double probability){
		this.probability = probability;
	}
	
	public int getNumberOfAttributes(){
		int i=0;
		switch(this.getNodeType()){
		case User:
			i = UserAttributeCount;
		case Content:
			i = MovieAttributeCount;
		}
		return i;
	}
	
	@Override
	public void setAttributes(Map<INode, IAttribute> attributes) {
		// TODO Auto-generated method stub
		this.attributes = attributes;
		
		switch(this.getNodeType()){
		case User:
			UserAttributeCount++;
		case Content:
			MovieAttributeCount++;
		}
	}


}
