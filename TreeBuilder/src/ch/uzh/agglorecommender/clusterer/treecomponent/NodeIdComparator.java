package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.util.Comparator;


/**
 * Compares two {@code INode}s by their id.
 */
public class NodeIdComparator implements Comparator<INode> {

	@Override
	public int compare(INode o1, INode o2) {
		return Long.compare(o1.getId(), o2.getId());
	}

}
