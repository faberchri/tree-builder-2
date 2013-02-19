package ch.uzh.agglorecommender.client;

import java.io.File;

import ch.uzh.agglorecommender.client.jcommander.AlgoConverterValidator;
import ch.uzh.agglorecommender.client.jcommander.DatasetValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileReadValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileWriteValidator;
import ch.uzh.agglorecommender.client.jcommander.NodeUpdaterValidatorConverter;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
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
	
	@Parameter(names = { "-m", "-metaset" },
			description = "Name of meta set type",
			validateWith = DatasetValidatorConverter.class,
			converter = DatasetValidatorConverter.class,
			arity = 1)
	protected Class<? extends IDataset<?>> metasetType =  GrouplensDatasetMeta.class;
	
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
	
	@Parameter(names = { "-mf", "-meta", "-metafile" },
			description = "Path to test input file of data set. Default test data set is used if not specified. Not applicable for RandomDataset. Example: /path/to/input/file",
			validateWith = FileReadValidatorConverter.class,
			converter = FileReadValidatorConverter.class,
			arity = 1)
	protected File metaFile = null;

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
	
	@Parameter(names = { "-c", "-ca", "-contentAlgo" },
			description = "Algorithm used to cluster the content data. Allowed Values: Cobweb or Classit. Default: Classit",
			validateWith = AlgoConverterValidator.class,
			converter = AlgoConverterValidator.class,
			arity = 1)
	protected TreeComponentFactory contentTreeComponentFactory = ClassitTreeComponentFactory.getInstance();
	
	@Parameter(names = { "-u", "-ua", "-userAlgo" },
			description = "Algorithm used to cluster the user data. Allowed Values: Cobweb or Classit. Default: Classit",
			validateWith = AlgoConverterValidator.class,
			converter = AlgoConverterValidator.class,
			arity = 1)
	protected TreeComponentFactory userTreeComponentFactory = ClassitTreeComponentFactory.getInstance();
	
	@Parameter(names = { "-nodeUpdater", "-nU", "-updater" },
			description = "Simple class name of INodeUpdater to use. Example: SimpleNodeUpdater, Default: SimpleNodeUpdater",
			validateWith = NodeUpdaterValidatorConverter.class,
			converter = NodeUpdaterValidatorConverter.class,
			arity = 1)
	protected INodeUpdater nodeUpdater = new SimpleNodeUpdater();
	

}