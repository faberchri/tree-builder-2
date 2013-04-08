package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;

/**
 * Creates INodes that build the clustering hierarchy tree
 * and IAttributes, the attributes of the INode objects.
 */
public class TreeComponentFactory implements Serializable  {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static TreeComponentFactory factory = new TreeComponentFactory();	
	private TreeComponentFactory() {
		// singleton
	}

	/**
	 * Gets the TreeComponentFactory.
	 * @return the factory instance (singleton)
	 */
	public static TreeComponentFactory getInstance() {
		return factory;
	}
	
	/**
	 * Creates a leaf node of a clustering hierarchy tree
	 * with initialized attribute maps.
	 * 
	 * @param typeOfNewNode The node type of the new node (User or Content)
	 * @param dataSetId the id of the data set entity that is 
	 * represented with this cluster tree leaf node.
	 * @param nominalMeta the nominal meta attributes of the data set entity
	 * @param numericalMeta the numerical meta attributes of the data set entity
	 * @param dataset the data set that is processed
	 * @return a new INode instance
	 */
	public INode createLeafNode(
			ENodeType typeOfNewNode,
			String dataSetId,
			Multimap<String, Object> nominalMeta,
			Multimap<String, Double> numericalMeta,
			IDataset dataset) {
		
		INode newNode = new Node(typeOfNewNode, dataSetId,  dataset);
		if (nominalMeta != null) {
			for (Entry<String, Collection<Object>> entry : nominalMeta.asMap().entrySet()) {
				newNode.addNominalMetaAttribute(entry.getKey(), createNominalLeafAttribute(entry.getValue()));
			}	
		}
		if (numericalMeta != null) {
			for (Entry<String, Collection<Double>> entry : numericalMeta.asMap().entrySet()) {
				newNode.addNumericalMetaAttribute(entry.getKey(), createNumericalLeafAttribute(entry.getValue().iterator().next()));
			}	
		}
		return newNode;
	}
	
	/**
	 * Creates the IAttribute object of a nominal meta attribute of a leaf node.
	 * @param attValues the collection of values of the nominal attribute
	 * @return A CobwebAttribute object for the attribute
	 */
	private IAttribute createNominalLeafAttribute(Collection<Object> attValues) {
		double initialProb = 1.0 / (double)attValues.size();
		Map<Object, Double> r = new HashMap<>();
		for (Object aV : attValues) {
			r.put(aV, initialProb);
		}
		Map<Object, Double> attMap = ImmutableMap.copyOf(r);
		return new CobwebAttribute(attMap);
	}
	
	/**
	 * Creates the ClassitAttribute for a particular numerical attribute
	 * (i.e.: another INode object for the ratings attribute).
	 * 
	 * @param rating the attribute value, i.e.: 
	 * ratings for a content item (user node) or from a user (content item node)
	 * @return the ClassitAttribute object
	 */
	public IAttribute createNumericalLeafAttribute(double rating) {
		// the stddev would be equal 0 but we use the acuity to prevent division by 0.
		// avg = rating, stdev = acuity, support = 1, sum of ratings = rating,
		// sum of squared ratings  = ratings^2
		return new ClassitAttribute(1, rating, Math.pow(rating, 2.0));
	}
	
	/**
	 * Creates a INode that is an internal node in the clustering
	 * hierarchy tree with initialized attribute maps.
	 * @param nodesToMerge the clusters to merge to a new cluster
	 * and that become children of new cluster.
	 * @param categoryUtility the category utility of the current merge.
	 * @return a new INode instance with initialized attribute maps.
	 */
	public INode createInternalNode( Collection<INode> nodesToMerge, double categoryUtility) {

		if (nodesToMerge.size() < 2) {
			TBLogger.getLogger(getClass().getName())
				.severe("Merge attempt with number of nodes < 2, in: "+getClass().getSimpleName());
			System.exit(-1);
		}

		Map<INode, IAttribute> ratMap = createRatingInternalAttMap(nodesToMerge);
		Map<String, IAttribute> nomMetMap = createNominalMetaInternalAttMap(nodesToMerge);
		Map<String, IAttribute> numMetMap = createNumericalMetaInternalAttMap(nodesToMerge);
		
		ENodeType typeOfNewNode = nodesToMerge.iterator().next().getNodeType();
		for (INode n : nodesToMerge) {
			if (! n.getNodeType().equals(typeOfNewNode)) {
				TBLogger.getLogger(getClass().getName())
				.severe("Merge attempt of nodes of different types, in: "+getClass().getSimpleName());
			System.exit(-1);
			}
		}	
		INode newN = new Node(typeOfNewNode, nodesToMerge, ratMap, numMetMap, nomMetMap, categoryUtility);
		return newN;
	}
	
	/**
	 * Creates the attribute map of the numerical meta attributes for an internal node.
	 * @param nodesToMerge the nodes of the current merge
	 * @return the numerical meta attribute map
	 */
	private Map<String, IAttribute> createNumericalMetaInternalAttMap(Collection<INode> nodesToMerge) {
		Map<String, IAttribute> map = new HashMap<String, IAttribute>();
		for (INode node : nodesToMerge) {
			for (String attNodes : node.getNumericalMetaAttributeKeys()) {
				map.put(attNodes, null);
			}			
		}
		for (Map.Entry<String, IAttribute> entry : map.entrySet()) {
			IAttribute newAtt = createNumericalInternalAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (map.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("Numerical attribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return map;
	}
	
	/**
	 * Creates the attribute map of the numerical rating attributes for an internal node.
	 * @param nodesToMerge the nodes of the current merge
	 * @return the rating attributes map
	 */
	private  Map<INode,IAttribute> createRatingInternalAttMap(Collection<INode> nodesToMerge) {
		Map<INode, IAttribute> map = new HashMap<INode, IAttribute>();
		for (INode node : nodesToMerge) {
			for (INode attNodes : node.getRatingAttributeKeys()) {
				map.put(attNodes, null);
			}			
		}
		for (Map.Entry<INode, IAttribute> entry : map.entrySet()) {
			IAttribute newAtt = createNumericalInternalAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (map.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("Numerical attribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return map;		
	}
	
	/**
	 * Creates a ClassitAttribute object based on the attributes
	 * that are mapped in the nodesToMerge to the passed attributeKey.
	 * @param attributeKey the key for the attribute
	 * @param nodesToMerge the nodes to merge
	 * @return a new ClassitAttribute for the attributeKey
	 */
	private IAttribute createNumericalInternalAttribute(Object attributeKey, Collection<INode> nodesToMerge) {
		List<IAttribute> atts = new ArrayList<>();
		for (INode n : nodesToMerge) {
			IAttribute tmp = n.getNumericalAttributeValue(attributeKey);
			if (tmp != null) {
				atts.add(tmp);
			}
		}
		return createNumericalInternalAttribute(atts);
	}
	
	/**
	 * Calculates the values of a new ClassitAttribute based on the passed ClassitAttributes.
	 * @param atts the calculation base for a new ClassitAttribute
	 * @return a new ClassitAttribute
	 */
	private IAttribute createNumericalInternalAttribute(Collection<IAttribute> atts) {
		
		int support = ClassitMaxCategoryUtilitySearcher.calcSupportOfAttribute(atts);
		if (support < 1) {
			TBLogger.getLogger(getClass().getName()).severe("Attempt to initialize attribute object with support smaller 1." );
			System.exit(-1);
		}
		double sumOfRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfRatingsOfAttribute(atts);
//		double average = sumOfRatings / (double) support;
		double sumOfSquaredRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfSquaredRatingsOfAttribute(atts);
//		double stdDev = ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(attributeKey, merge);
		
		return new ClassitAttribute(support, sumOfRatings, sumOfSquaredRatings);
	}
	
	/**
	 * Creates the attribute map of the nominal meta attributes for an internal node.
	 * @param nodesToMerge the nodes of the current merge
	 * @return the nominal meta attribute map
	 */
	private Map<String, IAttribute> createNominalMetaInternalAttMap(Collection<INode> nodesToMerge) {
		Map<String, IAttribute> map = new HashMap<String, IAttribute>();
		for (INode node : nodesToMerge) {
			for (String atts : node.getNominalMetaAttributeKeys()) {
				map.put(atts, null);
			}			
		}
		for (Map.Entry<String, IAttribute> entry : map.entrySet()) {
			IAttribute newAtt = createNominalInternalAttribute(entry.getKey(), nodesToMerge);
			entry.setValue(newAtt);
		}
		if (map.containsValue(null)) {
			TBLogger.getLogger(getClass().getName()).severe("Nominal attribute map of node resulting of merge contains null" +
					" as value; in : "+getClass().getSimpleName());
			System.exit(-1);
		}
		return map;	
	}
	
	/**
	 * Creates a CobwebAttribute object based on the attributes
	 * that are mapped in the nodesToMerge to the passed attributeKey.
	 * @param attributeKey the key for the attribute
	 * @param nodesToMerge the nodes to merge
	 * @return a new CobwebAttribute for the attributeKey
	 */
	private IAttribute createNominalInternalAttribute(Object attributeKey, Collection<INode> nodesToMerge) {
		int totalLeafCount = 0;
		for (INode node : nodesToMerge) {
			totalLeafCount += node.getNumberOfLeafNodes();
		}
		Map<Object, Double> attMap = ImmutableMap.copyOf(
				CobwebMaxCategoryUtilitySearcher
					.calculateAttributeProbabilities(
							attributeKey, nodesToMerge, totalLeafCount
					)
				);
		return new CobwebAttribute(attMap);
	}
	
}
