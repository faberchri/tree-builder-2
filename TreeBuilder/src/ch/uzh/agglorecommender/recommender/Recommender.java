package ch.uzh.agglorecommender.recommender;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.utils.NodeUnpacker;
import ch.uzh.agglorecommender.recommender.utils.RatingComparator;

/**
 * Instantiates a new Recommender, which offers the possibility to recommend
 * nodes and different helper methods supporting the recommendation
 *
 */
public final class Recommender {
	
	private Searcher searcher;
	
	/**
	 * Needs to be instantiated with a reference to a searcher instance,
	 * which provides connections to the trees
	 * 
	 * @param searcher
	 */
	public Recommender(Searcher searcher) {
		this.searcher = searcher;
	}
	
	/**
	 * Retrieves a random list of available users or content items
	 * 
	 * @param type type of nodes
	 * @param limit number of nodes
	 * @param inputNode items already contained in this item are skipped
	 * @return List generated list of nodes
	 */
	public List<INode> createItemList(ENodeType type, int limit, INode inputNode){
		
		List<String> watched = createKnownList(inputNode);
		
		List<INode> itemList = new LinkedList<INode>();
		Random randomGenerator = new Random();
		
		for(int i = 0;i < limit;i++){
			INode randomNode = null;
			if(type == ENodeType.Content){
				int randomID = randomGenerator.nextInt(searcher.getLeavesMapU().size());
				randomNode = searcher.getLeavesMapU().get("" + randomID);
			}
			else if (type == ENodeType.User){
				int randomID = randomGenerator.nextInt(searcher.getLeavesMapC().size());
				randomNode = searcher.getLeavesMapC().get("" + randomID);	
			}
			
			// Null check
			if(randomNode != null){
			
				// Already Added Check
				if(!itemList.contains(randomNode)){
				
					// Already Rated Check
					if(watched != null){
						if(randomNode.getDatasetId() != null){
							if(!watched.contains(randomNode.getDatasetId())){
								itemList.add(randomNode);
							}
						}
					}
				}
			}
		}
		
		return itemList;
	}

	/**
	 * Creates a list of nodes known to the input node
	 * 
	 * @param inputNode the node used to create the list
	 * @return list of known nodes
	 */
	public List<String> createKnownList(INode inputNode){
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<String> watched = new LinkedList<String>();
		Set<INode> ratingKeys = inputNode.getRatingAttributeKeys();
		for(INode rating : ratingKeys) {
			watched.addAll(rating.getDataSetIds());
		}
		
		return watched;
	}

	/**
	 * Collect all the nodes to recommend based on position and known nodes
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param known list of dataset items the user has already rated
	 */
	public Map<INode, IAttribute> recommend(INode position, List<String> known) {
		
		// Unpack the combined attributes to original productions
		NodeUnpacker unpacker = new NodeUnpacker(searcher);
		INode unpacked = unpacker.unpack(position);
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		for(INode attKey : unpacked.getRatingAttributeKeys()){
			if(!(known.contains(attKey.getDatasetId()))){
				recommendation.put(attKey, unpacked.getNumericalAttributeValue(attKey));
			}
		}
		
	    return recommendation;
	}

	/**
	 * Sorts the recommendation based on the ratings with the highest rating on top
	 * 
	 * @param unsortedRecommendation the unsorted recommendation
	 */
	public SortedMap<INode, IAttribute> rankRecommendation(Map<INode, IAttribute> unsortedRecommendation){
	
		// Order according to ratings
		RatingComparator bvc =  new RatingComparator(unsortedRecommendation);
		SortedMap<INode,IAttribute> sortedRecommendation = new TreeMap<>(bvc);
		sortedRecommendation.putAll(unsortedRecommendation);
		
	    return sortedRecommendation;
	}

	/**
	 * Prints a recommendation collection
	 * 
	 * @param recommendation collection of recommendations
	 */
	public void printRecommendation(SortedMap<INode, IAttribute> recommendation){
		
		if(recommendation != null){
			System.out.println("=> Recommended: ");
			
			for(Entry<INode,IAttribute> entry : recommendation.entrySet()){
				double rating = entry.getValue().getSumOfRatings() / entry.getValue().getSupport();
				System.out.printf("%.2f", rating);
				String title = searcher.getMeta(entry.getKey(),"title");
				System.out.println(" -> " + title);
			}
		}
	}

}
