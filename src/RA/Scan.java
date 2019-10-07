package RA;
import java.util.Iterator;

import src.Catalog;
import src.main;;
/**
 * iterable Class for scanning the relation row by row, inheritated from RANode
 * @author dennis
 * @param 
 *
 */
public class Scan<T>{
	private Catalog cat;
	private String rName;
	
	public Scan(Catalog cat) {
		this.cat = cat;
		this.rName = cat.getName();
	}
	
	
	public String getName() {
		return rName;
	}
	
	

	public ScanIterator iterator() {
		return new ScanIterator(cat);
	}

	public int getRows() {
		return cat.getRow();
	}
	
	public int getAtts() {
		return cat.getAtt();
	}
	
	public String toString() {
		String result = "Scan:"+rName;
		return result;
	}
}

