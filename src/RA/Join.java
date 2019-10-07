package RA;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import src.Operation;

public class Join extends RANode{
	private int leftC;
	private int rightC;
	private String leftName;
	private String rightName;
	
	private MapInfo map;

	private int [] currHold;
	private int [] rowR;
	private boolean cycle;
	private boolean firstCall;
	
	private HashMap<Integer, List<int []>> joinMap;
	private int leftTIndex;
	private int rightTIndex;
	private Iterator<int[]> currList;
	
//	private boolean rightSideExist;// variable to check if right side is already in map 
	
	public Join(Operation opt) {
		this.leftC = opt.getLeftC();
		this.leftName = opt.getLeftR();
		this.rightC = opt.getRightC();
		this.rightName = opt.getRightR();
		this.currHold = null;
		cycle = false;
		firstCall = true;	
	}
	
	
	
	public String cleanUpTree() {
		if(leftChild.isType("JOIN")) {
			Join jo = (Join) leftChild;
			String existR = jo.cleanUpTree();
			if(existR.contains(rightName)){
//				System.out.println("got rid of "+ rightName);
				rightChild = null;
			}else {
				existR += rightName;
			}
			return existR;
		}else {
			return leftName+rightName;
		}
	}
	
	@Override
	public MapInfo setUp(MapInfo map) {
		map.add(leftName, leftC);
		map.add(rightName, rightC);
		leftChild.setUp(map);
		if(rightChild!=null) {
			rightChild.setUp(map);
			rightTIndex = map.getR(rightName, rightC);
		}else {
			rightTIndex = map.get(rightName, rightC);
		}
		this.map = map;
		leftTIndex = map.get(leftName, leftC);
		return null;
	}
	
	
	
	

	
	@Override
	public boolean hasNext() {
			return true;
	}


	
	
	
	@Override
	public int[] next() {
		if(rightChild == null) {// if both relation to join already inside the map
			while((currHold = leftChild.next())!=null) {
				if(compare()) {
					return currHold;
				}
			}
			return null;// no output match
		}
		while(true) {
			if(firstCall) {
//				System.out.println("calling buildMap"+leftName+rightName);
				buildMap();
				firstCall = false;
			}else {
//				System.out.println("calling buildMap"+leftName+rightName);
				if(!cycle) {
					while((currHold = leftChild.next())!=null){
						if(joinMap.containsKey(currHold[leftTIndex])) {
							currList = joinMap.get(currHold[leftTIndex]).iterator();
							cycle = true;
							break;
						}
					}
					if(currHold == null) {
						return null;
					}
				}
				while(currList.hasNext()) {
					rowR = currList.next();
					return append(currHold,rowR);
				}
				cycle = false;
				
			}
		}
	}
		
	
	private void buildMap() {
		joinMap = new HashMap<Integer, List<int []>>();
//		System.out.println(rightName+"bulding map");
		
		while((rowR = rightChild.next())!=null) {
			
			if(joinMap.containsKey(rowR[rightTIndex])) {
				joinMap.get(rowR[rightTIndex]).add(rowR);
			}else {
				List<int []> arr = new ArrayList<int []>();
				arr.add(rowR);
				joinMap.put(rowR[rightTIndex], arr);
			}
		}
	}
	
	// add the two arrays together
	private int[] append(int[] temp, int[] rowR) {
		int [] newArr = new int [temp.length+rowR.length];
		int newIndex = 0;
		for(int add: temp) {
			newArr[newIndex] = add;
			newIndex++;
		}
		for(int add: rowR) {
			newArr[newIndex] = add;
			newIndex++;
		}
//		System.out.println(Arrays.toString(newArr));
		return newArr;
	}
	
	public MapInfo getMap() {
		return map;
	}
	private boolean compare(int [] R) {
		if(currHold[leftTIndex] == R[rightTIndex]) {
			return true;
		}
		return false;
	}
	
	private boolean compare() {
		if(currHold[leftTIndex] == currHold[rightTIndex]) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isType(String type) {
		if(type.equals("JOIN")) {
			return true;
		}
		return false;
	}
	
	public String getRightR() {
		return leftName;
	}
	public String getLeftR() {
		return rightName;
	}

	
	// don't need getName and getAtt and refresh in join
	@Override
	public String getName() {
		return null;
	}

	@Override
	public int getAtt() {
		return -1;
	}

	@Override
	public void refresh() {
		
	}

	@Override
	public String selfString() {
		String result = "Join:"+leftName+leftC+" with "+rightName+rightC;
		return result;
	}
}
