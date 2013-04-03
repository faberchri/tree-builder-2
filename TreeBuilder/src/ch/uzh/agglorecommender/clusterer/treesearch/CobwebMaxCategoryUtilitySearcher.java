package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class CobwebMaxCategoryUtilitySearcher extends BasicMaxCategoryUtilitySearcher implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**Calculates utility of merging nodes in possibleMerge based on Cobweb Category Utility formula
	 * Utility is calulated as follows:
	 * 1. For each attribute calculate the square probability of having certain value in the merged node
	 * P(A=V)^2
	 * This is calculated for each occurring value in the merged node
	 * 2. Divide the sum by the number of attributes in the merged node
	 * @param possibleMerge The nodes for which to calculate the utility
	 * @return the utility of merging the nodes in possibleMerge
	 **/
	public double calculateCategoryUtility(Collection<INode> possibleMerge) {
				
		Set<Object> allAttributes = new HashSet<Object>();
		int totalLeafCount = 0;
		for (INode node : possibleMerge) {
			Set<String> nomAttKeys = node.getNominalMetaAttributeKeys();
			if (nomAttKeys.isEmpty()) return 0.0;
			Collection<String> metAtts = node.getNominalMetaAttributeKeys();
			for (String att : metAtts) {
				if (node.useAttributeForClustering(att)) {
					allAttributes.add(att);
				}
			}
			totalLeafCount += node.getNumberOfLeafNodes();
		}
		
		List<Double> probabilities = new ArrayList<Double>();		
		for (Object att : allAttributes) {
			probabilities.addAll(calculateAttributeProbabilities(att, possibleMerge, totalLeafCount).values());
		}
		if (probabilities.size() == 0) {
			return -Double.MAX_VALUE;
		}
		
		double res = 0.0;
		for (Double prob : probabilities) {
			res += Math.pow(prob, 2.0);
		}
		
		// Normalize sum with total number of attributes
		return res / (double) allAttributes.size();
		
	}
	
	/**
	 * Calculates the probabilities in a merge set for the non-zero attribute values of a single attribute.
	 * Used in category utility calculation and in creating probability map of a new node.
	 *
	 * @param attribute the attribute for which the probability is calculated.
	 * @param possibleMerge the set of a possible merge.
	 * @param leafCount the number of all leaves of the resulting subtree of the merge.
	 * @return the map of all non-zero probabilities for the passed attribute (rating-value is key, probability is value).
	 */
	public static Map<Object,Double> calculateAttributeProbabilities(Object attribute, Collection<INode> possibleMerge, int leafCount) {
		Map<Object,Double> probabilities = new HashMap<Object, Double>();
		for (INode node : possibleMerge) {
			if (node.hasAttribute(attribute)) {
				int currLeafCount = node.getNumberOfLeafNodes();
				
				IAttribute aV = node.getNominalAttributeValue(attribute);
				if(aV != null){
					Iterator<Map.Entry<Object, Double>> it = aV.getProbabilities();
					while (it.hasNext()) {
						Map.Entry<Object, Double> entry = it.next();
						Double prevValueInMap = probabilities.get(entry.getKey());
						if (prevValueInMap != null) {
							probabilities.put(entry.getKey(), prevValueInMap + (entry.getValue() * ((double) currLeafCount / (double) leafCount)));						
						} else {
							probabilities.put(entry.getKey(), (entry.getValue() * ((double) currLeafCount / (double) leafCount)));
						}
					}
				}
			}
		}
		return probabilities;
	}
	
	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return 1.0;
	}
}
