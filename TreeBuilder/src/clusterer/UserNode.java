package clusterer;

import java.util.Map;
import java.util.Set;


final class UserNode extends AbstractNode  {
	
	private static int userNodeId = 0;
	private static Factory nodeFactory = null;
	
	private Map<INode, IAttribute> movies;
	private final int id = userNodeId++;
	
	public UserNode(NodeDistanceCalculator ndc) {
		super(ndc);
	}
	
	public void setAttributes(Map<INode, IAttribute> movies) {
		this.movies = movies;
	}
	
	public Set<INode> getAttributeKeys() {
		return movies.keySet();
	}
	
	public IAttribute getAttributeValue(INode node) {
		return movies.get(node);
	}
			
	public String getAttributesString() {
		return getAttributesString((Map)movies);
	}
	
	@Override
	public String toString() {
		return "UserNode".concat(" ").concat(String.valueOf(id));
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
	public void addAttribute(INode node, IAttribute attribute) {
		movies.put(node, attribute);		
	}
	
}