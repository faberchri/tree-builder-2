package ch.uzh.agglorecommender.evaluator;

import static org.junit.Assert.assertEquals;

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
	
	@Test
	public void testRMEI() throws InstantiationException, IllegalAccessException{
	//public double calculateRMSE (INode testNode, Map<Integer, IAttribute> predictedRatings){
		
		System.out.println("Starting RME test I");
	
	//Create test node to be compared with prediction
	IAttribute testNodeA1 = new ClassitAttribute(1,4,16);
	IAttribute testNodeA2 = new ClassitAttribute(1,3,9);
	
	// ClassitAttribute map of node 1
	Map<INode, IAttribute> attMap = new HashMap<INode, IAttribute>();

	// this node is an attribute of node 1 and node 2
	INode nodeAttribute1 = new Node(ENodeType.Content, null,null);
	INode nodeAttribute2 = new Node(ENodeType.Content, null, null);
	
	// add the corresponding attributes to the attribute map of node 1
	attMap.put(nodeAttribute1, testNodeA1);
	attMap.put(nodeAttribute2, testNodeA2);
	
	// create node 1
	INode testNode = new Node(ENodeType.User,null, null);
	testNode.setRatingAttributes(attMap);

	//Create predictions to be compared to node (Map<Integer, IAttribute> predictedRatings)
	IAttribute predictedA1 = new ClassitAttribute(1,5,25);
	IAttribute predictedA2 = new ClassitAttribute(1,8,64);
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 3.60555127546;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
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
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
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
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 7.07106781187;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
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
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 10;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
	
	@Test
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
	
	Map<Integer, IAttribute> predictionMap = new HashMap<Integer,IAttribute>();
	//????
	predictionMap.put(0, predictedA1);
	predictionMap.put(1, predictedA2);
	
	double calcResult = Evaluator.class.newInstance().calculateRMSE(testNode,predictionMap);
	double expectedResult = 0;
	
	assertEquals("Calculated RMSE",expectedResult,calcResult,0.00001);
	
	
}
}
