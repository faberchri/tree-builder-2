package ch.uzh.agglorecommender.recommender.clients;

import java.io.File;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.ClusterInteraction;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

import com.google.common.collect.ImmutableMap;

public class DefaultClient implements IClient {
	
	private boolean listen = true;
	private ClusterInteraction clusterInteraction;
	
	/**
	 * Defines the connection for the client
	 * to interact with the cluster result
	 */
	public void setController(ClusterInteraction clusterInteraction) {
		this.clusterInteraction = clusterInteraction;
	}

	/**
	 * Method to start the service
	 */
	public void startService() throws InterruptedException, ExecutionException{
		while(this.listen==true){
			String command = inputListener();
			runCommand(command);
		}
	}
	
	/**
	 * Method to stop the service
	 */
	public void stopService(){
		this.listen = false;
		System.out.println("service stopped");
	}
	
	/**
	 * Parses and runs a defined command
	 * 
	 * @param command the command input
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void runCommand(String command) throws InterruptedException, ExecutionException{
		
		// Stop signal for system
		if(command.equals("stop")){
			stopService();
			System.exit(0);
		}
		
		// Split command
		String[] fields = command.split("\\s+");
		
		// Check validity
		if(isValidCommand(fields)){
			
			// Get Information from Fields; Action, Files
			String pathRatings = "";
			String pathMeta = "";
			if(fields[2].equals("-r")){
				pathRatings = fields[3];
				pathMeta	= fields[5];
			}
			if(fields[2].equals("-m")){
				pathRatings = fields[5];
				pathMeta 	= fields[3];
			}
			ENodeType type = null;
			if(fields[1].equals("user")){
				type = ENodeType.User;
			}
			if(fields[1].equals("content")){
				type = ENodeType.Content;
			}

			ImmutableMap<String, INode> inputNodes = clusterInteraction.buildNodesFromFile(pathRatings,pathMeta, type);
			
			if(inputNodes != null){

				Set<INode> testNodes = new HashSet<>();
				for(String key : inputNodes.keySet()){

					INode inputNode = inputNodes.get(key);
					TreePosition position = clusterInteraction.getMostSimilarNode(inputNode);

					// Decide Action
					if(fields[0].equals("recommend")){
						SortedMap<INode,IAttribute> recommendation = clusterInteraction.recommend(inputNode,position);
						clusterInteraction.printRecommendation(recommendation);
					}
					if(fields[0].equals("insert")){
						System.out.println(clusterInteraction.insert(inputNode,position));
					}

					testNodes.add(inputNode);
				}

				if(fields[0].equals("evaluate")){
					clusterInteraction.kFoldEvaluation(testNodes);
				}
			}
		}
	}

	/**
	 * Reads the command from the command line
	 * 
	 * @return 
	 */
	@SuppressWarnings("resource")
	private static String inputListener(){	
		Scanner input = new Scanner(System.in);
		input.useDelimiter("\n");
		System.out.print("agglorecommender > ");
		String command = input.next( );
		
		return command;
	}

	/**
	 * Checks if the provided command has a 
	 * valid format 
	 * 
	 * @param fields the split command
	 * @return boolean value about validity of command
	 */
	private boolean isValidCommand (String[] fields){
		if(fields.length == 6){
			if(fields[0].equals("recommend") || fields[0].equals("insert") || fields[0].equals("evaluate")){
				if(fields[1].equals("user") || fields[1].equals("content")){
					if((fields[2].equals("-m") || fields[2].equals("-r")) && 
							(fields[4].equals("-m") || fields[4].equals("-r")) && 
							(!fields[2].equals(fields[4]))) {

						// Checking files
						File f1 = new File(fields[3]);
						File f2 = new File(fields[5]);
						if(f1.exists() && f2.exists()) { 
							return true;
						}
						else System.err.println("File not found");
					}
				}
			}
		}
		System.out.println("Command not valid");
		return false;
	}
}
