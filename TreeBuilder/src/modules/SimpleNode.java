package modules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clusterer.Counter;
import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.INodeDistance;
import clusterer.INodeDistanceCalculator;
import clusterer.IPrintableNode;
import clusterer.NodeIdComparator;


public class SimpleNode implements INode, IPrintableNode, Comparable<SimpleNode>{
	
	
	/**
	 * The unique id of this node.
	 */
	private final long id = ENodeType.getNewId();
	
	/**
	 * The node type.
	 */
	private final ENodeType nodeType;
	
	/**
	 * The attributes of the node.
	 * Stores for each node that is an
	 * attribute of this node a mapping
	 * to an IAttribute object.
	 */
	private Map<INode, IAttribute> attributes;
		
	/**
	 * The children of this node.
	 */
	private Set<INode> children = new HashSet<INode>();
	
	/**
	 * The parent of this node.
	 * Is null s long as the node was not merged.
	 */
	private INode parent = null;
	
	/**
	 * The distance calculator of the node.
	 */
	private INodeDistanceCalculator distanceCalculator;
	

	public SimpleNode( ENodeType nodeType, INodeDistanceCalculator ndc) {
		this.distanceCalculator = ndc;
		this.nodeType = nodeType;
	}
	
	public SimpleNode(ENodeType nodeType, INodeDistanceCalculator ndc, Set<INode> children, Map<INode, IAttribute> attributes) {
		this.distanceCalculator = ndc;
		this.nodeType = nodeType;
		this.children = children;
		this.attributes = attributes;
	}
	
	@Override
	public double getDistance(INode otherNode, Counter counter, Set<INode> openNodes) {
		return distanceCalculator.calculateDistance(this, otherNode, counter, openNodes);
	}
	
	@Override
	public INodeDistance getDistanceToClosestNode(List<INode> list) {
		double shortest = Double.MAX_VALUE;
		INode close = null;
		for (INode node : list) {
			if (node.equals(this)) continue;
			double tmp = this.getDistance(node, null, null);
			if (tmp < shortest) {
				shortest = tmp;
				close = node;
			}
		}
		return new SimpleNodeDistance(shortest, this, close);
	}
	
	@Override
	public String getAttributesString() {
		
		List<SimpleNode> keyList = new ArrayList<SimpleNode>((Collection<? extends SimpleNode>) attributes.keySet());
		Collections.sort(keyList, new NodeIdComparator());
		String s = "";
		for (IPrintableNode node : keyList) {
			s = s.concat(node.toString()).concat(": ").concat(attributes.get(node).toString()).concat(";\t");
		}

		if (s.length() == 0) {
			return "no_attributes";
		} else {
			return s.substring(0, s.length()-1);
		}
	}
		
	@Override
	public int compareTo(SimpleNode o) {
		return ((Long)this.getId()).compareTo((Long)o.getId());
	}
	
	@Override
	public Iterator<INode> getChildren() {
		return children.iterator();
	}
		
	@Override
	public boolean isChild(INode possibleChild) {
		return children.contains(possibleChild);
	}
	
	@Override
	public INode getParent() {
		return parent;
	}
	
	@Override
	public void addChild(INode child) {
		this.children.add(child);
	}
	
	@Override
	public INode setParent(INode parent) {
		INode prevP = this.parent;
		this.parent = parent;
		return prevP;
	}
	
	@Override
	public boolean isLeaf() {
		if (this.children.isEmpty()) return true;
		return false;
	}
	
	@Override
	public boolean isRoot() {
		if (this.parent == null) return true;
		return false;
	}
	
	@Override
	public void setAttributes(Map<INode, IAttribute> movies) {
		this.attributes = movies;
	}
	
	@Override
	public Set<INode> getAttributeKeys() {
		return attributes.keySet();
	}
	
	@Override
	public IAttribute getAttributeValue(INode node) {
		return attributes.get(node);
	}
				
	@Override
	public String toString() {
		return "SimpleNode".concat(" ").concat(String.valueOf(id));
	}

	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public void addAttribute(INode node, IAttribute attribute) {
		attributes.put(node, attribute);		
	}

	@Override
	public ENodeType getNodeType() {
		return nodeType;
	}

	@Override
	public boolean hasAttribute(INode attribute) {
		return attributes.containsKey(attribute);
	}

	@Override
	public IAttribute removeAttribute(INode attribute) {
		return attributes.remove(attribute);
		
	}
	
	@Override
	public List<Set<INode>> getAttributeGroups() {
		// empty implementation, not needed
		return null;
	}

	@Override
	public void addAttributeGroup(Set<INode> Attributegroup) {
		// empty implementation, not needed
		
	}

	@Override
	public void setChildrenCount(int totalChildren) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getChildrenCount() {
		return 0;
		// TODO Auto-generated method stub

	}
	
}