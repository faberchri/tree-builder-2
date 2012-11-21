 package visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.functors.ConstantTransformer;

import clusterer.Node;
import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.LayoutTransition;
import edu.uci.ics.jung.visualization.util.Animator;
//import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
//import edu.uci.ics.jung.graph.DelegateForest;
//import edu.uci.ics.jung.graph.DelegateTree;


@SuppressWarnings("serial")
public class VisualizationBuilder extends JApplet {
	
	private Set<Node> rootNodes = new HashSet<Node>();

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
	    
	    VisualizationServer.Paintable rings;
	    
	    String root;
	    
	    TreeLayout<String,Integer> treeLayout;
	    
	    RadialTreeLayout<String,Integer> radialLayout;

		public VisualizationBuilder(Set<Node> movieNodes, Set<Node> userNodes) {

	        
	        // create a simple graph for the demo
	        graph = new DelegateForest<String,Integer>();

	        createTree(movieNodes,userNodes);
	        
	        treeLayout = new TreeLayout<String,Integer>(graph);
	        radialLayout = new RadialTreeLayout<String,Integer>(graph);
	        radialLayout.setSize(new Dimension(600,600));
	        vv =  new VisualizationViewer<String,Integer>(treeLayout, new Dimension(600,600));
	        vv.setBackground(Color.white);
	        vv.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line());
	        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
	        // add a listener for ToolTips
	        vv.setVertexToolTipTransformer(new ToStringLabeller());
	        vv.getRenderContext().setArrowFillPaintTransformer(new ConstantTransformer(Color.lightGray));
	        rings = new Rings();

	        Container content = getContentPane();
	        final GraphZoomScrollPane panel = new GraphZoomScrollPane(vv);
	        content.add(panel);
	        
	        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();

	        vv.setGraphMouse(graphMouse);
	        
	        JComboBox modeBox = graphMouse.getModeComboBox();
	        modeBox.addItemListener(graphMouse.getModeListener());
	        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

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
	        
	        JToggleButton radial = new JToggleButton("Radial");
	        radial.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						
						LayoutTransition<String,Integer> lt =
							new LayoutTransition<String,Integer>(vv, treeLayout, radialLayout);
						Animator animator = new Animator(lt);
						animator.start();
						vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
						vv.addPreRenderPaintable(rings);
					} else {
						LayoutTransition<String,Integer> lt =
							new LayoutTransition<String,Integer>(vv, radialLayout, treeLayout);
						Animator animator = new Animator(lt);
						animator.start();
						vv.getRenderContext().getMultiLayerTransformer().setToIdentity();
						vv.removePreRenderPaintable(rings);
					}
					vv.repaint();
				}});

	        JPanel scaleGrid = new JPanel(new GridLayout(1,0));
	        scaleGrid.setBorder(BorderFactory.createTitledBorder("Zoom"));

	        JPanel controls = new JPanel();
	        scaleGrid.add(plus);
	        scaleGrid.add(minus);
	        controls.add(radial);
	        controls.add(scaleGrid);
	        controls.add(modeBox);

	        content.add(controls, BorderLayout.SOUTH);
	    }
	    
	    class Rings implements VisualizationServer.Paintable {
	    	
	    	Collection<Double> depths;
	    	
	    	public Rings() {
	    		depths = getDepths();
	    	}
	    	
	    	private Collection<Double> getDepths() {
	    		Set<Double> depths = new HashSet<Double>();
	    		Map<String,PolarPoint> polarLocations = radialLayout.getPolarLocations();
	    		for(String v : graph.getVertices()) {
	    			PolarPoint pp = polarLocations.get(v);
	    			depths.add(pp.getRadius());
	    		}
	    		return depths;
	    	}

			public void paint(Graphics g) {
				g.setColor(Color.lightGray);
			
				Graphics2D g2d = (Graphics2D)g;
				Point2D center = radialLayout.getCenter();

				Ellipse2D ellipse = new Ellipse2D.Double();
				for(double d : depths) {
					ellipse.setFrameFromDiagonal(center.getX()-d, center.getY()-d, 
							center.getX()+d, center.getY()+d);
					Shape shape = vv.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT).transform(ellipse);
					g2d.draw(shape);
				}
			}

			public boolean useTransform() {
				return true;
			}
	    }
	    
	    /**
	     * @param userNodes 
	     * @param rootNodes 
	     * 
	     */
	    private void createTree(Set<Node> movieNodes, Set<Node> userNodes) {
		 	
		 	// Build movie Nodes
	    	String prefix = "movie_";
			for (Node movieNode : movieNodes) {
				
				// Create first Element of Tree
				String firstID = String.valueOf(movieNode.getId());
				graph.addVertex(prefix.concat(firstID));
				
				// Start recursive build of Tree
			 	processChildren(movieNode,prefix);
            }
			
		 	// Build user Nodes
	    	prefix = "user_";
			for (Node userNode : userNodes) {
				
				// Create first Element of Tree
				String firstID = String.valueOf(userNode.getId());
				graph.addVertex(firstID);
				
				// Start recursive build of Tree
			 	processChildren(userNode,prefix);
            }
	       	
	    }
	    
	    private void processChildren(Node parent, String prefix) {
	    	
	    	//System.out.println("processing visualization...");
		 	
	    	String parentID = prefix.concat(String.valueOf(parent.getId()));
	    	Set<Node>children = parent.getChildrenSet();
	    	
	    	if(children != null && children.size() > 0){
		       	for (Node child : children) {
	        		String childID = prefix.concat(String.valueOf(child.getId()));
	        		graph.addEdge(edgeFactory.create(),parentID,childID);
	        		processChildren(child,prefix);
	        	}
	    	}
	    }
}
