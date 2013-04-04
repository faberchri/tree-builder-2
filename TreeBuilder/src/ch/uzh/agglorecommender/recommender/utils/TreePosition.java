package ch.uzh.agglorecommender.recommender.utils;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;

import org.neo4j.shell.Output;

import ch.uzh.agglorecommender.clusterer.treecomponent.INode;

public class TreePosition implements Output {
	
	private INode node;
	private double utility;
	private double level;

	public TreePosition(INode position, double utility, double level){
		this.node = position;
		this.utility = utility;
		this.level = level;
	}

	public INode getNode() {
		return node;
	}

	public void setNode(INode position) {
		this.node = position;
	}

	public Double getUtility() {
		return utility;
	}

	public void setUtility(Double utility) {
		this.utility = utility;
	}
	
	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}
	
	public String toString() {
		return node.toString() + " / Utility: " + utility + " / Level: " + level;
	}

	@Override
	public Appendable append(CharSequence csq) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appendable append(CharSequence csq, int start, int end)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appendable append(char c) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void print(Serializable arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void println() throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void println(Serializable arg0) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
