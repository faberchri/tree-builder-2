package ch.uzh.agglorecommender.visu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
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

				// Create Description Label
				JLabel label = new JLabel();
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setForeground(Color.blue);
				label.setText(description);

				// Create dialog that displays label                
				JDialog dia = new JDialog((Frame) SwingUtilities.windowForComponent(e.getComponent()), "Node attribute map", false);

				dia.add(new JScrollPane(label));

				dia.pack();
				dia.setSize(new Dimension(label.getWidth() + 20, 400));
				dia.setLocation(e.getXOnScreen() + 20, e.getYOnScreen() + 20);
				dia.setVisible(true);

			}
		}
	}
}
