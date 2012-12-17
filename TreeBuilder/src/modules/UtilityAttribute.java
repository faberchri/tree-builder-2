package modules;

import java.util.List;

//Attribute for Cobweb distance calculation

public class UtilityAttribute /*implements IAttribute*/ {

	//static int countAttributes = 0; 
	
	//Contains all distinct values for this attribute
	private List<AttributeValue> AttributeValues;

	
	public UtilityAttribute(String value, int rating){
		CobwebAttributeValue newValue = new CobwebAttributeValue(value, rating);
		AttributeValues.add(newValue);
	}
	
	public void addValue(String value, int rating){
		CobwebAttributeValue newValue = new CobwebAttributeValue(value, rating);
		AttributeValues.add(newValue);
	}
	
	/*
	public UtilityAttribute(double rating) {
		super(rating);
		countAttributes++;
	}

	static int getNumberOfAttributes(){
		return countAttributes;
	}
	*/



}

