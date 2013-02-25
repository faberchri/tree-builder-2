package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

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
	public IAttribute createNumericAttribute(double rating, Map<String,String> meta) {
		// the stddev would be equal 0 but we use the acuity to prevent division by 0.
		// avg = rating, stdev = acuity, support = 1, sum of ratings = rating,
		// sum of squared ratings  = ratings^2
		return new ClassitAttribute(1, rating, Math.pow(rating, 2.0), meta);
	}

	/**
	 * Used to calculate new nodes in the merging process
	 */
	@Override
	public IAttribute createMergedAttribute(INode attributeKey, Collection<INode> nodesToMerge) {
		
		int support = ClassitMaxCategoryUtilitySearcher.calcSupportOfAttribute(attributeKey, nodesToMerge);
		if (support < 1) {
			TBLogger.getLogger(getClass().getName()).severe("Attempt to initialize attribute object with support smaller 1." );
			System.exit(-1);
		}
		double sumOfRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfRatingsOfAttribute(attributeKey, nodesToMerge);
//		double average = sumOfRatings / (double) support;
		double sumOfSquaredRatings = ClassitMaxCategoryUtilitySearcher.calcSumOfSquaredRatingsOfAttribute(attributeKey, nodesToMerge);
//		double stdDev = ClassitMaxCategoryUtilitySearcher.calcStdDevOfAttribute(attributeKey, merge);

		Map<String,String> meta = attributeKey.getMeta();

		return new ClassitAttribute(support, sumOfRatings, sumOfSquaredRatings, meta);
	}

	@Override
	public IAttribute createSymbolicAttribute(int support, String key,
			String value) {
		return  new ClassitAttribute(support, 0,0, null);
	}


// ----------------------------------  For deletion ---------------------------------------------------

//	/**
//	 * Here attributes are finally combined, calculation of average, stddev, support, .. is done here
//	 */
//	private IAttribute calcAttributeValues(List<IAttribute> attributesToCombine) {
//		
//		// No Attributes
//		if (attributesToCombine.size() == 0) {
//			System.err.println("attempt to combine 0 attributes, "+getClass().getSimpleName());
//			System.exit(-1);
//		}
//		
//		// Only one occurrence of ClassitAttribute
//		if (attributesToCombine.size() == 1) {
//			IAttribute a = attributesToCombine.get(0);
//			return new ClassitAttribute(a.getAverage(), a.getStdDev(), a.getSupport(), a.getConsideredRatings());
//		}
//		
//		// ??? #####################################################
////		int sizeOfNewLeafList = 0;
////		for (IAttribute attribute : attributesToCombine) {
////			sizeOfNewLeafList += attribute.getConsideredRatings().size();
////		}		
////		Double[] tmpAr = new Double[sizeOfNewLeafList];
////		int prevAttLength = 0;
////		for (IAttribute attribute : attributesToCombine) {
////			System.arraycopy(attribute.getConsideredRatings(), 0, tmpAr, prevAttLength, attribute.getConsideredRatings().size());
////		}
//		// ??? #####################################################
//		
//		// Determine Considered Ratings
//		ArrayList<Double> tmpAr = new ArrayList<Double>();
//		for (IAttribute attribute : attributesToCombine) {
//			tmpAr.addAll(attribute.getConsideredRatings());
//		}
//		
//		// Average
//		double tmpAvg = 0.0;
//		for (Double avgLi : tmpAr) {
//			tmpAvg += avgLi;
//		}
//		tmpAvg = tmpAvg / tmpAr.size();
//		
//		// Support
//		int tmpSup = 0;
//		for (IAttribute attribute : attributesToCombine) {
//			 tmpSup += attribute.getSupport();
//		}
//		
//		// Standard Deviation
//		double tmpStD = 0.0;
//		for (Double avgLi : tmpAr) {
//			tmpStD += Math.pow((avgLi - tmpAvg),2.0);
//		}
//		tmpStD = Math.sqrt(tmpStD/(tmpAr.size() - 1.0));
////		
//		//Double[] doubleArray = ArrayUtils.toObject(tmpAr);
//		
//		return new ClassitAttribute(tmpAvg, tmpStD, tmpSup, tmpAr);
//	}

}