package ch.uzh.agglorecommender.recommender.extensions;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

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
import ch.uzh.agglorecommender.recommender.RecommendationView;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class WebExtension extends AbstractHandler {
	
	private final RecommendationView basicUI;
	private final Server server;

	public WebExtension(RecommendationView basicUI) throws Exception {
	    
		this.basicUI = basicUI;
			
		server = new Server(8081); // Port number should not be changed
	
	    ResourceHandler resourceHandler = new ResourceHandler();
	    resourceHandler.setDirectoriesListed(true);
	    resourceHandler.setWelcomeFiles(new String[] {"index.html"});
	    resourceHandler.setResourceBase("./src/ch/uzh/agglorecommender/recommender/extensions/");
	
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
		
		response.setContentType("text/text");  // text/xml
	    response.setHeader("Cache-Control", "no-cache");
	    response.setStatus(HttpServletResponse.SC_OK);
		
	    if(request.getParameterMap().containsKey("request")) {
			String requestType = request.getParameter("request");
			
			// Deliver items to rate
			if(requestType.equals("items")){
				
				ENodeType type = ENodeType.valueOf(request.getParameter("type"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				
				// Build Node
				INode inputNode = createNode(request);
				
				// Build item list
				List<INode> itemList = basicUI.getItemList(type, limit, inputNode);
				
				response.getWriter().write("<table>");
				
				int i=1;
				for(INode item : itemList){
					
					if(item != null){
						response.getWriter().write( 
								"<tr><td>" +
								"<select id='" + i + "' title='"+ getMeta(item,"title") +"' name=" + item.getDatasetId() +">" +
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
				
				// Build Node
				INode inputNode = createNode(request);
				
				// Create Recommendation
				if(requestType.equals("recommendation")){
					
					long startTime = System.nanoTime();
					
					TreePosition position = null;
					try {
						position = basicUI.getSimilarPosition(inputNode);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					System.out.println("chosen node: " + position.getNode().toString());
					
					SortedMap<INode,IAttribute> recommendations = basicUI.recommend(inputNode,position.getNode());
					
					long endTime = System.nanoTime();
					long duration = endTime - startTime;
					
					Map<String,Double> evaluation = null;
					try {
						evaluation = basicUI.evaluate(inputNode);
					} catch (NullPointerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Create String
					String evalString = "";
					
					if(evaluation != null){
						for(String eval : evaluation.keySet()){
							evalString += eval + ": " + evaluation.get(eval) + "<br>";
						}
					}
					
					
					response.getWriter().write("<table style='width:100%'>");
					
					for(Entry<INode,IAttribute> recommendation : recommendations.entrySet()){
						
						String title 	= getMeta(recommendation.getKey(),"title");
						String url		= "<a href='" + getMeta(recommendation.getKey(),"IMDb URL") + "' target='_blank'>IMDB Link</a>";
						
						IAttribute attribute = recommendation.getValue();
						Double rating = attribute.getSumOfRatings() / attribute.getSupport();
						DecimalFormat df = new DecimalFormat("#.##");
						
						response.getWriter().write("<tr><td style='width:10%'>" + df.format(rating) + "</td><td style='width:70%'>" + title + "</td><td style='width:20%'>" + url + "</td></tr>");
					}
					
					response.getWriter().write("</table><br>");
					response.getWriter().write("Utility: " + position.getUtility() + "<br><br>");
					response.getWriter().write(evaluation + "<br>");
					response.getWriter().write("Duration: " + (duration/1000000000) + " Seconds<br>");
					
					Boolean success = false; //basicUI.insert(inputNode);
					response.getWriter().write("Insertion: " + success + "<br>");
				}
			}
	    }
		else {
		    // Create Response
			response.getWriter().write("<message test>" + request.getParameter("test") + "</message>");
		}
	    
	    response.flushBuffer();
	    baseRequest.setHandled(true);
  }
	
	private INode createNode(HttpServletRequest request){
		
		// Retrieve Type
		ENodeType type = ENodeType.valueOf(request.getParameter("type"));
		
		// Read MetaInfo to List<String> -> attribute+value
		List<String> numMetaInfo = new LinkedList<String>();
		String numMetaFull = request.getParameter("nummeta");
		if(numMetaFull != null){
			String[] metaList = numMetaFull.split("\\*");
			for(String meta : metaList){
				numMetaInfo.add(meta);
			}
		}
		List<String> nomMetaInfo = new LinkedList<String>();
		String nomMetaFull = request.getParameter("nommeta");
		if(nomMetaFull != null){
			String[] nomMetaList = nomMetaFull.split("\\*");
			for(String meta : nomMetaList){
				nomMetaInfo.add(meta);
			}
		}
		
		// Read Ratings to List<String> -> content node+rating
		List<String> ratings = new LinkedList<String>();
		String ratingsFull = request.getParameter("ratings");
		if(ratingsFull != null){
			String[] ratingList = ratingsFull.split("\\*");
			for(String rating : ratingList){
				ratings.add(rating);
			}
		}
		
		return basicUI.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}
	
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