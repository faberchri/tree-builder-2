package ch.uzh.agglorecommender.recommender.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;
import ch.uzh.agglorecommender.util.TBLogger;

public class BasicUI {
	
	private static RecommendationBuilder rb;
	private NodeInserter ni;
	private boolean listen;
	private Evaluator ev;
	private INode rootU;

	public BasicUI(RecommendationBuilder rb, NodeInserter ni, Evaluator ev, ClusterResult clusterResult) {
		this.rb = rb;
		this.ni = ni;
		this.ev = ev;
		this.rootU = clusterResult.getUserTreeRoot();
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
	
	public void runCommand(String command){
		
//		// Stop signal for system
//		if(command.equals("stop")){
//			stopService();
//			System.exit(0);
//		}
//		
//		// Split command
//		String[] fields = command.split("\\s+");
//		
//		// Check validity
//		if(isValidCommand(fields)){
//			
//			System.out.println("Processing command");
//			
//			// Build inputNode from inputFiles
//			ENodeType type = ENodeType.User; // FIXME needs to be dynamic
//			File f1 = new File(fields[2]);
//			File f2 = new File(fields[4]);
//			List<String> metaInfo = readInputFile(f1);
//			List<String> ratings  = readInputFile(f2);
//			INode inputNode = buildNode(metaInfo, ratings, type);
//			
//			// Decide Action
//			if(fields[0].equals("recommend")){
//				recommend(inputNode);
//			}
//			else if(fields[0].equals("insert")){
//				insert(inputNode);
//			}
//		}
	}
	
	public SortedMap<INode, IAttribute> recommend(INode inputNode, INode position){
		
		List<String> watched = rb.createWatchedList(inputNode);
		Map<INode,IAttribute> unsortedRecommendation = rb.recommend(position,watched); // Create Recommendation
		SortedMap<INode, IAttribute> sortedRecommendation = rb.rankRecommendation(unsortedRecommendation,1,15); // Pick Top Movies for User
		
		return sortedRecommendation;
	}
	
	public boolean insert(INode inputNode){
		
		//**********//
		// MUSS HIER DIE DATASET ITEM ID DEFINIEREN -> +1 der anzahl leaf nodes dieses Typs
		//*********//
		
		boolean result = ni.insert(inputNode);
		return result;
	}
	
	// FIXME
	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		
//		System.out.println("Building Node");
		
		// Read Content Information
		Map<String, String> nomMetaMapTemp = new HashMap<String,String>();
		for(String meta : nomMetaInfo){
			String[] ratingSplit = meta.split("-");
			if(ratingSplit.length>=2){
				nomMetaMapTemp.put(ratingSplit[0], ratingSplit[1]);
			}
		}
		
		Map<String, String> numMetaMapTemp = new HashMap<String,String>();
		for(String meta : numMetaInfo){
			String[] ratingSplit = meta.split("-");
			if(ratingSplit.length>=2){
				numMetaMapTemp.put(ratingSplit[0], ratingSplit[1]);
			}
		}
			
		// Read Ratings Data
		Map<String, String> ratingMapTemp = new HashMap<String,String>();
		for(String rating : ratings){
			String[] ratingSplit = rating.split("-");
			if(ratingSplit.length>=2){
				ratingMapTemp.put(ratingSplit[0], ratingSplit[1]);
			}
		}
		
		Map<String,IAttribute> nomMetaMap = buildNomMetaAttributes(nomMetaMapTemp);
		Map<String,IAttribute> numMetaMap = buildNumMetaAttributes(numMetaMapTemp);
		Map<INode,IAttribute> ratingsMap =  buildRatingAttributes(ratingMapTemp,type);
		
		// Get dataset for configuration -> FIXME
		IDataset dataset = rb.getDataset();
		INode fakeChild = new Node(null,null,dataset); 
		Collection<INode> fakeChildren = new HashSet<>();
		fakeChildren.add(fakeChild);
		
		INode newNode = new Node(type,fakeChildren,ratingsMap,numMetaMap,nomMetaMap,0.0);
		newNode.addChild(rootU);
		
		return newNode;
	}
	
	public List<INode> getItemList(ENodeType type, int limit, INode inputNode){
		return rb.createItemList(type,limit, inputNode);
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
		InputStream stream = getCustomFileStream(file);
		List<String> lines = getStreamLineByLine(stream);
		return lines;
	}

	private static Map<INode, IAttribute> buildRatingAttributes(Map<String,String> attributes, ENodeType type) {
	
		Map<INode,IAttribute> numAttributes = new HashMap<>();
		for(String datasetID: attributes.keySet()){
		
			// Find Node with dataset id 
			INode node = findNode(datasetID, type);
			
			if(node != null){
				// Create Attribute
				int rating = Integer.parseInt(attributes.get(datasetID));
				IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
				
				numAttributes.put(node,attribute);
			}
		}
		
		return numAttributes;
	}

	private static INode findNode(String datasetIDString, ENodeType type) {
		
		int datasetID;
		
		try{
			datasetID = Integer.parseInt(datasetIDString);
		}
		catch (Exception e){
			return null;
		}
		
		return rb.findNode(datasetID,type);
	}

	private static Map<String, IAttribute> buildNomMetaAttributes(Map<String,String> attributes){
		
		Map<String,IAttribute> nomMeta = new HashMap<>();
		for(String attKey : attributes.keySet()){
			Map<String,Double> probabilityMap = new HashMap<String,Double>();
			probabilityMap.put(attributes.get(attKey),1.0);
			nomMeta.put(attKey, new CobwebAttribute(probabilityMap));
		}
		
		return nomMeta;
	}
	
	private static Map<String, IAttribute> buildNumMetaAttributes(Map<String,String> attributes){
		
		Map<String,IAttribute> numMeta = new HashMap<>();
		for(String attKey : attributes.keySet()){
			int rating = Integer.parseInt(attributes.get(attKey));
			IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
			numMeta.put(attKey, attribute);
		}
		
		return numMeta;
	}
	
	
	//************* DOPPELT (AbstractDataset) **********************//
	private InputStream getCustomFileStream(File file) {
		InputStream input = null;
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			TBLogger.getLogger(getClass().getName()).severe("Input file was not found: "+ file.getPath());
			System.exit(-1);
		}
		return input;
	}
	
	private List<String> getStreamLineByLine(InputStream input) {
		List<String> lines = new ArrayList<String>();
		try {
			int in = input.read();
			List<Character> charLi = new ArrayList<Character>();
			while (in != -1) {
				charLi.add((char)in);
				if (in == (int)'\n') {
					StringBuilder builder = new StringBuilder(charLi.size());
					for(Character ch: charLi) {
						builder.append(ch);
					}
					lines.add(builder.toString());				
					charLi.clear();
				}
				in = input.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;				
	}
	
	public String evaluate(INode inputNode){
		
		// Take 80% of ratings away -> FIXME
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
		
		// calculate evaluation
		Map<String,Double> evaluation =  ev.evaluate(inputNode);
		
		// Create String
		String evalString = "";
		
		if(evaluation != null){
			for(String eval : evaluation.keySet()){
				evalString += eval + ": " + evaluation.get(eval) + "<br>";
			}
		}
		
		return evalString;
	}
	
	public TreePosition getSimilarPosition(INode inputNode){
		return rb.findMostSimilar(inputNode);
	}
	
}
