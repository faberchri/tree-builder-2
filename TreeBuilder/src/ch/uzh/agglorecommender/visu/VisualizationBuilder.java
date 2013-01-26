package ch.uzh.agglorecommender.visu;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.map.LazyMap;

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
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;


//@SuppressWarnings("serial")
public class VisualizationBuilder extends JPanel {

	/**
	 * Factories for the graph, tree, edges and vertices
	 */
	Forest<INode,Integer> graph;

	Factory<DirectedGraph<INode,Integer>> graphFactory = new Factory<DirectedGraph<INode,Integer>>() {
		@Override
		public DirectedGraph<INode, Integer> create() {
			return new DirectedSparseMultigraph<INode,Integer>();
		}
	};

	Factory<Tree<INode,Integer>> treeFactory = new Factory<Tree<INode,Integer>> () {
		@Override
		public Tree<INode, Integer> create() {
			return new DelegateTree<INode,Integer>(graphFactory);
		}
	};

	Factory<Integer> edgeFactory = new Factory<Integer>() {
		int i=0;
		@Override
		public Integer create() {
			return i++;
		}
	};

	Factory<String> vertexFactory = new Factory<String>() {
		int i=0;
		@Override
		public String create() {
			return "V"+i++;
		}
	};

	/**
	 * the visual component and renderer for the graph
	 */
	VisualizationViewer<INode,Integer> vv;
//	String root;
	TreeLayout<INode,Integer> treeLayout;

	public VisualizationBuilder(Set<INode> nodes) {

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// Create the graphs
		graph = new DelegateForest<INode,Integer>();
		createTree(nodes);

		// Define Layout
		treeLayout = new TreeLayout<INode,Integer>(graph, 50, 100){ // x-dir and y-dir spacing between vertices
			@Override
			public void reset() {
				alreadyDone = new HashSet<INode>();
				basePositions = new HashMap<INode, Integer>();
				
				locations =    	LazyMap.decorate(new HashMap<INode, Point2D>(),
		    			new Transformer<INode,Point2D>() {
							public Point2D transform(INode arg0) {
								return new Point2D.Double();
							}});
			}
		};
		
		// Define Visualization Viewer, add a listener for ToolTips
		vv =  new VisualizationViewer<INode,Integer>(treeLayout);

		vv.setBackground(Color.white);
		vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
		vv.setVertexToolTipTransformer(new ToStringLabeller());
		vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.S);
		vv.getRenderContext().setVertexLabelTransformer(new ConstantTransformer(null) {
			@Override
			public Object transform(Object input) {
				
				String s = input.toString();
				s = s.replace("User Node", "Id: ");
				s = s.replace("Content Node", "Id: ");
				return s;
			}
		});

		// Add Elements to Visualization Viewer
		final GraphZoomScrollPane panelC = new GraphZoomScrollPane(vv);
		this.add(panelC);
		panelC.setBorder(BorderFactory.createLoweredBevelBorder());
		
		this.add(panelC);
		this.add(getControlElemnts());
		
		addMouseListenerNodeSelection();
	}

	private void addMouseListenerNodeSelection() {
		// Create Mouse Listener class
		final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
		vv.setGraphMouse(graphMouse);
		graphMouse.add(new PopupGraphMousePlugin()); // Integration of mouse listener functionality to show attributes of vertex
	}

	private JPanel getControlElemnts() {
		// Zooming Functions
		final ScalingControl scaler = new CrossoverScalingControl();

		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1/1.1f, vv.getCenter());
			}
		});
		
		JPanel scaleGrid = new JPanel(new GridLayout(1,0));
		scaleGrid.setBorder(BorderFactory.createTitledBorder(getBorder(), "Zoom", TitledBorder.CENTER, TitledBorder.CENTER));

		JPanel controls = new JPanel();
		scaleGrid.add(plus);
		scaleGrid.add(minus);
		controls.add(scaleGrid);
		return controls;
	}
	
	public void updateGraph(Set<INode> nodes) {
		treeLayout.reset();
		graph = new DelegateForest<INode,Integer>();
		createTree(nodes);
		treeLayout.setGraph(graph);

		vv.repaint();
	}

	/**
	 * @param userNodes 
	 * @param rootNodes 
	 * 
	 */
	private void createTree(Set<INode> nodes) {

		// Build Tree recursively
		for (INode node : nodes) {
			processChildren(node);
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