package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * 
 * A set which allows accessing of elements by index.
 * All added elements are also added to an internal list which is queried on an attempt to fetch an element by index.
 * An element is only accessible if it is contained in the internal set.
 * <br>
 * <br>
 * Beside the usual set methods the data structure provides three additional methods, namely:
 * <br>
 * <br>
 * - E getByIndex(int index)
 * <br>
 * - int getLastIndex() 
 * <br>
 * - int indexOf(E element)
 * <br>
 * <br>
 * All set methods behave like the corresponding methods of java.util.HashSet with the following exceptions:
 * <br>
 * <br>
 * - add(E e): addition of an element to the set adds the element also to the end of a list 
 * iff the list does not contain the element already.
 * <br>
 * - addAll(Collection c): addition of multiple elements to the set adds each element also to the end of a list 
 * iff the list does not contain the element already.
 * <br>
 * <br>
 * <b>Hence elements are never removed from the internal list.</b>
 *
 * @param <E>
 */
class IndexAwareSet<E> implements Set<E>, Serializable{

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private Set<E> internalSet = new HashSet<E>();
	private List<E> internalList = new ArrayList<E>();
	
	@Override
	public int size() {
		return internalSet.size();
	}

	@Override
	public boolean isEmpty() {
		return internalSet.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internalSet.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return internalSet.iterator();
	}

	@Override
	public Object[] toArray() {
		return internalSet.toArray();
	}

	@Override
	public <E> E[] toArray(E[] a) {
		return internalSet.toArray(a);
	}

	@Override
	public boolean add(E e) {
		if (! internalList.contains(e)) {
			internalList.add(e);			
		}
		return internalSet.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return internalSet.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internalSet.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			if (! internalList.contains(e)) {
				internalList.add(e);			
			}
		}
		return internalSet.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return internalSet.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return internalSet.removeAll(c);
	}

	@Override
	public void clear() {
		internalSet.clear();		
	}
	
	/**
	 * Gets the element at the passed index.
	 * 
	 * @param index the index to look up.
	 * @return the element at position index or null if the index is invalid.
	 * Invalid means either that the element at the specified index has been
	 * removed or that the index is not in the range of allowed indices.
	 * The range of allowed indices is [0, number of elements added to data structure minus 1].
	 */
	public E getByIndex(int index) {
		if (index < 0 || index > internalList.size() - 1) {
			return null;
		}
		E e = internalList.get(index);
		if (! internalSet.contains(e)) {
			return null;
		}
		return e;
	}
	
	/**
	 * Gets the last index.
	 * @return Returns the index of the element with the highest index.
	 * The index is not guaranteed to be valid,
	 * which means that the element with the returned index can be retrieved.
	 */
	public int getLastIndex() {
		return internalList.size() - 1;
	}
	
	/**
	 * Get the index of the passed element.
	 * 
     * @param o element to search for
     * @return the index of the specified element or -1 if the 
     * IndexAwareSet does not contain the element
	 */
	public int indexOf(Object o) {
		if (contains(o)) {
			return internalList.indexOf(o);
		} else {
			return -1;	
		}
	}

}
