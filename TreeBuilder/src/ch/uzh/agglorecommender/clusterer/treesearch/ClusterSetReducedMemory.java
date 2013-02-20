package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.math3.util.ArithmeticUtils;

import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableCollection;
import com.google.common.primitives.Ints;

public class ClusterSetReducedMemory<E> implements IClusterSet<E>, Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int SUBSET_SIZE = 2;

	List<E> openNodes = new ArrayList<E>();
//	Set<E> closedNodes = new HashSet<E>();
	
	Set<Collection<E>> combinations = new HashSet<Collection<E>>();
	
//	Map<E, Set<E>> combinations = new HashMap<E, Set<E>>();

	public ClusterSetReducedMemory(ImmutableCollection<E> leafNodes) {
		Logger log = TBLogger.getLogger(this.getClass().getName());

		openNodes.addAll(leafNodes);
		initCombinations();
		int expNumOfCombs = calcNumberOfInitialCombinations();
		if (combinations.size() != expNumOfCombs) {
			log.severe("Err.: Unexpected number of node combinations. Generated: "+ combinations.size() + ", Expected: " + expNumOfCombs);
			System.exit(-1);
		} else {
			log.info("Expected number of node combinations generated. Generated: "+ combinations.size() + ", Expected: " + expNumOfCombs);
			
		}
	}
	
	/**
	 * Calculates the total number of dual node combinations.
	 * @return number of possible combinations
	 */
	private int calcNumberOfInitialCombinations() {
		return Ints.checkedCast(ArithmeticUtils.binomialCoefficient(openNodes.size(), SUBSET_SIZE));
	}
	
	private void initCombinations() {
		for (int i = 0; i < openNodes.size(); i++) {
			for (int j = i + 1; j < openNodes.size(); j++) {
				Set<E> combination = new HashSet<E>(2);
				combination.add(openNodes.get(i));
				combination.add(openNodes.get(j));
				combinations.add(combination);
			}
		}
	}
	
	private Set<Collection<E>> createCombinations(E e) {
		Set<Collection<E>> res = new HashSet<Collection<E>>();
		for (E i : openNodes) {
			if (! i.equals(e)) {
				Set<E> combination = new HashSet<E>(2);
				combination.add(e);
				combination.add(i);
			}
		}
		return res;
	}
	
	@Override
	public boolean contains(Object o) {
		return openNodes.contains(o);
	}

	@Override
	public boolean remove(Object o) {
		Iterator<Collection<E>> it = combinations.iterator();
		while (it.hasNext()) {
			Collection<E> set = it.next();
			if (set.contains(o)) {
				it.remove();
			}			
		}
		return openNodes.remove(o);
	}

	@Override
	public boolean add(E e) {
		boolean r = openNodes.add(e);
		combinations.addAll(createCombinations(e));
		return r;
	}

	@Override
	public int size() {
		return openNodes.size();
	}

	@Override
	public boolean clusteringDone() {
		return size() < 2;
	}

	@Override
	public Collection<E> getUnmodifiableView() {
		return Collections.unmodifiableList(openNodes);
	}

	@Override
	public E getRoot() {
		if (! clusteringDone()) return null;
		if (size() == 0) return null;
		return openNodes.iterator().next();
	}

	@Override
	public E getAnyElement() {
		E e = null;
		if (openNodes.iterator().hasNext()){
			e = openNodes.iterator().next();			
		}
		return e;
	}

	@Override
	public Set<Collection<E>> getCombinations() {
		return new HashSet<Collection<E>>(combinations);
	}

	@Override
	public Set<Collection<E>> getCombinations(E element) {
		return createCombinations(element);
	}
	

//	public static void main(String[] args) {
//		Set<Set<Integer>> t = new HashSet<Set<Integer>>();
//		
//		Set<Integer> l1 = new HashSet<Integer>();
//		Set<Integer> l2 = new HashSet<Integer>();
//		
//		Integer i1 = new Integer(1000);
//		Integer i2 = new Integer(1001);
//		Integer i3 = new Integer(1002);
//		Integer i4 = new Integer(1003);
//		
//		l1.add(i1);
//		l1.add(i2);
//		l2.add(i3);
//		l2.add(i4);
//		
//		t.add(l1);
//		t.add(l2);
//		
//		System.out.println(t);
//		
//		Set<Integer> l3 = new HashSet<Integer>();
//		
//		l3.add(i3);
//		l3.add(i4);
//		
//		t.add(l3);
//		
//		System.out.println(t);
//		
//		Set<Integer> l4 = new HashSet<Integer>();
//		
//		l4.add(i3);
//		l4.add(new Integer(1003));
//		
//		t.add(l4);
//		
//		System.out.println(t);
//		
//		Set<Integer> l5 = new HashSet<Integer>();
//		
//		l5.add(new Integer(1003));
//		l5.add(i3);
//		
//		t.add(l5);
//		
//		System.out.println(t);
//	}


}
