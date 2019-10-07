package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import RA.RANode;
import RA.Scan;
import RA.ScanIterator;

public class main {
	public static String PATH;
	public static int SIZE = 1024*1024;
	
	
	
	public static void main(String [] args) throws IOException {
		Scanner console = new Scanner(System.in);
		new File("dataHold").mkdir();
		
		// read paths from input, and initialize a loader for each file
		String sPaths = console.nextLine();
		String [] paths = sPaths.split(",");
		PATH = paths[0].substring(0, paths[0].length()-6);// set the globe variable for path
		
		Loader load = new Loader(paths);
		Scan [] scans = load.loadAll();
		
		String qNums = console.nextLine();
		int qNum = Integer.parseInt(qNums);// number of queries
		
//		long start = System.currentTimeMillis();
		for(int i =0; i<qNum; i++) {
			String query ="";
			query = query+console.nextLine();
			query = query+" "+console.nextLine();
			query = query+" "+console.nextLine();
			query = query+" "+console.nextLine();
			console.nextLine();
			Parser pro = new Parser(query);// construct the scans and operations 
			pro.process();
			Operation [] opts = pro.getOpts();// get the operations 
			String [] rNames = pro.getrNames();// get the relation needed for the query 
			
			Scan [] scanIs = assignScan(rNames, scans);// create scan with the rNames
//			System.out.println(Arrays.toString(opts));
		
			Engine eng = new Engine(opts, scanIs);
			System.out.println(eng.Run());
			eng=null;

		}
//		long end = System.currentTimeMillis();
//		System.out.println(end-start);
		console.close();
	}
	private static Scan[] assignScan(String[] rNames, Scan[] scans) {
		Scan [] scNeed = new Scan[rNames.length];
		for(int i= 0; i < rNames.length; i++) {
			for(Scan sc : scans) {
				if(sc.getName().equals(rNames[i])) {
					scNeed[i] = sc;
				}
			}
		}
		return scNeed;
	}
}
