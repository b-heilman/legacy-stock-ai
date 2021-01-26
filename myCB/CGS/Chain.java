/**
 * 
 */
package myCB.CGS;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import myCB.CGS.Molecule;
import myCB.data.Serial;

/**
 * @author Bro
 * v4
 */
public class Chain extends Serial<Molecule>{
	private static final long serialVersionUID = 1L;

	public Chain(){
		super();
		}
	
	public Chain(Chain in){
		super(in);
		}
	
	public Chain(LinkedList<Molecule> in){
		super(in);
		}
	
	public void sort(){
		data = new LinkedList<Molecule>(new HashSet<Molecule>(data));
			
		Collections.sort(this.data);
		}
	
	public int compareTo(Chain that) {
		return (int)((that.getValue() - this.getValue())*10000);
		}
	
	public boolean equals(Object o) {
		Chain that;
		
		try	{
			that = (Chain)o;
			}
		catch (ClassCastException ex)
			{
			return false;
			}
		
		that.sort();
		this.sort();
		
		if (that.data.size() != this.data.size())
			return false;
		
		return that.data.equals(this.data);
		}

}
