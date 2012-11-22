package clusterer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import client.INormalizer;

class SimpleAttributeFactory<T> extends AttributeFactory<T> {

	private static SimpleAttributeFactory factory = new SimpleAttributeFactory();
	
	/*
	 * Must not be instantiated.
	 */
	private SimpleAttributeFactory() {
		// singleton
	}
	
	public static  AttributeFactory getInstance(INormalizer normalizer) {
		factory.setNormalizer(normalizer);
		return factory;
	}
	
	@Override
	public IAttribute createAttribute(T rating) {
		return new SimpleAttribute(normalizeInput(rating));
	}

	@Override
	public IAttribute createAttribute(List<IAttribute> attributes) {
		return calcAttributeValues(attributes);
	}
	
	private IAttribute calcAttributeValues(List<IAttribute> attributesToCombine) {
		if (attributesToCombine.size() == 0) {
			System.err.println("attempt to combine 0 attributes, "+getClass().getSimpleName());
			System.exit(-1);
		}
		if (attributesToCombine.size() == 1) {
			IAttribute a = attributesToCombine.get(0);
			return new SimpleAttribute(a.getAverage(), a.getStdDev(), a.getSupport(), a.getConsideredRatings());
		}
		int sizeOfNewLeafList = 0;
		for (IAttribute attribute : attributesToCombine) {
			sizeOfNewLeafList += attribute.getConsideredRatings().size();
		}		
//		Double[] tmpAr = new Double[sizeOfNewLeafList];
//		int prevAttLength = 0;
//		for (IAttribute attribute : attributesToCombine) {
//			System.arraycopy(attribute.getConsideredRatings(), 0, tmpAr, prevAttLength, attribute.getConsideredRatings().size());
//		}
		List<Double> tmpAr = new ArrayList<>(sizeOfNewLeafList);
		for (IAttribute attribute : attributesToCombine) {
			tmpAr.addAll(attribute.getConsideredRatings());
		}

		int tmpSup = 0;
		for (IAttribute attribute : attributesToCombine) {
			 tmpSup += attribute.getSupport();
		}
		
		double tmpAvg = 0.0;
		for (Double avgLi : tmpAr) {
			tmpAvg += avgLi;
		}
		tmpAvg = tmpAvg / tmpAr.size();
		
		double tmpStD = 0.0;
		for (Double avgLi : tmpAr) {
			tmpStD += Math.pow((avgLi - tmpAvg),2.0);
		}
		tmpStD = Math.sqrt(tmpStD/(tmpAr.size() - 1.0));
//		Double[] doubleArray = ArrayUtils.toObject(tmpAr);
		return new SimpleAttribute(tmpAvg, tmpStD, tmpSup, tmpAr);
	}

}
