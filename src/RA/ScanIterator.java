package RA;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import src.Catalog;
import src.main;

public class ScanIterator extends RANode{
	private Catalog cat;
	private String rName;
	private String rPath;
	private int att;
//	private int row;
	private FileInputStream [] fiss;
	
	private int [] columns;// containning all the column numbers needed from the relation
	
	private byte[][] bbs;// contain all buffers for each column
	
	private int attSize;// total size of attributs
	
	private boolean cycle;
	private int currIndex;
	private int arrIndex;
	
	
	
	public ScanIterator(Catalog cat){
		super();
		this.cat = cat;
		this.rName  = cat.getName();
		this.att = cat.getAtt();
	}
	
	@Override
	public MapInfo setUp(MapInfo map){
		columns = map.get(rName);
		attSize = columns.length;
		fiss = new FileInputStream[attSize];
		bbs = new byte[attSize][];
		
		cycle = false;
		for(int i = 0; i< attSize;i++) {
			try {
				fiss[i] = new FileInputStream("dataHold/"+rName+"/"+columns[i]+".dat");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bbs[i] = new byte[main.SIZE];
		}
//		System.out.println(rName+"setUp");
		return map;
	}
	
	public void setUp(int [] columns) throws FileNotFoundException {
		this.columns = columns;
		attSize = columns.length;
		fiss = new FileInputStream[attSize];
		bbs = new byte[attSize][];
		
		cycle = false;
		for(int i = 0; i< attSize;i++) {
			fiss[i] = new FileInputStream("dataHold/"+rName+"/"+columns[i]+".dat");
			bbs[i] = new byte[main.SIZE];
		}
	}
	

	@Override
	public int[] next() {
		
		while(true) {
			if(!cycle) {
				try {
					if((currIndex = fiss[0].read(bbs[0])) != -1) {
//						System.out.println("filled"); 
						arrIndex = 0;
						for(int i= 1; i<attSize;i++) {
							fiss[i].read(bbs[i]);
						}
						cycle = true;
//						System.out.println(currIndex);
//						return getRow(arrIndex);
					}else {
//						System.out.println("empty");
						return null;
					}
				} catch (IOException e) {
					System.out.println("empty buffer");
					e.printStackTrace();
				}
			}else {
				if(arrIndex != currIndex) {
					return getRow();
				}else {
//					System.out.println(currIndex);
					cycle = false;
				}
			}
		}
	}
		
		
		
	private int[] getRow() {
		int [] temp = new int[attSize];
		for(int i= 0; i<attSize;i++) {
			temp[i] = bbs[i][arrIndex+3] & 0xFF | (bbs[i][arrIndex+2] & 0xFF) << 8 | (bbs[i][arrIndex+1] & 0xFF) << 16 | (bbs[i][arrIndex] & 0xFF) << 24;
		}
		arrIndex +=4;
//		System.out.println(Arrays.toString(temp));
		return temp;
	}
	



	@Override
	public boolean hasNext() {
		return true;
	}
	
	@Override
	public int getAtt() {
		return att;
	}
	
	
	@Override
	public String getName() {
		return rName;
	}
	
	@Override
	public boolean isType(String type) {
		if(type.equals("SCAN")) {
			return true;
		}
		return false;
	}
	@Override
	public void refresh(){
	}



	@Override
	public String selfString() {
		String result = "Scan:"+rName;
		return result;

	}
}	
