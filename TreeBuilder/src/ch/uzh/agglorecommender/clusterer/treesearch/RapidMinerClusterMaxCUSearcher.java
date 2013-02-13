package ch.uzh.agglorecommender.clusterer.treesearch;
import java.util.List;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

//This class is going to use RapidMiner to cluster the data. 
//RapidMiner must be installed on computer (no source code needed, only installed program). 
//Will use the RapidMinerDataTransformator to parse data to RapidMiner format

public class RapidMinerClusterMaxCUSearcher extends MaxCategoryUtilitySearcherDecorator{
	   
	    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

		public RapidMinerClusterMaxCUSearcher(IMaxCategoryUtilitySearcher searcher){
	        super(searcher);
	    }

		@Override
		public Set<IMergeResult> getMaxCategoryUtilityMerges(
				Set<List<INode>> combinationsToCheck,
				IClusterSet<INode> clusterSet) {
			// TODO Auto-generated method stub
			return null;
		}
	
}
