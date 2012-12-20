package clusterer;

import java.util.List;

/**
 * 
 * Stores the distance between two nodes.
 *
 */
public interface IMergeResult extends Comparable<IMergeResult>{
	
	/**
	 * Gets the category utility resulting from a merge of the
	 * referenced nodes.
	 * 
	 * @return the category utility
	 */
	public double getCategoryUtility();
		
	/**
	 * Gets the list of all nodes considered in this merge.
	 * 
	 * @return list with all nodes of the merge.
	 */
	public List<INode> getNodes();
}
