package ch.uzh.agglorecommender.recommender.ui.extensions;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.ui.BasicUI;

public class WebExtension extends AbstractHandler {
	
	private final BasicUI basicUI;
	private final Server server;

	public WebExtension(BasicUI basicUI) throws Exception {
	    
		this.basicUI = basicUI;
			
		server = new Server(8081); // Port number should not be changed
	
	    ResourceHandler resourceHandler = new ResourceHandler();
	    resourceHandler.setDirectoriesListed(true);
	    resourceHandler.setWelcomeFiles(new String[] {"index.html"});
	    resourceHandler.setResourceBase("./src/ch/uzh/agglorecommender/recommender/ui/extensions/"); // FIXME Error here
	
	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] {resourceHandler, new DefaultHandler()});
	
	    server.setHandler(handlers);
	  }
	
	public void startService() throws Exception{
	    server.start();
	    server.join();
	}
	
	public void stopService() throws Exception {
		this.server.stop();
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {
   
		// Retrieve Type
		ENodeType type = ENodeType.valueOf(request.getParameter("type"));
		
		// Read MetaInfo to List<String>
		List<String> metaInfo = new LinkedList();
		// FIXME Implement
		
		// Read Ratings to List<String>
		List<String> ratings = new LinkedList();
		// FIXME Implement
		
		INode inputNode = basicUI.buildNode(metaInfo, ratings, type);
		
		// Create Recommendation
		Map<INode,IAttribute> recommendation = null;
		if(request.getParameter("method") == "recommendation"){	
			recommendation = basicUI.recommend(inputNode);	
		}
		// Write Insertion
		else if(request.getParameter("method") == "insertion"){
			basicUI.insert(inputNode);
		}
		    
		response.setContentType("text/text");  // text/xml
	    response.setHeader("Cache-Control", "no-cache");
	    response.setStatus(HttpServletResponse.SC_OK);
	    
	    // Create Response
	    response.getWriter().write("<message>" + recommendation.toString() + "</message>");
	    
	    response.flushBuffer();
	    baseRequest.setHandled(true);
  }
}
