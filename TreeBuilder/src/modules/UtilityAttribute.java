package modules;


public class UtilityAttribute extends SimpleAttribute {

	static int countAttributes = 0; 
	
	public UtilityAttribute(double rating) {
		super(rating);
		countAttributes++;
	}

	static int getNumberOfAttributes(){
		return countAttributes;
	}

}
