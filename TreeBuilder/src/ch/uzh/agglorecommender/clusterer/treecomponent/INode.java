package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JTree;

import ch.uzh.agglorecommender.client.IDataset;

import com.google.common.collect.ImmutableMap;

public interface INode {
		
	/**
	 * Sets the nominal meta attribute map of this node to {@code attributes}.
	 * 
	 * @param attributes the new nominal meta attribute map for this node.
	 */
	public void setNominalMetaAttributes(Map<String, IAttribute> attributes);
	
	/**
	 * Sets the numerical meta attribute map of this node to {@code attributes}.
	 * 
	 * @param attributes the new numerical meta attribute map for this node.
	 */
	public void setNumericalMetaAttributes(Map<String, IAttribute> attributes);
	
	/**
	 * Sets the numerical attribute map of this node to {@code attributes}.
	 * 
	 * @param attributes the new numerical meta attribute map for this node.
	 */
	public void setRatingAttributes(Map<INode, IAttribute> attributes);
	
	/**
	 * Adds a new entry (INode-IAttribute-key-value-pair)
	 * to the rating attribute map of this node. A previously
	 * existing mapping for the key (INode) is replaced.
	 * 
	 * @param key the key for the numerical node attribute mapping
	 * @param value the value for the node attribute mapping.
	 */
	public void addRatingAttribute(INode key, IAttribute value);
	
	/**
	 * Adds a new entry (String-IAttribute-key-value-pair)
	 * to the numerical meta attribute map of this node. A previously
	 * existing mapping for the key (INode) is replaced.
	 * 
	 * @param key the key for the numerical node attribute mapping
	 * @param value the value for the node attribute mapping.
	 */
	public void addNumericalMetaAttribute(String key, IAttribute value);
	
	/**
	 * Adds a new entry (String-IAttribute-key-value-pair)
	 * to the nominal meta attribute map of this node. A previously
	 * existing mapping for the key (INode) is replaced.
	 * 
	 * @param key the key for the nominal node attribute mapping
	 * @param value the value for the node attribute mapping.
	 */
	public void addNominalMetaAttribute(String key, IAttribute value);
	
	/**
	 * Gets the attribute value for the passed {@code node}.
	 *
	 * @param attribute the key for which the IAttribute
	 * value is looked up.
	 * @return the {@code IAttribute} object mapped to the
	 * passed node or null if no mapping is present.
	 */
	public IAttribute getNumericalAttributeValue(Object attribute);
	
	/**
	 * Gets the attribute value for the passed {@code attribute}.
	 *
	 * @param attribute the key to fetch the attribute value.
	 * @return the {@code IAttribute} object mapped to the
	 * passed node or null if no mapping is present.
	 */
	public IAttribute getNominalAttributeValue(Object attribute);
	
	/**
	 * Checks if the passed attribute should be used for clustering.
	 * @param attribute the attribute to query
	 * @return false if explicitly specified in the data set property
	 * file else true. Ratings attributes return always true.
	 */
	public boolean useAttributeForClustering(Object attribute);
	
	/**
	 * Gets the immutable clustering control map that specifies
	 * which attributes shall be used for clustering.
	 * @return immutable map attribute tag -> boolean
	 */
	public ImmutableMap<String, Boolean> getClusteringControlMap();
	
	/**
	 * Gets a reference to the data set of the data set
	 * instance contained in this cluster.
	 * @return a reference to the data set
	 */
	public IDataset getDataset();
	
	/**
	 * Gets all nodes from the numerical meta attribute map of this node.
	 * 
	 * @return an unmodifiable set with all keys
	 * contained in this nodes numerical attribute map.
	 */
	public Set<String> getNumericalMetaAttributeKeys();
	
	/**
	 * Gets all nodes from the nominal meta attribute map of this node.
	 * 
	 * @return an unmodifiable set with all keys
	 * contained in this nodes nominal meta attribute map.
	 */
	public Set<String> getNominalMetaAttributeKeys();
	
	/**
	 * Gets all nodes from the rating attribute map of this node.
	 * 
	 * @return an unmodifiable set with all INodes (keys)
	 * contained in this ratings attribute map.
	 */
	public Set<INode> getRatingAttributeKeys();
			
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
	public boolean hasAttribute(Object attribute);
	
	/**
	 * Removes the mapping for {@code attribute }
	 * from all attribute maps of this node.
	 * 
	 * @param attribute The object for
	 * which the mapping is removed.
	 */
	public void removeAttribute(Object attribute);
			
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
	 * Gets a string representation of the nodes numerical attribute map.
	 * 
	 * @return string representation of the nodes numerical attribute map.
	 */
	public String getNumericalAttributesString();
	
	/**
	 * Gets a string representation of the nodes nominal attribute map.
	 * 
	 * @return string representation of the nodes nominal attribute map.
	 */
	public String getNominalAttributesString();
	
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
	public String getDatasetId();
	
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
	public List<String> getDataSetIds();
	
	/**
	 * Sets the id of the node. Used for evaluations.
	 * 
	 * @param id the node id
	 */
	public void setId(long id);
	
	/**
	 * Gets A HTML string representation of this nodes attributes.
	 * @return HTML string
	 */
	public String getAttributeHTMLLabelString();
	
	/**
	 * Gets the subtree of this node as JTree.
	 * @return a JTree of the subtree
	 */
	public JTree getJTreeOfSubtree();
		
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