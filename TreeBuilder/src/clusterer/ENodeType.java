package clusterer;

/**
 * The existing type of nodes.
 *
 */
public enum ENodeType {
	/**
	 * A node of type user.
	 */
	User, 
	
	/**
	 * A node of type content
	 */
	Content;
	
	/**
	 * The node id counter.
	 */
	private static long id = 0;
	
	/**
	 * Gets a new unique id for a node.
	 * 
	 * @return a unique id.
	 */
	public static long getNewId() {
		long tmp = id;
		id++;
		return tmp;
	}
	
}
