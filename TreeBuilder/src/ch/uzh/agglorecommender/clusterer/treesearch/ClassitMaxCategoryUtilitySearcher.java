package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
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
	
	private static final double acuity = 1.0;
	
	private static final double maxThoereticalPossibleCategoryUtility = 1.0 / acuity;
	
	private static Logger log = TBLogger.getLogger(ClassitMaxCategoryUtilitySearcher.class.getName());

	/**Calculates utility of merging nodes in possibleMerge based on Classit Category Utility formula
	 * Utility is calculated as follows:
	 * 1. For all attributes calculate 1/stdev
	 * 2. Divide the sum of this values by the number of attributes
	 * @param possibleMerge The nodes for which to calculate the utility
	 * @return the utility of merging the nodes in possibleMerge
	 **/
	protected double calculateCategoryUtility(List<INode> possibleMerge) {
		Set<INode> allAttributes = new HashSet<INode>();

		for (INode node : possibleMerge) {
			allAttributes.addAll(node.getAttributeKeys());
		}
		
		double utility = 0.0;
		
		for (INode node : allAttributes) {
			if (isAttributeKnownToAllMergeNodes(node, possibleMerge)) {
				utility += 1.0 / calcStdDevOfAttribute(node, possibleMerge);
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
	private boolean isAttributeKnownToAllMergeNodes(INode attribute, List<INode> possibleMerge) {
		for (INode iNode : possibleMerge) {
			if (iNode.getAttributeValue(attribute) == null) {
				return false;
			}
		}
		return true;
	}
		
	/**
	 * Calculates the support of attribute for the nodes in possibleMerge if these nodes were merged
	 * @param attribute The attribute for which to calculate the support
	 * @param possibleMerge The nodes for which to calculate the support if merged
	 * @return support of attribute
	 **/
	public static int calcSupportOfAttribute(INode attribute, List<INode> possibleMerge) {
		int res = 0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSupport();
			}
		}
		log.finest("support: "+res);
		return res;
	}
	
	/** 
	 * Calculates the sum of the ratings for attribute in the nodes in possibleMerge.
	 * This method is mainly used to calculate the new average of this attribute when two nodes are merged, 
	 * or to calculate the standard deviation.
	 * @param attribute The attribute whose sum needs to be calculated
	 * @param possibleMerge The nodes who's attribute ratings are calculated
	 * @return The sum of the ratings for attribute in the nodes
	 */
	public static double calcSumOfRatingsOfAttribute(INode attribute, List<INode> possibleMerge) {
		double res = 0.0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSumOfRatings();
			}
		}
		log.finest("sum of ratings: "+res);
		return res;
	}
	
	/**
	 * Calculates the sum of squared ratings of attribute in the nodes in possibleMerge. This method is mainly
	 * used to calculate the standard deviation or when merging nodes. 
	 * @param attribute The attribute whose squared ratings are calculated
	 * @param possibleMerge The nodes possibly containing the attribute for which the sum of the squared 
	 * ratings. 
	 * @return the sum of squared ratings of attribute
	 */
	public static double calcSumOfSquaredRatingsOfAttribute(INode attribute, List<INode> possibleMerge) {
		double res = 0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSumOfSquaredRatings();
			}
		}
		log.finest("sum of squared ratings: " + res);
		return res;
	}
	
	/**
	 * Calculates the standard deviation of attribute if nodes in possibleMerge were merged.
	 * @param attribute The attribute whose standard deviation is calculated
	 * @param possibleMerge The nodes possibly containing the attribute
	 * @return The standard deviation of attribute in the nodes. Returns acuity if no node in 
	 * possibleMerge contains the attribute.
	 */
	public static double calcStdDevOfAttribute(INode attribute, List<INode> possibleMerge) {
		int support = calcSupportOfAttribute(attribute, possibleMerge);
		double sumOfRatings = calcSumOfRatingsOfAttribute(attribute, possibleMerge);
		double sumOfSquaredRatings = calcSumOfSquaredRatingsOfAttribute(attribute, possibleMerge);
				 
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
	
	public static double getAcuity() {
		return acuity;
	}
	
	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return maxThoereticalPossibleCategoryUtility;
	}
}
