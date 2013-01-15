package ch.uzh.agglorecommender.clusterer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.visu.Display;



/**
 * Class that keeps current count of nodes in the tree
 * Can be used for other data saving during clustering.
 * 
 * That's a singleton, right?
 *
 */
public class Counter implements Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static Counter counter = new Counter();
	
	private long movieNodeCount = 0;
	private long userNodeCount = 0;
	private long openMovieNodes = 0;
	private long openUserNodes = 0;
	
	private long totalComparisons = 0;
	private long cycles = 0;
	private long startTime = 0;
	private transient Display display = null;
	
	/**
	 * Constructor to establish node counts and display
	 */
	private Counter () {
		// singleton
	}
	
	public static Counter getInstance() {
		return counter;
	}
	
	/**
	 * Initialize the counter on start of clustering process.
	 * 
	 * @param userNodes the set of open user nodes. 
	 * @param contentNodes the set of open content nodes.
	 */
	public void initCounter(Set<INode> userNodes,
			Set<INode> contentNodes) {
		setInitialCounts(contentNodes.size(), userNodes.size());
		setOpenUserNodeCount(userNodes.size());
		setOpenMovieNode(contentNodes.size());
		setStartTime(System.currentTimeMillis());
	}
	
	/**
	 * Set the initial number of content and user nodes.
	 * 
	 * @param contentNodeCounts number of content nodes
	 * @param userNodeCounts number of user nodes
	 */
	public void setInitialCounts(long contentNodeCounts, long userNodeCounts) {
		this.movieNodeCount = contentNodeCounts;
		this.userNodeCount = userNodeCounts;
	}
	
	public void setDisplay(Display display) {
		this.display = display;
		//display.update(this);
	}
	
	public long getTotalComparisons() {
		return totalComparisons;
	}

	public void addComparison() {
		this.totalComparisons++;
		TBLogger.getLogger(getClass().getName()).info("comparison nr: " + totalComparisons);
		display.update(this);
	}
	
	public long getOpenMovieNodeCount() {
		return openMovieNodes;
	}

	public void setOpenMovieNode(int movieNodeCountOnCurrentLevel) {
		this.openMovieNodes = movieNodeCountOnCurrentLevel;
	}

	public long getOpenUserNodeCount() {
		return openUserNodes;
	}

	public void setOpenUserNodeCount(int userNodeCountOnCurrentLevel) {
		this.openUserNodes = userNodeCountOnCurrentLevel;
	}

	public long getMovieNodeCount() {
		return movieNodeCount;
	}

	public void setMovieNodeCount(int movieNodeCount) {
		this.movieNodeCount = movieNodeCount;
	}

	public long getUserNodeCount() {
		return userNodeCount;
	}

	public void setUserNodeCount(int userNodeCount) {
		this.userNodeCount = userNodeCount;
	}
	
	public void addMovieNode() {
		this.movieNodeCount++;
	}
	
	public void addUserNode() {
		this.userNodeCount++;
	}
	
	public void addCycle() {
		this.cycles++;
		TBLogger.getLogger(getClass().getName()).info("cycle nr: "+cycles);
	}
	
	public long getCycleCount() {
		return cycles;
	}
	
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	
    /*
     * Calculates elapsed time since start
     */
	public long getElapsedTime() {
		return (long) (((double)(System.currentTimeMillis() - this.startTime)) / 1000.0);
	}
	
    /*
     * Calculates number of total nodes
     */
	public long getTotalOpenNodes() {
    	
		long toBeCompared = 0;
    	toBeCompared += getOpenMovieNodeCount();
    	toBeCompared += getOpenUserNodeCount();
    	
    	return toBeCompared;
	}
	
    /*
     * Calculates number of total mergedNodes
     */
	public long getTotalMergedNodes() {
    	
		long totalMergedNodes = 0;
    	totalMergedNodes += getMovieNodeCount() - getOpenMovieNodeCount();
    	totalMergedNodes += getUserNodeCount() - getOpenUserNodeCount();
    	
    	return totalMergedNodes;
	}
	
    /*
     * Calculates expected total comparisons on current level
     */
    public long getTotalExpectedComparisonsLvl(long toBeCompared) {
    	
    	// Calculate comparisons on current Level
    	long totalComparisonsOnLevel = 0;
    	for (long i = 1;i < toBeCompared; i++) {
    		totalComparisonsOnLevel += toBeCompared - i;
    	}
    	
    	return totalComparisonsOnLevel;
    } 
	
    /*
     * Calculates expected total comparisons
     */
    public long getTotalExpectedComparisons(long toBeCompared) {
    	
    	// Calculate comparisons on current Level
    	long totalComparisonsOnLevel = getTotalExpectedComparisonsLvl(toBeCompared);
		TBLogger.getLogger(getClass().getName()).fine("totalComparisonsOnLevel: " + totalComparisonsOnLevel);

    	
    	// Calculate expected total comparisons for the whole clustering process
    	long totalComparisons = 0;
    	for (long i = 0;i < totalComparisonsOnLevel; i++) {
    		totalComparisons += totalComparisonsOnLevel - i;
    		TBLogger.getLogger(getClass().getName()).fine("calculated: " + totalComparisonsOnLevel +"," + i + ";" + totalComparisons);
    	}
    	
		TBLogger.getLogger(getClass().getName()).fine("totalComparisons: " + totalComparisons);
    	
    	return totalComparisons;
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException { 
    	ois.defaultReadObject();
    	Counter c = Counter.getInstance();
    	c.movieNodeCount = this.movieNodeCount;

    	c.userNodeCount = this.userNodeCount;
    	c.openMovieNodes = this.openMovieNodes;
    	c.openUserNodes = this.openUserNodes;
    	
    	c.totalComparisons = this.totalComparisons;

    	c.cycles = this.cycles;

    	c.startTime = this.startTime;
    	c.display = new Display();
    }

}
