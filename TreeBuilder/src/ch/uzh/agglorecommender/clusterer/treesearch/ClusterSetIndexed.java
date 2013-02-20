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

public class ClusterSetIndexed<E> implements IClusterSetIndexed<E> {

	private static final int SUBSET_SIZE = 2;
	
	private static final short NO_ENTRY_CONST = -1;

	TShortObjectMap<E> openIndexElementMap;
	
	TObjectShortHashMap<E> openElementIndexMap;

//	TIntShortMap combinationIdXNodeIdMap;
	
	TIntSet combinationIds;

	short nodeCount = 0;

	public ClusterSetIndexed(ImmutableCollection<E> leafNodes) {
		openIndexElementMap = new TShortObjectHashMap<>(2 * leafNodes.size(), 0.5f, NO_ENTRY_CONST);
		openElementIndexMap = new TObjectShortHashMap<>(2 * leafNodes.size(), 0.5f, NO_ENTRY_CONST);
//		combinationIdXNodeIdMap = new TIntShortHashMap(calcNumberOfCombinations(leafNodes.size()), 0.5f, -1, (short) -1);
		combinationIds = new TIntHashSet(calcNumberOfCombinations(leafNodes.size()), 0.5f, NO_ENTRY_CONST);
		
		for (E e : leafNodes) {
			openIndexElementMap.put(nodeCount, e);
			openElementIndexMap.put(e, nodeCount);
			nodeCount++;
		}

		for (short x = 0; x < nodeCount; x++) {
			for (short y = (short) (x + 1); y < nodeCount; y++) {
				
				// y always bigger than x -> no sorting needed to prevent of duplicates
				combinationIds.add(Cantor.compute(x, y));
			}
		}
//		System.out.println(calcNumberOfCombinations(leafNodes.size()));	
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
		return createCombinationIds(s);
	}
	
	/**
	 * Calculates the total number of dual node combinations.
	 * @return number of possible combinations
	 */
	private int calcNumberOfCombinations(int n) {
		return Ints.checkedCast(ArithmeticUtils.binomialCoefficient(n, SUBSET_SIZE));
	}
	
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
	
	
	
	public static class Cantor {
		public static int compute(short x, short y) {
			return (x+y)*(x+y+1)/2 + y;
		}
		public static short computeX(int z) {
			int j  = (int) Math.floor(Math.sqrt(0.25 + 2*z) - 0.5);
			return (short) (j - (z - j*(j+1)/2));
		}
		public static short computeY(int z) {
			int j  = (int) Math.floor(Math.sqrt(0.25 + 2*z) - 0.5);
			return (short) (z - j*(j+1)/2);
		}
	}
	
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
	
//	public static void main(String[] args) {
//		Set<int[]> s = new HashSet<>();
//
//		s.add(new int[] {1,2});
//		s.add(new int[] {1,2});
//		s.add(new int[] {2,1});
//		s.add(new int[] {2,2});
//
//		for (int[] is : s) {
//			for (int i : is) {
//				System.out.println(i);
//			}
//			System.out.println("---");
//		}
//		System.out.println(Integer.MAX_VALUE);
//
//	}

}
