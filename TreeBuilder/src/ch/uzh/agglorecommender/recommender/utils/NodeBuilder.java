package ch.uzh.agglorecommender.recommender.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlException;

import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.client.IDatasetItem;
import ch.uzh.agglorecommender.client.InputParser;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNominalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.ContentNumericalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNominalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.InputDocument.Input.UserNumericalAttribute;
import ch.uzh.agglorecommender.client.inputbeans.RatingFile;
import ch.uzh.agglorecommender.clusterer.InitialNodesCreator;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.CobwebAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.recommender.Searcher;

import com.google.common.collect.ImmutableMap;

/**
 * Provides methods to build nodes from different input sources 
 *
 */
public class NodeBuilder {
	
	private static Searcher searcher;

	/**
	 * Needs to be instantiated with a reference to a searcher instance,
	 * which provides connections to the trees
	 * 
	 * @param searcher
	 */
	public NodeBuilder (Searcher searcher){
		this.searcher = searcher;
	}
	
	/**
	 * Builds node from string input
	 * 
	 * @param nomMetaInfo the nominal meta information
	 * @param numMetaInfo the numerical meta information
	 * @param ratings the rating information
	 * @param type type of the node
	 * @return
	 */
	public INode buildNode(List<String> nomMetaInfo,List<String> numMetaInfo, List<String> ratings, ENodeType type){
		
		
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
		
		// Build children
		IDataset dataset = searcher.getDataset();
		INode child = new Node(null,null,dataset); 
		Collection<INode> children = new HashSet<>();
		children.add(child);
		
		return new Node(type,children,ratingsMap,numMetaMap,nomMetaMap,0.0);
	}
	
	/**
	 * Builds one or multiple nodes from file input
	 * 
	 * @param ratingLocation the location of the rating file
	 * @param metaLocation the location of the respective meta file
	 * @param type the type of node that should be built
	 * @return ImmutableMap collection of built nodes
	 */
	public ImmutableMap<String, INode> buildNodesFromFile(String ratingLocation, String metaLocation, ENodeType type){
		
		// Redefine Locations in Properties File
		File properties = searcher.getProperties();
		InputDocument inputDoc = null;
		try {
			inputDoc = InputDocument.Factory.parse(properties);
		} catch (XmlException | IOException e) {
			System.err.println("Error while parsing the data set property file: " + properties.getPath());
			e.printStackTrace();
		}
		
		RatingFile[] fileA = inputDoc.getInput().getRatingArray()[0].getFileArray();
		for(RatingFile att : fileA){
			att.setLocation(ratingLocation);
		}
		
		if(type == ENodeType.User){
			
			UserNominalAttribute[] userNomA = inputDoc.getInput().getUserNominalAttributeArray();
			for(UserNominalAttribute  att : userNomA){
					att.getFileArray()[0].setLocation(metaLocation);
			}
			
			UserNumericalAttribute[] userNumA = inputDoc.getInput().getUserNumericalAttributeArray();
			for(UserNumericalAttribute  att : userNumA){
				att.getFileArray()[0].setLocation(metaLocation);
			}
			
		}
		if(type == ENodeType.Content){
			
			ContentNominalAttribute[] contentNomA = inputDoc.getInput().getContentNominalAttributeArray();
			for(ContentNominalAttribute  att : contentNomA){
				att.getFileArray()[0].setLocation(metaLocation);
			}
			
			ContentNumericalAttribute[] contentNumA = inputDoc.getInput().getContentNumericalAttributeArray();
			for(ContentNumericalAttribute  att : contentNumA){
				att.getFileArray()[0].setLocation(metaLocation);
			}
			
		}
		
		// Create updated file
		File updated = null;
		try{
    	    updated = File.createTempFile("tempfile", ".tmp"); 
    	    BufferedWriter bw = new BufferedWriter(new FileWriter(updated));
    	    bw.write(inputDoc.xmlText().toString());
    	    bw.close();
 
    	} 
		catch(IOException e){
    	    e.printStackTrace();
    	    return null;
    	}
		
		// Read Data
		InputParser parseData = null;
		InitialNodesCreator in = null;
		
		try {
			parseData = new InputParser(updated);
		}
		catch (Exception e){
			e.printStackTrace();
			return null;
		}
		
		Iterator<IDatasetItem> it = parseData.getTrainigsDataset().iterateOverDatasetItems();
		while(it.hasNext()){
			IDatasetItem item = (IDatasetItem) it.next();
		}

		try{
			in = new InitialNodesCreator(
				parseData.getTrainigsDataset(),
				TreeComponentFactory.getInstance()
				);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		if(type == ENodeType.User){
			return in.getUserLeaves();
		}
		else if (type == ENodeType.Content){
			return in.getContentLeaves();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Build rating attribute from string input
	 * 
	 * @param attributes the rating information
	 * @param type of node
	 * @return IAttribute rating attribute
	 */
	private static Map<INode, IAttribute> buildRatingAttributes(Map<String,String> attributes, ENodeType type) {
		
		Map<INode,IAttribute> numAttributes = new HashMap<>();
		for(String datasetIDString: attributes.keySet()){
			
			// Find Node with dataset id 
			if(datasetIDString != null){
				
				int datasetID;
				try{
					datasetID = Integer.parseInt(datasetIDString);
				}
				catch(Exception e){
					System.out.println(e);
					break;
				}
				
				INode node = searcher.getNode(datasetID, type);
			
				if(node != null){
					// Create Attribute
					int rating = Integer.parseInt(attributes.get(datasetIDString));
					IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
					
					numAttributes.put(node,attribute);
				}
			}
		}
		return numAttributes;
	}
	
	/**
	 * Build nominal meta attribute from string input
	 * 
	 * @param attributes the nominal meta information
	 * @return IAttribute nominal meta attribute
	 */
	private static Map<String, IAttribute> buildNomMetaAttributes(Map<String,String> attributes){
		
		Map<String,IAttribute> nomMeta = new HashMap<>();
		for(String attKey : attributes.keySet()){
			Map<String,Double> probabilityMap = new HashMap<String,Double>();
			probabilityMap.put(attributes.get(attKey),1.0);
			nomMeta.put(attKey, new CobwebAttribute(ImmutableMap.copyOf(probabilityMap)));
		}
		
		return nomMeta;
	}
	
	/**
	 * Build numerical meta attribute from string input
	 * 
	 * @param attributes the numerical meta information
	 * @return IAttribute numerical meta attribute
	 */
	private static Map<String, IAttribute> buildNumMetaAttributes(Map<String,String> attributes){
		
		Map<String,IAttribute> numMeta = new HashMap<>();
		for(String attKey : attributes.keySet()){
			int rating = Integer.parseInt(attributes.get(attKey));
			IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
			numMeta.put(attKey, attribute);
		}
		
		return numMeta;
	}

}
