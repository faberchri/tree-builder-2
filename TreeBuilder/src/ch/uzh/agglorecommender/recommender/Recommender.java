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
 * 
 * Calculates different recommendations based on a given tree structure
 * A test of the rmse value of the recommendation can be run by runTestRecommendation()
 * A recommendation of movies a user has not yet rated can be run by runRecommendation()
 *
 */
public final class Recommender {
	
	private Searcher searcher;

	/**
	 * Instantiates a new recommendation builder which can give recommendations based on a given tree structure
	 * 
	 * @param clusterResult the trees on which the recommendation calculation is done
	 * @param testDataset 
	 * @param radiusU indicates the number of levels that should be incorporated from user tree
	 * @param radiusC indicates the number of levels that should be incorporated from content tree
	 */
	public Recommender(Searcher searcher) {
		this.searcher = searcher;
	}
	
	public List<INode> createItemList(ENodeType type, int limit, INode inputNode){
		
		List<String> watched = createWatchedList(inputNode);
		
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

	public List<String> createWatchedList(INode inputNode){
		
		// Create List of movies that the user has already rated (user is leaf and has leaf attributes)
		List<String> watched = new LinkedList<String>();
		Set<INode> ratingKeys = inputNode.getRatingAttributeKeys();
		for(INode rating : ratingKeys) {
			watched.addAll(rating.getDataSetIds());
		}
		
		return watched;
	}

	/**
	 * Collect all the content that should be recommended based on given user and radius
	 * 
	 * @param position starting point to calculate recommendation 
	 * @param watched list of dataset items the user has already rated
	 */
	public Map<INode, IAttribute> recommend(INode position, List<String> watched) {
		
		NodeUnpacker unpacker = new NodeUnpacker(searcher);
		INode unpacked = unpacker.unpack(position);
		
		Map<INode,IAttribute> recommendation = new HashMap<INode,IAttribute>();
		
		for(INode attKey : unpacked.getRatingAttributeKeys()){
			if(!(watched.contains(attKey.getDatasetId()))){
				recommendation.put(attKey, unpacked.getNumericalAttributeValue(attKey));
			}
		}
		
	    return recommendation;
	}

	/**
	 * Sorts the recommendation with an insertion sort and returns the movies which the
	 * user would most likely rate the best respectively the worst depending on the sorting
	 * parameter. the number of returned recommendations depends on the limit parameter.
	 * 
	 * @param unsortedRecommendation the unsorted recommendation
	 * @param direction defines if best or worst recommendations are returned (1=best,0=worst)
	 * @param limit defines the number of returned recommendations
	 */
	public SortedMap<INode, IAttribute> rankRecommendation(Map<INode, IAttribute> unsortedRecommendation,int direction, int limit){
	
		// Order according to ratings
		RatingComparator bvc =  new RatingComparator(unsortedRecommendation);
		SortedMap<INode,IAttribute> sortedRecommendation = new TreeMap<>(bvc);
		sortedRecommendation.putAll(unsortedRecommendation);
		
//		// Create Sets of items with same rating and order alphabetically
//		AlphabeticalComparator avc = new AlphabeticalComparator(searcher);
//		SortedMap<Double,TreeSet<INode>> orderedMap = new TreeMap<Double,TreeSet<INode>>();
//		
//		for(Entry<INode,IAttribute> entry : sortedRecommendation.entrySet()){
//			double rating = entry.getValue().getSumOfRatings()/entry.getValue().getSupport();
//			TreeSet<INode> tempSet = null;
//			if(orderedMap.containsKey(rating)){
//				 tempSet = orderedMap.get(rating);
//			}
//			else {
//				tempSet = new TreeSet<>(avc);
//			}
//			tempSet.add(entry.getKey());
//			orderedMap.put(rating, tempSet);
//		}
//		
//		// Rebuild SortedMap
//		sortedRecommendation = new TreeMap<>();
//		for(Entry<Double,TreeSet<INode>> entry : orderedMap.entrySet()){
//			
//			// Find Information about Recommendation
//			double rating = entry.getKey();
//			
//			TreeSet<INode> tempSet = entry.getValue();
//			for(INode node : tempSet){
//				sortedRecommendation.put(node,unsortedRecommendation.get(node));
//			}
//		}
		
	    return sortedRecommendation;
	}

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
