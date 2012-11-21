package clusterer;

import java.util.List;

public interface Attribute {
	
	public double getAverage();
	
	public double getStdDev();
	
	public int getSupport();
	
	public double[] getLeafList();
}
