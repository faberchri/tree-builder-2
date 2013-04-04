package ch.uzh.agglorecommender.recommender;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;
import ch.uzh.agglorecommender.recommender.utils.FileReader;
import ch.uzh.agglorecommender.recommender.utils.Inserter;
import ch.uzh.agglorecommender.recommender.utils.NodeTransformer;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class RecommendationView {
	
	private boolean listen = true;
	
	private static RecommendationModel rm;
	private Inserter inserter;
	private Evaluator evaluator;
	
	private NodeTransformer transformer;
	private FileReader reader;

	private INode rootU;
	private INode rootC;

	public RecommendationView(RecommendationModel rm, Inserter inserter, Evaluator evaluator, ClusterResult clusterResult) {
		this.rm = rm;
		this.inserter = inserter;
		this.evaluator = evaluator;

		this.transformer = new NodeTransformer(rm);
		
		this.rootU = clusterResult.getUserTreeRoot();
		this.rootC = clusterResult.getContentTreeRoot();
	}

	public void startService() throws InterruptedException, ExecutionException{
		while(this.listen==true){
			String command = inputListener();
			runCommand(command);
		}
	}
	
	public void stopService(){
		this.listen = false;
		System.out.println("service stopped");
	}
	
	public void runCommand(String command) throws InterruptedException, ExecutionException{
		
		// Stop signal for system
		if(command.equals("stop")){
			stopService();
			System.exit(0);
		}
		
		// Split command
		String[] fields = command.split("\\s+");
		
		// Check validity
		if(isValidCommand(fields)){
			
			System.out.println("Processing command");
			
			// Build inputNode from inputFiles
			ENodeType type = ENodeType.User; // FIXME needs to be dynamic
			File f1 = new File(fields[2]);
			File f2 = new File(fields[4]);
			List<String> metaInfo = readInputFile(f1);
			List<String> ratings  = readInputFile(f2);
			INode inputNode = buildNode(metaInfo, null, ratings, type);
			
			// Decide Action
			if(fields[0].equals("recommend")){
				recommend(inputNode,null);
			}
			else if(fields[0].equals("insert")){
				insert(inputNode);
			}
		}
	}
	
	public SortedMap<INode, IAttribute> recommend(INode inputNode, INode position){
		
		List<String> watched = rm.createWatchedList(inputNode);
		Map<INode,IAttribute> unsortedRecommendation = rm.recommend(position,watched); // Create Recommendation
		SortedMap<INode, IAttribute> sortedRecommendation = rm.rankRecommendation(unsortedRecommendation,1,15); // Pick Top Movies for User
		
		return sortedRecommendation;
	}
	
	public boolean insert(INode inputNode) throws InterruptedException, ExecutionException{
		
		//**********//
		// MUSS HIER DIE DATASET ITEM ID DEFINIEREN -> +1 der anzahl leaf nodes dieses Typs
		//*********//
		
		boolean result = inserter.insert(inputNode);
		return result;
	}
		
	public Map<String, Double> kFoldEvaluation(Map<INode, String> testNodes) throws NullPointerException, InterruptedException, ExecutionException {
		System.out.println("kFoldEvaluation started..");
			
		// Establish Maps
		Map<String,Double> eval = null;
		Map<String,Double> totalEvalValue = new HashMap<String,Double>();
		totalEvalValue.put("RMSE", 0.0);
		totalEvalValue.put("AME", 0.0);
				
		// Calculate evaluation for all test nodes and update mean
		int counter = 0;
		for(INode testNode : testNodes.keySet()) {
			
			eval = evaluate(testNode);
			System.out.println(eval.toString());
			System.out.println((double)counter/2.5 + " %");
				
			for(String evalMethod : totalEvalValue.keySet()){
				if(eval.get(evalMethod) != null){
					Double currentSum = totalEvalValue.get(evalMethod);
					currentSum += eval.get(evalMethod);
					totalEvalValue.put(evalMethod, currentSum);
				}
			}
			
			counter++;
			if(counter > 250){
				break;
			}
		}
				
		// Take Mean of every value
		for(String evalMethod : totalEvalValue.keySet()){
			Double meanEvalValue = totalEvalValue.get(evalMethod) / 250; //testNodes.size();
			totalEvalValue.put(evalMethod, meanEvalValue);
		}
		
		return totalEvalValue;
	
	}

	public Map<String, Double> evaluate(INode inputNode) throws NullPointerException, InterruptedException, ExecutionException{
			
//		// Take 80% of ratings away -> FIXME
//		Set<INode> inputRatings = inputNode.getRatingAttributeKeys();
//		Map<INode,IAttribute> pickedRatings = new HashMap<>();
//		double percentage = 0;
//		double count = inputRatings.size();
//		for(INode inputRating : inputRatings){
//			if(percentage < 20){
//				pickedRatings.put(inputRating,inputNode.getNumericalAttributeValue(inputRating));
//				percentage += 1/count;
//			}
//		}
//		inputNode.setRatingAttributes(pickedRatings);
			
		// calculate evaluation
		//System.out.println(inputNode.getDatasetId());
		return evaluator.evaluate(inputNode);
	}

	public List<INode> getItemList(ENodeType type, int limit, INode inputNode){
		return rm.createItemList(type,limit, inputNode);
	}
		
	public TreePosition getSimilarPosition(INode inputNode) throws InterruptedException, ExecutionException{
			
		TreePosition mostSimilar = rm.findMostSimilar(inputNode);
//		System.out.println("most similar: " + mostSimilar);
		
		return mostSimilar;
	}
		
	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		return transformer.buildNode(nomMetaInfo, numMetaInfo, ratings, type);
	}

	private static String inputListener(){	
		Scanner input = new Scanner(System.in);
		input.useDelimiter("\n");
		System.out.print("agglorecommender > ");
		String command = input.next( );
		
		return command;
	}

	private boolean isValidCommand (String[] fields){
		if(fields.length == 5){
			if(fields[0].equals("recommend") || fields[0].equals("insert")){
				if((fields[1].equals("-m") || fields[1].equals("-r")) && 
					(fields[3].equals("-m") || fields[3].equals("-r")) && 
					(!fields[1].equals(fields[3]))); {
							
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

	private List<String> readInputFile(File file){
		InputStream stream = reader.getCustomFileStream(file);
		List<String> lines = reader.getStreamLineByLine(stream);
		return lines;
	}
}
