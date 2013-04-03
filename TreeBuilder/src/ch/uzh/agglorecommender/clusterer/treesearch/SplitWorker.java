package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 * Performs the category utility calculation for a fraction of all combination id's.
 */
final class SplitWorker extends Thread {

	/**
	 * List of combination id's to process by this worker.
	 */
	private final TIntList split;

	/**
	 * Maps to each processed combination id its category utility value.
	 */
	private TIntDoubleMap calcRes;

	/**
	 * Performs the category utility value calculation. 
	 */
	private final BasicMaxCategoryUtilitySearcher searcher;

	/**
	 * Needed to resolve the combination id's into the corresponding nodes.
	 */
	private final IClusterSetIndexed<INode> clusterSet;

	/**
	 * The theoretical maximal possible category utility value for a merge.
	 */
	private final double maxPossibleCU;

	/**
	 * Indicates whether in the current cycle a merge with the maximal possible category utility 
	 * was found. Search is terminated if true.
	 * <br>
	 * All SplitWorker threads read and write to this variable,
	 * however no synchronization is needed, since write
	 * sets variable always only to true. Additional cycles due to delayed
	 * update of the variable in memory do not harm.
	 * <br>
	 * After each cycle variable is reset to false.
	 */
	private static boolean maxCUFound = false;


	/**
	 * Instantiates a new SplitWorker that can be started in a new thread.
	 * 
	 * @param split combination'ids to calculate
	 * @param searcher searcher that calculates category utility.
	 * @param clusterSet set to cluster
	 */
	protected SplitWorker(TIntList split,
			BasicMaxCategoryUtilitySearcher searcher,
			IClusterSetIndexed<INode> clusterSet) {
		this.split = split;
		this.calcRes = new TIntDoubleHashMap(split.size());
		this.searcher = searcher;
		this.clusterSet = clusterSet;
		this.maxPossibleCU = searcher.getMaxTheoreticalPossibleCategoryUtility();
	}

	/**
	 * Calculates all the category utilities for the combination id's on the split or until
	 * maxCUFound is true.
	 */
	@Override
	public void run() {
		Logger log = TBLogger.getLogger(getClass().getName());
		TIntIterator iterator = split.iterator();
		for (int i = split.size(); i-- > 0;) { // faster iteration by avoiding hasNext()
			if (maxCUFound){
				log.fine("Thread Id " + this.getId() + ": Terminating searcher thread due to found max cu in different thread.");
				return;
			}
	
			int combinationId = iterator.next();
			double cu = searcher.calculateCategoryUtility(clusterSet.getCombination(combinationId));
			if (cu >= maxPossibleCU) {
				if (cu > maxPossibleCU) {
					// error. shouldn't be possible
					log.severe("Thread Id " + this.getId() + ": calculated category utility is greater than theoretical maximum.");
					log.severe("Thread Id " + this.getId() + ": Exiting application!");
					System.exit(-1);
				}
				log.fine("Thread Id " + this.getId() + ": Merge result with max theoretical category utility was found."
						+ " Terminating category utility calculation for remaining merges. Found CU: " + cu);
				calcRes.put(combinationId, cu);
				maxCUFound = true;
				return;
			}
			calcRes.put(combinationId, cu);
		}

	}

	protected TIntDoubleMap getCalcRes() {
		return calcRes;
	}
	
	protected static void setMaxCUFound(boolean maxCUFound) {
		SplitWorker.maxCUFound = maxCUFound;
	}

}
