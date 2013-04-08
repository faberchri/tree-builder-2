package ch.uzh.agglorecommender.recommender.clients;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.recommender.ClusterInteraction;
import ch.uzh.agglorecommender.recommender.utils.FileReader;
import ch.uzh.agglorecommender.recommender.utils.TreePosition;

public class DefaultClient implements Client {
	
	private boolean listen = true;
	private FileReader reader = new FileReader();
	private ClusterInteraction clusterInteraction;

	public DefaultClient(ClusterInteraction clusterInteraction) {
		this.clusterInteraction = clusterInteraction;
	}

	public void startService() throws InterruptedException, ExecutionException{
		while(this.listen==true){
			String command = inputListener();
			runCommand(command);
		}
	}
	
	public void stopService(){
		this.listen = false;
		System.out.println("service stopped");
	}
	
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
			
			System.out.println("Processing command");
			
			// Build inputNode from inputFiles
			ENodeType type = ENodeType.User; // FIXME needs to be dynamic
			File f1 = new File(fields[2]);
			File f2 = new File(fields[4]);
			List<String> metaInfo = readInputFile(f1);
			List<String> ratings  = readInputFile(f2);
			INode inputNode = clusterInteraction.buildNode(metaInfo, null, ratings, type);
			TreePosition position = clusterInteraction.getMostSimilarNode(inputNode);
			
			// Decide Action
			if(fields[0].equals("recommend")){
				clusterInteraction.recommend(inputNode,position);
			}
			else if(fields[0].equals("insert")){
				clusterInteraction.insert(inputNode,position);
			}
		}
	}

	@SuppressWarnings("resource")
	private static String inputListener(){	
		Scanner input = new Scanner(System.in);
		input.useDelimiter("\n");
		System.out.print("agglorecommender > ");
		String command = input.next( );
		
		return command;
	}

	private boolean isValidCommand (String[] fields){
		if(fields.length == 5){
			if(fields[0].equals("recommend") || fields[0].equals("insert")){
				if((fields[1].equals("-m") || fields[1].equals("-r")) && 
					(fields[3].equals("-m") || fields[3].equals("-r")) && 
					(!fields[1].equals(fields[3]))); {
							
					// Checking files
					File f1 = new File(fields[2]);
					File f2 = new File(fields[4]);
					if(f1.exists() && f2.exists()) { 
						System.out.println("Files exist");
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<String> readInputFile(File file){
		InputStream stream = reader.getCustomFileStream(file);
		List<String> lines = reader.getStreamLineByLine(stream);
		return lines;
	}
}
