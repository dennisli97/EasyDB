package src;

import RA.Scan;
/**
 * Class to hold all query operatrions
 * 1: project
 * 2: JOIN
 * 3: SELECT
 * @author dennis
 *
 */
public class Operation {
	private int property;// kind of RAnode
	private String leftR;// relation name
	private String rightR;
	private int leftC;// colum name
	private int rightC;
	private String compare;// for selection
	private int value;// for selection
	
	private String [] rNames;
	private int [] columns;
	
	
	
	
	private Catalog leftCat;
	
	
	// for projection
	public Operation(String [] rNames, int [] columns) {
		this. rNames = rNames;
		this.columns = columns;
		this.property = 1;
	}
	
	//for join
	public Operation(String leftR, int leftC, String rightR, int rightC) {
		this. leftR = leftR;
		this.leftC = leftC;
		this. rightR = rightR;
		this.rightC = rightC;
		this.property = 2;
	}
	
	// for select
	public Operation(String leftR, int leftC, int value, String compare) {
		this. leftR = leftR;
		this.leftC = leftC;
		this.compare = compare;
		this.value = value;
		this.property = 3;
	}
	
	// method to swap left and right side of a Join
	public void swap() {
		String tempR = leftR;
		int tempC = leftC;
		leftR = rightR;
		leftC = rightC;
		rightR = tempR;
		rightC = tempC;
	}
	// method to get all projection relations
	public String [] getrNames() {
		if(property == 1) {
			return rNames;
		}
		return null;
	}
	// method to get all projection columns
	public int [] getColumns() {
		if(property == 1) {
			return columns;
		}
		return null;
	}
	
	/**
	 * method to get the relation name
	 * if it's for a join, the leftside of the equal join
	 * @return
	 */
	public String getLeftR() {
		return leftR;
	}
	/**
	 * method to get the relation column 
	 * if it's for a join, the leftside column of the equal join
	 * @return
	 */
	public int getLeftC() {
		return leftC;
	}
	
	// for join to get right side of the  relation name
	public String getRightR() {
		if(property==2){
			return rightR;
		}
		return null;
	}
	
	// method for join to get right side of column
	public int getRightC() {
		if(property==2){
			return rightC;
		}
		return -1;
	}
	
	// method for selection to find out the value to be conpare
	public int getValue() {
		if(property==3){
			return value;
		}
		return -1;
	}
	
	// method for selection to find what kind of comparision 
	
	public String getCompare() {
		if(property==3){
			return compare;
		}
		return null;
	}
	
	// find what property is the operation 
	public String getPropetery() {
		if(property ==1) {
			return "PROJECT";
		}
		if(property ==2) {
			return "JOIN";
		}
		if(property ==3) {
			return "SELECT";
		}
		return null;	
	}
	
	public String toString() {
		String built = "";
		if(property==1) {
			
			for(int i = 0; i <rNames.length; i++) {
				built += "PROJECT Column SUM for "+columns[i]+" in relation "+rNames[i]+"\n";
			}
			return built;
		}
		if(property==2) {
			built = "JOIN Column "+leftC+" in relation "+leftR+" with column "+ rightC +" in relation "+ rightR+"\n";
			return built;
		}
		if(property==3) {
			built = "SELECT Column "+leftC+" in relation "+leftR + " that is " +compare+" "+ value+"\n";
			return built;
		}
		return null;
	}
}
