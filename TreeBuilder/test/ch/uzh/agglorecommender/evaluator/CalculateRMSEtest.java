package ch.uzh.agglorecommender.evaluator;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;

public class CalculateRMSEtest {

	// Basic components
	INode movie1 = new Node(ENodeType.Content, "1",null);		
	INode movie2 = new Node(ENodeType.Content, "2",null);
	INode user = new Node(ENodeType.User,null, null);
	String datasetID1 = movie1.getDatasetId();
	String datasetID2 = movie2.getDatasetId();
	static Evaluator evaluator = new Evaluator(null);

	@Test
	public void testRMEI() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting RME test I");

		// Real Ratings
		IAttribute realR1 = new ClassitAttribute(1,4,16);
		IAttribute realR2 = new ClassitAttribute(1,3,9);
		addAttMap(realR1,realR2);

		// Predicted Ratings
		IAttribute predR1 = new ClassitAttribute(1,5,25);
		IAttribute predR2 = new ClassitAttribute(1,8,64);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 3.60555127546;
		double calcResult = calculateRMSE(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}

	@Test
	public void testRMEII() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting RME test II");

		// Real Ratings
		IAttribute realR1 = new ClassitAttribute(1,0,0);
		IAttribute realR2 = new ClassitAttribute(1,0,0);
		addAttMap(realR1,realR2);

		// Predicted Ratings
		IAttribute predR1 = new ClassitAttribute(1,0,0);
		IAttribute predR2 = new ClassitAttribute(1,0,0);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 0;
		double calcResult = calculateRMSE(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}

	@Test
	public void testRMEIII() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting RME test III");

		// Real Ratings
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,10,100);
		addAttMap(realR1,realR2);

		// Predicted Ratings
		IAttribute predR1 = new ClassitAttribute(1,10,100);
		IAttribute predR2 = new ClassitAttribute(1,10,100);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 0;
		double calcResult = calculateRMSE(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}

	@Test
	public void testRMEIV() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting RME test IV");

		// Real Ratings
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,10,100);
		addAttMap(realR1,realR2);

		// Predicted Ratings
		IAttribute predR1 = new ClassitAttribute(1,0,0);
		IAttribute predR2 = new ClassitAttribute(1,10,100);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 7.07106781187;
		double calcResult = calculateRMSE(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}

	@Test
	public void testRMEV() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting RME test V");

		// Real Ratings
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,0,0);
		addAttMap(realR1,realR2);

		// Predicted Ratings
		IAttribute predR1 = new ClassitAttribute(1,10,100);
		IAttribute predR2 = new ClassitAttribute(1,0,0);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Tests
		double expectedResult = 0;
		double calcResult = calculateRMSE(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}

	private void addAttMap(IAttribute rating1, IAttribute rating2){
		Map<INode, IAttribute> attMap = new HashMap<>();
		
		attMap.put(movie1, rating1);
		attMap.put(movie2, rating2);
		user.setRatingAttributes(attMap);
	}

	private Map<String,IAttribute> buildPredMap(IAttribute predRating1, IAttribute predRating2){
		Map<String, IAttribute> predictionMap = new HashMap<>();
		predictionMap.put(datasetID1, predRating1);
		predictionMap.put(datasetID2, predRating2);
		return predictionMap;
	}

	private static double calculateRMSE(INode testNode, Map<String, IAttribute> predictedRatings) throws InstantiationException, NoSuchMethodException, IllegalAccessException{

		double result = 0;

		//Access private method calculateRMSE (INode testNode, Map<String, IAttribute> predictedRatings)
		try {
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = {INode.class,Map.class};
			Method method = Evaluator.class.getDeclaredMethod("calculateRMSE", parameterTypes);
			method.setAccessible(true);
			result = (double) method.invoke(evaluator,testNode,predictedRatings);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return result;
	}
}
