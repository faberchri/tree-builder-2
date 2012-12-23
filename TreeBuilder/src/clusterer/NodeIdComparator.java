package clusterer;

import java.util.Comparator;

/**
 * Compares two {@code IPrintableNode}s by their id.
 */
public class NodeIdComparator implements Comparator<INode> {

	@Override
	public int compare(INode o1, INode o2) {
		return Long.compare(o1.getId(), o2.getId());
	}

}
