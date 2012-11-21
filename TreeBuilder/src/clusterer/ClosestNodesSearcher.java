package clusterer;

import java.util.List;
import java.util.Set;

public interface ClosestNodesSearcher {
	public List<Node> getClosestNodes(Set<Node> openNodes);
	public void setNodeOfLastMerge(Node newNode);
}
