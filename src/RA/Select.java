package RA;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import src.Catalog;
import src.main;
import src.Operation;

public class Select<T> extends RANode{
	private int column;
	private int cColumn;
	private String compare;
	private int value;
	private String rName;
	private MapInfo map;
	
	int [] row;
	
	
	public Select(Operation opt) {
		super();
		this.column = opt.getLeftC();
		this.compare = opt.getCompare();
		this.value = opt.getValue();
		this.rName = opt.getLeftR();
	}
	
	public MapInfo setUp(MapInfo map) {
		map.add(rName, column);
		this.map = leftChild.setUp(map);
		cColumn = map.getR(rName, column);
//		System.out.println(cColumn);
		return map;
	}
	
	
	/**
	 * method get a row from scan and find if the row should to return
	 * @param row
	 * @return if match condition
	 */
	private boolean compare(int [] row) {
		
		if(compare.equals("=")) {
			if(row[cColumn]==value) {
				return true;
			}
		}else if(compare.equalsIgnoreCase(">")) {
			if(row[cColumn]>value) {
				return true;
			}
		
		}else if(compare.equals("<")) {
			if(row[cColumn]<value) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getName() {
		return this.rName;
	}
	
	
	@Override
	public boolean isType(String type) {
		if(type.equals("SELECT")) {
			return true;
		}
		return false;
	}

	// leftChild of a Select can only be a scan, if scan is done, select is done;
	@Override
	public boolean hasNext() {
		if(leftChild.hasNext()) {
			return true;
		}
		return false;
	}
	
	@Override
	public int [] next() {
		while((row=leftChild.next())!=null) {// keep finding rows that pass pradicate
//			row = leftChild.next();
			if(compare(row)) {
				return row;
			}
		}
		return null;
	}
	


	@Override
	public int getAtt() {
		return leftChild.getAtt();
	}

// optimization point, try to store select result in memory;
	@Override
	public void refresh() {
//		System.out.println("refresh"+rName);
		leftChild.refresh();
		
	}


	@Override
	public String selfString() {
		String result = "Select:"+rName+column;
		return result;
	}
	
}
