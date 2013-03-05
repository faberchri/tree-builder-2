package ch.uzh.agglorecommender.recommender.ui;

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
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class WebExtension extends AbstractHandler {
	
	private final BasicUI basicUI;
	private final Server server;

	public WebExtension(BasicUI basicUI) {
	    
		this.basicUI = basicUI;
			
		this.server = new Server(8081); // Port number should not be changed
	
	    ResourceHandler resource_handler = new ResourceHandler();
	    resource_handler.setDirectoriesListed(true);
	    resource_handler.setWelcomeFiles(new String[] { "index.html" });
	    resource_handler.setResourceBase("./src/ch/uzh/agglorecommender/servlet/");
	
	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] { resource_handler, new WebExtension(basicUI)});
	
	    server.setHandler(handlers);
	  }
	
	public void startService() throws Exception{
	    this.server.start();
	    this.server.join();
	}
	
	public void stopService() throws Exception {
		this.server.stop();
	}

	public void handle(String target, Request baseRequest, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {
   
		// Read MetaInfo to List<String>
		List<String> metaInfo = new LinkedList();
		// Read Ratings to List<String>
		List<String> ratings = new LinkedList();
		
		// Create Recommendation
		Map<INode,IAttribute> recommendation = null;
		if(request.getParameter("method") == "recommendation"){	
			recommendation = basicUI.recommendation(metaInfo,ratings);	
		}
		// Write Insertion
		else if(request.getParameter("method") == "insertion"){
			basicUI.insertion(metaInfo,ratings);
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