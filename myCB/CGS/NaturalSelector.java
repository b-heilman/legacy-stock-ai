package myCB.CGS;

import myCB.ai.AIlogicalSerial;
import myCB.ai.AIvalidator;
import myCB.CGS.Genepool;
import myCB.CGS.Western;

public class NaturalSelector {
	private AIvalidator Valid;
	private CGSData Data;
	private AIlogicalSerial Table;
	
	public NaturalSelector(AIlogicalSerial lt, AIvalidator val, CGSData data){
		Table = lt;
		Valid = val;
		Data = data;
		}
	//---------
	// Other functional controls
	public Double test(Genepool Pool, Boolean how){
		return test(Pool, how, 0);
		}
	
	public Double test(Genepool Pool, Boolean how, int back){
		Double val = 0.0;
		Western exp = new Western(Table.subSection(back), Valid, Data);
		
		for (Pool.start(); Pool.hasNext();){
			Genecode gc = Pool.next();
			exp.test(gc, how);
			val += exp.getValue();
			}
		
		val /= Pool.size();
		
		Pool.setValue(val);
		return val;
		}
	
	public Double run(Genepool Pool, Boolean how){
		return run(Pool, how, 0);
		}
	
	public Double run(Genepool Pool, Boolean how, int back){
		Western Express = new Western(Table.subSection(back), Valid, Data);
		Double value = 0.0;
		
		for (Pool.start(); Pool.hasNext();){
			Express.eval(Pool.next(),how);
			value += Express.getValue();
			}
		
		return value/Pool.size();
		}
	}
