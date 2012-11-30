package modules;

import java.util.ArrayList;
import java.util.List;

import clusterer.AttributeFactory;
import clusterer.IAttribute;

public class ComplexAttributeFactory extends AttributeFactory {

	private static ComplexAttributeFactory factory = new ComplexAttributeFactory();
	
	/*
	 * Must not be instantiated with constructor.
	 */
	ComplexAttributeFactory() {
		// singleton
	}
	
	public static  AttributeFactory getInstance() {
		return factory;
	}
	
	/**
	 * Used to establish nodes
	 */
	@Override
	public IAttribute createAttribute(double rating) {
		return new SimpleAttribute(rating);
	}

	/**
	 * Used to calculate new nodes in the merging process
	 */
	@Override
	public IAttribute createAttribute(List<IAttribute> attributes) {
		return calcAttributeValues(attributes);
	}
	
	/**
	 * Here attributes are finally combined, calculation of average, stddev, support, .. is done here
	 */
	private IAttribute calcAttributeValues(List<IAttribute> attributesToCombine) {
		
		// No Attributes
		if (attributesToCombine.size() == 0) {
			System.err.println("attempt to combine 0 attributes, "+getClass().getSimpleName());
			System.exit(-1);
		}
		
		// Only one occurrence of Attribute
		if (attributesToCombine.size() == 1) {
			IAttribute a = attributesToCombine.get(0);
			return new SimpleAttribute(a.getAverage(), a.getStdDev(), a.getSupport(), a.getConsideredRatings());
		}
		
		// ??? #####################################################
//		int sizeOfNewLeafList = 0;
//		for (IAttribute attribute : attributesToCombine) {
//			sizeOfNewLeafList += attribute.getConsideredRatings().size();
//		}		
//		Double[] tmpAr = new Double[sizeOfNewLeafList];
//		int prevAttLength = 0;
//		for (IAttribute attribute : attributesToCombine) {
//			System.arraycopy(attribute.getConsideredRatings(), 0, tmpAr, prevAttLength, attribute.getConsideredRatings().size());
//		}
		// ??? #####################################################
		
		// Determine Considered Ratings
		ArrayList<Double> tmpAr = new ArrayList<Double>();
		for (IAttribute attribute : attributesToCombine) {
			tmpAr.addAll(attribute.getConsideredRatings());
		}
		
		// Average
		double tmpAvg = 0.0;
		for (Double avgLi : tmpAr) {
			tmpAvg += avgLi;
		}
		tmpAvg = tmpAvg / tmpAr.size();
		
		// Support
		int tmpSup = 0;
		for (IAttribute attribute : attributesToCombine) {
			 tmpSup += attribute.getSupport();
		}
		
		// Standard Deviation
		double tmpStD = 0.0;
		for (Double avgLi : tmpAr) {
			tmpStD += Math.pow((avgLi - tmpAvg),2.0);
		}
		tmpStD = Math.sqrt(tmpStD/(tmpAr.size() - 1.0));
//		
		//Double[] doubleArray = ArrayUtils.toObject(tmpAr);
		
		return new SimpleAttribute(tmpAvg, tmpStD, tmpSup, tmpAr);
	}

}
