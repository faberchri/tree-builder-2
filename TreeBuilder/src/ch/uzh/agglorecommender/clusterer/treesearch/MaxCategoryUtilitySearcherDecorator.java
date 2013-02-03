package ch.uzh.agglorecommender.clusterer.treesearch;

abstract class MaxCategoryUtilitySearcherDecorator implements IMaxCategoryUtilitySearcher {

	protected IMaxCategoryUtilitySearcher decoratedSearcher;
	
	public MaxCategoryUtilitySearcherDecorator(IMaxCategoryUtilitySearcher decoratedSearcher){
		this.decoratedSearcher = decoratedSearcher;
	}
	
	
	
}
