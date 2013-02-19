package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.agglorecommender.util.TBLogger;


/**
 * Creates instances of type {@code INode}s.
 */
public abstract class TreeComponentFactory implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates the leaf nodes of the cluster tree.
	 * 
	 * @param typeOfNewNode the type of new node.
	 * @param dataSetId the id of the corresponding
	 * instance in the data set.
	 * @return a new node instance.
	 */
	public final INode createLeafNode(ENodeType typeOfNewNode, int dataSetId, List<String> meta) {
		return new Node(typeOfNewNode, dataSetId, meta);
	}

	/**
	 * 
	 * Creates a new node and initializes its attribute map.
	 * The new node is added to the cluster tree as new
	 * root with all nodes in {@code nodesToMerge} as children.
	 * 
	 * @param typeOfNewNode the type of the node to create.
	 * @param nodesToMerge the nodes to combine for the new node.
	 * @return a new node instance.
	 */
	public final INode createInternalNode(
			ENodeType typeOfNewNode,
<<<<<<< HEAD
			List<INode> nodesToMerge,
			double categoryUtility) {
=======
			Collection<INode> nodesToMerge, double categoryUtility) {
>>>>>>> strategies for memory consumption reduction


		if (nodesToMerge.size() < 2) {
			TBLogger.getLogger(getClass().getName()).severe("Merge attempt with number of nodes < 2, in: "+getClass().getSimpleName());
			System.exit(-1);
		}

		Map<INode, IAttribute> attMap = createAttMap(nodesToMerge);
		INode newN = new Node(typeOfNewNode, nodesToMerge, attMap, categoryUtility);

		return newN;
	}
	
	/**
	 * Creates a new {@code IAttribute} object based on a single rating.
	 * 
	 * @param rating the rating for the new {@code IAttribute} object.
	 * @param metaData 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createAttribute(double rating, List<String> metaData); // single node
	
	/**
	 * Creates a new {@code IAttribute} object for the specified attribute
	 * (e.g. User_A) based on the list of nodes to merge
	 * (e.g. Movie_1, Movie_2, Movie_3).
	 *
	 * 
	 * @param nodesToMerge list of {@code INode} objects that are merged.
	 * @param attributeKey the attribute key of the new attribute object.
	 * 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createAttribute(INode attributeKey, Collection<INode> nodesToMerge); // group node

	
	private  Map<INode,IAttribute> createAttMap(Collection<INode> nodesToMerge) {
		Map<INode, IAttribute> map = new HashMap<INode, IAttribute>();
		for (INode node : nodesToMerge) {
			for (INode attNodes : node.getAttributeKeys()) {
				map.put(attNodes, null);
			}			
		}
		for (Map.Entry<INode, IAttribute> entry : map.entrySet()) {
			IAttribute newAtt = createAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (map.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("ClassitAttribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return map;		
	}

}
