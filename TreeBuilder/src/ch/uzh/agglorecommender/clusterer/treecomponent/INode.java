package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface INode {
		
	/**
	 * Sets the attribute map of this node to {@code attributes}.
	 * 
	 * @param attributes the new attribute map for this node.
	 */
	public void setAttributes(Map<INode, IAttribute> attributes);
	
	/**
	 * Adds a new entry (INode-IAttribute-key-value-pair)
	 * to the attribute map of this node. A previously
	 * existing mapping for the key (INode) is replaced.
	 * 
	 * @param key the key for the node attribute mapping
	 * @param value the value for the node attribute mapping.
	 */
	public void addAttribute(INode key, IAttribute value);
	
	/**
	 * Gets the attribute value for the passed {@code node}.
	 *
	 * @param node the key to fetch the attribute object.
	 * @return the {@code IAttribute} object mapped to the
	 * passed node or null if no mapping is present.
	 */
	public IAttribute getAttributeValue(INode node);
	
	/**
	 * Gets all nodes from the attribute map of this node.
	 * 
	 * @return an unmodifiable set with all nodes (keys)
	 * contained in this nodes attribute map.
	 */
	public Set<INode> getAttributeKeys();
	
	/**
	 * Gets the type of the attributes of the node (Cobweb/Classit/etc.)
	 * 
	 * @return description of type
	 */
	public String getAttributesType();
		
	/**
	 * Adds a child to the nodes children container.
	 * Does nothing if {@code child} is already a child of this node.
	 *  
	 * @param child the new child of this node.
	 */
	public void addChild (INode child);
	
	/**
	 * Removes a child from the nodes child container
	 * and (only if the passed INode is a child of this node)
	 * sets the parent of the removed child to null.
	 * 
	 * @param child the node to remove
	 * @return true if the node was a child of this node, else false.
	 */
	public boolean removeChild(INode child);
	
	/**
	 * Checks if {@code possibleChild} is
	 * contained in this nodes children collection.
	 * 
	 * @param possibleChild the node to test whether it is child of this node or note.
	 * @return true if {@code possibleChild} is child of this node, false otherwise.
	 */
	public boolean isChild(INode possibleChild);
	
	/**
	 * Gets the children of this node.
	 * 
	 * @return an iterator over this nodes children collection.
	 */
	public Iterator<INode> getChildren();

	/**
	 * Sets the parent of this node.
	 * 
	 * @param parent the new parent node of this node
	 * @return the previous parent node or null
	 * if parent node was previously null.
	 */
	public INode setParent(INode parent);
	
	/**
	 * Gets the parent of this node.
	 * 
	 * @return the parent of this node or null if parent is not set.
	 */
	public INode getParent();
	
	/**
	 * Checks if this contains node in its children collection.
	 * 
	 * @return true if node is leaf, else false.
	 */
	public boolean isLeaf();
	
	/**
	 * Checks if parent of this node is null.
	 * 
	 * @return true if parent is null, else false.
	 */
	public boolean isRoot();
	
	/**
	 * Get the node type of this node.
	 * 
	 * @return the node type
	 */
	public ENodeType getNodeType();
		
	/**
	 * Checks if {@code this} node has
	 * for the node {@code attribute}
	 * a mapping to an IAttribute object.
	 * 
	 * @param attribute The node to test
	 * for a present mapping.
	 * @return true if a mapping is present, else false.
	 */
	public boolean hasAttribute(INode attribute);
	
	/**
	 * Removes the mapping for {@code attribute }
	 * from this node's attribute map.
	 * 
	 * @param attribute The INode for
	 * which the mapping is removed.
	 * @return the value previously mapped to the {@code attribute}
	 * or null if no mapping was present.
	 */
	public IAttribute removeAttribute(INode attribute);
		
	/**
	 * Gets the total count of children
	 * 
	 * @return void
	 */
	public int getChildrenCount();
	
	
	/**
	 * Gets the size of the subtree.
	 * 
	 * @return the number of nodes contained in the this nodes subtree; 0 for leaves.
	 */
	public int getNumberOfNodesInSubtree();
	
	/**
	 * Gets the sum of leaf nodes in this nodes subtree.
	 * 
	 * @return the sum of leaf nodes in this nodes subtree.
	 * Returns 1, if this node is a leaf.
	 */
	public int getNumberOfLeafNodes();
	
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
	
	/**
	 * Gets the datasetID of the node.
	 * 
	 * @return the datasetID
	 */
	public long getDatasetId();
	
	/**
	 * Gets the id(s) of the data item(s) (i.e. user or contend item) 
	 * contained in this cluster from the clustered data set. 
	 * 
	 * @return an immutable list of all data set id's of the
	 * containing data set item, which are contained in this cluster.
	 * The size of the list is equal to 1 if this node is a leaf
	 * and equal to the number of cluster instances (e.g. users) if
	 * node is the root.
	 */
	public List<Integer> getDataSetIds();
	
	public List<String> getMeta();
	
	/**
	 * Sets the id of the node. Used for evaluations.
	 * 
	 * @param the node id
	 */
	public void setId(long id);
	
	/**
	 * Gets A HTML string representation of this nodes attributes.
	 * @return HTML string
	 */
	public String getAttributeHTMLLabelString();
	
	/**
	 * The category utility of this nodes children merge.
	 * 1 if node is leaf.
	 * @return the category utility
	 */
	public double getCategoryUtility();
	
	/**
	 * Queries for recent node attributes change.
	 * @return true if node attributes were changed recently, else false.
	 */
	public boolean isDirty();
	
	/**
	 * Invalidates the dirty flag (sets the flag to false).
	 */
	public void setClean();
}