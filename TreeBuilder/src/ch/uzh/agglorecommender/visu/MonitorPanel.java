package ch.uzh.agglorecommender.visu;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import ch.uzh.agglorecommender.clusterer.Monitor;



/*
 * Shows progress of comparisons on current level
 */
public class MonitorPanel extends JPanel{
	
		private Monitor monitor;
		
		private JLabel cycle;
		private JLabel elapsedTime;
		
		private JLabel totalOpenNodes;
		private JLabel comparisons;
		private JLabel merges;
		
		private JLabel speed;
		private JLabel expComparisons;
		private JLabel expTime;
		
		private JProgressBar progressBar;
		
	    private DecimalFormat df = new DecimalFormat("#.##");
	    private DecimalFormat nft = new DecimalFormat("#00.###");  

	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    public MonitorPanel() {
	        
	    	monitor = Monitor.getInstance();
	    	
	        // Add monitoring parameters
	        elapsedTime 	= new JLabel("Elapsed Time: " + monitor.getElapsedTime());	
	        totalOpenNodes	= new JLabel("Open Nodes: " + monitor.getTotalOpenNodes());
	        merges			= new JLabel("Merges: " + monitor.getCycleCount());
	        speed			= new JLabel("Speed: " + monitor.getTimePerMerge());
	        expTime 		= new JLabel("Time Left: " + monitor.getTotalExpectedTime());
	        
	        progressBar 	= new JProgressBar(0, 100); 
	        progressBar.setStringPainted(true);
	        
	        // Add to panel
	        add(elapsedTime);
	        add(totalOpenNodes);
	        add(merges);
	        add(speed);
	        add(expTime);
	        add(progressBar);
	        
	    }
	    
	    /**
	     * Update the data in the GUI
	     */
	    public void update() {
	    	
	    	// Format Time
	    	long elDays 	= TimeUnit.SECONDS.toDays(monitor.getElapsedTime());
	    	long elHours	= TimeUnit.SECONDS.toHours(monitor.getElapsedTime()) - elDays * 24;
	    	long elMinutes	= TimeUnit.SECONDS.toMinutes(monitor.getElapsedTime()) - elHours * 60;
	    	long elSeconds	= TimeUnit.SECONDS.toSeconds(monitor.getElapsedTime()) - elMinutes * 60;
	    	long exDays 	= TimeUnit.SECONDS.toDays(monitor.getTotalExpectedTime());
	    	long exHours	= TimeUnit.SECONDS.toHours(monitor.getTotalExpectedTime()) - exDays * 24;
	    	long exMinutes	= TimeUnit.SECONDS.toMinutes(monitor.getTotalExpectedTime()) - exHours * 60;
	    	long exSeconds	= TimeUnit.SECONDS.toSeconds(monitor.getTotalExpectedTime()) - exMinutes * 60;
	    	
	    	elapsedTime.setText		("Elapsed Time: " 	+ nft.format(elHours) + ":" + nft.format(elMinutes) + ":" + nft.format(elSeconds));
	    	totalOpenNodes.setText	("Open Nodes: " 	+ monitor.getTotalOpenNodes());
	    	merges.setText			("Merges: " 		+ monitor.getCycleCount());
	    	speed.setText			("Speed: " 			+ df.format(monitor.getTimePerMerge()) + "/s");
	    	expTime.setText			("Time Left: " 		+ nft.format(exHours) + ":" + nft.format(exMinutes) + ":" + nft.format(exSeconds));
	    	progressBar.setValue	((int) monitor.getPercentageOfMerges());
	    }

	}
