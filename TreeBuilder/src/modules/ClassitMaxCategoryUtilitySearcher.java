package modules;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import scala.actors.threadpool.Arrays;
import utils.TBLogger;
import clusterer.IAttribute;
import clusterer.INode;

public class ClassitMaxCategoryUtilitySearcher extends MaxCategoryUtilitySearcher implements Serializable {

	/**
	 * Determines if a de-serialized file is compatible with this class.
	 * <br>
	 * <br>
	 * Maintainers must change this value if and only if the new version
	 * of this class is not compatible with old versions.
	 */
	private static final long serialVersionUID = 1L;
	
	private static final double acuity = 1.0;
	
	private static Logger log = TBLogger.getLogger(ClassitMaxCategoryUtilitySearcher.class.getName());

	@Override
	protected double calculateCategoryUtility(INode[] possibleMerge) {
		Set<INode> allAttributes = new HashSet<INode>();

		for (INode node : possibleMerge) {
			allAttributes.addAll(node.getAttributeKeys());
		}
		
		double utility = 0.0;
		
		for (INode node : allAttributes) {
			utility += 1.0 / calcStdDevOfAttribute(node, possibleMerge);
		}
		
		// Normalize sum with the number of attributes
		utility /= (double) allAttributes.size();
		
		log.finer("Classit category utility of merge of " + Arrays.asList(possibleMerge).toString() + " is " + utility);
		return utility;
	}
		
	public static int calcSupportOfAttribute(INode attribute, INode[] possibleMerge) {
		int res = 0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSupport();
			}
		}
		log.finest("support for " + attribute +" on merge of "+ Arrays.asList(possibleMerge).toString() + " is "+res + ".");
		return res;
	}
	
	public static double calcSumOfRatingsOfAttribute(INode attribute, INode[] possibleMerge) {
		double res = 0.0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSumOfRatings();
			}
		}
		log.finest("sum of ratings for " + attribute +" on merge of "+ Arrays.asList(possibleMerge).toString() + " is "+res + ".");
		return res;
	}
	
	public static double calcSumOfSquaredRatingsOfAttribute(INode attribute, INode[] possibleMerge) {
		double res = 0;
		for (INode node : possibleMerge) {
			IAttribute att = node.getAttributeValue(attribute);
			if (att != null) {
				res += att.getSumOfSquaredRatings();
			}
		}
		log.finest("sum of squared ratings for " + attribute +" on merge of "+ Arrays.asList(possibleMerge).toString() + " is "+res + ".");
		return res;
	}
	
	public static double calcStdDevOfAttribute(INode attribute, INode[] possibleMerge) {
		int support = calcSupportOfAttribute(attribute, possibleMerge);
		double sumOfRatings = calcSumOfRatingsOfAttribute(attribute, possibleMerge);
		double sumOfSquaredRatings = calcSumOfSquaredRatingsOfAttribute(attribute, possibleMerge);
				 
		if (support < 1) {
			log.severe("Attempt to calculate standard deviation with support smaller 1." );
			System.exit(-1);
		} 

		if ( support == 1 ){
			// the stddev would be equal 0 but we return the acuity to prevent to prevent division by 0
			return acuity;
		}
		
		// calculate the standard deviation incrementally, according to
		/// http://de.wikipedia.org/wiki/Standardabweichung#Berechnung_f.C3.BCr_auflaufende_Messwerte 
		double stdDev = Math.sqrt( (1.0/((double) (support - 1))) * (sumOfSquaredRatings - (1.0 / (double) support) * Math.pow(sumOfRatings, 2.0)) );
		log.finest("standard deviation for " + attribute +" on merge of "+ Arrays.asList(possibleMerge).toString() + " is "+stdDev + ".");
		
		// check if the calculated standard dev is smaller than the specified acuity
		// if true use assign acuity to stddev.
		if (stdDev < acuity) {
			stdDev = acuity;
			log.finer("standard deviation is smaller than acuity. Acuity ("+acuity+") is returned as standard deviation.");
		}
		
		return stdDev;
	}
	
	public static double getAcuity() {
		return acuity;
	}

	
// ------------------------------------- for deletion ------------------------------	
	
//	System.out.println("multiple closest Nodes searcher");
//	
//	long time = System.currentTimeMillis();
//	double closestDistance = Double.MAX_VALUE;
//	List<INode> closestNodes = new ArrayList<INode>();
//	Set<INode> subSet = new HashSet<INode>(openNodes);
//	
//	// Add all Nodes with closest Distance to closestNodes List
//	for (INode openNode : openNodes) {
//		subSet.remove(openNode); // Should prevent duplicate comparisons
//		
//		// Compare with all other nodes
//		for (INode subSetNode : subSet) {
//			double tmpDistance = openNode.getDistance(subSetNode, Counter.getInstance(), openNodes);
//			
//			// Found closer distance -> all previous found nodes don't matter anymore
//			if (tmpDistance < closestDistance){
//				closestDistance = tmpDistance;
//				closestNodes.clear();
//				closestNodes.add(openNode);
//				closestNodes.add(subSetNode); // is this good?
//				System.out.println("Found new closest Distance beetween "+openNode+", "+subSetNode+" ("+tmpDistance+")");
//			}
//			
//			// Found similar distance -> add node to collection
//			if(tmpDistance == closestDistance){
//				
//				// Ignore already found pairs
//				if(!(closestNodes.contains(openNode) && closestNodes.contains(subSetNode))){
//					if(!closestNodes.contains(openNode)) {
//						closestNodes.add(openNode);
//					}
//					if(!closestNodes.contains(subSetNode)){
//						closestNodes.add(subSetNode);
//					}
//					System.out.println("Found similar distance for "+openNode+", "+subSetNode+" ("+tmpDistance+")");
//					System.out.println("Current Closest Nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
//				}
//			}
//			
//			// Update comparison counter
//			Counter.getInstance().addComparison();
//		}
//	}
//	
//	if (closestNodes.size() > 1) {
//		System.out.println("Closest nodes: "+ closestNodes.toString() +" ("+closestDistance+")");
//	}
//	time = System.currentTimeMillis() - time;
//	System.out.println("Time in getClosestNode() single: " + (double)time / 1000.0);
//	return closestNodes;
	

}
