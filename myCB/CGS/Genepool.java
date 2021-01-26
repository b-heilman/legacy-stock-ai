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
public class Genepool extends Serial<Genecode>{

	private static final long serialVersionUID = 1L;
	
	public Genepool(){
		super();
		}
	
	public Genepool(Genecode in){
		super(in);
		}
	
	public Genepool(Genepool in){
		super(in);
		}
	
	public Genepool(LinkedList<Genecode> in){
		super(in);
		}
	
	public void sort(){
		data = new LinkedList<Genecode>(new HashSet<Genecode>(data));
			
		Collections.sort(this.data);
		}
	
	public int compareTo(Genecode that) {
		return (int)((that.getValue() - this.getValue())*10000);
		}
	
	public boolean equals(Object o) {
		Genepool that;
		
		try	{
			that = (Genepool)o;
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
