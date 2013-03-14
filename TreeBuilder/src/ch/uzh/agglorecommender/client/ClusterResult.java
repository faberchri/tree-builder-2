package ch.uzh.agglorecommender.client;

import java.io.Serializable;
import java.util.UUID;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * Parameter object that stores the result of a clustering process.
 * Is immutable.
 *
 */
public class ClusterResult implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	

	/**
	 * Map of user id as in data set to the corresponding node.
	 */
	private final ImmutableMap<String, INode> userTreeLeavesMap;
	
	/**
	 * Map of content id as in data set to the corresponding node.
	 */
	private final ImmutableMap<String, INode> contentTreeLeavesMap;
	
	/**
	 * The root node of the user cluster tree of a completed
	 * clustering process.
	 */
	private final INode userTreeRoot;
	
	/**
	 * The root node of the content cluster tree of a completed
	 * clustering process.
	 */
	private final INode contentTreeRoot;
	
	/**
	 * The id of the tree builder. Can be used as run id.
	 */
	private final UUID treeBuilderId;

	/**
	 * Instantiates a new immutable cluster result.
	 * @param userTreeLeavesMap maps the user id's as
	 * in data set to the corresponding node.
	 * @param contentTreeLeavesMap maps the content item id's as
	 * in data set to the corresponding node.
	 * @param userTreeRoot root of the user tree
	 * @param contentTreeRoot root of the content tree
	 * @param treeBuilderId unique id of the tree builder / run.
	 */
	public ClusterResult(ImmutableMap<String, INode> userTreeLeavesMap,
			ImmutableMap<String, INode> contentTreeLeavesMap,
			INode userTreeRoot, INode contentTreeRoot, UUID treeBuilderId) {
		
		this.userTreeLeavesMap = userTreeLeavesMap;
		this.contentTreeLeavesMap = contentTreeLeavesMap;
		this.userTreeRoot = userTreeRoot;
		this.contentTreeRoot = contentTreeRoot;
		this.treeBuilderId = treeBuilderId;
	}

	/**
	 * Gets the data set user id to leaf node map.
	 * 
	 * @return the immutable map of the user id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<String, INode> getUserTreeLeavesMap() {
		return userTreeLeavesMap;
	}

	/**
	 * Gets the data set content id to leaf node map.
	 * 
	 * @return the immutable map of the content id as in the data set to the
	 * node in the cluster tree.
	 */
	public ImmutableMap<String, INode> getContentTreeLeavesMap() {
		return contentTreeLeavesMap;
	}

	/**
	 * Get the user tree root.
	 * 
	 * @return the root of the user tree. Is null if clustering has not yet
	 * converged.
	 */
	public INode getUserTreeRoot() {
		return userTreeRoot;
	}

	/**
	 * Get the content tree root.
	 * 
	 * @return the root of the content tree. Is null if clustering has not yet
	 * converged.
	 */
	public INode getContentTreeRoot() {
		return contentTreeRoot;
	}
	
	/**
	 * Gets the uuid of the tree builder.
	 * @return uuid of the tree builder.
	 */
	public UUID getTreeBuilderId() {
		return treeBuilderId;
	}
	
}
