package ch.uzh.agglorecommender.evaluator;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.utils.Evaluator;

/*
 * Testing EvaluationBuilder.calculateAME(INode testNode, Map<Integer, IAttribute> predictedRatings)
 */
public class CalculateAMEtest {
	
	/*
	 * Node to predict: A1 = 4.0, A2 = 3.0
	 * Prediction: A1 = 5.0, A2 = 8.0
	 * Expected AME = 3
	 */
	@Test
	public void testAMEI() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting AME test I..");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(4.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(3.0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, 0);
	INode nodeAttribute2 = new Node(ENodeType.Content,1);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, 3);
	testNode.setNumericalAttributes(attMap);

	System.out.println("Test node: "+testNode.getNumericalAttributesString());
	
	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(5.0);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(8.0);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	System.out.println("Prediction: "+predictionMap.toString());
	
	double calcResult = Double.MIN_VALUE;
	try {
        Class[] parameterTypes = {INode.class, Map.class};
        Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
        method.setAccessible(true);
      
        calcResult = (double) method.invoke(Evaluator.class.newInstance(),testNode,predictionMap);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }

	double expectedResult = 3;
	
	assertEquals("Calculated AME",expectedResult,calcResult,0.00001);
	
	System.out.println("Done.");
}

	/*
	 * Node to predict: A1 = 0.0, A2 = 0.0
	 * Prediction: A1 = 0.0, A2 = 0.0
	 * Expected AME = 0
	 */
	@Test
	public void testAMEII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting AME test II");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, 0);
	INode nodeAttribute2 = new Node(ENodeType.Content, 1);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, 3);
	testNode.setNumericalAttributes(attMap);
	
	System.out.println("Test node: "+testNode.getNumericalAttributesString());

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	System.out.println("Prediction: "+predictionMap.toString());
	
	double calcResult = Double.MIN_VALUE;
	try {
        Class[] parameterTypes = {INode.class, Map.class};
        Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
        method.setAccessible(true);
      
        calcResult = (double) method.invoke(Evaluator.class.newInstance(),testNode,predictionMap);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
	
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	System.out.println("Done.");
}
	/*
	 * Node to predict: A1 = 10.0, A2 = 10.0
	 * Prediction: A1 = 10.0, A2 = 0.0
	 * Expected AME = 5
	 */
	@Test
	public void testAMEIII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting AME test III");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, 0);
	INode nodeAttribute2 = new Node(ENodeType.Content,1);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, 3);
	testNode.setNumericalAttributes(attMap);

	System.out.println("Test node: "+testNode.getNumericalAttributesString());
	
	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	System.out.println("Prediction: "+predictionMap.toString());
	
	double calcResult = Double.MIN_VALUE;
	try {
        Class[] parameterTypes = {INode.class, Map.class};
        Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
        method.setAccessible(true);
      
        calcResult = (double) method.invoke(Evaluator.class.newInstance(),testNode,predictionMap);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
	
	double expectedResult = 5;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	System.out.println("Done.");
}
	/*
	 * Node to predict: A1 = 10.0, A2 = 0.0
	 * Prediction: A1 = 0.0, A2 = 10.0
	 * Expected AME = 10
	 */
	@Test
	public void testAMEIV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting AME test IV");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, 0);
	INode nodeAttribute2 = new Node(ENodeType.Content, 1);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, 3);
	testNode.setNumericalAttributes(attMap);
	
	System.out.println("Test node: "+testNode.getNumericalAttributesString());

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	System.out.println("Prediction: "+predictionMap.toString());
	
	double calcResult = Double.MIN_VALUE;
	try {
        Class[] parameterTypes = {INode.class, Map.class};
        Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
        method.setAccessible(true);
      
        calcResult = (double) method.invoke(Evaluator.class.newInstance(),testNode,predictionMap);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
	
	double expectedResult = 10;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	System.out.println("Done.");
	
}
	/*
	 * Node to predict: A1 = 10.0, A2 = 0.0
	 * Prediction: A1 = 10.0, A2 = 0.0
	 * Expected AME = 0
	 */
	@Test
	public void testAMEV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting AME test V");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, 0);
	INode nodeAttribute2 = new Node(ENodeType.Content, 1);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User, 3);
	testNode.setNumericalAttributes(attMap);
	
	System.out.println("Test node: "+testNode.getNumericalAttributesString());

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(10.0);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericalLeafAttribute(0.0);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	System.out.println("Prediction: "+predictionMap.toString());
	
	double calcResult = Double.MIN_VALUE;
	try {
        Class[] parameterTypes = {INode.class, Map.class};
        Method method = Evaluator.class.getDeclaredMethod("calculateAME", parameterTypes);
        method.setAccessible(true);
      
        calcResult = (double) method.invoke(Evaluator.class.newInstance(),testNode,predictionMap);
    } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        e.printStackTrace();
    }
	
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	System.out.println("Done.");
	
}
}
