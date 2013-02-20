package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.list.TIntList;
import gnu.trove.map.TIntDoubleMap;
import gnu.trove.map.hash.TIntDoubleHashMap;

import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;

class SplitWorker extends Thread {
	
	private final TIntList split;
	
	private TIntDoubleMap calcRes;
	
	private final BasicMaxCategoryUtilitySearcher searcher;
	
	private final IClusterSetIndexed<INode> clusterSet;
	
	private final double maxPossibleCU;
	
	private static boolean maxCUFound = false; // all threads read and write to this variable, 
											// however no synchronization needed, since write 
											// sets variable always only to true. Additional cycles due to delayed 
											// update of the variable in memory do not harm.
	
	public SplitWorker(TIntList split, BasicMaxCategoryUtilitySearcher searcher, IClusterSetIndexed<INode> clusterSet) {
		this.split = split;
		this.calcRes = new TIntDoubleHashMap(split.size());
		this.searcher = searcher;
		this.clusterSet = clusterSet;
		this.maxPossibleCU = searcher.getMaxTheoreticalPossibleCategoryUtility();
	}

	@Override
	public void run() {
		Logger log = TBLogger.getLogger(getClass().getName());
		TIntIterator iterator = split.iterator();
		for ( int i = split.size(); i-- > 0; ) { 			// faster iteration by avoiding hasNext()
			if (maxCUFound) return;
			int combinationId = iterator.next();
        	double cu = searcher.calculateCategoryUtility(clusterSet.getCombination(combinationId));
			if (cu >= maxPossibleCU) {
				if (cu > maxPossibleCU) {
					// error. shouldn't be possible
					log.severe("calculated category utility is greater than teoretical maximum.");
					log.severe("Exiting application!");
					System.exit(-1);
				}
				log.fine("Merge result with max theoretical category utility was found." +
						" Terminating category utilitie calculation for remaining merges.");
				maxCUFound = true;
				return;
			}
        	calcRes.put(combinationId, cu);
		}
		
	}
	
	protected TIntDoubleMap getCalcRes() {
		return calcRes;
	}
	
}
