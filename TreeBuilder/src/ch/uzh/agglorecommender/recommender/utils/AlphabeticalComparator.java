package ch.uzh.agglorecommender.recommender.utils;

import java.util.Comparator;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.Searcher;

public class AlphabeticalComparator implements Comparator<INode>{
	
	private Searcher searcher;


	public AlphabeticalComparator (Searcher searcher){
		this.searcher = searcher;
	}
	
	
	@Override
	public int compare(INode n1, INode n2) {
		
		String n1Title = searcher.getMeta(n1, "title");
		String n2Title = searcher.getMeta(n2, "title");
		
		return n1Title.compareTo(n2Title);
	}
}
