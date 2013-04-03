package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.Collection;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 * Allows for category utility calculation considering both numerical and nominal attributes.
 */
public class SharedMaxCategoryUtilitySearcher extends BasicMaxCategoryUtilitySearcher {

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
	
	/**
	 * Calculates the category utility for the numerical attributes and the nominal attributes separately
	 * and combines the two category utility weighted by the number of attributes.
	 */
	@Override
	public double calculateCategoryUtility(Collection<INode> possibleMerge) {
				
		int numOfNomAtts = 0;
		int numOfNumAtts = 0;
		
		for (INode n : possibleMerge) {

			numOfNumAtts += n.getRatingAttributeKeys().size();
			
			Set<String> set = n.getNumericalMetaAttributeKeys();
			for (String s : set) {
				if (n.useAttributeForClustering(s)) {
					numOfNumAtts++;
				}
			}
			
			set = n.getNominalMetaAttributeKeys();
			for (String s : set) {
				if (n.useAttributeForClustering(s)) {
					numOfNomAtts++;
				}
			}
		}
		
		double sumOfAtts = numOfNomAtts + numOfNumAtts;
		
		if (sumOfAtts == 0) {
			TBLogger.getLogger(this.getClass().getName()).severe("Err.: Category utility calculation with zero attributes.");
			System.exit(-1);
		}
		
		double utility = 0.0;
		utility += classit.calculateCategoryUtility(possibleMerge) * ((double)numOfNumAtts / sumOfAtts);
 		utility += cobweb.calculateCategoryUtility(possibleMerge) * ((double)numOfNomAtts / sumOfAtts);
				
		return utility;
	}

	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return (cobweb.getMaxTheoreticalPossibleCategoryUtility()
				+ (classit.getMaxTheoreticalPossibleCategoryUtility() * ClassitMaxCategoryUtilitySearcher.getAcuity())) / 2.0;
	}
}