package ch.uzh.agglorecommender.clusterer;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import ch.uzh.agglorecommender.clusterer.treesearch.ClusterSet;
import ch.uzh.agglorecommender.clusterer.treesearch.IClusterSet;
import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableSet;

/**
 * 
 * Ensures a balanced clustering of the two sets passed to the constructor.
 *
 * @param <T> the item stored in the set
 */
public class ClusteringBalancer<T> implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The set of all root nodes of type user.
	 */
	private final IClusterSet<T> userNodes;

	/**
	 * Saves the length of the userNodes set at each cluster cycle iteration.
	 */
	private  List<Integer> userSizeList = new ArrayList<Integer>();

	/**
	 * The set of all root nodes of type content.
	 */
	private final IClusterSet<T> contentNodes;

	/**
	 * Saves the length of the contentNodes set at each cluster cycle iteration.
	 */
	private  List<Integer> contentSizeList = new ArrayList<Integer>();

	/**
	 * Creates a new ClusteringBalancer for the passed sets.
	 * 
	 * @param setA one of the sets to cluster
	 * @param setB the other set to cluster
	 */
	public ClusteringBalancer(IClusterSet<T> setA, IClusterSet<T> setB) {
		this.userNodes = setA;
		this.contentNodes = setB;
		updateSizeLists();
	}

	/**
	 * Adds the current size of the open sets to the length lists.
	 */
	private void updateSizeLists() {
		userSizeList.add(userNodes.size());
		contentSizeList.add(contentNodes.size());
	}

	/**
	 * Estimates the number of remaining needed cluster cycles to reduce 
	 * the set corresponding to the passed list to size == 1 based on the 
	 * size reduction of the previous cycles.
	 * 
	 * @param lengthList the list with all size changes per cycle for
	 * a corresponding open nodes set.
	 * @return an estimate of the remaining needed cycles.
	 */
	private long getProjectedNumberOfRemainigCycles(List<Integer> lengthList) {
		if (lengthList.size() < 2) {
			Logger log = TBLogger.getLogger(this.getClass().getName());
			log.severe("Length list has size < 2 " + lengthList);
			System.exit(-1);
		}

		double avgReduction = 0;
		int prevLength = lengthList.get(0);
		for (int i = 1; i < lengthList.size(); i++) {
			int length = lengthList.get(i);
			avgReduction += length - prevLength;
			prevLength = length;
		}

		avgReduction /= lengthList.size() - 1;

		if (avgReduction == 0.0) return Long.MAX_VALUE; // does only apply to the set that
		// was not selected in the first round 

		int currentLength = lengthList.get(lengthList.size() - 1);

		if (currentLength < 2) return 0;

		long res = Math.round((1.0 - (double) currentLength) / avgReduction );
		if (res < 1) res = 1;
		return res;
	}

	/**
	 * Gets one of the two sets passed to the constructor that should be clustered next,
	 * in order to ensure a balanced clustering progress of both sets / trees.
	 * 
	 * @return the set that should be cluster next or null if both sets have size <= 1.
	 */
	public IClusterSet<T> getNextClusterSet() {
		updateSizeLists();
		long userProjection = getProjectedNumberOfRemainigCycles(userSizeList);
		long contentProjection = getProjectedNumberOfRemainigCycles(contentSizeList);
		if (userNodes.size() == 0 && contentNodes.size() == 0) return null;
		if (userNodes.size() == 0) return contentNodes;
		if (contentNodes.size() == 0) return userNodes;
		if (userProjection == 0 && contentProjection == 0) return null;
		if (userProjection == contentProjection) {
			Random r = new Random();
			boolean b = r.nextBoolean();
			IClusterSet<T> res = b ? userNodes : contentNodes;
			return res;
		}
		if (userProjection < contentProjection) return contentNodes;
		return userNodes;
	}


	///////////////////////////////////////////////////////
	///					 Test section 					///
	///////////////////////////////////////////////////////

	public static void main(String[] args) {
		Set<Integer> leaves1 = new HashSet<Integer>();
		for (int i = 0; i < 5; i++) {
			leaves1.add(10000 + i);
		}
		
		IClusterSet<Integer> sU = new ClusterSet<Integer>(ImmutableSet.copyOf(leaves1));
		
		leaves1.clear();
		for (int i = 0; i < 10; i++) {
			leaves1.add(i);
		}
		IClusterSet<Integer> sC = new ClusterSet<Integer>(ImmutableSet.copyOf(leaves1));

		ClusteringBalancer<Integer> cB = new ClusteringBalancer<Integer>(sU, sC);
		int cycleC = 0;
		Random r = new Random();
		final XYSeries userSetSize= new XYSeries("user set size");
		final XYSeries contentSetSize= new XYSeries("content set size");
		userSetSize.add(cycleC, cB.userNodes.size());
		contentSetSize.add(cycleC, cB.contentNodes.size());
		while (true) {
			System.out.print("cycle: " + cycleC++ + ", ");
			IClusterSet<Integer> selectedSet = cB.getNextClusterSet();

			if (selectedSet == null) {
				System.out.println("null");
				break;
			}

			System.out.print("set size: " + selectedSet.size() + ", ");
			int rI = r.nextInt(1) + 1;
//			Iterator<Integer> i = selectedSet.getSetView().iterator();
			while (! selectedSet.clusteringDone() && rI > -1) {
//				i.next();
//				i.remove();
				selectedSet.remove(selectedSet.getAnyElement());
				rI--;
			}
			if (! selectedSet.clusteringDone()) {
				selectedSet.add(new Integer(-1 * cycleC));				
			}

			userSetSize.add(cycleC, cB.userNodes.size());
			contentSetSize.add(cycleC, cB.contentNodes.size());

			if (selectedSet == sU) {
				System.out.println("user");
			}
			if (selectedSet == sC) {
				System.out.println("content");
			}
		}
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(userSetSize);
		dataset.addSeries(contentSetSize);
		MyPlot p = new MyPlot("Set sizes", dataset);

		p.pack();
		RefineryUtilities.centerFrameOnScreen(p);
		p.setVisible(true);

	}

	private static class MyPlot extends ApplicationFrame {

		/**
		 * Creates a new demo.
		 *
		 * @param title  the frame title.
		 */
		public MyPlot(final String title, final XYDataset ds) {

			super(title);

			final JFreeChart chart = createChart(ds);
			final ChartPanel chartPanel = new ChartPanel(chart);
			chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			setContentPane(chartPanel);

		}

		/**
		 * Creates a chart.
		 * 
		 * @param dataset  the data for the chart.
		 * 
		 * @return a chart.
		 */
		private JFreeChart createChart(final XYDataset dataset) {

			// create the chart...
			final JFreeChart chart = ChartFactory.createXYLineChart(
					"Set sizes vs. Cluster cycles ",      // chart title
					"Cycles",                      // x axis label
					"Set size",                      // y axis label
					dataset,                  // data
					PlotOrientation.VERTICAL,
					true,                     // include legend
					true,                     // tooltips
					false                     // urls
					);

			// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
			chart.setBackgroundPaint(Color.white);

			//	        final StandardLegend legend = (StandardLegend) chart.getLegend();
			//      legend.setDisplaySeriesShapes(true);

			// get a reference to the plot for further customisation...
			final XYPlot plot = chart.getXYPlot();
			plot.setBackgroundPaint(Color.lightGray);
			//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
			plot.setDomainGridlinePaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);

			final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
			renderer.setSeriesLinesVisible(0, false);
			renderer.setSeriesShapesVisible(1, false);
			plot.setRenderer(renderer);

			// change the auto tick unit selection to integer units only...
			final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
			// OPTIONAL CUSTOMISATION COMPLETED.

			return chart;

		}

	}
}