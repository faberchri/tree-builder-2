package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.util.TBLogger;

public class CachedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static Map<List<INode>, IMergeResult> cache = new HashMap<List<INode>,IMergeResult>();
	
	public CachedMaxCUSearcher(IMaxCategoryUtilitySearcher decoratedSearcher) {
		super(decoratedSearcher);
	}
	
	
	/**
	 * Removes combinations list that contain dirty nodes
	 * (nodes whit recently changed attributes) from the cache.
	 * The dirty flags of all nodes contained in the passed 
	 * cluster set are set to false and valid results are
	 * fetched from the cache.
	 * 
	 * @param combinationsToCheck the node combinations to check
	 * @param clusterSet the set of the nodes to cluster
	 * @return a set with IMergeResults obtained from the cache 
	 */
	private Set<IMergeResult> revalidateAndFetchCache(Set<List<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		long time = System.nanoTime();
		Logger log = TBLogger.getLogger(getClass().getName());
		int invalidLists = cache.keySet().size();
		Set<IMergeResult> result = new HashSet<IMergeResult>(combinationsToCheck.size());
		Set<INode> dirty = Node.getAllDirtyNodes();
		int dirtyNodes = dirty.size();
		Iterator<INode> i = dirty.iterator();
		long step = System.nanoTime();
		while (i.hasNext()) {
			INode n = i.next();
			if (clusterSet.contains(n)){
				cache.keySet().removeAll(clusterSet.getCombinations(n));
				i.remove();
			}
		}
//		System.err.println("remove from cahce and dirty set: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		for (List<INode> l : combinationsToCheck) {
			result.add(cache.get(l));
		}
		result.remove(null);
//		System.err.println("add to result set: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		
		time = System.nanoTime() - time;
		log.info("Number of invalidated lists: " + (invalidLists - cache.keySet().size())
				+ ", number of cleaned dirty nodes: " + (dirtyNodes - dirty.size())
				+", time for cache update: "+(double)time / 1000000000.0 + " s");
		return result;
	}

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<List<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time1 = System.nanoTime();
		int initialNumberOfCombinationsToCheck = combinationsToCheck.size();
		
		long step = System.nanoTime();
//		// (re)validate cache
//		revalidateCache();
//		System.err.println("reval: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
//		step = System.nanoTime();
//		
//		// new result set
//		Set<IMergeResult> result = new HashSet<IMergeResult>(combinationsToCheck);
//		
//		// fetch cached valid results and add to results
//		Set<List<INode>> kS = Sets.intersection(cache.keySet(), combinationsToCheck);
//		for (List<INode> l : kS) {
//			result.add(cache.get(l));
//		}
		
		// revalidate the cache and fetch valid cache values into results
		Set<IMergeResult> result = revalidateAndFetchCache(combinationsToCheck, clusterSet);
		
//		System.err.println("reval / fetch cache: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		
		// remove cached lists from combinationsToCheck
		combinationsToCheck.removeAll(cache.keySet());
//		System.err.println("rmv cached lists: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));

	
		log.info(result.size() + " of " + initialNumberOfCombinationsToCheck + " IMergeResults were found in cache");
		time1 = System.nanoTime() - time1;
		
		// fetch new merge results
		Set<IMergeResult> newMerges = decoratedSearcher.getMaxCategoryUtilityMerges(combinationsToCheck, clusterSet);
		
		step = System.nanoTime();
		
		long time2 = System.nanoTime();
		
		// add new merge results to cache
		for (IMergeResult nM : newMerges) {
			cache.put(nM.getNodes(), nM);
		}
		
//		System.err.println("increase cache: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));
		step = System.nanoTime();
		
		// combine cached results and new results
		result.addAll(newMerges);
		
//		System.err.println("combine results: " + ((double) (System.nanoTime() - step) / 1000000000.0 + " s"));

		
		time2 = System.nanoTime() - time2;
		log.info("Time in cache decorator: " + (double)(time1 + time2) / 1000000000.0 + " s");
		return result;
	}
	
	/**
	 * Removes combinations list that contain dirty nodes
	 * (nodes whit recently changed attributes) from the cache.
	 * The dirty flags of all nodes contained in the cache are 
	 * reset to false.
	 * <br>
	 * <br>
	 * NOT USED CURRENTLY DUE TO LACK OF PERFORMANCE
	 */
	private void revalidateCache() {
		long time = System.nanoTime();
		Logger log = TBLogger.getLogger(getClass().getName());

		final Set<INode> dirtyNodes = Collections.synchronizedSet(new HashSet<INode>()) ;
		final Set<List<INode>> invalidLists = Collections.synchronizedSet(new HashSet<List<INode>>());
		
		ExecutorService es = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		List<Future<?>> futures = new ArrayList<Future<?>>();
		for (final List<INode> l : cache.keySet()) {
			futures.add(es.submit(new Runnable() {
				
				@Override
				public void run() {
					for (INode n : l) {
						if(n.isDirty()) {				
							invalidLists.add(l);
							dirtyNodes.add(n);
							break;
						}
					}					
				}
			}));
		}
		es.shutdown();
		for (Future<?> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				log.severe("InterruptedException or ExecutionException while waiting for termination.");
				System.exit(-1);
			}
		}
				
		// remove list with dirty node(s) from cache
		cache.keySet().removeAll(invalidLists);
		
		// reset dirty flags of nodes
		for (INode n : dirtyNodes) {
			n.setClean();
		}
		
		time = System.nanoTime() - time;
		log.info("Number of invalidated lists: " + invalidLists.size()
				+ ", number of dirty nodes: " + dirtyNodes.size()
				+", time for cache update: "+(double)time / 1000000000.0 + " s");
	}
	

}
