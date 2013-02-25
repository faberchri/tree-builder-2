package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
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
	
	private static final double acuity = 1.0;
	
	private static final double maxThoereticalPossibleCategoryUtility = 1.0 / acuity;
	
	private static Logger log = TBLogger.getLogger(SharedMaxCategoryUtilitySearcher.class.getName());

	/**Calculates utility of merging nodes in possibleMerge based on Classit Category Utility formula
	 * Utility is calculated as follows:
	 * 1. For all attributes calculate 1/stdev
	 * 2. Divide the sum of this values by the number of attributes
	 * @param possibleMerge The nodes for which to calculate the utility
	 * @return the utility of merging the nodes in possibleMerge
	 **/
	public double calculateCategoryUtility(Collection<INode> possibleMerge) {
		
		Collection<INode> mergeNum = new HashSet<INode>();
		Collection<INode> mergeNom = new HashSet<INode>();
		
		// Create disjunct collections -> FIXME not very nice
		for(INode mergeCandidate : possibleMerge){
			
			Map<INode,IAttribute> numAtt = new HashMap<INode,IAttribute>();
			Map<INode,IAttribute> nomAtt = new HashMap<INode,IAttribute>();
			
			//Set<INode> originalKeys = mergeCandidate.getAttributeKeys();
			for(INode att : mergeCandidate.getAttributeKeys()){
				if(att.getNodeType() == ENodeType.Nominal){
					nomAtt.put(att,mergeCandidate.getAttributeValue(att));
				}
				else {
					numAtt.put(att,mergeCandidate.getAttributeValue(att));
				}
			}
			
			mergeCandidate.setAttributes(numAtt);
			mergeNum.add(mergeCandidate);
			
			mergeCandidate.setAttributes(nomAtt);
			mergeNom.add(mergeCandidate);			
		}
		
		// FIXME could have a nicer structure -> getInstance()
		ClassitMaxCategoryUtilitySearcher classit = new ClassitMaxCategoryUtilitySearcher();
		CobwebMaxCategoryUtilitySearcher cobweb = new CobwebMaxCategoryUtilitySearcher();
		
		// Define weighted utility 
		double percentageNumeric = 0.9;
		double percentageNominal = 0.1;
		
		double utility = 0.0;
		utility += classit.calculateCategoryUtility(mergeNum) * percentageNumeric;
 		utility += cobweb.calculateCategoryUtility(mergeNom) * percentageNominal;
		
		log.finest("Shared category utility is " + utility);
		
		return utility;
	}

	@Override
	protected double getMaxTheoreticalPossibleCategoryUtility() {
		return maxThoereticalPossibleCategoryUtility;
	}
}