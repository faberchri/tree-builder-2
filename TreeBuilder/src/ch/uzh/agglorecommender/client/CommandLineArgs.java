package ch.uzh.agglorecommender.client;

import java.util.List;

import ch.uzh.agglorecommender.client.jcommander.DatasetValidator;
import ch.uzh.agglorecommender.client.jcommander.FileReadValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileWriteValidator;
import ch.uzh.agglorecommender.client.jcommander.NodeUpdaterValidatorConverter;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.clusterer.treeupdate.SaveAttributeNodeUpdater;

import com.beust.jcommander.Parameter;

/**
 * 
 * The command line arguments to configure the application.
 * @see <a href="http://jcommander.org">JCommander</a>
 *
 */
public class CommandLineArgs {
	
	@Parameter(names = { "-resume", "-r"},
			description = "Path to file of a serialized run to resume. If not specified clustering of the data set is started from scratch. Example: /path/to/input/of/type.ser",
			validateWith = FileReadValidatorConverter.class,
			arity = 1)
	protected String resumePrevRun = null;
	
	@Parameter(names = { "-serialize", "-s"},
			description = "Path where serialized TreeBuilder is stored. If not specified the TreeBuilder is not serialized and the clustering result is lost! Example: /location/to/store/output.ser",
			validateWith = FileWriteValidator.class,
			arity = 1)
	protected String serializeRun = null;
		
	@Parameter(names = { "-nodeUpdater", "-nU", "-updater" },
			description = "Simple class name of INodeUpdater to use. Example: SimpleNodeUpdater",
			validateWith = NodeUpdaterValidatorConverter.class,
			converter = NodeUpdaterValidatorConverter.class,
			arity = 1)
	protected INodeUpdater nodeUpdater = new SaveAttributeNodeUpdater();
		
	public static final String randomDatasetIdentifier = "random";
	
	@Parameter(names = { "-p", "-datasetProperties" },
			description = "Path to dataset properties xml-file. Parameter is required!" +
					"\n\tExample: path/to/prop/file.xml" +
					"\n\tFor testing you can use \"random\" as parameter value to use a randomly " +
					"generated dataset of ratings. You can also specifie the size of the random data set.\n" +
					"\tExample: random \t generates a random data set with default size and sparcity.\n" +
					"\tExample: random 100 200 82.5 \t generates a random data set with 100 users, 200 content items " +
					"and approx. 82.5% of null entries in the user/content-matrix.",
			validateWith = DatasetValidator.class,
			variableArity = true,
			required = true)
	protected List<String> datasetProperties;
	
	@Parameter(names = { "-noGUI" },
			description = "Runs the clustering algorithm without a GUI (no visual representation of progress, no user interaction possible).")
	protected boolean noGui = false;
	
	@Parameter(names = {"--help", "-help", "--usage", "-usage"},
			description = "Shows this message and terminates application.",
			help = true)
	protected boolean help;

}