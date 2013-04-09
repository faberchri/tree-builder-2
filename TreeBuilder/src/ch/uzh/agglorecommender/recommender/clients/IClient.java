package ch.uzh.agglorecommender.recommender.clients;

import ch.uzh.agglorecommender.recommender.ClusterInteraction;


public interface IClient {
	
	public void setController(ClusterInteraction clusterInteraction);
	public void startService() throws Exception;
	public void stopService() throws Exception;

}
