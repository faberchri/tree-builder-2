package modules;

/**
 * Stores the occurring values for an attribute, including standard deviation, support, probability and rating.
 * Calculation of these values must happen in the class using the AttributeValue. This class only provides the setters and getters. 
 */

public interface AttributeValue {

  public void setParamteters(int support, double probability);
	public void setSupport(int support);
	public void setProbability(double probability);
}
