package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.List;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class OptimizedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator{
	
	public OptimizedMaxCUSearcher(MaxCategoryUtilitySearcherDecorator searcher){
		super(searcher);
	}

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<List<INode>> combinations) {
		// TODO implement optimization strategy, i.e. removing entries in combinations set.
		return decoratedSearcher.getMaxCategoryUtilityMerges(combinations);
	}


}
