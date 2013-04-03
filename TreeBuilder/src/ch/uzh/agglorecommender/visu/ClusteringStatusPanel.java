package ch.uzh.agglorecommender.visu;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;
import ch.uzh.agglorecommender.util.LimitedQueue;

/**
 * Class that keeps current count of nodes in the tree
 * Can be used for other data saving during clustering.
 *
 */
public class ClusteringStatusPanel extends JPanel implements Observer {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	private int openContentNodes = 0;
	private int openUserNodes = 0;
	private int numOfMerges = 0;
	private final long startTime;

	private long startTimeOfCycle;
	private int numberOfLeaves = -1;
	private LinkedList<Long> mergeTimes = new LimitedQueue<Long>(10);

	private JLabel elapsedTime;
	
	private JLabel totalOpenNodes;
	private JLabel merges;
	private JLabel expTime;
	
	private JProgressBar progressBar;
    private DecimalFormat nft = new DecimalFormat("#00.###"); 

	public ClusteringStatusPanel() {
		this.startTime = System.currentTimeMillis();
		this.startTimeOfCycle = startTime;
		
        // Add monitoring parameters
        elapsedTime 	= new JLabel("Elapsed Time: " + getElapsedTime());	
        totalOpenNodes	= new JLabel("Open Clusters: " + getTotalOpenNodes());
        merges			= new JLabel("Merges: " + getNumberOfMerges());
        expTime 		= new JLabel("Time Left: " + getTotalExpectedSeconds());
        
        progressBar 	= new JProgressBar(0, 100); 
        progressBar.setStringPainted(true);
        
        // Add to panel

        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 30));
        add(elapsedTime);
        add(sep);
        sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 30));
        add(totalOpenNodes);
        add(sep);
        sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 30));
        add(merges);
        add(sep);
        sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 30));
        add(expTime);
        add(sep);
        sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setPreferredSize(new Dimension(10, 30));
        add(progressBar);
	}

	private void addCycle() {	
		mergeTimes.add(System.currentTimeMillis() - startTimeOfCycle);
		startTimeOfCycle = System.currentTimeMillis();
	}

	private int getNumberOfMerges() {
		return numOfMerges;
	}

	/**
	 * Calculates elapsed time in seconds since since start
	 */
	private long getElapsedTime() {
		return (long) (((double)(System.currentTimeMillis() - this.startTime)) / 1000.0);
	}

	/**
	 * Calculates number of total nodes
	 */
	private int getTotalOpenNodes() {

		int totalNodes = 0;
		totalNodes += openContentNodes -1;
		totalNodes += openUserNodes -1;

		if(totalNodes >= 0) {
			return totalNodes;
		}
		else return 0;
	}

	private long getTotalExpectedSeconds() {

		double avg = 0.0;
		if (mergeTimes.size() > 0) {
			for (long l : mergeTimes) {
				avg += l;
			}
			avg /= (double)mergeTimes.size();
		}
		long r = Math.round((avg * getTotalOpenNodes()) / 1000.0);
		return r;
	}

	private double getProgressInPercentage() {
		return (1.0 - (double)getTotalOpenNodes() / (double)numberOfLeaves) * 100.0;

	}


	@Override
	public void update(IClusterSet<INode> userClusterSet, IClusterSet<INode> contentClusterSet) {
		// Update Open Nodes
		this.openUserNodes = userClusterSet.size();
		this.openContentNodes = contentClusterSet.size();

		numOfMerges = 0;
		for (INode n : userClusterSet.getUnmodifiableView()) {
			if (! n.isLeaf()) {
				numOfMerges++;
			}
		}
		for (INode n : contentClusterSet.getUnmodifiableView()) {
			if (! n.isLeaf()) {
				numOfMerges++;
			}
		}
		
		if (numberOfLeaves < 0) {
			this.numberOfLeaves = getNumberOfLeaves(userClusterSet, contentClusterSet);
		}

		// Update Cycle
		addCycle();
		
		update();
	}
	
    /**
     * Update the data in the GUI
     */
    private void update() {
    	
    	// Format Time
    	long elTime 	= getElapsedTime();
    	long elDays 	= TimeUnit.SECONDS.toDays(elTime);
    	long elHours	= TimeUnit.SECONDS.toHours(elTime) - (elDays * 24);
    	long elMinutes	= TimeUnit.SECONDS.toMinutes(elTime) - (elHours * 60 + elDays * 24 *60);
    	long elSeconds	= TimeUnit.SECONDS.toSeconds(elTime) - (elMinutes * 60 + elHours * 60 * 60 + elDays * 24 * 60 * 60);
    	
    	long exTime		= getTotalExpectedSeconds();
    	long exDays 	= TimeUnit.SECONDS.toDays(exTime);
    	long exHours	= TimeUnit.SECONDS.toHours(exTime) - (exDays * 24);
    	long exMinutes	= TimeUnit.SECONDS.toMinutes(exTime) - (exHours * 60 + exDays * 24 * 60);
    	long exSeconds	= TimeUnit.SECONDS.toSeconds(exTime) - (exMinutes * 60 + exHours * 60 * 60 + exDays * 24 * 60 * 60);
    	
    	elapsedTime.setText		("Elapsed Time: " 	+ nft.format(elHours) + ":" + nft.format(elMinutes) + ":" + nft.format(elSeconds));
    	totalOpenNodes.setText	("Open Nodes: " 	+ getTotalOpenNodes());
    	merges.setText			("Merges: " 		+ getNumberOfMerges());
    	expTime.setText			("Time Left: " 		+ nft.format(exHours) + ":" + nft.format(exMinutes) + ":" + nft.format(exSeconds));
    	progressBar.setValue	((int) Math.round(getProgressInPercentage()));
    }

	private int getNumberOfLeaves(IClusterSet<INode> userClusterSet, IClusterSet<INode> contentClusterSet) {
		int r = 0;
		for (INode n : userClusterSet.getUnmodifiableView()) {
			r += n.getNumberOfLeafNodes();
		}
		for (INode n : contentClusterSet.getUnmodifiableView()) {
			r += n.getNumberOfLeafNodes();
		}
		return r;
	}

}
