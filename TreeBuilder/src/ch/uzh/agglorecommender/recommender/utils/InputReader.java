package ch.uzh.agglorecommender.recommender.utils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.agglorecommender.client.AbstractDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;

public class InputReader {
	
//	public InputReader (userLeafs, contentLeafs){
//		
//	}
  
	/**
	 * Read Input File, parse lines (userid,itemid,)
	 * 
	 * @param file this file is going to be read
	 * 
	 */
	public INode processFile(File file, ENodeType type) {
		
		// Read file, read lines
		Map<String, String> numMapTemp = new HashMap<String,String>();
		Map<String, String> nomMapTemp = new HashMap<String,String>();
		
		InputStream stream = AbstractDataset.getCustomFileStream(file);
		List<String> lines = AbstractDataset.getStreamLineByLine(stream);
		for (String line : lines) {
			String[] fields = line.split("\\;");
			String[] stringFields = new String[fields.length];
			for (int i = 0; i < fields.length; i++) {
				if (i != fields.length - 1 ) {
					stringFields[i] = fields[i].trim();
				}
			}
			// Process Lines (0: name/dataset id, 1: value, 2: type)
			if(stringFields[2] == "nominal"){
				nomMapTemp.put(stringFields[0],stringFields[1]);
			}
			else if (stringFields[2] == "numerical"){
				numMapTemp.put(stringFields[0], stringFields[1]);
			}
		}
		
		// Create attributes Maps
		Map<INode,IAttribute> numMap = buildNumericalAttributes(numMapTemp,type);
		Map<Object,IAttribute> nomMap = buildNominalAttributes(nomMapTemp);
		
		// Node creator
		return new Node(type,null,numMap,nomMap,0.0);
	}
	
	
	/**
	 * Build attribute from line
	 * 
	 * @param file this file is going to be read
	 * 
	 */
	public static Map<Object,IAttribute> buildNominalAttributes(Map<String,String> attributes) {
		return null;
		
	}
	
	/**
	 * Build attribute from line
	 * 
	 * @param file this file is going to be read
	 * 
	 */
	public static Map<INode,IAttribute> buildNumericalAttributes(Map<String,String> attributes, ENodeType type) {
	
		Map<INode,IAttribute> numAttributes = new HashMap<INode,IAttribute>();
		for(String datasetID : attributes.keySet()){
		
			// Find Node with dataset id 
			INode node = findNode(datasetID,type);
			
			// Create Attribute
			int rating = Integer.parseInt(attributes.get(datasetID));
			IAttribute attribute = new ClassitAttribute(1,rating,Math.pow(rating,2));
			
			numAttributes.put(node,attribute);
		}
		
		return numAttributes;
	}
	
	private static INode findNode(String datasetID, ENodeType type){
		return null;
		
	}

}
