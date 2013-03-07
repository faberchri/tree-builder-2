package ch.uzh.agglorecommender.recommender.ui.extensions;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

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
	    resourceHandler.setResourceBase("./src/ch/uzh/agglorecommender/recommender/ui/extensions/");
	
	    HandlerList handlers = new HandlerList();
	    handlers.setHandlers(new Handler[] {resourceHandler, this});
	
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
		
		//********** PreDefinition *******************
		response.setContentType("text/text");  // text/xml
	    response.setHeader("Cache-Control", "no-cache");
	    response.setStatus(HttpServletResponse.SC_OK);
	    //********************************************
		
		String requestType = request.getParameter("request");
		
		// Deliver items to rate
		// http://localhost:8081/request?request=items&type=Content&limit=10
		if(requestType.equals("items")){
			
			ENodeType type = ENodeType.valueOf(request.getParameter("type"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			
			List<INode> itemList = basicUI.getItemList(type, limit);
			
			for(INode item : itemList){
				response.getWriter().write("<item><id>" + item.getDatasetId() + "</id><name>" + getMeta(item) + "</name></item>");
			}
			
		}
		
		else if(requestType.equals("recommendation") || requestType .equals("insertion")) {
			
//			http://localhost:8081/request?request=recommendation&type=Content&meta=age-23*gender-M&ratings=127-8*182-9
//			http://localhost:8081/request?request=insertion&type=Content&meta=age-23*gender-M&ratings=127-8*182-9
			
			// ************** Create Node ***************
			
			// Retrieve Type
			ENodeType type = ENodeType.valueOf(request.getParameter("type"));
			
			// Read MetaInfo to List<String> -> attribute+value
			List<String> metaInfo = new LinkedList<String>();
			String metaFull = request.getParameter("meta");
			String[] metaList = metaFull.split("\\*");
			for(String meta : metaList){
				metaInfo.add(meta);
			}
			
//			System.out.println(metaInfo.toString());
			
			// Read Ratings to List<String> -> content node+rating
			List<String> ratings = new LinkedList<String>();
			String ratingsFull = request.getParameter("ratings");
			String[] ratingList = ratingsFull.split("\\*");
			for(String rating : ratingList){
				ratings.add(rating);
			}
			
//			System.out.println(ratings.toString());
			
			INode inputNode = basicUI.buildNode(metaInfo, ratings, type);
			System.out.println("Created Node: " + 
					inputNode.toString() + 
					inputNode.getNominalAttributesString() + 
					inputNode.getNumericalAttributesString());
			
			// *************************************
			
			// Create Recommendation
			if(requestType.equals("recommendation")){	
				SortedMap<INode,IAttribute> recommendations = basicUI.recommend(inputNode);
				
				System.out.println(recommendations.toString());
				
				for(Entry<INode,IAttribute> recommendation : recommendations.entrySet()){
					String title = getMeta(recommendation.getKey());
					IAttribute attribute = recommendation.getValue();
					System.out.println(attribute.toString());
					Double rating = attribute.getSumOfRatings() / attribute.getSupport();
					response.getWriter().write("<message recommendation><title>" + title + "</title><rating>" + rating + "</message>");
				}
			}
			// Write Insertion
			else if(requestType.equals("insertion")){
				Boolean success = basicUI.insert(inputNode);
				response.getWriter().write("<message insertion>" + success + "</message>");
			}
			
			// *************************************
		}
		
		else {
		    // Create Response
			response.getWriter().write("<message test>" + request.getParameter("test") + "</message>");
		}
	    
		//********** PostDefinition ******************
	    response.flushBuffer();
	    baseRequest.setHandled(true);
		//********************************************
  }
	
	//********* DOPPELT ****************//
	private String getMeta(INode node){
		Iterator<Entry<Object, Double>> titleIt = node.getNominalAttributeValue("title").getProbabilities();
		String title="";
		while(titleIt.hasNext()){
			title = (String) titleIt.next().getKey();
		}
		return title;
	}
}
