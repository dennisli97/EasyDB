package src;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import RA.Join;
import RA.Project;
import RA.RANode;
import RA.Scan;
import RA.ScanIterator;
import RA.Select;
/**
 * the purpose of this class is to parse through the query input and construct different RA nodes for the execution engine.
 * @author dennis
 *
 */
public class Parser {
	private String q;
	private Scanner qScan;
	private String[] rNames;// array to hold all scanners
	private int scanCount;
	
	private Operation [] opts;
	private int optCount;

	public Parser(String query){
		this. q = query.substring(0, query.length()-1);
		qScan = new Scanner(q);
		opts = new Operation[30];
		rNames = new String [30];
		scanCount = 0;
		optCount = 0;
		
		
	}
	
	public void process() {
//		Scanner qScan = new Scanner(q);
		String temp = qScan.next();
		String rName =null;
		int att = 0;
		Operation opt;
		
		
		
		if(temp.equals("SELECT")) {// start of the query
//			temp = qScan.next(); // skip SELECT
			setProjection();
			
			temp = qScan.next();// skip FROM


			
			while(!temp.equals("WHERE")) {// process all the scan 
				rName = temp.substring(0, 1);
				rNames[scanCount] = rName;// place scan RA in array for join or select
				scanCount++;
				temp = qScan.next();
			}
			
			String leftR = null;
			String rightR = null;
			int leftC = -1;
			int rightC = -1;
			boolean done = false;
			while(qScan.hasNext()) {// process all the join and select
				
				done = false;// variable for ensure the predicate is join or select
				String one = qScan.next();
				String two = qScan.next();
				String three = qScan.next();
//				System.out.println(one+two+three);
				if(three.length()>1) {// edgeCase for three is only one digit
					if(three.charAt(1)=='.' && one.charAt(1)=='.' && two.equals("=")){// for a join command
						
						leftR = one.substring(0, 1);
						leftC = Integer.parseInt(one.substring(3));
						
						rightR = three.substring(0,1);
						rightC = Integer.parseInt(three.substring(3));
						
						opt = new Operation(leftR, leftC, rightR, rightC);
						opts[optCount] = opt;
						optCount++;
						done = true;
	
					}
				}
				if(one.charAt(1)=='.'&& !done){// for a select command
					
					leftR = one.substring(0, 1);
					leftC = Integer.parseInt(one.substring(3));
					
					opt = new Operation(leftR, leftC, Integer.parseInt(three), two);
					opts[optCount] = opt;
					optCount++;
				}
				if(qScan.hasNext()) {
					qScan.next();
				}
			}
		}	
	}
	
	/**
	 * method to process all the project and make an operation variable 
	 */
	private void setProjection() {
		String temp = qScan.next();
		String [] tempN = new String[10];// array to hold name of all projection
		int [] tempC = new int[10];// array to hold all column for each projection
		int count = 0;
		
		
		while(!temp.equals("FROM")) {// process projection
//			System.out.println(temp);
			if(temp.length()>4) {
				tempN[count] = temp.substring(4,5);// get the relation name
				if(temp.endsWith(",")) {
					tempC[count] = Integer.parseInt(temp.substring(7, temp.length()-2));// get the column to project
				}else {
					tempC[count] = Integer.parseInt(temp.substring(7, temp.length()-1));
				}
			}
			temp = qScan.next();
			count++;
		}
		
		String [] rNames = new String[count];
		int [] columns = new int[count];
		
		// reshape the array to correct size
		for(int i = 0; i<count;i++) {
			rNames[i] = tempN[i];
			columns[i] =tempC[i];
			
		}
		Operation opt = new Operation(rNames, columns);
		opts[optCount] = opt;
		optCount++;
	}
	
	public Operation[] getOpts() {
		Operation[] optss = new Operation[optCount];
		for(int i =0; i< optCount;i++) {
			optss[i] = opts[i];
		}
		return optss;
	}
	
	public String [] getrNames() {
		String[] rNamess = new String[scanCount];
		for(int i =0; i< scanCount;i++) {
			rNamess[i] = rNames[i];
		}
		return rNamess;
	}
	
}
