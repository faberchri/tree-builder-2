package ch.uzh.agglorecommender.clusterer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import ch.uzh.agglorecommender.util.TBLogger;
import ch.uzh.agglorecommender.visu.MonitorPanel;

/**
 * Class that keeps current count of nodes in the tree
 * Can be used for other data saving during clustering.
 *
 */
public class Monitor implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static Monitor monitor = new Monitor();
	
	private long contentNodeCount = 0;
	private long userNodeCount = 0;
	private long openContentNodes = 0;
	private long openUserNodes = 0;
	
	private long totalComparisons = 0;
	private long cycles = 0;
	private long startTime = 0;
	private transient MonitorPanel display = null;
	
	/**
	 * Constructor to establish node counts and display
	 */
	private Monitor () {
		// singleton
	}
	
	public static Monitor getInstance() {
		return monitor;
	}
	
	/**
	 * Initialize the counter on start of clustering process.
	 * 
	 * @param Count of userNodes in userNode Set 
	 * @param Count of contentNodes in contentNode Set
	 */
	public void initMonitoring(int openUserNodes,int openContentNodes) {
		setInitialCounts(openContentNodes,openUserNodes);
		this.openUserNodes 		= openUserNodes;
		this.openContentNodes 	= openContentNodes;
		this.startTime = System.currentTimeMillis();
	}
	
	/**
	 * Set the initial number of content and user nodes.
	 * 
	 * @param contentNodeCounts number of content nodes
	 * @param userNodeCounts number of user nodes
	 */
	public void setInitialCounts(long contentNodeCounts, long userNodeCounts) {
		this.contentNodeCount = contentNodeCounts;
		this.userNodeCount = userNodeCounts;
	}
	
	public void addComparison() {
		this.totalComparisons++;
		TBLogger.getLogger(getClass().getName()).info("comparison nr: " + totalComparisons);
	}
	
	private void addCycle() {	
		TBLogger.getLogger(getClass().getName()).info("cycle nr: "+cycles);
		this.cycles++;
	}
	
	public long getCycleCount() {
		return cycles;
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
    	toBeCompared += openContentNodes;
    	toBeCompared += openUserNodes;
    	
    	return toBeCompared;
	}
	
	/*
	 * Delivers total comparisons
	 */
	public long getTotalComparisons() {
		return totalComparisons;
	}
	
    /*
     * Calculates number of total mergedNodes
     */
	public long getTotalMergedNodes() {
    	
		long totalMergedNodes = 0;
    	totalMergedNodes += contentNodeCount - openContentNodes;
    	totalMergedNodes += userNodeCount - openUserNodes;
    	
    	return totalMergedNodes;
	}
	
    /*
	 * Calulates the time used for Merge
	 */
	public double getTimePerMerge() {
		double timePerMerge =  (double) getTotalMergedNodes() / (double) getElapsedTime();
		return timePerMerge;
	}
    
    public long getTotalExpectedTime() {
    	double expTime = getTimePerMerge() * getTotalOpenNodes();
    	return (long) expTime;
    }
    
    /*
     * Calulates the percentage of comparisons actually calculated
     */
    public double getPercentageOfMerges() {
    	if ((getTotalMergedNodes() + getTotalOpenNodes()) != 0){
    		double percentage = (double) getTotalMergedNodes() / ((double) getTotalMergedNodes() + getTotalOpenNodes());
    		return percentage;
    	}
    		return 0;
    }
    
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException { 
    	ois.defaultReadObject();
    	Monitor c = Monitor.getInstance();
    	
    	c.contentNodeCount = this.contentNodeCount;
    	c.userNodeCount = this.userNodeCount;
    	c.openContentNodes = this.openContentNodes;
    	c.openUserNodes = this.openUserNodes;
    	c.totalComparisons = this.totalComparisons;
    	c.cycles = this.cycles;
    	c.startTime = this.startTime;
    }

	public void update(int openUserNodes, int openContentNodes) {
		
		// Update Cycle
		addCycle();
		
		// Update Open Nodes
		this.openUserNodes = openUserNodes;
		this.openContentNodes = openContentNodes;
	}

}
