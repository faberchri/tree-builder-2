package ch.uzh.agglorecommender.recommender.clients;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import ch.uzh.agglorecommender.recommender.RecommenderProxy;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class WebClient extends AbstractHandler {
	
	private final RecommenderProxy rc;
	private final Server server;
	private DecimalFormat df = new DecimalFormat("#.##");

	public WebClient(RecommenderProxy rc) throws Exception {
	    
		this.rc = rc;
			
		server = new Server(8081); // Port number should not be changed
	
	    ResourceHandler resourceHandler = new ResourceHandler();
	    resourceHandler.setDirectoriesListed(true);
	    resourceHandler.setWelcomeFiles(new String[] {"index.html"});
	    resourceHandler.setResourceBase("./src/ch/uzh/agglorecommender/recommender/clients/");
	
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
				itemRequest(request,response);
			}
			
			// Create Recommendation
			if(requestType.equals("recommendation")){

				// Build Node
				INode inputNode = createNode(request);

				try {
					recommendationRequest(inputNode,request,response);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
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
	
	private void itemRequest(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		ENodeType type = ENodeType.valueOf(request.getParameter("type"));
		int limit = Integer.parseInt(request.getParameter("limit"));
		
		// Build Node
		INode inputNode = createNode(request);
		
		// Build item list
		List<INode> itemList = rc.getItemList(type, limit, inputNode);
		
		// Write Message
		response.getWriter().write("<table>");
		int i=1;
		for(INode item : itemList){
			
			if(item != null){
				response.getWriter().write( 
						"<tr><td>" +
						"<select id='" + i + "' title='"+ rc.getMeta(item,"title") +"' name=" + item.getDatasetId() +">" +
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
						rc.getMeta(item,"title") +
						"</td></tr>");
				i++;
			}
		}
		
		response.getWriter().write("</table>");
	}
	
	private void recommendationRequest(INode inputNode, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, ExecutionException{
		
		// Build Recommendation
		long startTime = System.nanoTime();
		TreePosition position = rc.getSimilarPosition(inputNode);
		SortedMap<INode, IAttribute> recommendations = rc.recommend(inputNode,position);
		long endTime = System.nanoTime();
		long duration = endTime - startTime;
		
		// Create Evaluation
		inputNode = removeRatings(inputNode);
		Map<String,Double> evaluation = rc.evaluate(inputNode);
		
		// Extra Infos & Action
		response.getWriter().write("<table style='border:1px solid #fff;width:95%'>");
		response.getWriter().write("<tr><td>Utility</td><td>" + position.getUtility() + "</td></tr>");
		response.getWriter().write("<tr><td>Evaluation</td><td>" + evaluation.toString() + "</td></tr>");
		response.getWriter().write("<tr><td>Duration</td><td>" + df.format((double)(duration/1000000000)) + " Seconds</td></tr>");
		String insert = request.getParameter("insert");
		if(insert.equals("true")){
			response.getWriter().write("<tr><td>Insertion</td><td>" + rc.insert(inputNode,position) + "</td></tr>");	
		}
		response.getWriter().write("</table><br>");
		
		// Write Recommendation Message
		response.getWriter().write("<table style='width:100%'>");
		for(Entry<INode,IAttribute> recommendation : recommendations.entrySet()){
			
			String title 	= rc.getMeta(recommendation.getKey(),"title");
			String url		= ""; //"<a href='" + rc.getMeta(recommendation.getKey(),"url") + "' target='_blank'>IMDB Link</a>";
			
			IAttribute attribute = recommendation.getValue();
			Double rating = attribute.getSumOfRatings() / attribute.getSupport();
			
			response.getWriter().write("<tr><td style='width:10%'>" + df.format(rating) + "</td><td style='width:70%'>" + title + "</td><td style='width:20%'>" + url + "</td></tr>");
		}
		response.getWriter().write("</table><br>");
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
		
		return rc.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}
	
	private INode removeRatings(INode inputNode){
		Set<INode> inputRatings = inputNode.getRatingAttributeKeys();
		Map<INode,IAttribute> pickedRatings = new HashMap<>();
		double percentage = 0;
		double count = inputRatings.size();
		for(INode inputRating : inputRatings){
			if(percentage < 20){
				pickedRatings.put(inputRating,inputNode.getNumericalAttributeValue(inputRating));
				percentage += 1/count;
			}
		}
		inputNode.setRatingAttributes(pickedRatings);
		return inputNode;
	}
}
