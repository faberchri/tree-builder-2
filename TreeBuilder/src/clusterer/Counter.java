package clusterer;

/**
 * Class that keeps current count of nodes in the tree
 * Can be used for other data saving during clustering
 *
 */
public class Counter {
	
	int movieNodeCount = 0;
	int userNodeCount = 0;
	int openMovieNodes = 0;
	int openUserNodes = 0;
	
	int totalComparisonsOnCurrentLevel = 0;
	
	public int getTotalComparisonsOnCurrentLevel() {
		return totalComparisonsOnCurrentLevel;
	}

	public void addComparisonOnCurrentLevel() {
		this.totalComparisonsOnCurrentLevel++;
	}

	public Counter (int movieNodeCount, int userNodeCount) {
		this.movieNodeCount = movieNodeCount;
		this.userNodeCount = userNodeCount;
	}
	
	public int getOpenMovieNodeCount() {
		return openMovieNodes;
	}

	public void setOpenMovieNode(int movieNodeCountOnCurrentLevel) {
		this.openMovieNodes = movieNodeCountOnCurrentLevel;
	}

	public int getOpenUserNodeCount() {
		return openUserNodes;
	}

	public void setOpenUserNodeCount(int userNodeCountOnCurrentLevel) {
		this.openUserNodes = userNodeCountOnCurrentLevel;
	}

	public int getMovieNodeCount() {
		return movieNodeCount;
	}

	public void setMovieNodeCount(int movieNodeCount) {
		this.movieNodeCount = movieNodeCount;
	}

	public int getUserNodeCount() {
		return userNodeCount;
	}

	public void setUserNodeCount(int userNodeCount) {
		this.userNodeCount = userNodeCount;
	}
	
	public void addMovieNode() {
		movieNodeCount++;
	}
	
	public void addUserNode() {
		userNodeCount++;
	}

}
