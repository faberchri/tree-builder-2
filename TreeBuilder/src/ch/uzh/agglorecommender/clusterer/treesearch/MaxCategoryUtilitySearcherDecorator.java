package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;

abstract class MaxCategoryUtilitySearcherDecorator implements IMaxCategoryUtilitySearcher, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	protected IMaxCategoryUtilitySearcher decoratedSearcher;
	
	public MaxCategoryUtilitySearcherDecorator(IMaxCategoryUtilitySearcher decoratedSearcher){
		this.decoratedSearcher = decoratedSearcher;
	}
	
	
	
}
