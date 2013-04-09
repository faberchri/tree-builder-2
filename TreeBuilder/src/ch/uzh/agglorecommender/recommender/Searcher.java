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

public class Searcher {
	
	private INode rootU;
	private INode rootC;
	private ImmutableMap<String, INode> leavesMapU;
	private ImmutableMap<String, INode> leavesMapC;
	private IDataset dataset;
	private PositionFinder finder;
	private File properties;

	public Searcher(ClusterResult clusterResult, IDataset testDataset, File propertiesXmlFile) {
		
		// Retrieve Root Nodes of the user tree
		this.rootU  		= clusterResult.getUserTreeRoot(); 
		this.rootC			= clusterResult.getContentTreeRoot();
		this.leavesMapU 	= clusterResult.getUserTreeLeavesMap();
		this.leavesMapC 	= clusterResult.getContentTreeLeavesMap();
		this.dataset		= testDataset;
		this.finder			= new PositionFinder(this);
		this.properties		= propertiesXmlFile;
	}
	
	public TreePosition getMostSimilarNode(INode inputNode) throws InterruptedException, ExecutionException{
		
		// Find position of the similar node in the tree
		TreePosition position = new TreePosition(null,0,0);
		if(inputNode.isLeaf()){
			if(inputNode.getNodeType() == ENodeType.User)
				position.setNode(leavesMapU.get(inputNode.getDatasetId()));
			else if(inputNode.getNodeType() == ENodeType.Content)
				position.setNode(leavesMapC.get(inputNode.getDatasetId()));
		}
		
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
	
	public String getMeta(INode node, String attribute){
		Iterator<Entry<Object, Double>> it = node.getNominalAttributeValue(attribute).getProbabilities();
		String title="";
		while(it.hasNext()){
			title = (String) it.next().getKey();
		}
		return title;
	}
	
	public ImmutableMap<String, INode> getLeavesMapU() {
		return leavesMapU;
	}

	public ImmutableMap<String, INode> getLeavesMapC() {
		return leavesMapC;
	}

	public IDataset getDataset() {
		return dataset;
	}
	
	public File getProperties() {
		return properties;
	}
}
