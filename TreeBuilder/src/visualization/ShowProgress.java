package visualization;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import clusterer.Counter;

/*
 * Shows progress of comparisons on current level
 */
public class ShowProgress {
	
		private JFrame frame;
		private JLabel numbers;
		private JProgressBar progressBar;

	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    public void start() {
	        //Create and set up the window.
	        this.frame = new JFrame("ShowProgress");
	        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.frame.setSize(200, 100);

	        // Add text
	        this.numbers = new JLabel("0 / 0");
	        this.frame.getContentPane().add(this.numbers);
	        
	        // Add statusbar
	        this.progressBar = new JProgressBar(0, 600000);
	        this.progressBar.setValue(0);
	        this.progressBar.setStringPainted(true);
	        this.frame.getContentPane().add(this.progressBar);

	        //Display the window.
	        this.frame.pack();
	        this.frame.setVisible(true);
	    }
	    
	    public void update(Counter counter) {
	    	
	    	// Determine openNodes
	    	int toBeCompared = 0;
	    	toBeCompared += counter.getOpenMovieNodeCount();
	    	toBeCompared += counter.getOpenUserNodeCount();
	    	
	    	System.out.println("toBeCompared: " + toBeCompared);
	    	
	    	// Determine already compared
	    	int alreadyCompared = counter.getTotalComparisonsOnCurrentLevel();
	    	
	    	// Determine future comparison
	    	int totalComparisons = toBeCompared * (toBeCompared - 1); // Noch fehlerhaft
	    	
	    	// Calculate Percentage
	    	float value = (alreadyCompared / totalComparisons) * 100;
	    	
	    	// Update Frame
	    	this.numbers.setText(alreadyCompared + " / " + totalComparisons);
	        this.progressBar.setValue(alreadyCompared);
	        this.frame.repaint();
	    }

	}
