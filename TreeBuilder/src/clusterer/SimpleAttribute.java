package clusterer;


/**
 * 
 * @author faber
 * 
 * Is immutable object.
 *
 */
class SimpleAttribute implements Attribute {
	private final double average;
	private final double stdDev;
	private final int support;
	private final double[] leafList; //FIXME is mutable: we would need something like: List<Integer> items = Collections.unmodifiableList(Arrays.asList(0,1,2,3));
	
	public SimpleAttribute(double average) {
		this.average = average;
		this.stdDev = 0.0;
		this.support = 1;
		leafList = new double[] {average};
	}
	
	public SimpleAttribute(double average, double stdDev,
			int support, double[] leafList) {
		this.average = average;
		this.stdDev = stdDev;
		this.support = support;
		this.leafList = leafList;
	}
		
	public double getAverage() {
		return average;
	}
	
	public double getStdDev() {
		return stdDev;
	}
	
	public int getSupport() {
		return support;
	}
	
	public double[] getLeafList() {
		return leafList;
	}
	
	@Override
	public String toString() {
		return "Avg.: "
				.concat(String.valueOf(getAverage()))
				.concat(", StdDev.: ")
				.concat(String.valueOf(getStdDev()))
				.concat(", Sup.: ")
				.concat(String.valueOf(getSupport()));
	}
}
