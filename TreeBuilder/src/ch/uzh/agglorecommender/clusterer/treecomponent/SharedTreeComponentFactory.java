package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SharedTreeComponentFactory extends TreeComponentFactory implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static SharedTreeComponentFactory factory = new SharedTreeComponentFactory();
	
	/*
	 * Must not be instantiated with constructor.
	 */
	private SharedTreeComponentFactory() {
		// singleton
	}
	
	public static  TreeComponentFactory getInstance() {
		return factory;
	}
	
	/**
	 * Used to create the (single) numeric attribute object of leaf nodes
	 * 
	 *@param rating
	 *@param meta meta info about the attribute
	 * 
	 *@return IAttribute numeric attribute object
	 */
	@Override
	public IAttribute createNumericAttribute(double rating, Map<String,String> meta) {
		// the stddev would be equal 0 but we use the acuity to prevent division by 0.
		// avg = rating, stdev = acuity, support = 1, sum of ratings = rating,
		// sum of squared ratings  = ratings^2
		return new SharedAttribute(1, rating, Math.pow(rating, 2.0), null, meta);
	}
	
	/**
	 * Used to create the nominal attributes object of leaf nodes
	 * 
	 * @param support 
	 * @param valueMap map of all values and their support for attribute
	 * @param meta meta info about the attribute
	 * 
	 * @return IAttribute symbolic attribute object
	 */
	@Override
	public IAttribute createNominalAttribute(int support, String key, String value) {
		
		Map<String,Double> valueMap = new HashMap<String,Double>();
		Map<String,String> meta = new HashMap<String,String>();
		
		valueMap.put(key,1.0);
		meta.put(key,value);
		
		return new SharedAttribute(support, 0,0, valueMap, meta);
	}

	/**
	 * Used to calculate new nodes in the merging process (numeric, nominal)
	 * 
	 * @return IAttribute new IAttribute object
	 */
	@Override
	public IAttribute createMergedAttribute(INode attributeKey, Collection<INode> nodesToMerge) {
		
		if(attributeKey.getNodeType() != ENodeType.Nominal){
			IAttribute generated = ClassitTreeComponentFactory.getInstance().createMergedAttribute(attributeKey, nodesToMerge);
			return new SharedAttribute(generated.getSupport(), generated.getSumOfRatings(), generated.getSumOfSquaredRatings(), null, generated.getMeta());
		}
		else {
			IAttribute generated = CobwebTreeComponentFactory.getInstance().createMergedAttribute(attributeKey, nodesToMerge);
			Map<Object,Double> valueMap = new HashMap<Object,Double>();
			while(generated.getProbabilities().hasNext()){
				Entry<Object, Double> entry = generated.getProbabilities().next();
				valueMap.put(entry.getKey(), entry.getValue());
			}
			return new SharedAttribute(2, 0, 0, valueMap , generated.getMeta()); // FIXME support
		}
	}		
}
