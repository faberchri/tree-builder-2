package clusterer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public abstract class AbstractNode implements PrintableNode, Comparable<AbstractNode>{
	
	private Set<Node> children = null;
	private Node parent = null;
	
	private NodeDistanceCalculator distanceCalculator;
	
//	private boolean dirty = true;
			
	public AbstractNode(NodeDistanceCalculator ndc) {
		this.distanceCalculator = ndc; 
	}
	
	public double getDistance(Node otherNode) {
		return distanceCalculator.calculateDistance(this, otherNode);
	}
	
	public NodeDistance getDistanceToClosestNode(List<Node> list) {
		double shortest = Double.MAX_VALUE;
		Node close = null;
		for (Node node : list) {
			if (node.equals(this)) continue;
			double tmp = this.getDistance(node);
			if (tmp < shortest) {
				shortest = tmp;
				close = node;
			}
		}
		return new SimpleNodeDistance(shortest, this, close);
	}
	
//	public boolean isDirty() {
//		if (closestNode == null) {
//			return true;
//		}
//		return dirty;
//	}
	
	String getAttributesString(Map<PrintableNode, Attribute> map) {

		List<PrintableNode> keyList = new ArrayList<PrintableNode>(map.keySet());
		Collections.sort(keyList, new NodeIdComparator());
		String s = "";
		for (PrintableNode node : keyList) {
			s = s.concat(node.toString()).concat(": ").concat(map.get(node).toString()).concat(";\t");
		}

		if (s.length() == 0) {
			return "no_attributes";
		} else {
			return s.substring(0, s.length()-1);
		}
	}
	
	public abstract int getId();
	
	@Override
	public int compareTo(AbstractNode o) {
		return ((Integer)this.getId()).compareTo((Integer)o.getId());
	}
	
	@Override
	public Iterator<Node> getChildren() {
		return children.iterator();
	}
	
	public Set<Node> getChildrenSet() {
		return children;
	}
	
	@Override
	public boolean isChild(Node possibleChild) {
		return children.contains(possibleChild);
	}
	
	@Override
	public Node getParent() {
		return parent;
	}
	
	@Override
	public void addChild(Node child) {
		if (this.children == null) {
			this.children = new HashSet<Node>(); 
		}
		this.children.add(child);
	}
	
	@Override
	public void setParent(Node parent) {
		this.parent = parent;		
	}
	
	@Override
	public boolean isLeaf() {
		if (this.children == null) return true;
		return false;
	}
	
	@Override
	public boolean isRoot() {
		if (this.parent == null) return true;
		return false;
	}
	
}