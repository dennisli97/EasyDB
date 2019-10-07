package src;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import RA.Scan;

/**
 * class to process the CSV file and write into a dat file 
 * also construct catalog for analysing on the way
 * @author dennis
 *
 */
public class Loader {
	String[] paths;
	File write;
	int [] max;// not used
	int [] min;// not used
	/**
	 * 
	 * @param paths
	 */
	public Loader(String [] paths) {
		this.paths = paths;// put paths into array
	}
	
	public Loader() {
		
	}
	
	/**
	 * read every CSV file and construct
	 * array contain all catalogs needed for the every relation
	 * @return
	 * @throws IOException
	 */
	public Scan [] loadAll() throws IOException {
		Catalog [] cats= new Catalog[paths.length];// array contain all catalog needed for the every realtion
		Scan [] scans = new Scan[paths.length];
		for(int i =0; i< paths.length; i++) {
			scans[i] = new Scan(this.read(paths[i]));
//			
		}
		return scans;
	}
	
	/**
	 * read the CSV file and write it on a dat file
	 * @return
	 * @throws IOException
	 */
	public Catalog read(String path) throws IOException {
		String name = path.substring(path.length()-5, path.length()-4);// find the name of the file
		Catalog cat = new Catalog(name);// initialize the catalog for the file
		new File("dataHold/"+name).mkdir();
		FileReader fr = new FileReader(path);
//		FileReader fr = new FileReader(""+name+".csv");
//		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dataHold/"+name+".dat")));
//		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("data/xxxs/"+"/"+name+".dat")));
		
		CharBuffer cb1 = CharBuffer.allocate(main.SIZE);
		CharBuffer cb2 = CharBuffer.allocate(main.SIZE);
		
		
	
		boolean attFound = false;
		int att = 0;
		int row = 0;
		List<Integer> firstRow = new ArrayList<Integer>();
		DataOutputStream [] doss = null;
		int counting;
		int count = 0;
		while(fr.read(cb1) != -1) {
			cb1.flip();
			if(!attFound) {
				int i = 0;
				int lastNumberStart = 0;
				while(!attFound) {
					if(cb1.charAt(i) == ',' || cb1.charAt(i)=='\n') {
						CharBuffer temp = cb1.subSequence(lastNumberStart, i);
						int numread = Integer.parseInt(temp.toString(), 10);
//						System.out.println(numread);
						firstRow.add(numread);
						lastNumberStart = i+1;
						att++;
						
						if(cb1.charAt(i)=='\n') {// count the att on first row
							attFound = true;
							cat.setAtt(att);
						}
					}
					i++;
				}
				doss = new DataOutputStream [att];
				for(i= 0; i< att;i++) {
					DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dataHold/"+name+"/"+i+".dat")));
//					dos.writeInt(firstRow.get(i));
					doss[i] = dos;
				}
			}	
				
			int lastNumberStart = 0;
//			int count = 0;
			for(int i =0; i<cb1.length(); i++) {
				if(cb1.charAt(i) == ',' || cb1.charAt(i)=='\n') {
					CharBuffer temp = cb1.subSequence(lastNumberStart, i);
					int numread = Integer.parseInt(temp.toString(), 10);
//					if(count ==0){
//						System.out.println(numread);
//					}
					
					doss[count].writeInt(numread);
					count++;
					lastNumberStart = i+1;
					// row counter and att assign
					if(cb1.charAt(i)=='\n') {
						row++;
						count = 0;
					}
				}
			}
			cb2.clear();
			cb2.append(cb1, lastNumberStart, cb1.length());
			
			CharBuffer tmp= cb2;
			cb2 = cb1;
			cb1 = tmp;
			
		}
		
		fr.close();
		for(int i= 0; i< att;i++) {
			doss[i].close();
		}
		cat.setRow(row);
//		System.out.println(cat.getAtt()+" "+cat.getRow());
		return cat;
	}
}
