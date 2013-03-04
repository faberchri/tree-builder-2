package ch.uzh.agglorecommender.recommender.ui;

import java.io.Console;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.InputReader;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;

public class BasicUI {
	
	private RecommendationBuilder rb;
	private NodeInserter ni;

	public BasicUI(RecommendationBuilder rb, NodeInserter ni) {
		this.rb = rb;
		this.ni = ni;
	}

	public static void startService(){
		while(1==1){
			Console console = System.console();
			String input = console.readLine("What do you want, bitch:");
			
			// just waits for input -> insertion / recommendation -f file -> processFile, do action return result
			 INode inputNode = InputReader.processFile(file);
			
		}
	}
	
	public static void stopService(){
		//???
	}
	
	public Map<INode, IAttribute> recommendation(List<String> metaInfo, List<String> ratings){
		
		INode inputNode = buildNode(metaInfo, ratings);
		Map<INode,IAttribute> unsortedRecommendation = this.rb.runRecommendation(inputNode); // Create Recommendation
		SortedMap<INode, IAttribute> sortedRecommendation = this.rb.rankRecommendation(unsortedRecommendation,1, 100); // Pick Top Movies for User
		this.rb.printRecommendation(sortedRecommendation);
		
		return sortedRecommendation;
	}
	
	public boolean insertion(List<String> metaInfo, List<String> ratings){
		
		INode inputNode = buildNode(metaInfo,ratings);
		Boolean result = this.ni.insert(inputNode);
		
		return result;
	}
	
	public INode buildNode(List<String> metaInfo,List<String> ratings){
		// Define Type of Node
				ENodeType type = ENodeType.valueOf(request.getParameter("type"));
				
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
				Map<INode,IAttribute> numMap = InputReader.buildNumericalAttributes(nomMapTemp,type);
							
				Node inputNode = new Node(type,null,numMap,nomMap,0.0);
	}
	
	

}
