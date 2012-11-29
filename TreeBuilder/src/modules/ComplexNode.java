package modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistanceCalculator;


public class ComplexNode extends SimpleNode {
	
	
	/**
	 * The distance calculator of the node.
	 */
	private List<Set<INode>> attributeGroups;

	public ComplexNode( ENodeType nodeType, INodeDistanceCalculator ndc) {
		super(nodeType, ndc);
	}
	
	public ComplexNode(ENodeType nodeType, INodeDistanceCalculator ndc, Set<INode> children, Map<INode, IAttribute> attributes, ArrayList<INode> attributeGroup) {
		super(nodeType, ndc, children, attributes);
	}
		
	@Override
	public List<Set<INode>> getAttributeGroups() {
		return attributeGroups;
	}

	@Override
	public void addAttributeGroup(Set<INode> attributeGroup) {
		attributeGroups.add(attributeGroup);
	}
	
}