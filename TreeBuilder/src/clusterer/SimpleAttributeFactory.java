package clusterer;

import java.util.List;

import client.Normalizer;

class SimpleAttributeFactory<T> extends AttributeFactory<T> {

	private static SimpleAttributeFactory factory = new SimpleAttributeFactory();
	
	private SimpleAttributeFactory() {
		// singleton
	}
	
	public static  AttributeFactory getInstance(Normalizer normalizer) {
		if (normalizer == null && factory.getNormalizer() != null) {
			return factory;			
		}
		factory.setNormalizer(normalizer);
		return factory;
	}
	
	@Override
	public Attribute createAttribute(T rating) {
		return new SimpleAttribute(normalizeInput(rating));
	}

	@Override
	public Attribute createAttribute(List<Attribute> attributes) {
		return calcAttributeValues(attributes);
	}
	
	private Attribute calcAttributeValues(List<Attribute> attributesToCombine) {
		if (attributesToCombine.size() == 0) {
			System.err.println("attempt to combine 0 attributes, "+getClass().getSimpleName());
			System.exit(-1);
		}
		if (attributesToCombine.size() == 1) {
			Attribute a = attributesToCombine.get(0);
			return new SimpleAttribute(a.getAverage(), a.getStdDev(), a.getSupport(), a.getLeafList());
		}
		int sizeOfNewLeafList = 0;
		for (Attribute attribute : attributesToCombine) {
			sizeOfNewLeafList += attribute.getLeafList().length;
		}		
		double[] tmpAr = new double[sizeOfNewLeafList];
		int prevAttLength = 0;
		for (Attribute attribute : attributesToCombine) {
			System.arraycopy(attribute.getLeafList(), 0, tmpAr, prevAttLength, attribute.getLeafList().length);
		}
		
		int tmpSup = 0;
		for (Attribute attribute : attributesToCombine) {
			 tmpSup += attribute.getSupport();
		}
		
		double tmpAvg = 0.0;
		for (Double avgLi : tmpAr) {
			tmpAvg += avgLi;
		}
		tmpAvg = tmpAvg / tmpAr.length;
		
		double tmpStD = 0.0;
		for (Double avgLi : tmpAr) {
			tmpStD += Math.pow((avgLi - tmpAvg),2.0);
		}
		tmpStD = Math.sqrt(tmpStD/(tmpAr.length - 1.0));
		
		return new SimpleAttribute(tmpAvg, tmpStD, tmpSup, tmpAr);
	}

}
