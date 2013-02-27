package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableMap;

public class SharedTreeComponentFactory extends TreeComponentFactory implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static SharedTreeComponentFactory factory = new SharedTreeComponentFactory();
	
	/*
	 * Must not be instantiated with constructor.
	 */
	private SharedTreeComponentFactory() {
		// singleton
	}
	
	public static  TreeComponentFactory getInstance() {
		return factory;
	}
	@Override
	public INode createInternalNode(ENodeType typeOfNewNode,
			Collection<INode> nodesToMerge, double categoryUtility) {

		Map<INode,IAttribute> numericMap = createNumericalAttMap(nodesToMerge);
		Map<Object,IAttribute> nominalMap = createNominalAttMap(nodesToMerge);
		
		return new Node(typeOfNewNode, nodesToMerge, numericMap, nominalMap, categoryUtility);
	}
	
	/**
	 * Used to create the (single) numeric attribute object of leaf nodes
	 * 
	 *@param rating
	 *@param meta meta info about the attribute
	 * 
	 *@return IAttribute numeric attribute object
	 */
	@Override
	public IAttribute createNumericAttribute(double rating) {
		// the stddev would be equal 0 but we use the acuity to prevent division by 0.
		// avg = rating, stdev = acuity, support = 1, sum of ratings = rating,
		// sum of squared ratings  = ratings^2
		return new SharedAttribute(1, rating, Math.pow(rating, 2.0), null);
	}
	
	/**
	 * Used to create the nominal attributes object of leaf nodes
	 * 
	 * @param support 
	 * @param valueMap map of all values and their support for attribute
	 * @param meta meta info about the attribute
	 * 
	 * @return IAttribute symbolic attribute object
	 */
	@Override
	public IAttribute createNominalAttribute(int support, Object key, Object object) {
		
		Map<String,Double> valueMap = new HashMap<String,Double>();
		valueMap.put((String) key,1.0);
		
		return new SharedAttribute(support, 0,0, valueMap);
	}

	@Override
	public IAttribute createMergedAttribute(Object object,
			Collection<INode> nodesToMerge) {
		
		INode attributeKey = (INode) object;
		
		// Merge Numeric
		int support = ClassitMaxCategoryUtilitySearcher.calcSupportOfAttribute(attributeKey, nodesToMerge);
		if (support < 1) {
			TBLogger.getLogger(getClass().getName()).severe("Attempt to initialize attribute object with support smaller 1." );
			System.exit(-1);
		}
		double sumOfRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfRatingsOfAttribute(attributeKey, nodesToMerge);
//		double average = sumOfRatings / (double) support;
		double sumOfSquaredRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfSquaredRatingsOfAttribute(attributeKey, nodesToMerge);
//		double stdDev = ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(attributeKey, merge);
		
		// Merge Nominal
		int totalLeafCount = 0;
		for (INode node : nodesToMerge) {
			totalLeafCount += node.getNumberOfLeafNodes();
		}
		Map<Object, Double> attMap = ImmutableMap.copyOf(
				CobwebMaxCategoryUtilitySearcher
					.calculateAttributeProbabilities(
							(INode)object, nodesToMerge, totalLeafCount
					)
				);
		
		return new SharedAttribute(support, sumOfRatings, sumOfSquaredRatings, attMap);
	}		
}
