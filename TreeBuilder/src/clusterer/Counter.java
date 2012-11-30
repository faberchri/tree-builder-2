package clusterer;

/**
 * Class that keeps current count of nodes in the tree
 * Can be used for other data saving during clustering
 *
 */
public class Counter {
	
	int movieNodeCount = 0;
	int userNodeCount = 0;
	int movieNodeCountOnCurrentLevel = 0;
	int userNodeCountOnCurrentLevel = 0;
	
	public int getMovieNodeCountOnCurrentLevel() {
		return movieNodeCountOnCurrentLevel;
	}

	public void setMovieNodeCountOnCurrentLevel(int movieNodeCountOnCurrentLevel) {
		this.movieNodeCountOnCurrentLevel = movieNodeCountOnCurrentLevel;
	}

	public int getUserNodeCountOnCurrentLevel() {
		return userNodeCountOnCurrentLevel;
	}

	public void setUserNodeCountOnCurrentLevel(int userNodeCountOnCurrentLevel) {
		this.userNodeCountOnCurrentLevel = userNodeCountOnCurrentLevel;
	}

	public Counter (int movieNodeCount, int userNodeCount) {
		this.movieNodeCount = movieNodeCount;
		this.userNodeCount = userNodeCount;
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
