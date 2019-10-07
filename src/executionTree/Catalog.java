/**
 * catalog class to contain information for each relation
 * @author dennis
 *
 */
package src;
public class Catalog {
	private String fName;
	private int attribute;
	private int row;
	
	public Catalog(String fName) {
		this.fName = fName;
	}
	
	
	public void setAtt(int attribute) {
		this.attribute = attribute;
	}
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public String getName() {
		return this.fName;
	}
	
	public int getAtt() {
		return this.attribute;
	}
	
	public int getRow() {
		return this.row;
	}
}
