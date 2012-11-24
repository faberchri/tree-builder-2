package clusterer;

public enum ENodeType {
	User, Content;
	
	private static long id = 0;
	
	public static long getNewId() {
		long tmp = id;
		id++;
		return tmp;
	}
	
}
