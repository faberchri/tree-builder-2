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
import java.util.SortedMap;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;
import ch.uzh.agglorecommender.util.TBLogger;

public class BasicUI {
	
	private static RecommendationBuilder rb;
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
	
	public SortedMap<INode, IAttribute> recommend(INode inputNode){
		Map<INode,IAttribute> unsortedRecommendation = this.rb.runRecommendation(inputNode); // Create Recommendation
		SortedMap<INode, IAttribute> sortedRecommendation = this.rb.rankRecommendation(unsortedRecommendation,1, 15); // Pick Top Movies for User
//		this.rb.printRecommendation(sortedRecommendation);
		return sortedRecommendation;
	}
	
	public boolean insert(INode inputNode){
		
		//**********//
		// MUSS HIER DIE DATASET ITEM ID DEFINIEREN -> +1 der anzahl leaf nodes dieses Typs
		//*********//
		
		Boolean result = this.ni.insert(inputNode);
		return result;
	}
	
	// FIXME
	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		
		System.out.println("Building Node");
		
//		System.out.println("------------------");
//		System.out.println(nomMetaInfo.toString());
//		System.out.println(numMetaInfo.toString());
//		System.out.println(ratings.toString());
//		System.out.println("------------------");
		
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
		
//		System.out.println("------------------");
//		System.out.println(ratingMapTemp.toString());
//		System.out.println(numMetaMapTemp.toString());
//		System.out.println(nomMetaMapTemp.toString());
//		System.out.println("------------------");
		
		Map<String,IAttribute> nomMetaMap = buildNominalAttributes(nomMetaMapTemp);
		Map<String,IAttribute> numMetaMap = buildNominalAttributes(numMetaMapTemp);
		Map<INode,IAttribute> ratingsMap = buildNumericalAttributes(ratingMapTemp,type);
		
//		System.out.println("------------------");
//		System.out.println(ratingsMap.toString());
//		System.out.println(numMetaMap.toString());
//		System.out.println(nomMetaMap.toString());
//		System.out.println("------------------");
		
		// Get dataset for configuration -> FIXME
		IDataset<?> dataset = rb.getDataset();
		INode fakeChild = new Node(null,null,dataset); 
		Collection<INode> fakeChildren = new HashSet<>();
		fakeChildren.add(fakeChild);
		
		return new Node(type,fakeChildren,ratingsMap,numMetaMap,nomMetaMap,0.0);
	}
	
	//************* DOPPELT **********************//
	
	public List<INode> getItemList(ENodeType type, int limit){
		return rb.createItemList(type,limit);
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

	private static Map<INode,IAttribute> buildNumericalAttributes(Map<String,String> attributes, ENodeType type) {
	
		Map<INode,IAttribute> numAttributes = new HashMap<INode,IAttribute>();
		for(String datasetID: attributes.keySet()){
		
			// Find Node with dataset id 
			INode node = findNode(datasetID, type);
			
			// Create Attribute
			int rating = Integer.parseInt(attributes.get(datasetID));
			IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
			
			numAttributes.put(node,attribute);
		}
		
		return numAttributes;
	}
	
	private static INode findNode(String datasetID, ENodeType type) {
		return rb.findNode(Integer.parseInt(datasetID),type);
	}

	private static Map<String, IAttribute> buildNominalAttributes(Map<String,String> attributes) {
		Map<String,IAttribute> nominalAttributes = new HashMap<>();
		for(String attKey : attributes.keySet()){
			Map<String,Double> probabilityMap = new HashMap<String,Double>();
			probabilityMap.put(attributes.get(attKey),1.0);
			nominalAttributes.put(attKey, new CobwebAttribute(probabilityMap));
		}
		return nominalAttributes;
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
	//************* DOPPELT **********************//
	
}
