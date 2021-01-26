/**
 * 
 */
package myCB.ai;

import myCB.CGS.Molecule;
import myCB.data.Serial;

/**
 * @author Bro
 *
 */
public class StatValidator implements AIvalidator{
	AIlogical LogicTable;
	AIoptions[] Ops;
	
	public StatValidator(AIoptions[] o){
		Ops = o;
		}
	
	public boolean validate(AIvalidable v){
		double left = LogicTable.getValue(v.getRow(),v.getLeft());
		double right = LogicTable.getValue(v.getRow(),v.getRight());
		
		return Ops[v.getRow()].eval(left,v.getOp(),right);
		}
	
	public void logicTable(AIlogical lt){
		LogicTable = lt;
		}
	
	public void setValues(Serial<Molecule> in, Double[] vals){
		// maybe check the size to see if you need to throw an exception
		for (in.start(); in.hasNext(); in.next().setValue(vals[in.at()])){}
		}
	}
