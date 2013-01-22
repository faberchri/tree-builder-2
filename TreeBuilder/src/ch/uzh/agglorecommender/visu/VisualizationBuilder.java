package ch.uzh.agglorecommender.visu;

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

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
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
	     * Factories for the graph, tree, edges and vertices
	     */
	    Forest<INode,Integer> graph;

	    Factory<DirectedGraph<INode,Integer>> graphFactory = 
	    	new Factory<DirectedGraph<INode,Integer>>() {

				public DirectedGraph<INode, Integer> create() {
					return new DirectedSparseMultigraph<INode,Integer>();
				}
			};

		Factory<Tree<INode,Integer>> treeFactory =
			new Factory<Tree<INode,Integer>> () {

			public Tree<INode, Integer> create() {
				return new DelegateTree<INode,Integer>(graphFactory);
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
	    VisualizationViewer<INode,Integer> vv;
	    String root;
	    TreeLayout<INode,Integer> treeLayout;

		public VisualizationBuilder(Set<INode> movieNodes, Set<INode> userNodes) {

	        // Create the graph
	        graph = new DelegateForest<INode,Integer>();
	        createTree(movieNodes,userNodes);

	        // Define Layout
	        treeLayout 		= new TreeLayout<INode,Integer>(graph);

	        // Define Visualization Viewer, add a listener for ToolTips
	        vv =  new VisualizationViewer<INode,Integer>(treeLayout, new Dimension(600,600));
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
	        graphMouse.add(new PopupGraphMousePlugin()); // Integration of mouse listener functionality to show attributes of vertex
	        
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

	    	// Build movie Nodes recursively
			for (INode movieNode : movieNodes) {
			 	processChildren(movieNode);
            }

		 	// Build user Nodes recursively
			for (INode userNode : userNodes) {
			 	processChildren(userNode);
            }

	    }

	    private void processChildren(INode parent) {
	    	
	    	Iterator<INode> iter = parent.getChildren();
	    	
	    	// Process every child
	    	while (iter.hasNext()) {
	    		
	    		// Build edge between parent and child, build subtree recursively
	    		INode child = (INode) iter.next();
        		graph.addEdge(edgeFactory.create(),parent,child);
        		processChildren(child);
	    	}
	    }
}