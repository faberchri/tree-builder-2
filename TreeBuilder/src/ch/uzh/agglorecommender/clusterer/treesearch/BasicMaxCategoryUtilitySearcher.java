package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;
import gnu.trove.set.TIntSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

/**
 * 
 * Responsible for calculating category utilities for the passed list of nodes calculation.
 * Is the concrete component of the decorator pattern used with the {@link IMaxCategoryUtilitySearcher}
 * interface.
 *
 */
abstract class BasicMaxCategoryUtilitySearcher implements IMaxCategoryUtilitySearcher {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public TIntDoubleMap getMaxCategoryUtilityMerges(TIntSet combinationIds,
			IClusterSetIndexed<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		
		List<SplitWorker> workers = new ArrayList<SplitWorker>();
		List<TIntList> splits = splitCombinationIdsSet(combinationIds);
		
		// create new SplitWorker object for each entry in splits
		// and start SplitWorker in new thread. Allows parallel
		// category utility calculation.
		for (TIntList split : splits) {
			SplitWorker w = new SplitWorker(split, this, clusterSet);
			workers.add(w);
			w.start();
		}
		TIntDoubleMap res = new TIntDoubleHashMap(combinationIds.size());
		
		// wait for completion of each SplitWorker and collect its results
		for (SplitWorker t : workers) {
			try {
				t.join();
			} catch (InterruptedException e) {
				log.severe(e.getStackTrace().toString());
				log.severe(e.getMessage());
				log.severe("InterruptedException while waiting at join().");
				System.exit(-1);
			}
			res.putAll(t.getCalcRes());
		}
		
		// reset static variable of split worker for next clustering cycle
		SplitWorker.setMaxCUFound(false);
				
		time = System.nanoTime() - time;
		log.finer("Time to calculate new category utility values: " + ( (double) (time) ) / 1000000000.0 + " seconds. On "
					+ combinationIds.size() + " combinations. Calculated combinations: " + res.keySet().size());
		return res;
	}
	
	/**
	 * Splits the collection of combination id's into that many equally sized lists of
	 * combination id's as processors are available.
	 *  
	 * @param combinationIds collection of possible merges
	 * @return list of combination id lists
	 */
	private List<TIntList> splitCombinationIdsSet(TIntSet combinationIds) {
		double numOfSplits = Runtime.getRuntime().availableProcessors();
		int splitSize = (int) Math.ceil((double)combinationIds.size() / numOfSplits);
		
		List<TIntList> res = new ArrayList<TIntList>();
		int splitCount = 0;
		TIntList split = new TIntArrayList(splitSize + 1);
		TIntIterator iterator = combinationIds.iterator();
		for ( int i = combinationIds.size(); i-- > 0; ) { // faster iteration by avoiding hasNext()
			if (splitCount > splitSize) {
				res.add(split);
				split = new TIntArrayList(splitSize + 1);
				splitCount = 0;
			}
			split.add(iterator.next());
			splitCount++;
		}
		res.add(split);
		return res;
	}
	
		
	/**
	 * Calculates the category utility according to the implementing subclass (i.e. Cobweb or Classit)
	 * of a single node resulting from a merge of all nodes in the passed nodes array.
	 * 
	 * @param possibleMerge the nodes to merge
	 * @return the category utility of a node resulting from the merge.
	 */
	protected abstract double calculateCategoryUtility(Collection<INode> possibleMerge);
	
	/**
	 * Gets the theoretical maximal possible Category Utility of a merge
	 * depending on the category utility calculation implementation (Cobweb / Classit).
	 * 
	 * @return the max possible category utility value
	 */
	protected abstract double getMaxTheoreticalPossibleCategoryUtility();
	
}
