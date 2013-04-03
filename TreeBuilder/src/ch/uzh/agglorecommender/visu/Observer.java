package ch.uzh.agglorecommender.visu;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;

/**
 * The observer of the GoF observer pattern.
 */
public interface Observer {
	
	/**
	 * Called by the observable upon a data model change.
	 * @param userClusterSet the set of user clusters
	 * @param contentClusterSet the set of content clusters
	 */
	public void update(IClusterSet<INode> userClusterSet, IClusterSet<INode> contentClusterSet);

}
