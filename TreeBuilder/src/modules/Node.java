package modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clusterer.ENodeType;
import clusterer.IAttribute;
import clusterer.INode;
import clusterer.NodeIdComparator;


public class Node implements INode, Comparable<Node>{
	
	
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
	private List<INode> children = new ArrayList<INode>();
	
	/**
	 * The parent of this node.
	 * Is null s long as the node was not merged.
	 */
	private INode parent = null;
	
	/**
	 * What is this ? (Fabian)
	 */
	private List<Set<INode>> attributeGroups = new ArrayList<Set<INode>>();
	
	public Node( ENodeType nodeType) {
		this.nodeType = nodeType;
	}
		
	public Node(ENodeType nodeType, List<INode> children, Map<INode, IAttribute> attributes) {
		this.nodeType = nodeType;
		if (children != null) {
			this.children = children;	
		}
		this.attributes = attributes;
	}
			
	@Override
	public String getAttributesString() {
		
		List<INode> keyList = new ArrayList<INode>(attributes.keySet());
		Collections.sort(keyList, new NodeIdComparator());
		String s = "";
		for (INode node : keyList) {
			s = s.concat(node.toString()).concat(": ").concat(attributes.get(node).toString()).concat(";\t");
		}

		if (s.length() == 0) {
			return "no_attributes";
		} else {
			return s.substring(0, s.length()-1);
		}
	}
		
	@Override
	public int compareTo(Node o) {
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
	public boolean removeChild(INode child) {
		if (children.remove(child)) {
			child.setParent(null);
			return true;
		}
		return false;
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
		return "Node".concat(" ").concat(String.valueOf(id));
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
		return attributeGroups;
	}

	@Override
	public void addAttributeGroup(Set<INode> attributeGroup) {
		attributeGroups.add(attributeGroup);
	}	

	@Override
	public int getChildrenCount() {
		return children.size();

	}

	@Override
	public int getNumberOfNodesInSubtree() {
		int res = children.size();
		for (INode child : children) {
			res += child.getNumberOfNodesInSubtree();
		}
		return res;
	}

	@Override
	public int getNumberOfLeafNodes() {
		if (isLeaf()) return 1;
		int sum = 0;
		for (INode child : children) {
			sum += child.getNumberOfLeafNodes();
		}
		return sum;
	}
	
}