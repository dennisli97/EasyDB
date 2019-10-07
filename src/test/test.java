package src;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import RA.Join;
import RA.Project;
import RA.RANode;
import RA.Scan;
import RA.ScanIterator;
import RA.Select;

public class test {
	public static void main(String[] args) throws IOException {
		ScanIterator sc1 = scanTest1(loadertest1());
		ScanIterator sc2 = scanTest2(loadertest2());
		ScanIterator sc3 = scanTest3(loadertest3());
		RANode sel = selectTest(sc1);
		loadertest1();
		System.out.println(sel.toString());
		
		baseJoinTestwithSelect(sc1,sc2, sel);
		System.out.println("finished");
		joinTest(sel, sc2, sc3);
		
		
		
		
	}
//	
	public static void baseJoinTestwithSelect(RANode leftleft, RANode right, RANode left) throws IOException {
		Operation opt = new Operation("A",1, "B",0 );
		Join jo = new Join(opt);
		right.setLeft(leftleft);
		jo.setLeft(left);
		jo.setRight(right);
		
		jo.setUp();
		while(jo.hasNext()) {
//			System.out.println("hasNext");
			System.out.println(Arrays.toString(jo.next()));
//			Arrays.toString(jo.next());
		}
	}
	
	public static void joinTest(RANode left, ScanIterator right, ScanIterator second) throws IOException {
		Operation optB = new Operation("A",1, "B",0 );
		Operation opt = new Operation("A",3, "D",0 );
		Join joB = new Join(optB);
		Join jo = new Join(opt);
		joB.setLeft(left);
		joB.setRight(right);
//		System.out.println(right.getLeft());
		jo.setLeft(joB);
		jo.setRight(second);
//		jo.setUp();
		System.out.println(jo.toString());
		int [] arr;
		while((arr = jo.next())!=null) {
			
//			if(arr!=null) {
				System.out.println(Arrays.toString(arr));
//				arr = null;
//			}
//			Arrays.toString(jo.next());
		}
	}
	
	
	
	public static Catalog loadertest1() throws IOException {
		Loader lo = new Loader();
		Catalog cat = lo.read("data/xxxs/A.csv");
		System.out.println("finished loading");
		return cat;
	}
	
	public static Catalog loadertest2() throws IOException {
		Loader lo = new Loader();
		Catalog cat = lo.read("data/xs/B.csv");
		System.out.println("finished loading");
		return cat;
	}
	
	public static Catalog loadertest3() throws IOException {
		Loader lo = new Loader();
		System.out.println("finished loading");
		Catalog cat = lo.read("data/xxxs/D.csv");
		return cat;
	}
	
	
	
	public static ScanIterator scanTest1(Catalog cat) throws IOException {
		ScanIterator sc = new ScanIterator(cat);
		int [] co = new int [] {4};
		sc.setUp(co);
		int [] currHold = sc.next();
//		System.out.println(Arrays.toString(currHold));
//		int row = 0;
		while(currHold != null) {
			System.out.println(Arrays.toString(currHold));
//			row+=1;
			currHold =sc.next();
		}
//		System.out.println(row);
		return sc;

	}
	
	public static ScanIterator scanTest2(Catalog cat) throws IOException {
		ScanIterator sc = new ScanIterator(cat);
//		while(sc.hasNext()) {
//			System.out.println(Arrays.toString(sc.next()));
//		}
		return sc;

	}
	
	public static ScanIterator scanTest3(Catalog cat) throws IOException {
		ScanIterator sc = new ScanIterator(cat);
//		while(sc.hasNext()) {
//			System.out.println(Arrays.toString(sc.next()));
//		}
		return sc;
	}
	
	public static RANode selectTest(ScanIterator sc) throws IOException {
		Operation opt = new Operation("A",0, 0,"<" );
		Select sel = new Select(opt);
		sel.setLeft(sc);
//		System.out.println(sel.toString());
		return sel;

	}
	
	public static void projectTest(Scan sc) {
		Project pro = new Project("SUM(A.c0)");
		pro.setLeft(sc);
		System.out.println(pro.runFromScan());
	}
	

	
}
