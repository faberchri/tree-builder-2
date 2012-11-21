package clusterer;

import java.util.List;

import client.Normalizer;

abstract class AttributeFactory<T> {
	public abstract Attribute createAttribute(T rating); // single node
	public abstract Attribute createAttribute(List<Attribute> attributes); // group node
	
	private Normalizer<T> normalizer = null;
	
	double normalizeInput(T inp) {
		if (normalizer == null) {
			return ((Double)inp).doubleValue();
		}
		return normalizer.normalizeRating(inp);
	}
	
	public void setNormalizer(Normalizer<T> normalizer) {
		this.normalizer = normalizer;
	}
	
	public Normalizer<T> getNormalizer() {
		return normalizer;
	}
}
