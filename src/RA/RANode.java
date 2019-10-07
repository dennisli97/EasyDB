package RA;

import java.util.Iterator;

public abstract class RANode implements Iterator<int []>{
	protected RANode rightChild;
	protected RANode parent;
	protected RANode leftChild;
	
	
	public RANode() {
		rightChild = null;
		parent= null;
		leftChild = null;
	}
	
	
	public void setLeft(RANode node) {
		this.leftChild = node;
		node.setParent(this);
	}
	
	public void setRight(RANode node) {
		this.rightChild = node;
		node.setParent(this);
	}
	
	public RANode getLeft() {
		return leftChild;
	}
	
	public RANode getRight() {
		return rightChild;
	}
	
	public void setParent(RANode node) {
		this.parent = node;
	}
	
	public String toString() {
		String result="";
		if(leftChild != null) {
			result = leftChild.toString()+result;
		}
		result = "("+result+selfString();
		if(rightChild != null) {
			result = result + rightChild.toString();
		}
		return result+")";
	}
	abstract public MapInfo setUp(MapInfo map);
	abstract public String selfString();
	public abstract boolean isType(String type);
	abstract public String getName();
	abstract public int getAtt();
	abstract public void refresh();
		// TODO Auto-generated method stub
		
}
