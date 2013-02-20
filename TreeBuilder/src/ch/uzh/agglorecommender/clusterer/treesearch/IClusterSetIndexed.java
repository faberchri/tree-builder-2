package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.set.TIntSet;

import java.util.Collection;

public interface IClusterSetIndexed<E> extends IClusterSet<E> {

	public Collection<E> getCombination(int combinationId);
	
	public TIntSet getCombinationsIds();
	
	public TIntSet getCombinationsIds(E e);
}
