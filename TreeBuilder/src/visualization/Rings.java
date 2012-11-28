package visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.uci.ics.jung.algorithms.layout.PolarPoint;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;

class Rings implements VisualizationServer.Paintable {

	Collection<Double> depths;
	RadialTreeLayout<String,Integer> radialLayout;
	Forest<String,Integer> graph;
	VisualizationViewer<String,Integer> vv;

	public Rings(RadialTreeLayout<String,Integer> radialLayout,Forest<String,Integer> graph,VisualizationViewer<String,Integer> vv) {
		this.radialLayout = radialLayout;
		this.graph = graph;
		this.vv = vv;
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