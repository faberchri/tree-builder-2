package ch.uzh.agglorecommender.visu;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
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
       
    	@SuppressWarnings("unchecked")
		final VisualizationViewer<INode,Integer> vv = (VisualizationViewer<INode,Integer>)e.getSource();
        final Point2D ivp = e.getPoint();

        GraphElementAccessor<INode,Integer> pickSupport = vv.getPickSupport();
        
        if(pickSupport != null) {

            final INode pickedNode = pickSupport.getVertex(vv.getGraphLayout(), ivp.getX(), ivp.getY());

            if(pickedNode != null) {
            	
            	// Differentiate Types for specific display
            	String description = "<html><head><style>td { width:40px; border:1px solid black; text-align: center; }</style></head>" +
						 "<body> Node: " + pickedNode.getId() + "<br><table><tr>";
            	
            	if(pickedNode.getAttributesType() == "Classit") {
            		
            		// Header
            		description += "<td>attr</td><td>mean</td><td>std</td></tr>";
            		
            		// Data
            		Set<INode> attributeKeys = pickedNode.getAttributeKeys();
                	for(INode attributeKey : attributeKeys) {
                		
                		IAttribute AttributeValue = pickedNode.getAttributeValue(attributeKey);
                		description += "<tr><td>" + attributeKey.getId() + "</td>" +
                				"<td>" + (double)Math.round((AttributeValue.getSumOfRatings()/AttributeValue.getSupport())* 100) / 100 + "</td>" +
                				"<td>" + AttributeValue.getStd() + "</td></tr>";
                		
                	}
            		
            	}
            	
            	else if(pickedNode.getAttributesType() == "Cobweb"){
            		
            		// Header
            		description += "<td>attr</td><td>value</td><td>probability</td></tr>";
            		
            		// Data
            		Set<INode> attributeKeys = pickedNode.getAttributeKeys();
                	for(INode attributeKey : attributeKeys) {
                		
                		IAttribute AttributeValue = pickedNode.getAttributeValue(attributeKey);
                		description += "<tr><td>" + attributeKey.getId() + "</td>";
                		
                		Iterator<Entry<Object,Double>> values = AttributeValue.getProbabilities();
                		 while ( values.hasNext() ){
                		
                				description += "<td>" + values.next().toString() + "</td>" +
                				"<td>" + "AttributeValue.getStd()" + "</td></tr>";
                		
                		 }
                	}
            	}
            	
            	
						 
            	
            	
	
            	description += "</table></body></html>";
            	
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
