package ch.uzh.agglorecommender.clusterer.treesearch;


/**
 * A decorator for the BasicMaxCategoryUtilitySearcher
 * in the sense of the GoF decorator pattern.
 *
 */
abstract class MaxCategoryUtilitySearcherDecorator implements IMaxCategoryUtilitySearcher {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The concrete component of the GoF decorator pattern.
	 * Might be already decorated.
	 */
	protected IMaxCategoryUtilitySearcher decoratedSearcher;
	
	/**
	 * Instantiates a new decorator with the passed object as concrete component.
	 * @param decoratedSearcher the concrete component, might be wrapped in a decorator.
	 */
	public MaxCategoryUtilitySearcherDecorator(IMaxCategoryUtilitySearcher decoratedSearcher){
		this.decoratedSearcher = decoratedSearcher;
	}
}
