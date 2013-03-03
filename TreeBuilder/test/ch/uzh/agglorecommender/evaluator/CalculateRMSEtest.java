package ch.uzh.agglorecommender.evaluator;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import ch.uzh.agglorecommender.clusterer.treecomponent.ClassitTreeComponentFactory;
import ch.uzh.agglorecommender.clusterer.treecomponent.ENodeType;
import ch.uzh.agglorecommender.clusterer.treecomponent.IAttribute;
import ch.uzh.agglorecommender.clusterer.treecomponent.INode;
import ch.uzh.agglorecommender.clusterer.treecomponent.Node;
import ch.uzh.agglorecommender.recommender.evaluator.EvaluationBuilder;

public class CalculateRMSEtest {
	
	@Test
	public void testRMEI() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test I");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(4.0);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(3.0);
	
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

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(5.0, null);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(8.0, null);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = EvaluationBuilder.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 5.09901951359;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
}
	
	@Test
	public void testRMEII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test II");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
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

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = EvaluationBuilder.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
	public void testRMEIII() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test III");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	
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

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = EvaluationBuilder.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 7.07106781187;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
	public void testRMEIV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test IV");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
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

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = EvaluationBuilder.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 10;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
	public void testRMEV() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test V");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	IAttribute testNodeA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
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

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(10.0, null);
	IAttribute predictedA2 = ClassitTreeComponentFactory.getInstance()
			.createNumericAttribute(0.0, null);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = EvaluationBuilder.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
}
