package ch.uzh.agglorecommender.evaluator;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.TreeBuilder;
import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.RecommendationModel;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;

public class CalculateRMSEtest {
	
	// Basic components
	INode movie1 = new Node(ENodeType.Content, null,null);		
	INode movie2 = new Node(ENodeType.Content, null, null);
	INode user = new Node(ENodeType.User,null, null);
	String datasetID1 = movie1.getDatasetId();
	String datasetID2 = movie2.getDatasetId();
	
	@Test
	public void testRMEI() throws InstantiationException, IllegalAccessException, NoSuchMethodException{
		//	public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
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
		double calcResult = calculateRMSE(user,predictionMap);
		double expectedResult = 3.60555127546;
		assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	}
	
	//@Test
	public void testRMEII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test II");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = new ClassitAttribute(1,0,0);
	IAttribute testNodeA2 = new ClassitAttribute(1,0,0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, null, null);
	INode nodeAttribute2 = new Node(ENodeType.Content, null, null);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, null,null);
	testNode.setRatingAttributes(attMap);

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = new ClassitAttribute(1,0,0);
	IAttribute predictedA2 = new ClassitAttribute(1,0,0);
	
	Map<String, IAttribute> predictionMap = new HashMap<>();
	predictionMap.put("0", predictedA1);
	predictionMap.put("1", predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	//@Test
	public void testRMEIII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test III");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = new ClassitAttribute(1,10,100);
	IAttribute testNodeA2 = new ClassitAttribute(1,10,100);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, null, null);
	INode nodeAttribute2 = new Node(ENodeType.Content, null, null);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, null, null);
	testNode.setRatingAttributes(attMap);

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = new ClassitAttribute(1,10,100);
	IAttribute predictedA2 = new ClassitAttribute(1,10,100);
	
	Map<String, IAttribute> predictionMap = new HashMap<>();
	predictionMap.put("0", predictedA1);
	predictionMap.put("1", predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 7.07106781187;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	//@Test
	public void testRMEIV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test IV");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = new ClassitAttribute(1,10,100);
	IAttribute testNodeA2 = new ClassitAttribute(1,10,100);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, null, null);
	INode nodeAttribute2 = new Node(ENodeType.Content, null, null);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, null, null);
	testNode.setRatingAttributes(attMap);

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = new ClassitAttribute(1,0,0);
	IAttribute predictedA2 = new ClassitAttribute(1,10,100);
	
	Map<String, IAttribute> predictionMap = new HashMap<>();
	predictionMap.put("0", predictedA1);
	predictionMap.put("1", predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 10;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	//@Test
	public void testRMEV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test V");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = new ClassitAttribute(1,10,100);
	IAttribute testNodeA2 = new ClassitAttribute(1,0,0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, null, null);
	INode nodeAttribute2 = new Node(ENodeType.Content, null, null);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, null, null);
	testNode.setRatingAttributes(attMap);

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = new ClassitAttribute(1,10,100);
	IAttribute predictedA2 = new ClassitAttribute(1,0,0);
	
	Map<String, IAttribute> predictionMap = new HashMap<>();
	predictionMap.put("0", predictedA1);
	predictionMap.put("1", predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
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
		//Instantiate evaluator
		Evaluator ev = null;
						try {
		            Class[] parameterTypes = new Class[1];
		            parameterTypes[0] = RecommendationModel.class;
		            Constructor cons = Evaluator.class.getDeclaredConstructor(parameterTypes);
		            cons.setAccessible(true);
		            //Problem here: 
		           // ev = (Evaluator)cons.newInstance(new RecommendationModel(null,null));
		            ev = (Evaluator)cons.newInstance(RecommendationModel.class.newInstance());
		        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		            e.printStackTrace();
		        }

				double result = 0;
				//Access private method calculateRMSE (INode testNode, Map<String, IAttribute> predictedRatings)
				try {
		            Class[] parameterTypes = {INode.class,Map.class};
		            
		            Method method = TreeBuilder.class.getDeclaredMethod("calculateRMSE", parameterTypes);
		            
		            method.setAccessible(true);
		            result = (double) method.invoke(ev,testNode,predictedRatings);
		        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
		            e.printStackTrace();
		        }
				return result;
	}
}
