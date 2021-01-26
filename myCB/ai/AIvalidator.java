/**
 * Validates a CG
 */
package myCB.ai;

import myCB.CGS.Molecule;
import myCB.data.Serial;

public interface AIvalidator {

	public boolean validate(AIvalidable v);
	public void logicTable(AIlogical lt);
	public void setValues(Serial<Molecule> in, Double[] vals);
	}
