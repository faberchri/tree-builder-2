package clusterer;

import java.util.List;

public interface NodeDistance extends Comparable<NodeDistance>{
	public double getDistance();
	public INode getOtherNode(INode n);
	public List<INode> getBothNode();
}
