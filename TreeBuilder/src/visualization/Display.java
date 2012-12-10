package visualization;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import clusterer.Counter;

/*
 * Shows progress of comparisons on current level
 */
public class Display {
	
		private JFrame frame;
		private JLabel comparisons;
		private JLabel compPercentage;
		private JLabel avCompTime;
		private JLabel elapsedTime;
		private JLabel expectedTime;
		private JLabel cycle;
		private JLabel mergedNodes;
		private JProgressBar progressBar;
		private JLabel nodesOnCurrentLevel;
		private JLabel comparisonsOnCurrentLevel;
		private JLabel nodeCompPerSecond;

	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    public void start(Counter counter) {
	        
	    	// Create and set up the Frame
	        this.frame = new JFrame("ShowProgress");
	        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.frame.setSize(400, 500);
	        
	        // Define Layout/Container
	        JPanel listPane = new JPanel();
	        listPane.setLayout(new BoxLayout(listPane, BoxLayout.PAGE_AXIS));
	        Container contentPane = frame.getContentPane();
	        contentPane.add(listPane, BorderLayout.CENTER);
	        //contentPane.setSize(400, 500);
	        
	    	// Determine open Nodes
	    	long toBeCompared = counter.getTotalOpenNodes();
	    	
	    	// Determine expected total comparisons based on current level
	    	long totalComparisons = counter.getTotalExpectedComparisons(toBeCompared);
	        
	        // Progress Display
	        this.comparisons = new JLabel("");
	        listPane.add(this.comparisons);
	        
	        this.compPercentage = new JLabel("");
	        listPane.add(this.compPercentage);
	        
	        this.progressBar = new JProgressBar(0, 100); 
	        this.progressBar.setSize(400, 50);
	        this.progressBar.setStringPainted(true);
	        listPane.add(this.progressBar,BorderLayout.PAGE_END);
	        
	        listPane.add(Box.createRigidArea(new Dimension(0,15)));
	      
	        // Control Data
	        this.nodesOnCurrentLevel = new JLabel("");
	        listPane.add(this.nodesOnCurrentLevel);	
	        
	        this.comparisonsOnCurrentLevel = new JLabel("");
	        listPane.add(this.comparisonsOnCurrentLevel);
	        
	        this.cycle = new JLabel("");
	        listPane.add(this.cycle);
	        
	        this.mergedNodes = new JLabel("");
	        listPane.add(this.mergedNodes);
	        
	        listPane.add(Box.createRigidArea(new Dimension(0,15)));
	        
	        // Time
	        this.avCompTime = new JLabel("");
	        listPane.add(this.avCompTime);
	        
	        this.nodeCompPerSecond = new JLabel("");
	        listPane.add(this.nodeCompPerSecond );
	        		
	        this.elapsedTime = new JLabel("");
	        listPane.add(this.elapsedTime);
	        
	        this.expectedTime = new JLabel("");
	        listPane.add(this.expectedTime);

	        //Display the window.
	        this.frame.pack();
	        this.frame.setVisible(true);
	    }
	    
	    /**
	     * Update the data in the GUI
	     */
	    public void update(Counter counter) {
	    	
	    	// Determine open Nodes
	    	long toBeCompared = counter.getTotalOpenNodes();
	    	
	    	// Determine already compared
	    	long alreadyCompared = counter.getTotalComparisons();
	    	
	    	// Determine expected total comparisons based on current level
	    	long totalExpectedComparisons = counter.getTotalExpectedComparisons(toBeCompared) + alreadyCompared;
	    	
	    	// Update Fast Display
	    	this.comparisons.setText("Progress: " + alreadyCompared + " / " + totalExpectedComparisons);
	    	
	    	// Threshold for updates, otherwise unreadable
	    	if (alreadyCompared % 100 == 0) {
		    	
		    	// Calculate Percentage
		    	float percentage = ((float)alreadyCompared / (float) totalExpectedComparisons) * 100;
		    	
		    	// Calculate average comparison time
		    	long elapsedTime = counter.getElapsedTime();
		    	float averageCompTime = (float) elapsedTime / (float) alreadyCompared;
		    	
		    	// Calculate expected total time and split
		    	float totalExpectedTime = averageCompTime * (float)totalExpectedComparisons;
		    	float rest = 0;
		    	float expectedYears = totalExpectedTime / (365*24*60*60); rest = totalExpectedTime % (365*24*60*60);
				float expectedDays = rest / (24*60*60); rest = rest % (24*60*60);
				float expectedHours = rest / (60*60); rest = rest % (60*60);
				float expectedMinutes = rest / 60; rest = rest % 60;
				float expectedSeconds = rest;
				
				String expectedTimeText = "Expected Run Time: " + (int)expectedYears + " Years / " + (int)expectedDays
				+ " Days / " + (int)expectedHours + " Hours / " + (int)expectedMinutes + " Minutes / " + (int)expectedSeconds + " Seconds";
		    	
		    	// Update Slow Display
		    	this.compPercentage.setText("Progress (%): " + percentage + "%");
		    	this.avCompTime.setText("Av. Comparison Time: " + averageCompTime);
		    	this.nodeCompPerSecond.setText("Nodes Compared / Second: " + 1/averageCompTime);
		    	this.elapsedTime.setText("Elapsed Time: " + elapsedTime);
		    	this.expectedTime.setText(expectedTimeText);
		    	this.cycle.setText("Cycle: " + counter.getCycleCount());
		    	this.mergedNodes.setText("Merged Nodes: " + counter.getTotalMergedNodes());
		        this.progressBar.setValue((int)percentage);
		        this.nodesOnCurrentLevel.setText("Total Nodes on curr. level: " + counter.getTotalOpenNodes());
		        this.comparisonsOnCurrentLevel.setText("Total comparisons on curr. level: "  + counter.getTotalExpectedComparisonsLvl(toBeCompared));
	    	}
	        this.frame.repaint();
	    }

	}
