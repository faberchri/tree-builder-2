package ch.uzh.agglorecommender.clusterer.treesearch;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TObjectShortHashMap;
import gnu.trove.map.hash.TShortObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.math3.util.ArithmeticUtils;

import scala.actors.threadpool.Arrays;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Ints;

/**
 * 
 * This cluster set manages the elements to cluster by keeping indices to the 
 * elements. For all dual combinations of indices a combination id is created.
 * The set of all combination id's can be used as search space for the best
 * cluster merge.
 *
 * @param <E> The type of object to store in the cluster set.
 */
public class ClusterSetIndexed<E> implements IClusterSetIndexed<E> {
	
	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = -8422132856718902351L;

	/**
	 * The subset size. This implementation works only with subset size == 2!
	 */
	private static final int SUBSET_SIZE = 2;
	
	/**
	 * Value that is returned if entry was not found in
	 * the primitive collections (TROVE collections)
	 */
	private static final short NO_ENTRY_CONST = -1;

	/**
	 * Maps id (index) -> element. Used to create and resolve combinations.
	 */
	private TShortObjectMap<E> openIndexElementMap;
	
	/**
	 * Maps element -> id (index). Used to create and resolve combinations.
	 */
	private TObjectShortHashMap<E> openElementIndexMap;
	
	/**
	 * All currently valid combination id's.
	 */
	private TIntSet combinationIds;

	private short nodeCount = 0;

	public ClusterSetIndexed(ImmutableCollection<E> leafNodes) {
		openIndexElementMap = new TShortObjectHashMap<>(2 * leafNodes.size(), 0.5f, NO_ENTRY_CONST);
		openElementIndexMap = new TObjectShortHashMap<>(2 * leafNodes.size(), 0.5f, NO_ENTRY_CONST);
		combinationIds = new TIntHashSet(calcNumberOfCombinations(leafNodes.size()), 0.5f, NO_ENTRY_CONST);
		
		// we map to each element an id and to each id the element 
		for (E e : leafNodes) {
			openIndexElementMap.put(nodeCount, e);
			openElementIndexMap.put(e, nodeCount);
			nodeCount++;
		}
		
		// create the initial combination id's
		for (short x = 0; x < nodeCount; x++) {
			for (short y = (short) (x + 1); y < nodeCount; y++) {			
				// y always bigger than x -> no sorting needed to prevent of duplicates
				combinationIds.add(Cantor.compute(x, y));
			}
		}
	}

	@Override
	public Collection<E> getCombination(int combinationId) {
		
		short x = Cantor.computeX(combinationId);
		short y = Cantor.computeY(combinationId);
						
		return Arrays.asList(new Object[] {openIndexElementMap.get(x), openIndexElementMap.get(y)});
	}

	@Override
	public boolean contains(Object o) {
		return openElementIndexMap.containsKey(o);
	}

	@Override
	public boolean remove(Object o) {
		if (! openElementIndexMap.contains(o)) {
			return false;
		}
		short eId = openElementIndexMap.get(o);
		combinationIds.removeAll(createCombinationIds(eId));
		openElementIndexMap.remove(o);
		openIndexElementMap.remove(eId);
		return true;
	}

	@Override
	public boolean add(E e) {
		if (openElementIndexMap.contains(e)) {
			return false;
		}
		openElementIndexMap.put(e, nodeCount);
		openIndexElementMap.put(nodeCount, e);
		combinationIds.addAll(createCombinationIds(nodeCount));
		nodeCount++;
		return true;
	}

	@Override
	public int size() {
		return openElementIndexMap.keySet().size();
	}

	@Override
	public boolean clusteringDone() {
		return size() < 2;
	}

	@Override
	public Collection<E> getUnmodifiableView() {
		return Collections.unmodifiableSet(openElementIndexMap.keySet());
	}

	@Override
	public E getRoot() {
		if (! clusteringDone()) return null;
		if (size() == 0) return null;
		return openElementIndexMap.keySet().iterator().next();
	}

	@Override
	public E getAnyElement() {
		E e = null;
		if (openElementIndexMap.keySet().iterator().hasNext()){
			e = openElementIndexMap.keySet().iterator().next();			
		}
		return e;
	}

	@Override
	public Set<Collection<E>> getCombinations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Collection<E>> getCombinations(E element) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public TIntSet getCombinationsIds() {
		return new TIntHashSet(combinationIds);
	}
	
	@Override
	public TIntSet getCombinationsIds(E e) {
		short s = openElementIndexMap.get(e);
		if (s == NO_ENTRY_CONST) return null;
		return createCombinationIds(s);
	}
	
	/**
	 * Calculates the total number of dual node combinations.
	 * @return number of possible combinations
	 */
	private int calcNumberOfCombinations(int n) {
		return Ints.checkedCast(ArithmeticUtils.binomialCoefficient(n, SUBSET_SIZE));
	}
	
	/**
	 * Creates all dual combination id's for the passed index.
	 * @param s the index
	 * @return set of all combinations for the index
	 */
	private TIntSet createCombinationIds(short s){
		TIntSet res = new TIntHashSet(openIndexElementMap.size());
		TShortIterator it = openIndexElementMap.keySet().iterator();
		while (it.hasNext()) {
			short s2 =  it.next();
			// sorting needed to prevent of duplicates
			if (s2 < s) {
				res.add(Cantor.compute(s2, s));
			}
			if (s2 > s) {
				res.add(Cantor.compute(s, s2));
			}		
		}
		return res;
	}
	
	/**
	 * 
	 * Utility class that implements the Cantor pairing function.
	 *
	 */
	private static class Cantor {
		private static int compute(short x, short y) {
			return (x+y)*(x+y+1)/2 + y;
		}
		private static short computeX(int z) {
			int j  = (int) Math.floor(Math.sqrt(0.25 + 2*z) - 0.5);
			return (short) (j - (z - j*(j+1)/2));
		}
		private static short computeY(int z) {
			int j  = (int) Math.floor(Math.sqrt(0.25 + 2*z) - 0.5);
			return (short) (z - j*(j+1)/2);
		}
	}
	
	

	///////////////////////////////////////////////////////
	///					 Test section 					///
	///////////////////////////////////////////////////////
	
	
	public static void main(String[] args) {
		
//		TIntSet lx= new TIntHashSet();
//		for (int i = 0; i < 11000000; i++) {
//			lx.add(i);
//		}
		
		List<Integer> l = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			l.add(new Integer(i));
		}
//		ImmutableCollection<String> c = ImmutableList.of("A", "B", "C", "D", "E", "F", "G", "H", "I", "K");
		ImmutableList<Integer> c = ImmutableList.copyOf(l);
		IClusterSetIndexed<Integer> s = new ClusterSetIndexed<>(c);
//		System.out.println(s.getCombinationsIds());
		System.out.println(Cantor.compute((short)19999, (short)20000));
		System.out.println(Integer.MAX_VALUE);
		printTest(s);
		
		s.add(new Integer(10300));
		printTest(s);
		
		s.remove(new Integer(5));
		printTest(s);
		
		s.add(new Integer(10500));
		printTest(s);
		
		s.add(new Integer(10600));
		printTest(s);

	}
	
	private static void printTest(IClusterSetIndexed s) {
		TIntIterator it = s.getCombinationsIds().iterator();
		Map<Integer, Collection<String>> m = new TreeMap<>();
		List<Queue<String>> p = new ArrayList<Queue<String>>();
		while(it.hasNext()) {
			int i = it.next();
			Collection<String> tmp = s.getCombination(i);
			m.put(i, tmp);
			p.add(new PriorityQueue<String>(tmp));
		}
		System.out.println(m);
		System.out.println(m.size());
		Collections.sort(p, new Comparator<Queue<String>>() {
			@Override
			public int compare(Queue<String> o1,
					Queue<String> o2) {
				int r = o1.peek().compareTo(o2.peek());
				if (r != 0) return r;

				Iterator<String> i1 = o1.iterator();
				i1.next();
				String s1 = i1.next();
				Iterator<String> i2 = o2.iterator();
				i2.next();
				String s2 = i2.next();
				return s1.compareTo(s2);

			}
		});
		System.out.println(p);
		System.out.println(p.size());
	}
	
}
