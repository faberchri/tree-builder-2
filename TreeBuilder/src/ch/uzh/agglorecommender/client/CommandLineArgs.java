package ch.uzh.agglorecommender.client;

import java.io.File;

import ch.uzh.agglorecommender.client.jcommander.DatasetValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileReadValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileWriteValidator;
import ch.uzh.agglorecommender.client.jcommander.NodeUpdaterValidatorConverter;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.clusterer.treeupdate.SimpleNodeUpdater;

import com.beust.jcommander.Parameter;

public class CommandLineArgs {
	
	@Parameter(names = { "-d", "-dataset" },
			description = "Name of data set type to process. Supported Values: Grouplens, GrouplensBig, Floarea, Random. Default: Grouplens",
			validateWith = DatasetValidatorConverter.class,
			converter = DatasetValidatorConverter.class,
			arity = 1)
	protected Class<? extends IDataset<?>> datasetType =  GrouplensDataset.class;
		
	@Parameter(names = { "-tr", "-training", "-trainingDataFile" },
			description = "Path to training input file of data set. Default training data set is used if not specified. Not applicable for RandomDataset. Example: /path/to/input/file",
			validateWith = FileReadValidatorConverter.class,
			converter = FileReadValidatorConverter.class,
			arity = 1)
	protected File trainingFile = null;
	
	@Parameter(names = { "-ts", "-test", "-testDataFile" },
			description = "Path to test input file of data set. Default test data set is used if not specified. Not applicable for RandomDataset. Example: /path/to/input/file",
			validateWith = FileReadValidatorConverter.class,
			converter = FileReadValidatorConverter.class,
			arity = 1)
	protected File testFile = null;
	
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
			description = "Simple class name of INodeUpdater to use. Example: SimpleNodeUpdater, Default: SimpleNodeUpdater",
			validateWith = NodeUpdaterValidatorConverter.class,
			converter = NodeUpdaterValidatorConverter.class,
			arity = 1)
	protected INodeUpdater nodeUpdater = new SimpleNodeUpdater();
	
	@Parameter(names = { "-mU", "-metaUser" },
			description = "Meta information of users are taken into account for clustering if provided by the specified data set.")
	protected boolean useUserMetaDataForClustering = false;

	@Parameter(names = { "-mC", "-metaContent" },
			description = "Meta information of content are taken into account for clustering if provided by the specified data set.")
	protected boolean useContentMetaDataForClustering = false;
	
	@Parameter(names = { "-p", "-datasetProperties" },
			description = "Path to dataset properties xml-file. Example: /path/to/input/file",
			validateWith = FileReadValidatorConverter.class,
			converter = FileReadValidatorConverter.class,
			arity = 1,
			required = true)
	protected File datasetProperties;
	
	
}