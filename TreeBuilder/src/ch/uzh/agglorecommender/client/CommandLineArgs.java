package ch.uzh.agglorecommender.client;

import ch.uzh.agglorecommender.client.jcommander.AlgoConverterValidator;
import ch.uzh.agglorecommender.client.jcommander.DatasetValidatorConverter;
import ch.uzh.agglorecommender.client.jcommander.FileReadValidator;
import ch.uzh.agglorecommender.client.jcommander.FileWriteValidator;
import ch.uzh.agglorecommender.client.jcommander.NodeUpdaterValidatorConverter;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.TreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treeupdate.INodeUpdater;
import ch.uzh.agglorecommender.clusterer.treeupdate.SimpleNodeUpdater;

import com.beust.jcommander.Parameter;

public class CommandLineArgs {
	
	@Parameter(names = { "-d", "-dataset" },
			description = "Name of data set type to process. Supported Values: Grouplens, Floarea, Random. Default: Grouplens",
			validateWith = DatasetValidatorConverter.class,
			converter = DatasetValidatorConverter.class,
			arity = 1)
	protected Class<? extends IDataset<?>> datasetType =  GrouplensDataset.class;
	
	@Parameter(names = { "-f", "-inputDataFile" },
			description = "Path to input file of data set. Default data set is used if not specified. Is not applicable for RandomDataset. Example: /path/to/input/file",
			validateWith = FileReadValidator.class,
			arity = 1)
	protected String datasetFile = null;
	
	@Parameter(names = { "-resume", "-r"},
			description = "Path to file of a serialized run to resume. If not specified clustering of the data set is started from scratch. Example: /path/to/input/of/type.ser",
			validateWith = FileReadValidator.class,
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