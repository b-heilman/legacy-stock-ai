/**
 * 
 */
package myCB.CGS;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import myCB.data.Serial;

/**
 * @author Bro
 *
 */
public class Genecode extends Serial<Chain>{

	private static final long serialVersionUID = 1L;
	
	public Genecode(){
		super();
		}
	
	public Genecode(Chain in){
		super(in);
		}
	
	public Genecode(Genecode in){
		super(in);
		}
	
	public Genecode(LinkedList<Chain> in){
		super(in);
		}
	
	public void sort(){
		data = new LinkedList<Chain>(new HashSet<Chain>(data));
		
		Collections.sort(data);
		}
	
	public int compareTo(Genecode that) {
		return (int)((that.getValue() - this.getValue())*10000);
		}
	
	public boolean equals(Object o) {
		Genecode that;
		
		try	{
			that = (Genecode)o;
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
