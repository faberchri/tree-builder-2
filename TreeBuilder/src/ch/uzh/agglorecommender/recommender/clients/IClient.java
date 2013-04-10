package ch.uzh.agglorecommender.recommender.clients;

import ch.uzh.agglorecommender.recommender.ClusterInteraction;

public interface IClient {
	
	/**
	 * Defines the connection for the client
	 * to interact with the cluster result
	 * 
	 * @param clusterInteraction the proxy for cluster interaction
	 */
	public void setController(ClusterInteraction clusterInteraction);
	
	/**
	 * Method to start the service
	 * 
	 * @throws Exception
	 */
	public void startService() throws Exception;
	
	/**
	 * Method to stop the service
	 * 
	 * @throws Exception
	 */
	public void stopService() throws Exception;

}
