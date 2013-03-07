package ch.uzh.agglorecommender.recommender.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationBuilder;
import ch.uzh.agglorecommender.recommender.utils.NodeInserter;
import ch.uzh.agglorecommender.util.TBLogger;

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
	
	public void runCommand(String command){
		
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
			INode inputNode = buildNode(metaInfo, ratings, type);
			
			// Decide Action
			if(fields[0].equals("recommend")){
				recommend(inputNode);
			}
			else if(fields[0].equals("insert")){
				insert(inputNode);
			}
		}
	}
	
	public Map<INode, IAttribute> recommend(INode inputNode){
		Map<INode,IAttribute> unsortedRecommendation = this.rb.runRecommendation(inputNode); // Create Recommendation
		SortedMap<INode, IAttribute> sortedRecommendation = this.rb.rankRecommendation(unsortedRecommendation,1, 100); // Pick Top Movies for User
		this.rb.printRecommendation(sortedRecommendation);
		return sortedRecommendation;
	}
	
	public boolean insert(INode inputNode){
		
		//**********//
		// MUSS HIER DIE DATASET ITEM ID DEFINIEREN -> +1 der anzahl leaf nodes dieses Typs
		//*********//
		
		Boolean result = this.ni.insert(inputNode);
		return result;
	}
	
	public INode buildNode(List<String> metaInfo,List<String> ratings, ENodeType type){
			
		// Read Content Information
		Map<String, String> nomMapTemp = new HashMap<String,String>();
		for(String meta : metaInfo){
			String[] ratingSplit = meta.split("\\-");
			nomMapTemp.put(ratingSplit[0], ratingSplit[1]);
		}
		Map<Object,IAttribute> nomMap = buildNominalAttributes(nomMapTemp);
			
		// Read Collaborative Information
		Map<String, String> numMapTemp = new HashMap<String,String>();
		for(String rating : ratings){
			String[] ratingSplit = rating.split("\\-");
			numMapTemp.put(ratingSplit[0], ratingSplit[1]);
		}
		Map<INode,IAttribute> numMap = buildNumericalAttributes(numMapTemp,type);
						
		return new Node(type,null,numMap,nomMap,0.0);
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
		for(String datasetID : attributes.keySet()){
		
			// Find Node with dataset id 
			INode node = findNode(datasetID,type); // FIXME wie damit umgehen?
			
			// Create Attribute
			int rating = Integer.parseInt(attributes.get(datasetID));
			IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
			
			numAttributes.put(node,attribute);
		}
		
		return numAttributes;
	}
	
	private static INode findNode(String datasetID, ENodeType type) {
		// FIXME Implement
		return null;
	}

	private static Map<Object,IAttribute> buildNominalAttributes(Map<String,String> attributes) {
		// FIXME Implement
		return null;
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
