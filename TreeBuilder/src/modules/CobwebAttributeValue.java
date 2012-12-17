package modules;

/*Stores the occuring values for an attribute, including standard deviation, support, probability and rating.
 * Calculation of these values must happen in the class using the AttributeValue. This class only provides the setters and getters. 
 */
public class CobwebAttributeValue implements AttributeValue {

  private String AttributeValue;
	private int rating;
	private int support;
	private double probability;
	
	public CobwebAttributeValue(String value, int rating){
		setAttributeValue(value);
		this.setRating(rating);
	}

	@Override
	public void setParamteters(int support, double probability) {
		this.support = support;
		this.probability = probability;
		
	}

	@Override
	public void setSupport(int support) {
		this.support = support;
		
	}

	public void setProbability(double probability) {
		this.probability = probability;
		
	}
	
	public double getProbability(){
		return probability;
	}

	public String getAttributeValue() {
		return AttributeValue;
	}

	public void setAttributeValue(String attributeValue) {
		AttributeValue = attributeValue;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	public int getSupport(){
		return support;
	}

}
