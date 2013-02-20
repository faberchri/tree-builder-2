package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.map.TIntDoubleMap;
import gnu.trove.set.TIntSet;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class OptimizedMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator implements Serializable{
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	public OptimizedMaxCUSearcher(IMaxCategoryUtilitySearcher searcher){
		super(searcher);
	}

	@Override
	public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<Collection<INode>> combinations, IClusterSet<INode> clusterSet) {
		// TODO implement optimization strategy, i.e. removing entries in combinations set.
		return decoratedSearcher.getMaxCategoryUtilityMerges(combinations, clusterSet);
	}

	@Override
	public TIntDoubleMap getMaxCategoryUtilityMerges(
			TIntSet combinationIds, IClusterSetIndexed<INode> clusterSet) {
		// TODO Auto-generated method stub
		return decoratedSearcher.getMaxCategoryUtilityMerges(combinationIds, clusterSet);
	}


}
