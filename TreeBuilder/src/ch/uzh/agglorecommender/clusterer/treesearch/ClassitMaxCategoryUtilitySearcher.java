package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

public class ClassitMaxCategoryUtilitySearcher extends BasicMaxCategoryUtilitySearcher implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This [CLASSIT] evaluation function is equivalent to the function used by COBWEB;
	 * it is a transformation of category utility. Unfortunately, this transformation
	 * introduces a problem when the standard deviation is zero for a concept.
	 * For any concept based on a single instance, the value of 1/(standard deviation)
	 * is therefore infinite. In order to resolve this problem, we have introduced
	 * the notion of acuity, a system parameter that specifies the minimum value for
	 * the standard deviation. This limit corresponds to the notion of a
	 * "just noticeable difference" in psychophysics - the lower limit on our
	 * perception ability. Because acuity strongly affects the score of new disjuncts,
	 * it indirectly controls the breadth, or branching factor of the concept hierarchy
	 * produced, just as the cutoff parameter controls the depth of the hierarchy.
	 * 
	 * @see John H. Gennari, Pat Langley and Doug Fisher: Models of Incremental Concept Formation.
	 * Artificial Intelligence 411 (1989) 40. 
	 */
	private static final double acuity = 1.0;

	/**
	 * The maximal theoretical possible category utility of any merge.
	 */
	private static final double maxThoereticalPossibleCategoryUtility = 1.0 / acuity;

	/**
	 * The logger of the class.
	 */
	private static Logger log = TBLogger.getLogger(ClassitMaxCategoryUtilitySearcher.class.getName());


	/**Calculates utility of merging nodes in possibleMerge based on Classit Category Utility formula
	 * Utility is calculated as follows:
	 * 1. For all attributes calculate 1/stdev
	 * 2. Divide the sum of this values by the number of attributes
	 * @param possibleMerge The nodes for which to calculate the utility
	 * @return the utility of merging the nodes in possibleMerge
	 * @throws IllegalArgumentException if possibleMerge has length != 2
	 **/
	public double calculateCategoryUtility(Collection<INode> possibleMerge) {
		
		if (possibleMerge == null || possibleMerge.size() != 2) {
			throw new IllegalArgumentException("Merge candidate is null or does contain " +
					"a numer of INode object(s) unequal 2.");			
		}

		Set<Object> allAttributes = new HashSet<>();

		for (INode node : possibleMerge) {
			allAttributes.addAll(node.getRatingAttributeKeys());
			Collection<String> metAtts = node.getNumericalMetaAttributeKeys();
			for (String att : metAtts) {
				if (node.useAttributeForClustering(att)) {
					allAttributes.add(att);
				}
			}
		}

		double utility = 0.0;

		for (Object att : allAttributes) {
			if (isAttributeKnownToAllMergeNodes(att, possibleMerge)) {
				
//				System.out.println("Attribute is known!!!");
				
				List<IAttribute> iAtts = new ArrayList<IAttribute>();
				for (INode mergeNode : possibleMerge) {
					IAttribute v = mergeNode.getNumericalAttributeValue(att);
					if (v != null) {
						iAtts.add(v);
					}				
				}
				utility += 1.0 / calcStdDevOfAttribute(iAtts);
			} else {
				// TODO If we want to support merges candidates with length > 2
				// we need to handle the case of an attribute that appears not
				// in all nodes of the merge candidate as an attribute but
				// only in some. Currently merge candidates have always length == 2.
			}

		}

		// Normalize sum with the number of attributes
		utility /= (double) allAttributes.size();

		log.finest("Classit category utility is " + utility);
		return utility;
	}

	/**
	 * Checks if the passed attribute is an attribute of all nodes in the passed merge candidate.
	 * 
	 * @param attribute the attribute to test
	 * @param possibleMerge the merge candidate
	 * @return true if {@code attribute} appears in all nodes of {@code possibleMerge}, else false.
	 */
	private boolean isAttributeKnownToAllMergeNodes(Object attribute, Collection<INode> possibleMerge) {
		for (INode iNode : possibleMerge) {
			if (iNode.getNumericalAttributeValue(attribute) == null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculates the support of attribute for the nodes in possibleMerge if these nodes were merged.
	 * This method is used as helper to calculate the new average of this attribute when two nodes are merged, 
	 * or to calculate the standard deviation.
	 * @param attributes The attributes for which to calculate the support
	 * @return support of attribute
	 **/
	public static int calcSupportOfAttribute(Collection<IAttribute> attributes) {
		int res = 0;
		for (IAttribute att : attributes) {
			res += att.getSupport();
		}
		log.finest("support: "+res);
		return res;
	}

	/** 
	 * Calculates the sum of the ratings for attribute in the nodes in possibleMerge.
	 * This method is used as helper to calculate the new average of this attribute when two nodes are merged, 
	 * or to calculate the standard deviation.
	 * @param attributes The attributes whose sum needs to be calculated
	 * @return The sum of the ratings for attribute in the nodes
	 */
	public static double calcSumOfRatingsOfAttribute(Collection<IAttribute> attributes) {
		double res = 0.0;
		for (IAttribute att : attributes) {
			res += att.getSumOfRatings();
		}
		log.finest("sum of ratings: "+res);
		return res;
	}


	/**
	 * Calculates the sum of squared ratings of attribute in the nodes in possibleMerge. This method is
	 * used as helper to calculate the standard deviation or when merging nodes. 
	 * @param attributes The attributes whose squared ratings are calculated
	 * @return the sum of squared ratings of attributes
	 */
	public static double calcSumOfSquaredRatingsOfAttribute(Collection<IAttribute> attributes) {
		double res = 0.0;
		for (IAttribute att : attributes) {
			res += att.getSumOfSquaredRatings();
		}
		log.finest("sum of squared ratings: " + res);
		return res;
	}

	/**
	 * Calculates the standard deviation of attribute if nodes in possibleMerge were merged.
	 * @param attributes The attributes whose standard deviation is calculated
	 * @return The standard deviation of attribute in the nodes. Returns acuity if no node in 
	 * possibleMerge contains the attribute.
	 */
	public static double calcStdDevOfAttribute(Collection<IAttribute> attributes) {
		int support = calcSupportOfAttribute(attributes);
		double sumOfRatings = calcSumOfRatingsOfAttribute(attributes);
		double sumOfSquaredRatings = calcSumOfSquaredRatingsOfAttribute(attributes);

		if (support < 1) {
			log.severe("Attempt to calculate standard deviation with support smaller 1." );
			System.exit(-1);
		} 

		if ( support == 1 ){
			// the stddev would be equal 0 but we return the acuity to prevent division by 0
			return acuity;
		}

		// calculate the standard deviation incrementally, according to
		/// http://de.wikipedia.org/wiki/Standardabweichung#Berechnung_f.C3.BCr_auflaufende_Messwerte 
		double stdDev = Math.sqrt( (1.0/((double) (support - 1))) * (sumOfSquaredRatings - (1.0 / (double) support) * Math.pow(sumOfRatings, 2.0)) );
		log.finest("standard deviation: "+stdDev);

		// check if the calculated standard dev is smaller than the specified acuity
		// if true use assign acuity to stddev.
		if (stdDev < acuity) {
			stdDev = acuity;
			log.finest("standard deviation is smaller than acuity. Acuity ("+acuity+") is returned as standard deviation.");
		}

		return stdDev;
	}

	/**
	 * Gets the hard-coded acuity value.
	 * @return the {@code acuity}
	 */
	static double getAcuity() {
		return acuity;
	}

	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return maxThoereticalPossibleCategoryUtility;
	}
}
