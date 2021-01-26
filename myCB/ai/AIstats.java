/**
 * 
 */
package myCB.ai;

import java.io.Serializable;

public class AIstats implements Serializable{
	private static final long serialVersionUID = 1;
	private int[] Ops, Vals;
	private int Types;
	public AIstats(Double[][] logicTable, AIoptions[] options){
		Ops = new int[options.length];
		for (int i=0; i<options.length; i++){
			Ops[i] = options[i].numOfOps();
			}
		Vals = new int[logicTable.length];
		for (int i=0; i<logicTable.length; i++){
			Vals[i] = logicTable[i].length;
			}
		Types = logicTable.length;
		}
	// count the number of ops for that type
	public int countOps(int x){
		return Ops[x];
		}
	// count the number of values for that type
	public int countVal(int x){
		return Vals[x];
		}
	// count the number of types available
	public int theTypes(){
		return Types;
		}
	}
