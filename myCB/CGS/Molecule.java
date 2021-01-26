/**
 * 
 */
package myCB.CGS;

import java.io.Serializable;

import myCB.ai.AIgenerator;
import myCB.ai.AIvalidable;

/**
 * @author Bro
 *
 */
public class Molecule implements Comparable<Molecule>, AIvalidable, Serializable {
	private static final long serialVersionUID = 1;
	
	private AIgenerator Creator;
	private Double value;
	private int Row;
	private int Left;
	private int Op;
	private int Right;
	//----the constructors----
	public Molecule(AIgenerator maker, int ro, int l, int o, int r){
		Creator = maker;
		Row = ro;
		Left = l;
		Op = o;
		Right = r;
		value = 0.0;
		}
	
//----the accessors----
	public void setValue(Double v){
		value = v;
		}
	
	public Double getValue(){
		return value;
		}
	
	public int getRow(){
		return Row;
		}
	
	public int getLeft(){
		return Left;
		}
	
	public int getRight(){
		return Right;
		}
	
	public int getOp(){
		return Op;
		}
	//----the implemented----
	public int compareTo(Molecule that) {
		return Creator.compare(this, that);
		}
	
	public boolean equals(Object o) {
		Molecule that;
		try {
			that = (Molecule)o;
			}
		catch (ClassCastException ex){
			return false;
			}
		return Creator.equal(this, that);
		}
	
	public int hashCode(){
		int temp = Row;
		temp *= 100;
		temp += Op;
		temp *= 1000;
		temp += Left;
		temp *= 1000;
		temp += Right;
		return temp;
		}
	
	public String toString(){
		return "{"+Row+":"+Left+"-"+Op+"-"+Right+"=>"+value+"}";
		}
}
