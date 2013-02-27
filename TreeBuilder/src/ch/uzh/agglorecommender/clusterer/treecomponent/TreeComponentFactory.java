package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.agglorecommender.client.IDataset;
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
	public final INode createLeafNode(ENodeType typeOfNewNode, int dataSetId, IDataset<?> metaset) {
		return new Node(typeOfNewNode, dataSetId, metaset);
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
	public INode createInternalNode(
			ENodeType typeOfNewNode,
			Collection<INode> nodesToMerge,
			double categoryUtility) {

		if (nodesToMerge.size() < 2) {
			TBLogger.getLogger(getClass().getName()).severe("Merge attempt with number of nodes < 2, in: "+getClass().getSimpleName());
			System.exit(-1);
		}

		// Create Attribute Map
		
		// Versuch der Verallgemeinerung von createAttMap mit Reflection -> bin nicht weitergekommen
//		Class  nodeClass = INode.class;
//		Method numericKeyMethod = nodeClass.getMethod("getNumericalAttributeKeys", null);
//		Method nominalKeyMethod = nodeClass.getMethod("getNominalAttributeKeys", null);
//		Class  treeComponentClass = TreeComponentFactory.class;
//		Method numericMergeMethod = nodeClass.getMethod("createMergedNumericAttribute", new Class[]{Entry.class}, new Class[]{Collection.class});
//		Method nominalMergeMethod = nodeClass.getMethod("createMergedNominalAttribute", new Class[]{Entry.class}, new Class[]{Collection.class});
//		Object = ?
		
		Map<INode, IAttribute> numericalMap = createNumericalAttMap(nodesToMerge);
		Map<Object, IAttribute> nominalMap = createNominalAttMap(nodesToMerge);
		
		INode newNode = new Node(typeOfNewNode, nodesToMerge, numericalMap, nominalMap, categoryUtility);

		return newNode;
	}
	
	protected Map<Object, IAttribute> createNominalAttMap(Collection<INode> nodesToMerge) {
		Map<Object, IAttribute> allAttributes = new HashMap<Object, IAttribute>();
		for (INode node : nodesToMerge) {
			for (Object attNodes : node.getNominalAttributeKeys()) {
				allAttributes.put(attNodes, null);
			}			
		}
		
		// Create merged attributes of all attributes with multiple instances
		for (Map.Entry<Object, IAttribute> entry : allAttributes .entrySet()) {
			IAttribute newAtt = createMergedNominalAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (allAttributes.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("ClassitAttribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return allAttributes;	
	}

	protected Map<INode, IAttribute> createNumericalAttMap(Collection<INode> nodesToMerge) {
		Map<INode, IAttribute> allAttributes = new HashMap<INode, IAttribute>();
		for (INode node : nodesToMerge) {
			for (INode attNodes : node.getNumericalAttributeKeys()) {
				allAttributes.put(attNodes, null);
			}			
		}
		
		// Create merged attributes of all attributes with multiple instances
		for (Map.Entry<INode, IAttribute> entry : allAttributes .entrySet()) {
			IAttribute newAtt = createMergedNominalAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (allAttributes.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("ClassitAttribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return allAttributes;	
	}

	/**
	 * Creates a new {@code IAttribute} object based on a single rating.
	 * 
	 * @param rating the rating for the new {@code IAttribute} object.
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createNumericAttribute(double rating); // single node
	
	/**
	 * Creates a new {@code IAttribute} object based on meta data.
	 * 
	 * @param support support of the attribute
	 * @param valueMap of the different values
	 * @param meta meta information
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createNominalAttribute(int support, Object key, Object object); // single node
	
	/**
	 * Creates a new {@code IAttribute} object for the specified attribute
	 * (e.g. User_A) based on the list of nodes to merge
	 * (e.g. Movie_1, Movie_2, Movie_3).
	 *
	 * 
	 * @param nodesToMerge list of {@code INode} objects that are merged.
	 * @param object the attribute key of the new attribute object.
	 * 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createMergedNumericalAttribute(INode node, Collection<INode> nodesToMerge);
	
	/**
	 * Creates a new {@code IAttribute} object for the specified attribute
	 * (e.g. User_A) based on the list of nodes to merge
	 * (e.g. Movie_1, Movie_2, Movie_3).
	 *
	 * 
	 * @param nodesToMerge list of {@code INode} objects that are merged.
	 * @param object the attribute key of the new attribute object.
	 * 
	 * @return a new instance of an {@code IAttribute} object.
	 */
	public abstract IAttribute createMergedNominalAttribute(Object object, Collection<INode> nodesToMerge);

	
//	private  Map<Object,IAttribute> createAttMap(Collection<INode> nodesToMerge, Method method, Method numericMergeMethod) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//		
//		// Collect the combined attributes of all nodes that should be merged
//		Map<Object, IAttribute> allAttributes = new HashMap<Object, IAttribute>();
//		for (INode node : nodesToMerge) {
//			Set<INode> attributeKeys = (Set<INode>) method.invoke(null, null);
//			for (INode attNodes : attributeKeys) {
//				allAttributes.put(attNodes, null);
//			}			
//		}
//		
//		// Create merged attributes of all attributes with multiple instances
//		for (Map.Entry<Object, IAttribute> entry : allAttributes .entrySet()) {
//			IAttribute newAtt = createMergedNumericAttribute(entry.getKey(), nodesToMerge);
//			entry.setValue(newAtt);
//		}
//		if (allAttributes.containsValue(null)) {
//			TBLogger.getLogger(getClass().getName()).severe("ClassitAttribute map of node resulting of merge contains null" +
//					" as value; in : "+getClass().getSimpleName());
//			System.exit(-1);
//		}
//		return allAttributes;		
//	}

}
