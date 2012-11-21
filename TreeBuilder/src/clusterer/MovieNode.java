package clusterer;

import java.util.Map;
import java.util.Set;


final class MovieNode extends AbstractNode {
	
	private static int movieNodeId = 0;
	private static Factory nodeFactory = null;
	
	private Map<Node, Attribute> users;
	private final int id = movieNodeId++;
	
	public MovieNode(NodeDistanceCalculator ndc) {
		super(ndc);
	}
	
	public void setAttributes(Map<Node, Attribute> users) {
		this.users = users;
	}
	
	public Set<Node> getAttributeKeys() {
		return users.keySet();
	}
	
	public Attribute getAttributeValue(Node node) {
		return users.get(node);
	}
			
	public String getAttributesString() {
		return getAttributesString((Map)users);
	}
	
	@Override
	public String toString() {
		return "MovieNode".concat(" ").concat(String.valueOf(id));
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public Factory getNodeFactory() {
		return nodeFactory;
	}
	
	public static Factory getFactory() {
		return nodeFactory;
	}
	
	public static void setFactory(Factory factory) {
		nodeFactory = factory;
	}

	@Override
	public void addAttribute(Node node, Attribute attribute) {
		users.put(node, attribute);
	}
	
}
