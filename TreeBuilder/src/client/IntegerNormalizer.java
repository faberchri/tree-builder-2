package client;

public class IntegerNormalizer implements Normalizer<Integer> {
	
	private final int minValue;
	private final int maxValue;
	private double range;
	
	public IntegerNormalizer(int minValue, int maxValue) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		range = (double)(maxValue - minValue);
	}
	
	@Override
	public double normalizeRating(Integer rating) {
		return (rating.doubleValue() - (double)minValue) / range;
	}

}
