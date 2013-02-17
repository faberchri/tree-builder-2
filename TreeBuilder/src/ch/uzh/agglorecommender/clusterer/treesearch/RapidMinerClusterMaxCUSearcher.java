package ch.uzh.agglorecommender.clusterer.treesearch;

/*
//This class is going to use RapidMiner to cluster the data. 
//RapidMiner must be installed on computer (no source code needed, only installed program). 
//Uses RapidMinerDataTransformator to parse data to RapidMiner format

public class RapidMinerClusterMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator{
	   
	    /**
	 * 
	 
	private static final long serialVersionUID = 1L;

		public RapidMinerClusterMaxCUSearcher(IMaxCategoryUtilitySearcher searcher){
	        super(searcher);
	    }

		@Override
		public Set<IMergeResult> getMaxCategoryUtilityMerges(Set<List<INode>> combinationsToCheck, IClusterSet<INode> clusterSet) {
			//Transform combinationsToCheck into a RapidMiner-readable form
			RapidMinerDataTransformator transformer = new RapidMinerDataTransformator();
			Set<ExampleSet> oldExmapleSet = transformer.transform(combinationsToCheck);
			
			//Send to each ExampleSet to RapidMiner to cluster
			ExampleSet tempSet;
			Set<ExampleSet> newExampleSet = new HashSet();
			Set<ExampleSet> tempExampleSet;
			Iterator<ExampleSet> i = oldExmapleSet.iterator();
			//Run clustering for each combination
			while(i.hasNext()){
				tempSet = i.next();
				tempExampleSet = this.runRMclustering(tempSet);
				Iterator<ExampleSet> j = tempExampleSet.iterator();
				while(j.hasNext()){
					tempSet = j.next();
					newExampleSet.add(tempSet);
				}				
			}
			//Return merge result
			return null;
		}
		
		public Set<ExampleSet> runRMclustering(ExampleSet setToCluster){
			
			
			
			return null;
		}
}*/
