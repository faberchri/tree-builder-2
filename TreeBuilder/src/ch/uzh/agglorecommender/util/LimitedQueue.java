package ch.uzh.agglorecommender.util;

import java.util.LinkedList;

public class LimitedQueue<E> extends LinkedList<E> {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;

	private int limit;

	public LimitedQueue(int limit) {
		this.limit = limit;
	}

	@Override
	public boolean add(E o) {
		boolean r = super.add(o);
		while (size() > limit) {
			super.remove();
		}
		return r;
	}
}
