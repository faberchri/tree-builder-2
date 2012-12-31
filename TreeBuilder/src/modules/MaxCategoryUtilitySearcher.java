package modules;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import utils.TBLogger;
import clusterer.IMaxCategoryUtilitySearcher;
import clusterer.IMergeResult;
import clusterer.INode;

public abstract class MaxCategoryUtilitySearcher implements IMaxCategoryUtilitySearcher, Serializable {

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
	 * 
	 */
	private final int MAX_SUBSET_SIZE = 2;
	
	private final int INITIAL_COMBINATION_INDICES_LIST_CAPACITY = 4000000;

	private int highestNodeIndex = -1;
	
	/**
	 * Stores all possible combinations of the indices of the passed set limited to the subset size <= MAX_SUBSET_SIZE.
	 * Requires a set that allows accessing elements by index like modules.IndexAwareSet.
	 */
	private List<scala.collection.immutable.List<Object>> combinationIndicesList = new ArrayList<scala.collection.immutable.List<Object>>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
	
	@Override
	public IMergeResult getMaxCategoryUtilityMerge(Set<INode> openNodes) {
		Logger log = TBLogger.getLogger(getClass().getName());
		long time = System.nanoTime();
		// first we need a set of all possible subsets of open nodes.
		// This set is called a power set.
		// however we want to exclude the following sets from the power set:
		// - the empty set
		// - sets with size == 1
		// Furthermore, generating all possible subsets results in 2^n
		// new sets. Keeping all this objects in memory is clearly not possible.
		// Therefore we only generate "small" subsets, i.e. sets with size > 1 
		// and size <= MAX_SUBSET_SIZE.
		IndexAwareSet<INode> openNodesList = (IndexAwareSet<INode>)openNodes;
				
		long prevNumOfCombinations = combinationIndicesList.size(); // only used for logging
		
		if (highestNodeIndex < 0) {
			// performed on first call of getMaxCategoryUtilityMerge to obtain the initial indices combination list.
			initializeCombinationIndicesList(openNodesList);
		} else {
			// performed on all calls of getMaxCategoryUtilityMerge except on the first call to update the indices combination list
			// with the combinations formed from the indices of newly added nodes.
			updateCombinationIndicesList(openNodesList);
		}
		
		highestNodeIndex = openNodesList.getLastIndex(); // needed to obtain the new indices in the next round.
		
		time = System.nanoTime() - time;
		log.finer("Time to obtain combination indices list: " + ( (double) (time) ) / 1000000000.0 
				+ " seconds; number of obtained new lists: " + (combinationIndicesList.size() - prevNumOfCombinations)
				+ "; total number of lists: " + combinationIndicesList.size());		
		time = System.nanoTime();
		
		// Calculate for all indices combinations in 
		// combinationIndicesList the category
		// utility and store it in a IMergeResult object.
		// Add the IMergeResults to a list.
		List<IMergeResult> mergeResults = null;
		try {
			mergeResults = obtainMergeResultsMultithreadedMaxCUCheck(combinationIndicesList, openNodesList);
		} catch (InterruptedException | ExecutionException e) {
			log.severe(e.getStackTrace().toString());
			log.severe(e.getMessage());
			log.severe("Error in merge results calculation.");
//			mergeResults = obtainMergeResultsSinglethreaded(combinationIndicesList, openNodesList);
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
		
		time = System.nanoTime() - time;
		log.finer("Time to calculate all category utility values: " + ( (double) (time) ) / 1000000000.0 + " seconds");
		return best;
	}
	
	/**
	 * Calculates all possible combinations of the indices of the openNodesList limited to subset size <= MAX_SUBSET_SIZE.
	 * The resulting lists are stored in combinationIndicesList.
	 * <br>
	 * Method is called only on first call of getMaxCategoryUtilityMerge.
	 * In subsequent calls of getMaxCategoryUtilityMerge updateCombinationIndicesList is called.
	 * <br>
	 * @param openNodesList the list from which the combinations are calculated.
	 */
	private void initializeCombinationIndicesList( IndexAwareSet<INode> openNodesList) {
		Logger log = TBLogger.getLogger(getClass().getName());
		
		long subListsCount = combinationIndicesList.size();
		for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
			if (openNodesList.size() < sublistLength) continue;
			scala.collection.Iterator<scala.collection.immutable.List<Object>> it
					= SubsetsGenerator.subsets(openNodesList.size(), sublistLength);
			while (it.hasNext()) {
				scala.collection.immutable.List<Object> sublist =  it.next();
				log.finest("sublist "+ subListsCount+": "+ sublist);
				subListsCount++;
				combinationIndicesList.add(sublist);
			}
		}
	}
	
	/**
	 * Updates the combinationIndicesList with the possible combinations
	 * (limited to sublist length <= MAX_SUBSET_SIZE)
	 * formed from the indices that have been added since the last call of
	 * getMaxCategoryUtilityMerge and all other indices of the openNodeList.
	 *
	 * @param openNodesList the list from which the combinations are calculated.
	 */
	private void updateCombinationIndicesList(IndexAwareSet<INode> openNodesList) {
		Logger log = TBLogger.getLogger(getClass().getName());
		
		List<Integer> newIndices = new ArrayList<Integer>();
		for (int i = highestNodeIndex + 1; i <= openNodesList.getLastIndex(); i++) {
			newIndices.add(i);
		}
		
		long subListsCount = combinationIndicesList.size();
		for (Integer newIndex : newIndices) {
			for (int sublistLength = 2; sublistLength <= MAX_SUBSET_SIZE; sublistLength++) {
				if (sublistLength > newIndex) continue;
				scala.collection.Iterator<scala.collection.immutable.List<Object>> it 
					= SubsetsGenerator.remainingSubsets(newIndex, sublistLength);
				while (it.hasNext()) {
					scala.collection.immutable.List<Object> sublist = it.next();
					log.finest("sublist "+ subListsCount+": "+ sublist);
					subListsCount++;
					combinationIndicesList.add(sublist);
				}
			}
			
		}
	}
	
	/**
	 * Calculates the category utility according to the implementing subclass (i.e. Cobweb or Classit)
	 * of a single node resulting from a merge of all nodes in the passed nodes array.
	 * 
	 * @param possibleMerge the nodes to merge
	 * @return the category utility of a node resulting from the merge.
	 */
	protected abstract double calculateCategoryUtility(INode[] possibleMerge);
	
	protected abstract double getMaxTheoreticalPossibleCategoryUtility();
	
	/**
	 * Calculates for all entries in the combinationIndicesList the resulting IMergeResult, which includes the category utility.
	 * <br>
	 * Multi-threaded implementation using ExecutorService.
	 * 
	 * @param combinationIndicesList list of merge combinations. Corresponds to indices of the openNodesList.
	 * @param openNodesList list of all open nodes
	 * @return the list of all IMergeResults (length of return list == combinationIndicesList.size())
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<IMergeResult> obtainMergeResultsMultithreaded(List<scala.collection.immutable.List<Object>> combinationIndicesList, final IndexAwareSet<INode> openNodesList)
	        throws InterruptedException, ExecutionException {

	    int threads = Runtime.getRuntime().availableProcessors();
	    ExecutorService service = Executors.newFixedThreadPool(threads);

	    List<Future<IMergeResult>> futures = new ArrayList<Future<IMergeResult>>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
	    for (final scala.collection.immutable.List<Object> sublist : combinationIndicesList) {
	        Callable<IMergeResult> callable = new Callable<IMergeResult>() {
	            public IMergeResult call() throws Exception {
	                	    			
	    			INode[] possibleMerge = new INode[sublist.length()];
	    			int i = 0;
	    			scala.collection.Iterator<Object> it = sublist.iterator();
	    			boolean validSequence = true;
	    			while (it.hasNext()) {
	    				Integer openNodesListIndex = (Integer) it.next();
	    				INode tmpN =  openNodesList.getByIndex(openNodesListIndex);
	    				if (tmpN == null) {
	    					validSequence = false;
	    					break;
	    				}
	    				possibleMerge[i] = tmpN;
	    				i++;
	    			}
	    			if (validSequence) {
	    				return new MergeResult(calculateCategoryUtility(possibleMerge), possibleMerge);
	    			} else {
	    				return null;
	    			}
	    				                
	            }
	        };
	        futures.add(service.submit(callable));
	    }

	    service.shutdown();

	    List<IMergeResult> mergeResults = new ArrayList<IMergeResult>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
	    for (Future<IMergeResult> future : futures) {
	    	IMergeResult tmp = future.get();
	    	if (tmp != null) {
		        mergeResults.add(tmp);
	    	}
	    }
	    return mergeResults;
	}
	
	/**
	 * Calculates for entries in the combinationIndicesList the resulting IMergeResult
	 * until a MergeResult has the maximal theoretical possible
	 * category utility or all entries in the combinationIndicesList are calculated.
	 * <br>
	 * Multi-threaded implementation using ExecutorService.
	 * 
	 * @param combinationIndicesList list of merge combinations. Corresponds to indices of the openNodesList.
	 * @param openNodesList list of all open nodes
	 * @return the list of all IMergeResults (length of return list == combinationIndicesList.size())
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<IMergeResult> obtainMergeResultsMultithreadedMaxCUCheck(List<scala.collection.immutable.List<Object>> combinationIndicesList, final IndexAwareSet<INode> openNodesList)
	        throws InterruptedException, ExecutionException {

		final Logger log = TBLogger.getLogger(getClass().getName());
		
	    int threads = Runtime.getRuntime().availableProcessors();
//	    ExecutorService service = Executors.newFixedThreadPool(threads);

	    ExecutorService service = new ThreadPoolExecutor(threads, threads,
	    		0L, TimeUnit.MILLISECONDS,
	    		new LinkedBlockingQueue<Runnable>()) {
	    	@Override
	    	protected void afterExecute(Runnable r, Throwable t) {
	    		super.afterExecute(r, t);
	    		FutureTask<IMergeResult> ft = (FutureTask<IMergeResult>) r;
//	    		if (ft.isDone()) {
	    			try {
	    				IMergeResult mr = ft.get();
	    				if (mr == null) return;
	    				double cu = mr.getCategoryUtility();
	    				double theoreticalMaxCu = getMaxTheoreticalPossibleCategoryUtility();
	    				if (cu >= theoreticalMaxCu) {
	    					if (cu > theoreticalMaxCu) {
	    						// error. shouldn't be possible
	    						log.severe("calculated category utility is greater than teoretical maximum.");
	    						log.severe("Exiting application!");
	    						System.exit(-1);
	    					}
	    					log.fine("Merge result with max theoretical category utility was found." +
	    							" Terminating category utilitie calculation for remaining merges.");
	    					
	    					
	    					shutdownNow();
	    					awaitTermination(2, TimeUnit.SECONDS);
	    				}
	    			} catch (InterruptedException | ExecutionException e) {
						log.warning("InterruptedException or ExecutionException in attempt to fetch calculatin result in afterExecute call.");  				
	    			}
//	    		}
	    	}
	    };

	    List<Future<IMergeResult>> futures = new ArrayList<Future<IMergeResult>>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
	    for (final scala.collection.immutable.List<Object> sublist : combinationIndicesList) {
	        Callable<IMergeResult> callable = new Callable<IMergeResult>() {

	            public IMergeResult call() throws Exception {
	                	    			
	    			INode[] possibleMerge = new INode[sublist.length()];
	    			int i = 0;
	    			scala.collection.Iterator<Object> it = sublist.iterator();
	    			boolean validSequence = true;
	    			while (it.hasNext()) {
	    				Integer openNodesListIndex = (Integer) it.next();
	    				INode tmpN =  openNodesList.getByIndex(openNodesListIndex);
	    				if (tmpN == null) {
	    					validSequence = false;
	    					break;
	    				}
	    				possibleMerge[i] = tmpN;
	    				i++;
	    			}
	    			if (validSequence) {
	    				return new MergeResult(calculateCategoryUtility(possibleMerge), possibleMerge);
	    			} else {
	    				return null;
	    			}
	    				                
	            }
	        };
	        try {
	        	futures.add(service.submit(callable));
			} catch (RejectedExecutionException e) {
				break; 
			}
	        
	    }

	    service.shutdown();
	    service.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);

	    List<IMergeResult> mergeResults = new ArrayList<IMergeResult>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
	    for (Future<IMergeResult> future : futures) {
	    	IMergeResult tmp = null;
	    	
	    	if (future.isDone()) {
	    		tmp = future.get();
	    	}

	    	if (tmp != null) {
	    		mergeResults.add(tmp);
	    	}
	    }

	    return mergeResults;
	}
	
	/**
	 * Calculates for all entries in the combinationIndicesList the resulting IMergeResult, which includes the category utility.
	 * <br>
	 * Single-threaded implementation.
	 * 
	 * @param combinationIndicesList list of merge combinations. Corresponds to indices of the openNodesList.
	 * @param openNodesList list of all open nodes
	 * @return the list of all IMergeResults (length of return list == combinationIndicesList.size())
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private List<IMergeResult> obtainMergeResultsSinglethreaded(List<scala.collection.immutable.List<Object>> combinationIndicesList, IndexAwareSet<INode> openNodesList) {
		List<IMergeResult> mergeResults = new ArrayList<IMergeResult>(INITIAL_COMBINATION_INDICES_LIST_CAPACITY);
		for (scala.collection.immutable.List<Object> sublist : combinationIndicesList) {
			INode[] possibleMerge = new INode[sublist.length()];
			int i = 0;
			scala.collection.Iterator<Object> it = sublist.iterator();
			boolean validSequence = true;
			while (it.hasNext()) {
				Integer openNodesListIndex = (Integer) it.next();
				INode tmpN =  openNodesList.getByIndex(openNodesListIndex);
				if (tmpN == null) {
					validSequence = false;
					break;
				}
				possibleMerge[i] = tmpN;
				i++;
			}
			if (validSequence) {
				mergeResults.add(new MergeResult(calculateCategoryUtility(possibleMerge), possibleMerge));
			}
		}
		return mergeResults;
	}

}
