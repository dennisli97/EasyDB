package src;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import RA.Join;
import RA.Project;
import RA.RANode;
import RA.Scan;
import RA.ScanIterator;
import RA.Select;

public class Optimizer {
	private Operation [] opts;
	private Catalog cats;
	private String [] scans;
	private Map<String, Scan> scanMap;
	private Map<String, LinkedList<String>> joinMap;
	private Map<String, RANode> selectMap;
	private Stack<Select> selects;
	private  LinkedList<Join> joins;
	private String existR;//keep track of all realtions that's already in the tree
	private Scan maxScan;
	
	
	
	
	
	public Optimizer(Operation[] opts, Scan [] scans) {
		this.opts = opts;
		this.selects = new Stack<Select>();
		this.joins = new LinkedList<Join>();
		existR = "";
		
		scanMap = new HashMap<String, Scan>();
		selectMap = new HashMap<String, RANode>();
		
		for(Scan sc : scans) {
			scanMap.put(sc.getName(), sc);
			if(maxScan != null) {
				if(maxScan.getRows()<sc.getRows()) {
					maxScan = sc;
				}
			}else {
				maxScan = sc;
			}
		}
		// TODO Auto-generated constructor stub
	}
	
	// method based on maxScan file at the bottom 
	public Project getComplexTree() {
		Project root = new Project(opts[0]);
		int end = selections();
		root.setLeft(getBestTree(end));
		return root;
	}
	
	
	public RANode getBestTree(int end) {
		int minCost = Integer.MAX_VALUE;
		int bestBase = getBestBase(end);
//		Queue<String> q = new LinkedList<String>();
		if(bestBase<0) {
			bestBase = bestBase*-1;
			opts[bestBase].swap();
		}
		String existR = opts[bestBase].getLeftR()+opts[bestBase].getRightR();
		String order = opts[bestBase].getLeftR()+opts[bestBase].getRightR();
		Operation [] remainJoin = new Operation[end];
		int [] joinIndexOrder = new int [end];
		joinIndexOrder[0] = bestBase; 
		
		int[] bestOrder = getOrder(order, end, joinIndexOrder);
		return getTreeFromArray(bestOrder);
		
		
		// build based on queue;
	}
	
	
	private RANode getTreeFromArray(int[] bestOrder) {
		RANode hold = getBJoin(bestOrder[0]);
		for(int i= 1; i< bestOrder.length;i++) {
			Join jo = getMJoin(bestOrder[i]);
			jo.setLeft(hold);
			hold = jo;
		}
		return hold;
	}

	private int [] getOrder(String existR, int end, int[] joinIndexOrder) {
//		System.out.println(Arrays.toString(joinIndexOrder));
		if(joinIndexOrder[end-1] != 0) {// when the joinIndexOrder array is full, all join is processed
			return joinIndexOrder;
		}else {
			int [] bestOrder = null;
			int minCost = Integer.MAX_VALUE;
			for(int i= 1; i<=end; i++) {
				int [] temp = null;
				boolean swapped = false;
				if(existR.contains(opts[i].getLeftR())) {
					if(!checkIn(joinIndexOrder, i)) {
						int [] nJoinOrder = joinIndexOrder.clone();// get a new order array 
						placeInA(nJoinOrder, i);// add the index in tp the array 
						String nexistR = existR+opts[i].getRightR();// get a new existR 
						temp = getOrder(nexistR, end, nJoinOrder);
		
					}
				}else if(existR.contains(opts[i].getRightR())) {
					if(!checkIn(joinIndexOrder, i)) {
						opts[i].swap();
						swapped = true;
						int [] nJoinOrder = joinIndexOrder.clone();// get a new order array 
						placeInA(nJoinOrder, i);
						String nexistR = existR+opts[i].getRightR();// get a new existR 
						temp = getOrder(nexistR, end, nJoinOrder);
					}
				}
				if(temp != null) {
					int cost = getCost(temp);
					if(cost<minCost) {// get the best path
						bestOrder = temp;
					}else if(swapped = true) {// if not the best path, redo the swap
						opts[i].swap();
					}
				}
			}
			return bestOrder;
		}
	}
	
	
	private void placeInA(int [] arr, int index) {
		for(int i= 0; i< arr.length; i++) {
			if(arr[i]==0) {
				arr[i] = index;
				return;
			}
		}
	}
	
	
	private boolean checkIn(int [] joinIndexOrder, int index) {
		for(int i= 0; i< joinIndexOrder.length; i++) {
			if(joinIndexOrder[i] == index) {
				return true;
			}
			if(joinIndexOrder[i] == 0) {
				return false;
			}
		}
		return false;
	}
	

	private int getBestBase(int end) {
		Scan left;
		Scan right;
		Operation opt;
		int best = 0;
		int bCost = -1;
		int tempCost;
		for(int i =1; i <=end; i++) {
			opt = opts[i];
			left = scanMap.get(opt.getLeftR());
			right = scanMap.get(opt.getRightR());
			if(left.equals(maxScan)) {
				tempCost = getCost(left, right);
				if(tempCost>bCost) {
					best = i;
				}
			}else if(right.equals(maxScan)){
				opts[i].swap();
				tempCost = getCost(left, right);
				if(tempCost>bCost) {
					best = i;
				}else {
					opts[i].swap();
				}
			}
		}
		return best;
	}
	
	
	private int getCost(Scan left, Scan right) {
//		System.out.println(left.toString()+" "+right.toString());
		int leftT = (left.getAtts()*left.getRows())/(main.SIZE/4);
		int rightT =(right.getAtts()*right.getRows())/(main.SIZE/4);
		
		return leftT+leftT*rightT;
	}

	
	// get the cost of current tree
	private int getCost(int [] arr) {
		int cost = getCost(scanMap.get(opts[arr[0]].getLeftR()), scanMap.get(opts[arr[0]].getRightR()));
		for(int i= 1; i< arr.length; i++) {
			int temp = (scanMap.get(opts[arr[0]].getRightR())).getAtts()*scanMap.get(opts[arr[0]].getRightR()).getRows()/(main.SIZE/4);
			cost = cost + cost*temp;
		}
		return cost;
	}
	
	
	
	
	
	// this join tree is based on projection priority 
	public Project getSimpleTree() {
		Project root = new Project(opts[0]);
		Operation opt;
		
		int end = selections();
		
		int lastJ = end;
		
		
		RANode hold = null;
		
		while(end != 0) {// process fist join with double select, and place at the bottom
//			System.out.println("d joins");
//			if(opts[end] != null) {
				opt = opts[end];
				if(selectMap.containsKey(opt.getLeftR()) && selectMap.containsKey(opt.getRightR())) {
					Join jo = getBJoin(end);
					if(hold != null) {
						jo.setLeft(hold);
						System.out.println("two join all have predicate");
					}
					hold = jo;
				}
//			}
			end--;
		}
		
		end = lastJ;
		
		while(end != 0) {//process join with single select and add into tree
//			System.out.println("s joins");
			if(opts[end] != null) {
				opt = opts[end];
				if(hold == null) {// if no double join
					if(selectMap.containsKey(opt.getLeftR())||selectMap.containsKey(opt.getRightR())) {// if at least 1 side has a scan
						Join jo = getBJoin(end);
						hold = jo;
						end = lastJ;
//						System.out.println("bJoins");
					}else {
						end--;
					}
				}else if(selectMap.containsKey(opt.getRightR())&& existR.contains(opt.getLeftR())) {// if leftSide is in tree and has a select for right side
				
					Join jo = getMJoin(end);
					jo.setLeft(hold);
					hold = jo;
					end = lastJ;// after new Join is in, do the matching again
					
				}else if(selectMap.containsKey(opt.getLeftR())&&existR.contains(opt.getRightR())) {// if rightSide is in tree and has a select for left side
					opt.swap();
					Join jo = getMJoin(end);
					jo.setLeft(hold);
					hold = jo;
					end = lastJ;// after new Join is in, do the matching again
					
				}else{
//					System.out.println(hold.toString());
					end--;
				}
				
			}else {
				end--;
			}
		}
			
		end = lastJ;
		while(end != 0) {// put all the rest join in the tree
//			System.out.println(hold.toString());
			if(opts[end] != null) {
				opt = opts[end];
				if(existR.contains(opt.getLeftR())) {// if left side is in tree
					Join jo = getMJoin(end);
					jo.setLeft(hold);
					hold = jo;
					end = lastJ;
						
				}else if (existR.contains(opt.getRightR())) {// if right side is in tree
					opt.swap();
					Join jo = getMJoin(end);
					jo.setLeft(hold);
					hold = jo;
					end = lastJ;
					
				}else {
					end--;
				}
			}else {
				end--;
			}
		}
		root.setLeft(hold);
		return root;
	}
	
	/**
	 * method process all the selections from the array
	 * @return the index of the last join
	 */
	private int selections() {
		int end = opts.length-1;
		
		Operation opt = opts[end];
		while(opt.getPropetery()!="JOIN") {// get all projections
//			selections.add(opts[end].getLeftR());
//			Operation opt = opts[end];
			if(!selectMap.containsKey(opt.getLeftR())) {// if selection is not in map 
				RANode temp = new Select(opt);
				temp.setLeft(scanMap.get(temp.getName()).iterator());
				selectMap.put(opt.getLeftR(), temp);
			}else {
				RANode temp = new Select(opt);
				temp.setLeft(selectMap.get(opt.getLeftR()));
				selectMap.put(opt.getLeftR(), temp);// update the selection
			}
			end--;
			opt = opts[end];
		}
		return end;
	}
	
	
	
	// get bottom join
	private Join getBJoin(int index) {
		Operation opt= opts[index];
		Join jo = new Join(opt);
		if(selectMap.containsKey(opt.getLeftR())) {// if leftSide has selection
			jo.setLeft(selectMap.get(opt.getLeftR()));
			selectMap.remove(opt.getLeftR());
		}else {
			jo.setLeft(scanMap.get(opt.getLeftR()).iterator());
		}
		
		if(selectMap.containsKey(opt.getRightR())) {// if right side has selection
			jo.setRight(selectMap.get(opt.getRightR()));
			selectMap.remove(opt.getRightR());
		} else {
			jo.setRight(scanMap.get(opt.getRightR()).iterator());
		}
		
		existR = existR+opt.getLeftR()+opt.getRightR();// add to record
		opts[index] = null;// free the operation
		return jo;
	}
	
	// get middle join
	private Join getMJoin(int index) {
		Operation opt= opts[index];
		Join jo = new Join(opt);
		
		if(selectMap.containsKey(opt.getRightR())) {// if right side has a selection
			jo.setRight(selectMap.get(opt.getRightR()));
			selectMap.remove(opt.getRightR());
		} else {
			jo.setRight(scanMap.get(opt.getRightR()).iterator());
		}
		
		existR = existR+opt.getRightR();
		opts[index] = null;
		return jo;
	}


	
	
}
