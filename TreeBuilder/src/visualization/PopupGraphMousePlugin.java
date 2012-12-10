package visualization;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * a GraphMousePlugin that offers popup
 * menu support
 */
class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin
implements MouseListener {

    public PopupGraphMousePlugin() {
        this(MouseEvent.BUTTON3_MASK);
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
       
    	final VisualizationViewer<String,Integer> vv =
                (VisualizationViewer<String,Integer>)e.getSource();
        final Point2D p = e.getPoint();
        final Point2D ivp = p;

        GraphElementAccessor<String,Integer> pickSupport = vv.getPickSupport(); // Interesting
        if(pickSupport != null) {

            final String vertexID = pickSupport.getVertex(vv.getGraphLayout(), ivp.getX(), ivp.getY()); // Interesting

            if(vertexID != null) {
            	
            	// Get Data for this vertexID
            	// Implement
            	String description = "Vertex: " + vertexID;
            	
            	// Create Description Label
            	JLabel label = new JLabel();
            	label.setHorizontalAlignment(JLabel.CENTER);
                label.setForeground(Color.blue);
                label.setText(description);
            	
                // Create Popup that displays label
            	JPopupMenu popup = new JPopupMenu();
            	 popup.add(label);
            	 
                if(popup.getComponentCount() > 0) {
                  popup.show(vv, e.getX(), e.getY());
                }                
           }
       }
    }
}