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

/*
 * Testing EvaluationBuilder.calculateAME(INode testNode, Map<Integer, IAttribute> predictedRatings)
 */
public class CalculateAMEtest {

	// Basic components
	INode movie1 = new Node(ENodeType.Content, "1",null);		
	INode movie2 = new Node(ENodeType.Content, "2",null);
	INode user = new Node(ENodeType.User,null, null);
	String datasetID1 = movie1.getDatasetId();
	String datasetID2 = movie2.getDatasetId();
	static Evaluator evaluator = new Evaluator(null);

	/*
	 * Node to predict: A1 = 4.0, A2 = 3.0
	 * Prediction: A1 = 5.0, A2 = 8.0
	 * Expected AME = 3
	 */
	@Test
	public void testAMEI() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting AME test I..");

		//Create test node to be compared with prediction
		IAttribute realR1 = new ClassitAttribute(1,4,16);
		IAttribute realR2 = new ClassitAttribute(1,3,9);
		addAttMap(realR1,realR2);

		//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
		IAttribute predR1 = new ClassitAttribute(1,5,25);
		IAttribute predR2 = new ClassitAttribute(1,8,64);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 3;
		double calcResult = calculateAME(user,predictionMap);
		assertEquals("Calculated AME",expectedResult,calcResult,0.00001);
	}

	/*
	 * Node to predict: A1 = 0.0, A2 = 0.0
	 * Prediction: A1 = 0.0, A2 = 0.0
	 * Expected AME = 0
	 */
	@Test
	public void testAMEII() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting AME test II");
		
		//Create test node to be compared with prediction
		IAttribute realR1 = new ClassitAttribute(1,0,0);
		IAttribute realR2 = new ClassitAttribute(1,0,0);
		addAttMap(realR1,realR2);

		//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
		IAttribute predR1 = new ClassitAttribute(1,0,0);
		IAttribute predR2 = new ClassitAttribute(1,0,0);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 0;
		double calcResult = calculateAME(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}
	
	/*
	 * Node to predict: A1 = 10.0, A2 = 10.0
	 * Prediction: A1 = 10.0, A2 = 0.0
	 * Expected AME = 5
	 */
	@Test
	public void testAMEIII() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting AME test III");

		//Create test node to be compared with prediction
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,10,100);
		addAttMap(realR1,realR2);

		//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
		IAttribute predR1 = new ClassitAttribute(1,10,100);
		IAttribute predR2 = new ClassitAttribute(1,10,100);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		double expectedResult = 0;
		double calcResult = calculateAME(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}
	
	/*
	 * Node to predict: A1 = 10.0, A2 = 0.0
	 * Prediction: A1 = 0.0, A2 = 10.0
	 * Expected AME = 10
	 */
	@Test
	public void testAMEIV() throws InstantiationException, IllegalAccessException, NoSuchMethodException{
		//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){

		System.out.println("Starting AME test IV");

		//Create test node to be compared with prediction
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,0,0);
		addAttMap(realR1,realR2);

		//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
		IAttribute predR1 = new ClassitAttribute(1,0,0);
		IAttribute predR2 = new ClassitAttribute(1,10,100);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 10;
		double calcResult = calculateAME(user,predictionMap);
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}
	
	/*
	 * Node to predict: A1 = 10.0, A2 = 0.0
	 * Prediction: A1 = 10.0, A2 = 0.0
	 * Expected AME = 0
	 */
	@Test
	public void testAMEV() throws InstantiationException, IllegalAccessException, NoSuchMethodException{

		System.out.println("Starting AME test V");

		//Create test node to be compared with prediction
		IAttribute realR1 = new ClassitAttribute(1,10,100);
		IAttribute realR2 = new ClassitAttribute(1,0,0);
		addAttMap(realR1,realR2);

		//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
		IAttribute predR1 = new ClassitAttribute(1,10,100);
		IAttribute predR2 = new ClassitAttribute(1,0,0);
		Map<String, IAttribute> predictionMap = buildPredMap(predR1,predR2);

		// Run Test
		double expectedResult = 0;
		double calcResult = calculateAME(user,predictionMap);
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

	private static double calculateAME(INode testNode, Map<String, IAttribute> predictedRatings) throws InstantiationException, NoSuchMethodException, IllegalAccessException{

		double result = 0;

		//Access private method calculateRMSE (INode testNode, Map<String, IAttribute> predictedRatings)
		try {
			@SuppressWarnings("rawtypes")
			Class[] parameterTypes = {INode.class,Map.class};
			Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
			method.setAccessible(true);
			result = (double) method.invoke(evaluator,testNode,predictedRatings);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return result;
	}
}
