package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import clusterer.INode;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;




@SuppressWarnings("serial")
public class VisualizationBuilder extends JApplet {

	private Set<INode> rootNodes = new HashSet<INode>();

	    /**
	     * the graph
	     */
	    Forest<String,Integer> graph;

	    Factory<DirectedGraph<String,Integer>> graphFactory = 
	    	new Factory<DirectedGraph<String,Integer>>() {

				public DirectedGraph<String, Integer> create() {
					return new DirectedSparseMultigraph<String,Integer>();
				}
			};

		Factory<Tree<String,Integer>> treeFactory =
			new Factory<Tree<String,Integer>> () {

			public Tree<String, Integer> create() {
				return new DelegateTree<String,Integer>(graphFactory);
			}
		};

		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int i=0;
			public Integer create() {
				return i++;
			}};

	    Factory<String> vertexFactory = new Factory<String>() {
	    	int i=0;
			public String create() {
				return "V"+i++;
			}};

	    /**
	     * the visual component and renderer for the graph
	     */
	    VisualizationViewer<String,Integer> vv;
	    String root;
	    TreeLayout<String,Integer> treeLayout;

		public VisualizationBuilder(Set<INode> movieNodes, Set<INode> userNodes) {

	        // Create the graph
	        graph = new DelegateForest<String,Integer>();
	        createTree(movieNodes,userNodes);

	        // Define Layout
	        treeLayout 		= new TreeLayout<String,Integer>(graph);

	        // Define Visualization Viewer, add a listener for ToolTips
	        vv =  new VisualizationViewer<String,Integer>(treeLayout, new Dimension(600,600));
	        vv.setBackground(Color.white);
	        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
	        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	        vv.setVertexToolTipTransformer(new ToStringLabeller());
	        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));

	        // Add Elements to Visualization Viewer
	        Container content = getContentPane();
	        
	        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
	        content.add(panel);
	        
	        // Create Mouse Listener class
	        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
	        vv.setGraphMouse(graphMouse);
	        graphMouse.add(new PopupGraphMousePlugin()); // Integration of add. mouse functionality to show description of vertex
	        
	        // Zooming Functions
	        final ScalingControl scaler = new CrossoverScalingControl();

	        JButton plus = new JButton("+");
	        plus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1.1f, vv.getCenter());
	            }
	        });
	        JButton minus = new JButton("-");
	        minus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1/1.1f, vv.getCenter());
	            }
	        });

	        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
	        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

	        JPanel controls = new JPanel();
	        scaleGrid.add(plus);
	        scaleGrid.add(minus);
	        controls.add(scaleGrid);

	        content.add(controls, BorderLayout.SOUTH);
	    }

	    /**
	     * @param userNodes 
	     * @param rootNodes 
	     * 
	     */
	    private void createTree(Set<INode> movieNodes, Set<INode> userNodes) {

	    	// Build movie Nodes
	    	String prefix = "movie_";
			for (INode movieNode : movieNodes) {

				// Start recursive build of Tree
			 	processChildren(movieNode,prefix);
            }

		 	// Build user Nodes
	    	prefix = "user_";
			for (INode userNode : userNodes) {

				// Start recursive build of Tree
			 	processChildren(userNode,prefix);
            }

	    }

	    private void processChildren(INode parent, String prefix) {

	    	String parentID = prefix.concat(String.valueOf(parent.getId()));
	    	Iterator<INode> iter = parent.getChildren();
	    	
	    	while (iter.hasNext()) {
	    		
	    		// Define Name of Item
	    		INode child = (INode) iter.next();
        		String childID = prefix.concat(String.valueOf(child.getId()));
        		
        		// Add Item to Tree
        		String Description = ""; // Implement
        		graph.addEdge(edgeFactory.create(),parentID,childID);
        		
        		// Build child items recursively
        		processChildren(child,prefix);
	    	}
	    }
}