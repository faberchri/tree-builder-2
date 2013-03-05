package ch.uzh.agglorecommender.recommender.ui;

import java.io.File;
import java.util.Scanner;

import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;

public class BasicUI {
	
	private RecommendationBuilder rb;
	private NodeInserter ni;
	private boolean listen;

	public BasicUI(RecommendationBuilder rb, NodeInserter ni) {
		this.rb = rb;
		this.ni = ni;
		this.listen = true;
	}

	public void startService(){
		while(this.listen==true){
			String command = inputListener();
			runCommand(command);
		}
	}
	
	public void stopService(){
		this.listen = false;
		System.out.println("service stopped");
	}
	
	public static String inputListener(){	
		Scanner input = new Scanner(System.in);
		System.out.print("agglorecommender > ");
		String command = input.next( );
		
		return command;
	}
	
	public void runCommand(String command){
		
		// Stop signal
		if(command.equals("stop")){
			stopService();
			System.exit(0);
		}
		
		// Split command
		String[] fields = command.split("\\s+"); // FIXME not working, wird nicht erkannt
		
		// Check validity
		if(isValidCommand(fields)){
			
			System.out.println("Processing command");
		
			// Decide Type
			
			// Build metainfos & ratings
			
			// Run Action
			
			// just waits for input -> insertion / recommendation -f file -> processFile, do action return result
	//		 INode inputNode = InputReader.processFile(file);
			
		}
		
	}
	
	public boolean isValidCommand (String[] fields){
		System.out.println(fields.length);
		if(fields.length == 5){
			System.out.println("correct length");
			if(fields[0].equals("recommend") || fields[0].equals("insert")){
				System.out.println("main parameter found");
				
				if((fields[1].equals("-m") || fields[1].equals("-r")) && 
						(fields[3].equals("-m") || fields[3].equals("-r")) && 
						(!fields[1].equals(fields[3]))); {
							
					System.out.println("sub parameters found");
				
					// Checking files
					File f1 = new File(fields[2]);
					File f2 = new File(fields[4]);
					if(f1.exists() && f2.exists()) { 
						System.out.println("Files exist");
						return true;
					}
				
				}
			}
		}
		return false;
	}
	
//	public Map<INode, IAttribute> recommendation(List<String> metaInfo, List<String> ratings){
//		
//		INode inputNode = buildNode(metaInfo, ratings);
//		Map<INode,IAttribute> unsortedRecommendation = this.rb.runRecommendation(inputNode); // Create Recommendation
//		SortedMap<INode, IAttribute> sortedRecommendation = this.rb.rankRecommendation(unsortedRecommendation,1, 100); // Pick Top Movies for User
//		this.rb.printRecommendation(sortedRecommendation);
//		
//		return sortedRecommendation;
//	}
//	
//	public boolean insertion(List<String> metaInfo, List<String> ratings){
//		
//		INode inputNode = buildNode(metaInfo,ratings);
//		Boolean result = this.ni.insert(inputNode);
//		
//		return result;
//	}
	
//	public INode buildNode(List<String> metaInfo,List<String> ratings){
//		// Define Type of Node
//				ENodeType type = ENodeType.valueOf(request.getParameter("type"));
//				
//				// Read Content Information
//				Map<String, String> nomMapTemp = new HashMap<String,String>();
//				String[] contentData = request.getParameter("userData").split("\\-");
//				for(String rating : contentData){
//					String[] ratingSplit = rating.split("\\*");
//					nomMapTemp.put(ratingSplit[0], ratingSplit[1]);
//				}
//				Map<Object,IAttribute> nomMap = InputReader.buildNominalAttributes(nomMapTemp);
//				
//				// Read Collaborative Information
//				Map<String, String> numMapTemp = new HashMap<String,String>();
//				String[] collaborativeData = request.getParameter("ratingData").split("\\-");
//				for(String rating : collaborativeData){
//					String[] ratingSplit = rating.split("\\*");
//					numMapTemp.put(ratingSplit[0], ratingSplit[1]);
//				}
//				Map<INode,IAttribute> numMap = InputReader.buildNumericalAttributes(nomMapTemp,type);
//							
//				Node inputNode = new Node(type,null,numMap,nomMap,0.0);
//	}
	
	

}
