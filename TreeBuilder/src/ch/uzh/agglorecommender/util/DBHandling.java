package ch.uzh.agglorecommender.util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;



public class DBHandling {
	
	private GraphDatabaseService graphDb;
	private Index nodeIndex;
	private Index referenceIndex;
	
	public void connect() {
		
		// Establish Database
		String path = null;
		try {
			path = new File(".").getCanonicalPath();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(path + "/lib/neo4j"); //169.254.7.86:7474
		this.nodeIndex = graphDb.index().forNodes( "nodes" );
		this.referenceIndex = graphDb.index().forNodes( "references" );
		registerShutdownHook( graphDb );
		
		// Test Functionality
		//Node parent = graphDb.createNode();
		Map<String,String> attributes = null;
		insertLeaf(attributes);
	}
	
	public void insertLeaf(Map<String,String> attributes) {
	
		// Start Transaction
		Transaction tx = graphDb.beginTx();			
		try 	{
					// Create Node
					Node node = graphDb.createNode();
					
					// Add Properties/Attributes
					node.setProperty( "movies", "Hello" ); // build in attributes here
					TBLogger.getLogger(getClass().getName()).finer( node.getProperty( "movies" ).toString() );
					referenceIndex.add( node, "reference", "users" );
				
					tx.success();
				}
		finally {
					tx.finish();
				}
	}
	
	public void insertParent(Map<String,String> attributes, List<INode> children) {
		Logger log = TBLogger.getLogger(getClass().getName());
		// Start Transaction
		Transaction tx = graphDb.beginTx();			
		try 	{
					// Create Node
					Node node = graphDb.createNode();
					
					// Create fake child node -> just for testing
					Node child = graphDb.createNode();
					
					// Add Properties/Attributes
					node.setProperty( "id", "movie1" ); // build in id and attributes here
					log.finer( node.getProperty( "movies" ).toString() );
					
					// Add Relationship to children and set property for relationship -> process children list
					Relationship relationship;
					relationship = node.createRelationshipTo(child, RelTypes.KNOWS );
					relationship.setProperty( "message", "is parent of" );
					log.finer( relationship.getProperty( "message" ).toString() );
					log.finer( child.getProperty( "message" ).toString() );
					
					tx.success();
				}
		finally {
					tx.finish();
				}
	}
	
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running example before it's completed)
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	private static enum RelTypes implements RelationshipType
	{
	    KNOWS
	}

	public void shutdown() {
		graphDb.shutdown();
		
	}

}
