package clusterer;

import java.util.List;

import client.INormalizer;

abstract class AttributeFactory<T> {
	
	public abstract IAttribute createAttribute(T rating); // single node
	public abstract IAttribute createAttribute(List<IAttribute> attributes); // group node
	
	private INormalizer<T> normalizer = null;
	
	double normalizeInput(T inp) {
		return normalizer.normalizeRating(inp);
	}
	
	public void setNormalizer(INormalizer<T> normalizer) {
		this.normalizer = normalizer;
	}
	
	public INormalizer<T> getNormalizer() {
		return normalizer;
	}
}
