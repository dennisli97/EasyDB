package RA;
import java.util.Arrays;

import src.Operation;

public class Project extends RANode{
	private String [] rNames ;// relation name 
	private int [] columns;
	private int [] maskC;
	private int size;// number of projection needed
	
	private long [] resultSum;
	private int [] currRow;// to hold the temp result for previous operation
	
	public Project(Operation opt) {
		this.rNames = opt.getrNames();
		this.columns = opt.getColumns();
		size = columns.length;
		maskC = new int [size];
		this.resultSum = new long [columns.length];
	}
	
	public void cleanUpTree() {
		if(leftChild.isType("JOIN")) {
//			System.out.println("cleaning");
			Join jo = (Join) leftChild;
			jo.cleanUpTree();
		}
	}
	
	
	
	
	@Override
	public MapInfo setUp(MapInfo map) {
		for(int i=0; i<size;i++) {
			map.add(rNames[i], columns[i]);
		}
		
		leftChild.setUp(map);
		
		for(int i=0; i<size;i++) {
			maskC[i] = map.get(rNames[i], columns[i]);
		}
		return map;
	}
	
	
	
	
	
	@Override
	public boolean isType(String type) {
		if(type.equals("PROJECT")) {
			return true;
		}
		return false;
	}
	
	public long [] getResult() {
		currRow = leftChild.next();// get next row from leftChild
		if(currRow == null) {// nothing output from the children
			return null;
		}
		while(currRow != null) {
//			System.out.println(Arrays.toString(currRow));
			for(int i = 0; i<size; i++) {
				resultSum[i] += currRow[maskC[i]];// sum up all information
			}
			currRow = leftChild.next();
		}

		return resultSum;
	}

	@Override
	public int  [] next() {
		return null;
	}

	
	// don't need the following method for project
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAtt() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean hasNext() {
		return false;
	}


	@Override
	public String selfString() {
		String result = "Project:"+Arrays.toString(rNames)+Arrays.toString(columns);
		return result;
	}
}
