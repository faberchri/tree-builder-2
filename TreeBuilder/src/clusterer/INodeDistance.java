package clusterer;

import java.util.List;

public interface INodeDistance extends Comparable<INodeDistance>{
	public double getDistance();
	public INode getOtherNode(INode n);
	public List<INode> getBothNode();
}
