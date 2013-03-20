package ch.uzh.agglorecommender.recommender.ui.extensions;

import java.io.IOException;
import java.text.DecimalFormat;
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
		
	    if(request.getParameterMap().containsKey("request")) {
			String requestType = request.getParameter("request");
			
			// Deliver items to rate
			if(requestType.equals("items")){
				
				ENodeType type = ENodeType.valueOf(request.getParameter("type"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				
				List<INode> itemList = basicUI.getItemList(type, limit);
				
				response.getWriter().write("<table>");
				
				int i=1;
				for(INode item : itemList){
					
					if(item != null){
						response.getWriter().write( 
								"<tr><td>" +
								"<select id='" + i + "' name=" + item.getDatasetId() +">" +
								"<option value='not seen'>not seen</option>" +
								"<option value='1'>1</option>" +
								"<option value='2'>2</option>" +
								"<option value='3'>3</option>" +
								"<option value='4'>4</option>" +
								"<option value='5'>5</option>" +
								"<option value='6'>6</option>" +
								"<option value='7'>7</option>" +
								"<option value='8'>8</option>" +
								"<option value='9'>9</option>" +
								"<option value='10'>10</option>" +
								"</select> " +
								"</td><td width='250'>" +
								getMeta(item,"title") +
								"</td></tr>");
						i++;
					}
				}
				
				response.getWriter().write("</table>");
			}
			
			else if(requestType.equals("recommendation") || requestType .equals("insertion")) {
				
				// Retrieve Type
				ENodeType type = ENodeType.valueOf(request.getParameter("type"));
				
				// Read MetaInfo to List<String> -> attribute+value
				List<String> numMetaInfo = new LinkedList<String>();
				String numMetaFull = request.getParameter("nummeta");
				String[] metaList = numMetaFull.split("\\*");
				for(String meta : metaList){
					numMetaInfo.add(meta);
				}
				List<String> nomMetaInfo = new LinkedList<String>();
				String nomMetaFull = request.getParameter("nommeta");
				String[] nomMetaList = nomMetaFull.split("\\*");
				for(String meta : nomMetaList){
					nomMetaInfo.add(meta);
				}
				
				// Read Ratings to List<String> -> content node+rating
				List<String> ratings = new LinkedList<String>();
				String ratingsFull = request.getParameter("ratings");
				String[] ratingList = ratingsFull.split("\\*");
				for(String rating : ratingList){
					System.out.println(rating);
					ratings.add(rating);
				}
				
				// Create Node
//				System.out.println("--------------------");
//				System.out.println(numMetaInfo.toString());
//				System.out.println(nomMetaInfo.toString());
//				System.out.println(ratingList.toString());
//				System.out.println("--------------------");
				
				INode inputNode = basicUI.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
//				System.out.println("Created Node: " + 
//						inputNode.toString() + 
//						inputNode.getNominalAttributesString() + 
//						inputNode.getNumericalAttributesString() + 
//						inputNode.getNominalMetaAttributeKeys() +
//						inputNode.getNumericalMetaAttributeKeys()
//						);
				
				// Create Recommendation
				if(requestType.equals("recommendation")){	
					SortedMap<INode,IAttribute> recommendations = basicUI.recommend(inputNode);
					
					System.out.println("resulting recommendation" + recommendations.toString());
					
					response.getWriter().write("<table style='width:570px'>");
					
					for(Entry<INode,IAttribute> recommendation : recommendations.entrySet()){
						
//						System.out.println(recommendation.getKey().getNominalAttributesString());
						
						String title 	= getMeta(recommendation.getKey(),"title");
						String url		= "<a href='" + getMeta(recommendation.getKey(),"IMDb URL") + "' target='_blank'>IMDB Link</a>";
						
						IAttribute attribute = recommendation.getValue();
						Double rating = attribute.getSumOfRatings() / attribute.getSupport();
						DecimalFormat df = new DecimalFormat("#.##");
						
						response.getWriter().write("<tr><td style='width:30px'>" + df.format(rating) + "</td><td style='width:440px'>" + title + "</td><td style='width:100px'>" + url + "</td></tr>");
					}
					
					response.getWriter().write("</table>");
				}
				// Write Insertion
				else if(requestType.equals("insertion")){
					Boolean success = basicUI.insert(inputNode);
					response.getWriter().write("<message insertion>" + success + "</message>");
				}
			}
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
	private String getMeta(INode node, String info){
		if(node.getNominalAttributeValue(info) != null){
			Iterator<Entry<Object, Double>> metaIt = node.getNominalAttributeValue(info).getProbabilities();
			String meta ="";
			while(metaIt.hasNext()){
				meta = (String) metaIt.next().getKey();
			}
			return meta;
		}
		
		return "meta fehlt";
	}
}
