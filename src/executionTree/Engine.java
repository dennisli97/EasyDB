package src;

import java.io.IOException;

import RA.Join;
import RA.MapInfo;
import RA.Project;
import RA.RANode;
import RA.Scan;
import RA.Select;

public class Engine {
	private Operation[] opts;
	private Scan [] scans;

	Project root;
	
	
	public Engine(Operation[] opts, Scan [] scans) {
		this.opts = opts;
		this.scans = scans;
	}
	
	public String Run() {
		Optimizer opm = new Optimizer(opts, scans);
		this.root = opm.getComplexTree();
		root.cleanUpTree();
		MapInfo map = new MapInfo();
		root.setUp(map);
//		System.out.println(map.toString());
//		System.out.println(root.toString());
		long [] result = root.getResult();
		String output ="";
		if(result == null) {
			int i = opts[0].getColumns().length;
			while(i>0) {
				output = output+",";
				i--;
			}
		}else {
			for(long r : result) {
				output = output + ","+ r;
			}
		}
		output = output.substring(1,output.length());
		return output;
	}
	
//	public int [] Run1() {
//		
//	}
	
	//run the whole operation array
//	public String Run() throws IOException {	
//		result = "";
//		while(optIn>=0) {// run all selection and projection
////			System.out.println(opts[optIn].getPropetery());
//			
//			if(opts[optIn].getPropetery().equals("SELECT")) {
//				System.out.println("running Selection");
//				selectOperation();
//				
//				
//			}else if(opts[optIn].getPropetery().equals("JOIN")) {
//				System.out.println("running join");
//				joinOperation(opts[optIn]);
//			}else {
//				
//				System.out.println("running projection");
//				result =projectOperation()+","+result;
//				
//			}
//			optIn--;
//		}
//		result = result.substring(0,result.length()-1);
//		return result;
//	}
//	
//	/**
//	 * method to update old joins based on new Join
//	 * @throws IOException
//	 */
////	private void updateOldJ() throws IOException {
////		String currL = opts[optIn].getLeftR();
////		String currR = opts[optIn].getRightR();
////		Scan scL = scans[findScan(currL)];
////		Scan scR = scans[findScan(currR)];
////		int lenL = scL.getFullName().length();
////		int lenR = scR.getFullName().length();
////		if(lenL>2) {
////			String name = scL.getFullName();
//////			System.out.println("updating: " + name);
////			lenL--;
////			while(lenL>1) {
////				String tobeU = name.substring(lenL-1, lenL);
////				joinOperation(findJoinOpt(scL.getrName(), tobeU));
////				lenL--;
////			}
////		}
////		if(lenR>2) {
////			String name = scR.getFullName();
//////			System.out.println("updating: "+name);
////			lenR--;
////			while(lenR>1) {
////				String tobeU = name.substring(lenR-1, lenR);
////				joinOperation(findJoinOpt(scR.getrName(), tobeU));
////				lenR--;
////			}
////		}
////		
////	}
//	
//	
////	private Operation findJoinOpt(String L, String R) {
//////		System.out.println("updating: "+L+R);
////		for(Operation opt : opts ) {
////			if(opt.getPropetery().equals("JOIN")) {
////				if(opt.getLeftR().equals(L)&&opt.getRightR().equals(R)) {
////					return opt;
////				}
////				if(opt.getLeftR().equals(R)&&opt.getRightR().equals(L)) {
////					return opt;
////				}
////			}
////		}
////		return null;
////		
////	}
//	
//	/**
//	 * select operations
//	 * @throws IOException
//	 */
//	private void selectOperation() throws IOException {
//		Select se = new Select(opts[optIn]);
//		// find the scan and attach to leftCHild;
//		int index = findScan(opts[optIn].getLeftR());
//		se.setLeft(scans[index]);
//		Scan newSc = se.runWhole();
//		replaceScan(newSc);
//	}
//	
//	
//	/**
//	 * join operation
//	 * @throws IOException
//	 */
//	private void joinOperation(Operation opt) throws IOException {
//		Join jo = new Join(opt);// initialize node
//		// find the scanner from array
//		int indexL = findScan(opt.getLeftR());
//		int indexR = findScan(opt.getRightR());
//		// setup children
//		jo.setLeft(scans[indexL]);
//		jo.setRight(scans[indexR]);
//		
//		Scan[] scs = jo.runWhole();
//		//replace old scan with new scan
//		for(Scan sc: scs) {
//			replaceScan(sc);
//		}
//		
//	}
//	
//	/**
//	 * projection operation
//	 * @return
//	 * @throws IOException
//	 */
//	private int projectOperation() throws IOException {
//		Project pjt = new Project(opts[optIn]);
//		int index = findScan(opts[optIn].getLeftR());
//		pjt.setLeft(scans[index]);
//		int sum = pjt.runFromScan();
//		return sum;
//	}
//	
//	/**
//	 * method to replace the new scan in the array after opration of a RAnode
//	 * @param sc
//	 */
//	private void replaceScan(Scan sc) {
//		int index = findScan(sc.getrName());
//		if(index != -1) {
//			scans[index].close();// close the scan for space
//			scans[index] = sc;
//		}else {
//			System.out.println("error");
//		}
//	}
//	
//	
//	/**
//	 * mehtod to find the scan for the RAnode
//	 * @param rName
//	 * @return scan index in scans, -1 if not found
//	 */
//	private int findScan(String rName) {
//		int scanIn = 0;
//		while(scans[scanIn] != null) {
//			if(scans[scanIn].getrName().equals(rName)) {
//				return scanIn;
//			}
//			scanIn++;
//		}
//		return -1;
//	}
}
