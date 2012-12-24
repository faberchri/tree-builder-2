package modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import scala.collection.IndexedSeq;
import utils.TBLogger;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.IMergeResult;
import clusterer.INode;

public abstract class MaxCategoryUtilitySearcher implements IMaxCategoryUtilitySearcher {

	/**
	 * All generated subsets have size <= MAX_SUBSET_SIZE.
	 * This limit reduces the number of generated sets from
	 * <br>
	 * <pre>
	 * C(set_size, 2) +  C(set_size, 3) + ... + C(set_size,set_size)
	 * </pre>
	 * <br>
	 * to
	 * <br>
	 * <pre>
	 * C(set_size, 2) +  C(set_size, 3) + ... + C(set_size, MAX_SUBSET_SIZE)
	 * </pre>
	 * <br>
	 * with n = set_size, m = length_of_sublist, and
	 * <br>
	 * <pre>
	 *                  n!
	 *  C(n,m) = ----------------
	 *              m! (n - m)!
	 * </pre>
	 * <br>
	 * Which is the binomial coefficient for nonnegative n and m:
	 * the number of ways of picking unordered outcomes from  possibilities,
	 * also known as a combination or combinatorial number. 
	 * <br>
	 * (Calculating the complete power set would result in generating 2^n subsets.)
	 */
	private static final int MAX_SUBSET_SIZE = 2;

	
	@Override
	public IMergeResult getMaxCategoryUtilityMerge(Set<INode> openNodes) {
		Logger log = TBLogger.getLogger(getClass().getName());
		
		// first we need a set of all possible subsets of open nodes.
		// This set is called a power set.
		// however we want to exclude the following sets from the power set:
		// - the empty set
		// - sets with size == 1
		// Furthermore, generating all possible subsets results in 2^n
		// new sets. Keeping all this objects in memory is clearly not possible.
		// Therefore we only generate "small" subsets, i.e. sets with size > 1 
		// and size <= MAX_SUBSET_SIZE.
		List<INode> openNodesList = new ArrayList<INode>(openNodes);
		List<IndexedSeq<Object>> permutationIndicesList = new ArrayList<IndexedSeq<Object>>();
		long count = 0;
		for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
			if (openNodesList.size() < sublistLength) continue;
			scala.collection.Iterator<scala.collection.immutable.IndexedSeq<Object>> it
					= SubsetsGenerator.subsets(openNodesList.size(), sublistLength);
			while (it.hasNext()) {
				IndexedSeq<java.lang.Object> sublist = (IndexedSeq<java.lang.Object>) it.next();
				log.finer("sublist "+ count+": "+ sublist);
				count++;
				permutationIndicesList.add(sublist);
			}
		}
		
				
		
		// Calculate for all indices combinations in 
		// permutationIndicesList the category
		// utility and store it in a IMergeResult object.
		// Add the IMergeResults to a list.
		List<IMergeResult> mergeResults = new ArrayList<IMergeResult>();
		for (IndexedSeq<Object> sublist : permutationIndicesList) {
			INode[] possibleMerge = new INode[sublist.length()];
			int i = 0;
			scala.collection.Iterator<Object> it = sublist.iterator();
			while (it.hasNext()) {
				Integer openNodesListIndex = (Integer) it.next();
				possibleMerge[i] = openNodesList.get(openNodesListIndex);
				i++;
			}
			mergeResults.add(new MergeResult(calculateCategoryUtility(possibleMerge), possibleMerge));
		}
		
		// get the IMergeResult with the highest utility value
		// from the list of all IMergeResults
		IMergeResult best = null;
		double max = -Double.MAX_VALUE; // initialized with the smallest possible double value
 		for (IMergeResult iMergeResult : mergeResults) {
			double cUt = iMergeResult.getCategoryUtility();
			if (max < cUt) {
				best = iMergeResult;
				max = cUt;
			}
		}
		if (best == null) {
			log.severe("Err: Best merge is == null; in: " + getClass().getSimpleName() );
			System.exit(-1);
		}
		return best;
	}
	
	protected abstract double calculateCategoryUtility(INode[] possibleMerge); 

}
