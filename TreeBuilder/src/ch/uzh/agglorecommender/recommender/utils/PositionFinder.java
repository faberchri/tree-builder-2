package ch.uzh.agglorecommender.recommender.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treesearch.ClassitMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.clusterer.treesearch.CobwebMaxCategoryUtilitySearcher;
import ch.uzh.agglorecommender.recommender.Searcher;

public class PositionFinder {
	
	private CobwebMaxCategoryUtilitySearcher cobwebSearcher;
	private ClassitMaxCategoryUtilitySearcher classitSearcher;
	private NodeUnpacker unpacker;
	private int threads;
	
	public PositionFinder(Searcher searcher){
		
		this.cobwebSearcher 	= new CobwebMaxCategoryUtilitySearcher();
		this.classitSearcher 	= new ClassitMaxCategoryUtilitySearcher();
		this.unpacker 			= new NodeUnpacker(searcher);
		this.threads 			= Runtime.getRuntime().availableProcessors();
		
	}
	
	/**
	 * Finds the best position (most similar node) in the tree for a given node
	 * Calculations are based on category utility. If the previously calculated
	 * utility value is higher than the best utility value on the current level
	 * then the previous position is the best. If the search does not stop in
	 * the tree, it ends on a leaf node.
	 * 
	 * @param inputNodeID this node is the base of the search
	 * @param node this is the current starting point of the search
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public TreePosition getMostSimilarNode(INode inputNode,INode root) throws InterruptedException, ExecutionException {
		
		// Grab all nodes from tree
		List<TreePosition> rawPos 	= getAllNodesOfTree(root);
		
		// Split list into subpackets
		List<List<TreePosition>> listCollection = new LinkedList<List<TreePosition>>();
		int portion = (int) ((double) rawPos.size() / threads);
		
		for(int i = 0; i < rawPos.size(); i = i + portion){
			
			List<TreePosition> subList;
			if(i + portion <= (rawPos.size()-1)){
				subList = rawPos.subList(i, i + portion);
			}
			else {
				subList = rawPos.subList(i, rawPos.size()-1);
			}
			
			listCollection.add(subList);
		}
		
		// Calculate utilities of all positions in parallel
		List<TreePosition> finalPos	= calculateUtilites(inputNode, listCollection);
		
		// Find best position
		TreePosition bestPosition = new TreePosition(null,0,0);
		for(TreePosition tempPosition : finalPos) {
			
			// Find child with highest utility of all children and higher utility than previously found
			if(tempPosition != null){
				if(tempPosition.getUtility() > bestPosition.getUtility()){
					bestPosition = tempPosition;
				}
				else if(tempPosition.getUtility().equals(bestPosition.getUtility())){
					if(tempPosition.getLevel() > bestPosition.getLevel()){
						bestPosition = tempPosition;
					}
				}
			}
		}
		
		return bestPosition;
	}
	
	private List<TreePosition> calculateUtilites(final INode inputNode,
			List<List<TreePosition>> listCollection) throws InterruptedException, ExecutionException {
	
	    ExecutorService service = Executors.newFixedThreadPool(threads);
	
	    List<Future<List<TreePosition>>> futures = new ArrayList<Future<List<TreePosition>>>();
	    for (final List<TreePosition> positionList  : listCollection) {
	        Callable<List<TreePosition>> callable = new Callable<List<TreePosition>>() {
	            
	        	public List<TreePosition> call() throws Exception {
	        		
	        		for(TreePosition position : positionList){
		        		
	        			// Calculate Utility of Position
		    			List<INode> nodesToCalculate = new LinkedList<INode>();
		    			INode unpackedNode = unpacker.unpack(position.getNode());
		    			nodesToCalculate.add(inputNode);
		    			nodesToCalculate.add(unpackedNode);
		    			
		    			// Determine Weight
		    			Set<Object> numAtts = new HashSet<Object>();
		    			Set<Object> nomAtts = new HashSet<Object>();
		    			for (INode n : nodesToCalculate) {
		    				numAtts.addAll(n.getRatingAttributeKeys());
		    				numAtts.addAll(n.getNumericalMetaAttributeKeys());
		    				nomAtts.addAll(n.getNominalMetaAttributeKeys());
		    			}
		    			
		    			double numOfNomAtts = nomAtts.size();
		    			double numOfNumAtts = numAtts.size();
		    
		    			double sumOfAtts = numOfNomAtts + numOfNumAtts;
		    			
		    			double cobweb = cobwebSearcher.calculateCategoryUtility(nodesToCalculate);
		    			double classit = classitSearcher.calculateCategoryUtility(nodesToCalculate);
		    			
		    			double utility = 0;
		    			if(inputNode.getRatingAttributeKeys().size() == 0){
		    				utility += cobweb;
		    				utility += classit;
		    			}
		    			else {
		    				utility += classit * (numOfNumAtts / sumOfAtts);
		    		 		utility += cobweb * (numOfNomAtts / sumOfAtts);
		    			}
		    			position.setUtility(utility);
	        		}
	        		return positionList;
	            }
	        	
	        };
	        futures.add(service.submit(callable));
	    }
	
	    service.shutdown();
	
	    List<TreePosition> outputs = new ArrayList<>();
	    for (Future<List<TreePosition>> future : futures) {
	        outputs.addAll(future.get());
	    }
	    return outputs;
	}

	private List<TreePosition> getAllNodesOfTree(INode node) {
		
		Iterator<INode> it = node.getChildren();
		List<TreePosition> list = new ArrayList<>();
		
		TreePosition position = new TreePosition(node,-1,0);
		list.add(position);
		
		while (it.hasNext()) {
			INode n = (INode) it.next();
			list.addAll(getAllNodesOfTree(n, 1));		
		}
		return list;
	}
	
	private List<TreePosition> getAllNodesOfTree(INode node, int level) {
		
		List<TreePosition> li = new ArrayList<>(); 
		
		TreePosition position =  new TreePosition(node,-1,level);
		li.add(position);
		
		Iterator<INode> it = node.getChildren();
		while (it.hasNext()) {
			INode child = it.next();				
			li.addAll(getAllNodesOfTree(child, level+1));
		}
		return li;
	}	
}
