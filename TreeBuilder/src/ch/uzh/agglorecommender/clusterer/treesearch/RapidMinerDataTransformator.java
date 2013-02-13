package ch.uzh.agglorecommender.clusterer.treesearch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DoubleArrayDataRow;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.tools.Ontology;

/*
 * Creates an ExampleSet for each set from the passed List. 
 * The ExampleSet is needed to apply RapidMiner operators on the data.  
 * 
 * Labels in the data are all set to 1
 */

//Instructions here: http://rapid-i.com/wiki/index.php?title=Integrating_RapidMiner_into_your_application#Transform_data_for_RapidMiner
public class RapidMinerDataTransformator {
	
	Set<ExampleSet> setsOfRMdata;
	/*NOT FINISHED YET
	 * Returns the clusterSet as an ExampleSet, so it can be handled by RapidMiner
	 */
	public Set<ExampleSet> transform(Set<List<INode>> NodeSet){
	
	//create attribute list
	List<Attribute> attributes = new LinkedList<Attribute>();
	
	attributes.add(AttributeFactory.createAttribute("Node_ID", Ontology.NOMINAL));
    attributes.add(AttributeFactory.createAttribute("Attribute_ID", Ontology.NOMINAL));

    Attribute label = AttributeFactory.createAttribute("Rating", Ontology.REAL);
    attributes.add(label);
	
    // create table
    MemoryExampleTable table = new MemoryExampleTable(attributes);
		
    // fill table (here: only real values)
    Iterator<List<INode>> setIterator = NodeSet.iterator();
    
    List<INode> list = new ArrayList<INode>();
    
    // For each set
    while(setIterator.hasNext()){
    	list = setIterator.next();
    	
    	//Fill values from list in an ExampleSet
     	Iterator<INode> nodesToClusterIterator = list.iterator();
    	while(nodesToClusterIterator.hasNext()){
    		INode clusterNode = nodesToClusterIterator.next();

    	    double[] data = new double[attributes.size()];
    		
    		//Fill the data. 
    		Set<INode> attributeNodes = clusterNode.getAttributeKeys();
    		Iterator<INode> attributeNodesIterator = attributeNodes.iterator();
    		while(attributeNodesIterator.hasNext()){
    			INode attributeNode = attributeNodesIterator.next();
    			data[0] = clusterNode.getId();
    			data[1] = attributeNode.getId();
    			int value = 1;
    			data[2] = value;
    		}
    		// ???? No idea what this does
    	      // maps the nominal classification to a double value
    	      //data[data.length - 1] = label.getMapping().mapString(getMyClassification(d));
    	          
    	      // add data row
    	      table.addDataRow(new DoubleArrayDataRow(data));
    	}
    	
    	// create example set
    	ExampleSet exampleSet = table.createExampleSet(label);   
    	this.setsOfRMdata.add(exampleSet);
    }	
 
    return setsOfRMdata;
}
}
