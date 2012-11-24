package clusterer;

import java.util.Comparator;

/**
 * Compares two {@code IPrintableNode}s by their id.
 */
public class NodeIdComparator implements Comparator<IPrintableNode> {

	@Override
	public int compare(IPrintableNode o1, IPrintableNode o2) {
		return Long.compare(o1.getId(), o2.getId());
		//return 1;
	}

}
