package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class OptimizedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator{
	
	public OptimizedMaxCUSearcher(MaxCategoryUtilitySearcherDecorator searcher){
		super(searcher);
	}

	@Override
	public IMergeResult getMaxCategoryUtilityMerge(Set<INode> openNodes) {
		// TODO implement optimization strategy
		return decoratedSearcher.getMaxCategoryUtilityMerge(openNodes);
	}

}
