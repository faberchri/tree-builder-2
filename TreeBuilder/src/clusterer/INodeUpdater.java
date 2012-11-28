package clusterer;

import java.util.Set;

public interface INodeUpdater {
	public void updateNodes(INode newNode, Set<INode> nodesToUpdate); // newNode is the node which resulted from last merge
}
