package ch.uzh.agglorecommender.servlet;

import java.io.IOException;
import java.util.HashMap;
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

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.treeutils.InputReader;
import ch.uzh.agglorecommender.recommender.treeutils.NodeInserter;

public class RecommendationServlet extends AbstractHandler {
	
	private RecommendationBuilder rb;
	private NodeInserter ni;
  
	public void handle(String target, Request baseRequest, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {
   
	// Define Type of Node
	ENodeType eNodeType = ENodeType.valueOf(request.getParameter("type"));
	
	// Read Content Information
	Map<String, String> nomMapTemp = new HashMap<String,String>();
	String[] contentData = request.getParameter("userData").split("\\-");
	for(String rating : contentData){
		String[] ratingSplit = rating.split("\\*");
		nomMapTemp.put(ratingSplit[0], ratingSplit[1]);
	}
	Map<Object,IAttribute> nomMap = InputReader.buildNominalAttributes(nomMapTemp);
	
	// Read Collaborative Information
	Map<String, String> numMapTemp = new HashMap<String,String>();
	String[] collaborativeData = request.getParameter("ratingData").split("\\-");
	for(String rating : collaborativeData){
		String[] ratingSplit = rating.split("\\*");
		numMapTemp.put(ratingSplit[0], ratingSplit[1]);
	}
	Map<INode,IAttribute> numMap = InputReader.buildNumericalAttributes(nomMapTemp);
				
	Node inputNode = new Node(eNodeType,null,numMap,nomMap,0.0);
	
	// Create Recommendation
	if(request.getParameter("method") == "recommendation"){	
		Map<INode,IAttribute> recommendation = rb.runRecommendation(inputNode);	
	}
	// Write Insertion
	else if(request.getParameter("method") == "insertion"){
		ni.insert(inputNode);
	}
	    
	response.setContentType("text/text");  // text/xml
    response.setHeader("Cache-Control", "no-cache");
    response.setStatus(HttpServletResponse.SC_OK);
    
    // Create Response
    response.getWriter().write("<message>" + id + "</message>");
    
    response.flushBuffer();
    baseRequest.setHandled(true);
  }

//  public static void main(String[] args) throws Exception {
	public RecommendationServlet(RecommendationBuilder rb, NodeInserter ni) throws Exception {
    
	this.rb = rb;
	this.ni = ni;
		
	Server server = new Server(8081);

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setWelcomeFiles(new String[] { "index.html" });
    resource_handler.setResourceBase("./src/ch/uzh/agglorecommender/servlet/");

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resource_handler, new RecommendationServlet(rb,ni)});

    server.setHandler(handlers);
    server.start();
    server.join();
  }
}
