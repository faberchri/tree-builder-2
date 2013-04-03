package ch.uzh.agglorecommender.clusterer;

import ch.uzh.agglorecommender.visu.Observer;

/**
 * The observable (a.k.a as subject) of the GoF observer pattern.
 */
public interface Observable {
	
	/**
	 * Adds an observer to this observable
	 */
	public void addObserver(Observer observer);
	
	/** 
	 * Removes an observer from this observable. 
	 */
	public void removeObserver(Observer observer);

}
