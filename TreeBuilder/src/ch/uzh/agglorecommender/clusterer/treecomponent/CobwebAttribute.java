package ch.uzh.agglorecommender.clusterer.treecomponent;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * A simple parameter object, which stores attribute value probabilities of
 * of a particular key in the attribute map of a node.
 * <br>
 * Is immutable.
 *
 */
public final class CobwebAttribute implements IAttribute, Serializable {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
		
	/**
	 * Attribute value - probability pairs.
	 * <br>
	 * e.g.:
	 * <br>
	 * <pre>
	 *   Rating Values  |    Probability
	 * -----------------------------------
	 *       5          |       0.5
	 *       1          |       0.25
	 *       
	 * Probabilities do not necessarily add
	 * up to 1, due to the sparse nature of
	 * the user-content-rating matrix.
	 * </pre>
	 */
	private final Map<Object, Double> attributeProbabilities;
	 
	private final Map<String,String> meta;
	
	/**
	 * Instantiates a new {@code CobwebAttribute} with a 
	 * attribute-value probability map.
	 * <br>
	 * @param meta 
	 * @param attribute-value probability map of this attribute.
	 */
	public CobwebAttribute(Map<?, Double> probabilityMap, Map<String, String> meta) {
		this.attributeProbabilities = (Map<Object, Double>) probabilityMap;
		this.meta = meta;
	}
	
	@Override
	public Iterator<Entry<Object, Double>> getProbabilities() {
		return attributeProbabilities.entrySet().iterator();
	}

	@Override
	public int getSupport() {
		throw new UnsupportedOperationException(
				"Method of ClassitAttribute object called on CobwebAttribute object");
	}

	@Override
	public double getSumOfSquaredRatings() {
		throw new UnsupportedOperationException(
				"Method of ClassitAttribute object called on CobwebAttribute object");
	}

	@Override
	public double getSumOfRatings() {
		throw new UnsupportedOperationException(
				"Method of ClassitAttribute object called on CobwebAttribute object");
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("attribute probability map: ");
		Iterator<Entry<Object, Double>> it = getProbabilities();
		while(it.hasNext()) {
			Entry<Object, Double> e = it.next();
			sb.append("[");
			sb.append(e.getKey());
			sb.append("|");
			sb.append(e.getValue());
			sb.append("]");
		}
		return sb.toString();
	}

	@Override
	public Map<String,String> getMeta() {
		return meta;
	}

	@Override
	public double getMeanOfRatings() {
		//?
		return 0;
	}

	@Override
	public Map<String, Integer> getValueMap() {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public double getStd() throws UnsupportedOperationException {
//		throw new UnsupportedOperationException(
//				"Method of ClassitAttribute object called on CobwebAttribute object");
//	}
	
}
