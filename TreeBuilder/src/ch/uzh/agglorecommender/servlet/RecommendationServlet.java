package ch.uzh.agglorecommender.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

public class RecommendationServlet extends AbstractHandler {
  
	public void handle(String target, Request baseRequest, HttpServletRequest request,
      HttpServletResponse response) throws IOException, ServletException {
   
	 System.out.println("Anfrage erhalten:" + request.getParameter("id"));
	 
	 //*******************
//	 
//	 Implementieren: Decision welche Methode aufgerufen; dann Parameter abrufen
//	 - GetMovies
//	 - GetRecommendation
//	 - WriteInsertion
//	 
	 //*******************
	  
	  String id = request.getParameter("id");
    response.setContentType("text/text");
    response.setHeader("Cache-Control", "no-cache");
//    response.setContentLength(19 + id.length());
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().write("<message>" + id + "</message>");
    response.flushBuffer();
    baseRequest.setHandled(true);
  }

  public static void main(String[] args) throws Exception {
    Server server = new Server(8081);

    ResourceHandler resource_handler = new ResourceHandler();
    resource_handler.setDirectoriesListed(true);
    resource_handler.setWelcomeFiles(new String[] { "index.html" });
    resource_handler.setResourceBase("./src/ch/uzh/agglorecommender/servlet/");

    HandlerList handlers = new HandlerList();
    handlers.setHandlers(new Handler[] { resource_handler, new RecommendationServlet() });

    server.setHandler(handlers);
    server.start();
    server.join();
  }
}