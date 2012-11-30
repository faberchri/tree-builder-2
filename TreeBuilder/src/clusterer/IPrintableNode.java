package clusterer;


/**
 *A printable representation of a node.
 */
public interface IPrintableNode extends INode {
	
	
	/**
	 * Gets a string representation of the nodes attribute map.
	 * 
	 * @return string representation of the nodes attribute map.
	 */
	public String getAttributesString();
	
	/**
	 * Gets the id of the node.
	 * 
	 * @return the node id
	 */
	public long getId();

}