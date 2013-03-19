package ch.uzh.agglorecommender.visu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * a GraphMousePlugin that offers popup
 * menu support
 */
class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
implements MouseListener {

	public PopupGraphMousePlugin() {
		this(MouseEvent.BUTTON3_DOWN_MASK);
	}
	public PopupGraphMousePlugin(int modifiers) {
		super(modifiers);
	}
	
	/**
	 * If this event is over a station (vertex), pop up a menu to
	 * allow the user to perform a few actions; else, pop up a menu over the layout/canvas
	 *
	 * @param e
	 */
	protected void handlePopup(MouseEvent e) {

		@SuppressWarnings("unchecked")
		final VisualizationViewer<INode,Integer> vv = (VisualizationViewer<INode,Integer>)e.getSource();
		final Point2D ivp = e.getPoint();

		GraphElementAccessor<INode,Integer> pickSupport = vv.getPickSupport();

		if(pickSupport != null) {

			final Object pickedO = pickSupport.getVertex(vv.getGraphLayout(), ivp.getX(), ivp.getY());
			INode pickedNode;
			if (pickedO instanceof Graph) {
				Graph g = ((Graph) pickedO);
				pickedNode = (INode) g.getVertices().iterator().next();
				while (true) {
					INode parent = pickedNode.getParent();
					if (parent != null && g.containsVertex(parent)) {
						pickedNode = parent;
					} else {
						break;
					}
				}
			} else {
				pickedNode = (INode) pickedO;
			}

			if(pickedNode != null) {

				String description = pickedNode.getAttributeHTMLLabelString();

				JSplitPane splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
				splitPanel.setOneTouchExpandable(true);
				
				JTree clusterStruc = pickedNode.getJTreeOfSubtree();
				clusterStruc.setBorder(BorderFactory.createTitledBorder("Cluster content"));
				splitPanel.setTopComponent(new JScrollPane(clusterStruc));
				
				// Create Description Label
				JLabel label = new JLabel();
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setForeground(Color.blue);
				label.setText(description);
				label.setBorder(BorderFactory.createTitledBorder("Cluster attributes"));
				splitPanel.setBottomComponent(new JScrollPane(label));
				
				// Create dialog that displays label                
				JDialog dia = new JDialog((Frame) SwingUtilities.windowForComponent(e.getComponent()), ("Cluster " + pickedNode.getId()), false);
						
				dia.add(splitPanel);
						
				dia.pack();
				dia.setSize(new Dimension(400, 400));
				dia.setLocation(e.getXOnScreen() + 20, e.getYOnScreen() + 20);
				dia.setVisible(true);
				splitPanel.setDividerLocation(0.5);
			}
		}
	}
}
