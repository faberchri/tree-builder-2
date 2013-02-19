package ch.uzh.agglorecommender.clusterer.treesearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.commons.math3.util.ArithmeticUtils;

import ch.uzh.agglorecommender.util.TBLogger;

import com.google.common.collect.ImmutableCollection;
import com.google.common.primitives.Ints;

public class ClusterSetNoCaching<E> implements IClusterSet<E>, Serializable {

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
	
	public ClusterSetNoCaching(ImmutableCollection<E> leafNodes) {
		openNodes.addAll(leafNodes);
	}

	/**
	 * Calculates the total number of dual node combinations.
	 * @return number of possible combinations
	 */
	private int calcNumberOfCombinations() {
		return Ints.checkedCast(ArithmeticUtils.binomialCoefficient(openNodes.size(), SUBSET_SIZE));
	}
	
	private Set<Collection<E>> createCombinations(E e) {
		Set<Collection<E>> res = new HashSet<Collection<E>>();
		for (E i : openNodes) {
			if (! i.equals(e)) {
				Set<E> combination = new HashSet<E>();
				combination.add(e);
				combination.add(i);
			}
		}
		return res;
	}
	
	private Set<Collection<E>> createCombinations() {
		Logger log = TBLogger.getLogger(this.getClass().getName());
		long time = System.nanoTime();
		Set<Collection<E>> res = new HashSet<Collection<E>>();
		for (int i = 0; i < openNodes.size(); i++) {
			for (int j = i + 1; j < openNodes.size(); j++) {
				Set<E> combination = new HashSet<E>();
				combination.add(openNodes.get(i));
				combination.add(openNodes.get(j));
				res.add(combination);
			}
		}
		log.info("Time to create combinations set: "
				+ (double)(System.nanoTime() - time) / 1000000000.0 + " seconds.");
		return res;
	}
	
	@Override
	public boolean contains(Object o) {
		return openNodes.contains(o);
	}

	@Override
	public boolean remove(Object o) {
		return openNodes.remove(o);
	}

	@Override
	public boolean add(E e) {
		return openNodes.add(e);
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
		Logger log = TBLogger.getLogger(this.getClass().getName());
		Set<Collection<E>> res = createCombinations();
		int expNumOfCombs = calcNumberOfCombinations();
		if (res.size() != expNumOfCombs) {
			log.severe("Err.: Unexpected number of node combinations. Generated: "+ res.size() + ", Expected: " + expNumOfCombs);
			System.exit(-1);
		} else {
			log.info("Expected number of node combinations generated. Generated: "+ res.size() + ", Expected: " + expNumOfCombs);
			
		}
		return res;
	}

	@Override
	public Set<Collection<E>> getCombinations(E element) {
		return createCombinations(element);
	}
	
//	public static void main(String[] args) {
//		
//		Random r2 = new Random();
//		
//		Collection<TestObjectSmall> s1 = new ArrayList<>();
//		for (int i = 0; i < 2000000; i++) {
////			s1.add(new String("A"));
//			s1.add(new TestObjectSmall());
//		}
//		
//		System.out.println(Runtime.getRuntime().totalMemory());
//		
//		Collection<TestObjectSmall> t1 = new ArrayList<>();
//		for (TestObjectSmall string : s1) {
//			t1.add(string);
//		}
//		
//		System.out.println(Runtime.getRuntime().totalMemory());
//		
//		Collection<TestObjectBig> s2 = new ArrayList<>();
//		for (int i = 0; i < 2000000; i++) {
//			s2.add(new TestObjectBig());
//		}
//		
//		System.out.println(Runtime.getRuntime().totalMemory());
//		
//		Collection<TestObjectBig> t2 = new ArrayList<>();
//		for (TestObjectBig string : s2) {
//			t2.add(string);
//		}
//		
//		System.out.println(Runtime.getRuntime().totalMemory());
//		
//		s1.remove(s1.iterator().next());
//		System.out.println(s1);
//		s2.remove(s2.iterator().next());
//		s2.remove(s2.iterator().next());
//		System.out.println(s2);
//		t1.remove(t1.iterator().next());
//		t1.remove(t1.iterator().next());
//		t1.remove(t1.iterator().next());
//		System.out.println(t1);
//		t2.remove(t2.iterator().next());
//		t2.remove(t2.iterator().next());
//		t2.remove(t2.iterator().next());
//		t2.remove(t2.iterator().next());
//		System.out.println(t2);
//
//
//
//	}
//	
//	private static class TestObjectBig{
//		String s;
//		int i;
//		
//		String s2;
//		int i2;
//		
//		static Random r = new Random();
//		
//		public TestObjectBig() {
//			
//			s = String.valueOf((char)r.nextInt(30));
//			i = r.nextInt(Integer.MAX_VALUE);
//			
//			s2 = String.valueOf((char)r.nextInt(30));
//			i2 = r.nextInt(Integer.MAX_VALUE);
//		}
//		
//		public int getI() {
//			return i;
//		}
//		
//		public String getS() {
//			return s;
//		}
//		
//		public String getS2() {
//			return s2;
//		}
//		
//		public int getI2() {
//			return i2;
//		}
//	}
//	
//	private static class TestObjectSmall{
//		String s;
//		int i;
//				
//		static Random r = new Random();
//		
//		public TestObjectSmall() {
//			
//			s = String.valueOf((char)r.nextInt(30));
//			i = r.nextInt(Integer.MAX_VALUE);
//			
//		}
//		
//		public int getI() {
//			return i;
//		}
//		
//		public String getS() {
//			return s;
//		}
//	}
}
