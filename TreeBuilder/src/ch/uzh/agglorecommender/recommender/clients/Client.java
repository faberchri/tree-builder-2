package ch.uzh.agglorecommender.recommender.clients;


public interface Client {
	
	void startService() throws Exception;
	void stopService() throws Exception;

}
