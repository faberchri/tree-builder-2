package clusterer;

public interface IPrintableNode extends INode {
	
	
	/**
	 * Gets a string representation of the nodes attribute map.
	 * 
	 * @return string representation of the nodes attribute map.
	 */
	public String getAttributesString();
	
	/**
	 * Gets the node is.
	 * 
	 * @return the node id
	 */
	public long getId();

}