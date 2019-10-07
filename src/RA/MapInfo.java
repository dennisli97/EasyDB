package RA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class MapInfo {
	String [] rNames;
	String [] rNames2;// order of scans after setup
	ArrayList<Integer> [] columns;
	int [] rStarts;
	int rCounts;
	int sCounts;
	int prev;
	int sum;
	
	public MapInfo() {
		rNames = new String [30];
		rNames2 = new String [30];
		rStarts = new int [30];
		columns = new ArrayList[30];
		rCounts = 0;
		prev = 0;
		sCounts = 0;// setUp counts
	}
	
	
	/**
	 * add a new row into the array, and allocate starting point in the array
	 * @param rName
	 * @param att
	 */
	public boolean add(String rName, int column) {
		// prevent adding duplicates
		for(int i =0; i<rCounts;i++) {
			if(rNames[i].equals(rName)) {
				if(!columns[i].contains(column)) {
					columns[i].add(column);
					columns[i].sort(new Comparator<Integer>() {// sort the arraylist during adding
                    	public int compare(Integer i1, Integer i2) {
                    		return i1-i2;
                    	}
                    });
					return true;
				}else {
					return false;
				}
			}// case of new adding is already inside of array
		}
		rNames[rCounts] = rName;
		columns[rCounts] = new ArrayList<Integer>(); 
		columns[rCounts].add(column);
		rCounts++;
		return true;
	}
	
	// let the scan get all columns needed and also set up the array map
	public int[] get(String rName) {
		ArrayList<Integer> lArr = findList(rName);
		if(lArr == null) {
			System.out.println("not in map");
			return null;
		}
		int [] arr = new int[lArr.size()];
		for(int j= 0; j< lArr.size();j++) {
			arr[j] = lArr.get(j);
		}
		rStarts[sCounts] = prev;
		rNames2[sCounts] = rName;
		prev += lArr.size();// move the size of the array;
		sCounts++;
		return arr;
		
	
	}
	// method to get mask column from the right child
	public int getR(String rName, int column) {
		ArrayList<Integer> lArr = findList(rName);
		return lArr.indexOf(column);
	}
	
	// method to get mack column from the leftCdild
	public int get(String rName, int column) {
		for(int i=0; i<sCounts; i++) {
			if(rNames2[i].equals(rName)) {
				int internLocation = findList(rName).indexOf(column);
				return rStarts[i]+internLocation;
			}
		}
		return -1;
	}
	
	private ArrayList<Integer> findList(String rName) {
		for(int i = 0; i<rCounts;i++) {
			if(rNames[i].equals(rName)) {
				return columns[i];
			}
		}
		return null;
	}
	
	
	public String toString() {
		String result;
		result = rCounts+"\n";
		result += Arrays.toString(rNames2)+"\n";
		result += Arrays.toString(rStarts);

		return result;
	}
}
