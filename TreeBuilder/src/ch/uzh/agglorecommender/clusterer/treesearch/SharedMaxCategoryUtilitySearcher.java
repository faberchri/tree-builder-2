package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

public class SharedMaxCategoryUtilitySearcher extends BasicMaxCategoryUtilitySearcher implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
		
	private ClassitMaxCategoryUtilitySearcher classit = new ClassitMaxCategoryUtilitySearcher();
	private CobwebMaxCategoryUtilitySearcher cobweb = new CobwebMaxCategoryUtilitySearcher();

	/**Calculates utility of merging nodes in possibleMerge based on Classit Category Utility formula
	 * Utility is calculated as follows:
	 * 1. For all attributes calculate 1/stdev
	 * 2. Divide the sum of this values by the number of attributes
	 * @param possibleMerge The nodes for which to calculate the utility
	 * @return the utility of merging the nodes in possibleMerge
	 **/
	@Override
	public double calculateCategoryUtility(Collection<INode> possibleMerge) {
				
		Set<Object> numAtts = new HashSet<Object>();
		Set<Object> nomAtts = new HashSet<Object>();
		for (INode n : possibleMerge) {
			numAtts.addAll(n.getRatingAttributeKeys());
			numAtts.addAll(n.getNumericalMetaAttributeKeys());
			nomAtts.addAll(n.getNominalMetaAttributeKeys());
		}
		
		double numOfNomAtts = nomAtts.size();
		double numOfNumAtts = numAtts.size();

		double sumOfAtts = numOfNomAtts + numOfNumAtts;
		
		if (sumOfAtts == 0) {
			TBLogger.getLogger(this.getClass().getName()).severe("Err.: Category utility calculation with zero attributes.");
			System.exit(-1);
		}
		
		double utility = 0.0;
		utility += classit.calculateCategoryUtility(possibleMerge) * (numOfNumAtts / sumOfAtts);
 		utility += cobweb.calculateCategoryUtility(possibleMerge) * (numOfNomAtts / sumOfAtts);
				
		return utility;
	}

	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return (cobweb.getMaxTheoreticalPossibleCategoryUtility()
				+ (classit.getMaxTheoreticalPossibleCategoryUtility() * ClassitMaxCategoryUtilitySearcher.acuity)) / 2.0;
	}
}