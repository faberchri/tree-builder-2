package ch.uzh.agglorecommender.recommender;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.client.ClusterResult;
import ch.uzh.agglorecommender.client.IDataset;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.PositionFinder;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

/**
 * Instantiates a new Searcher, which offers different search operations
 * on the trees and possibilities to retrieve information about the tree
 * This class acts as the connection point to the trees
 *
 */
public class Searcher {
	
	private INode rootU;
	private INode rootC;
	private ImmutableMap<String, INode> leavesMapU;
	private ImmutableMap<String, INode> leavesMapC;
	private IDataset dataset;
	private PositionFinder finder;
	private File properties;

	/**
	 * Needs to be instantiated with a reference to the clusterResult,
	 * the testDataset and the properties file of the clustering
	 * 
	 * @param clusterResult
	 * @param testDataset
	 * @param propertiesXmlFile
	 */
	public Searcher(ClusterResult clusterResult, IDataset testDataset, File propertiesXmlFile) {
		
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.rootC			= clusterResult.getContentTreeRoot();
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		this.dataset		= testDataset;
		this.finder			= new PositionFinder(this);
		this.properties		= propertiesXmlFile;
	}
	
	/**
	 * Finds the most similar node to a given input node calculated
	 * based on the category utility values from cobweb and classit
	 * 
	 * @param inputNode the node to find a similar node to
	 * @return TreePosition the position in the tree that is most similar
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public TreePosition getMostSimilarNode(INode inputNode){
		
		TreePosition position = new TreePosition(null,0,0);
		
		// Handling inputNodes which come from a testset
		if(inputNode.isLeaf()){
			if(inputNode.getNodeType() == ENodeType.User)
				position.setNode(leavesMapU.get(inputNode.getDatasetId()));
			else if(inputNode.getNodeType() == ENodeType.Content)
				position.setNode(leavesMapC.get(inputNode.getDatasetId()));
		}
		
		// Handling nodes that are created from user input
		if(position.getNode() == null){
			if(inputNode.getNodeType() == ENodeType.User){
				position = finder.getMostSimilarNode(inputNode,rootU);
			}
			else if(inputNode.getNodeType() == ENodeType.Content){
				position = finder.getMostSimilarNode(inputNode,rootC);
			}
		}
		
		return position;
	}
	
	/**
	 * Retrieves a node from a leaf set based on
	 * the datasetID
	 * 
	 * @param datasetID of the node that should be retrieved
	 * @param type of the node
	 * @return the leaf node
	 */
	public INode getNode(int datasetID, ENodeType type){
		INode node = null;
		if(type == ENodeType.User){
			node = leavesMapC.get("" + datasetID);
		}
		else if (type == ENodeType.Content){
			node = leavesMapU.get("" + datasetID);
		}
		
		return node;
	}
	
	/**
	 * Retrieves a meta information about a given node
	 * 
	 * @param node the node from which to find meta information
	 * @param attribute the attribute that should be retrieved
	 * @return meta info string
	 */
	public String getMeta(INode node, String attribute){
		Iterator<Entry<Object, Double>> it = node.getNominalAttributeValue(attribute).getProbabilities();
		String title="";
		while(it.hasNext()){
			title = (String) it.next().getKey();
		}
		return title;
	}
	
	/**
	 * Retrieves the leaf map of users
	 * 
	 * @return leaf map of users
	 */
	public ImmutableMap<String, INode> getLeavesMapU() {
		return leavesMapU;
	}

	/**
	 * Retrieves the leaf map of content
	 * 
	 * @return leaf map of content
	 */
	public ImmutableMap<String, INode> getLeavesMapC() {
		return leavesMapC;
	}

	/**
	 * Retrieves the dataset of the clustering
	 * 
	 * @return dataset
	 */
	public IDataset getDataset() {
		return dataset;
	}
	
	/**
	 * Retrieves the xml properties file
	 * 
	 * @return properties
	 */
	public File getProperties() {
		return properties;
	}
}
