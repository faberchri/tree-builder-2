package clusterer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Node {
	public double getDistance(Node otherNode);
	public NodeDistance getDistanceToClosestNode(List<Node> list);
	public void setAttributes(Map<Node, IAttribute> attributes);
	public void addAttribute(Node node, IAttribute attribute);
	public IAttribute getAttributeValue(Node node);
	public Set<Node> getAttributeKeys();
	public int getId();
	public void addChild (Node child);
	public boolean isChild(Node possibleChild);
	public Iterator<Node> getChildren();

	public void setParent(Node parent);
	public Node getParent();
	public boolean isLeaf();
	public boolean isRoot();
	public Factory getNodeFactory();
	
//	public Set<Node> getChildrenSet();
}