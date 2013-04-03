package ch.uzh.agglorecommender.clusterer.treeupdate;

import java.io.Serializable;
import java.util.Collection;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

/**
 * A node updater that does nothing.
 *
 */
public class NullUpdater implements INodeUpdater, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public void updateNodes(INode newNode, Collection<INode> nodesToUpdate) {
		// Nothing to do

	}

}
