package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;

import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.util.TBLogger;

public class ClassitTreeComponentFactory extends TreeComponentFactory implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	private static ClassitTreeComponentFactory factory = new ClassitTreeComponentFactory();

	/*
	 * Must not be instantiated with constructor.
	 */
	private ClassitTreeComponentFactory() {
		// singleton
	}

	public static  TreeComponentFactory getInstance() {
		return factory;
	}

	/**
	 * Used to create the (single) attribute object of leaf nodes
	 */
	@Override
	public IAttribute createNumericAttribute(double rating) {
		// the stddev would be equal 0 but we use the acuity to prevent division by 0.
		// avg = rating, stdev = acuity, support = 1, sum of ratings = rating,
		// sum of squared ratings  = ratings^2
		return new ClassitAttribute(1, rating, Math.pow(rating, 2.0));
	}

	@Override
	public IAttribute createNominalAttribute(int support, Object key,Object object) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Used to calculate new nodes in the merging process
	 */
	@Override
	public IAttribute createMergedNumericalAttribute(INode attributeKey, Collection<INode> nodesToMerge) {
		
		int support = ClassitMaxCategoryUtilitySearcher.calcSupportOfAttribute(attributeKey, nodesToMerge);
		if (support < 1) {
			TBLogger.getLogger(getClass().getName()).severe("Attempt to initialize attribute object with support smaller 1." );
			System.exit(-1);
		}
		double sumOfRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfRatingsOfAttribute(attributeKey, nodesToMerge);
//		double average = sumOfRatings / (double) support;
		double sumOfSquaredRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfSquaredRatingsOfAttribute(attributeKey, nodesToMerge);
//		double stdDev = ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(attributeKey, merge);

//		Map<String,String> meta = attributeKey.getMeta();

		return new ClassitAttribute(support, sumOfRatings, sumOfSquaredRatings);
	}

	@Override
	public IAttribute createMergedNominalAttribute(Object object,
			Collection<INode> nodesToMerge) {
		// TODO Auto-generated method stub
		return null;
	}

}