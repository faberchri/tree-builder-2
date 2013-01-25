package ch.uzh.agglorecommender.visu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Tree;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.PersistentLayout;
import edu.uci.ics.jung.visualization.layout.PersistentLayoutImpl;

public class Dendrogram {
	
	Forest<INode, String> forest = new DelegateForest<INode, String>();


    /**
     * the name of the file where the layout is saved
     */
    String fileName;

    /**
     * the visual component and renderer for the graph
     */
    VisualizationViewer<INode,String> vv;
    
    PersistentLayout<INode,String> persistentLayout;

    /**
     * create an instance of a simple graph with controls to
     * demo the persistence and zoom features.
     * 
     * @param fileName where to save/restore the graph positions
     */
    public Dendrogram(final String fileName) {
        this.fileName = fileName;
        
        // create a simple graph for the demo
        persistentLayout = 
            new PersistentLayoutImpl<INode,String>(new FRLayout<INode,String>(forest));

        vv = new VisualizationViewer<INode,String>(persistentLayout);
        
        // add my listener for ToolTips
        vv.setVertexToolTipTransformer(new ToStringLabeller());
        DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
        vv.setGraphMouse(gm);
        
        // create a frame to hold the graph
        final JFrame frame = new JFrame();
        frame.getContentPane().add(new GraphZoomScrollPane(vv));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // create a control panel and buttons for demo
        // functions
        JPanel p = new JPanel();
        
        JButton persist = new JButton("Save Layout");
        // saves the graph vertex positions to a file
        persist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    persistentLayout.persist(fileName);
                } catch (IOException e1) {
                    System.err.println("got "+e1);
            	}
            }
        });
        p.add(persist);

        JButton restore = new JButton("Restore Layout");
        // restores the graph vertex positions from a file
        // if new vertices were added since the last 'persist',
        // they will be placed at random locations
        restore.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                PersistentLayout<String,Number> pl = (PersistentLayout<String,Number>) vv.getGraphLayout();
                try {
                    persistentLayout.restore(fileName);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        p.add(restore);
        p.add(gm.getModeComboBox());

        frame.getContentPane().add(p, BorderLayout.SOUTH);
        frame.pack();//setSize(600, 600);
        frame.setVisible(true);
    }
	
	private void init() {
		// TODO Auto-generated method stub
		// register an observable
		

		
	}
	
	private void createForest() {
		// TODO Auto-generated method stub

	}
	
	private void update(INode newNode) {
		
	}
	
	private void notify(Set<INode> message, INode newNode) {
		Iterator<INode> children = newNode.getChildren();
		List<Tree<INode, String>> selectedTrees = new ArrayList<Tree<INode, String>>();
		while(children.hasNext()) {
			INode child = children.next();
			Iterator<Tree<INode, String>> trees = forest.getTrees().iterator();
			while(trees.hasNext()) {
				Tree<INode, String> tree = trees.next();
				if(tree.containsVertex(child)) {
					forest.removeVertex(child);
				}
			}
		}
//		forest.addTree(createTree(newNode));
		Tree<INode, String> t = createTree(newNode);
		forest.addVertex(t.getRoot());

//		forest.addVertex(createTree(newNode).getRoot());
	}
	
	private void notify(Set<INode> message) {
		for (INode iNode : message) {
			Tree<INode, String> t = createTree(iNode);
			forest.addVertex(t.getRoot());
//			forest.addTree(t);
		}
	}
	
	private Tree<INode, String> createTree(INode root) {
		DirectedGraph<INode, String> g = createGraph(root, new DirectedSparseGraph<INode, String>());
		DelegateTree<INode, String> tree = new DelegateTree<INode, String>(g);
		tree.setRoot(root);
		return tree;
	}
	
	private DirectedGraph<INode, String> createGraph(INode node, DirectedGraph<INode, String> graph) {
		if(node.isLeaf()){
			graph.addVertex(node);
		} else {
			graph.addVertex(node);
			Iterator<INode> children = node.getChildren();
			
			while(children.hasNext()) {
				INode child = children.next();
				graph.addVertex(child);
				graph.addEdge(Integer.toString(edgeCounter++), child, node);
				createGraph(child, graph);
			}
		}
		return graph;
	}
	int edgeCounter = 0;

    /**
     * a driver for this demo
     * @param args should hold the filename for the persistence demo
     */
    public static void main(String[] args) {
        String filename;
        if (args.length >= 1)
            filename = args[0];
        else
            filename = "/Users/faber/TreeBuilder_logs/PersistentLayoutDemo.out";
        Dendrogram plot = new Dendrogram(filename);
        Set<INode> set = new HashSet<INode>();
        set.add(new Node(ENodeType.User));
        set.add(new Node(ENodeType.User));
        set.add(new Node(ENodeType.User));
        set.add(new Node(ENodeType.User));
        INode n1 = new Node(ENodeType.User);
        INode n2 = new Node(ENodeType.User);
        set.add(n1);
        set.add(n2);
        plot.notify(set);
        
        INode n3 = new Node(ENodeType.User);
        n3.addChild(n1);
        n3.addChild(n2);
        n1.setParent(n3);
        n2.setParent(n3);
        set.remove(n1);
        set.remove(n2);
        set.add(n3);
        plot.notify(set, n3);
        
        System.out.println("done");
    }
}
