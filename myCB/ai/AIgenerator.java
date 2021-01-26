package myCB.ai;

import java.io.Serializable;
import java.util.LinkedList;

import myCB.CGS.Molecule;

public interface AIgenerator extends Serializable{
	// an int value is passed in so that the generator can know where in the
	// chain the molecule is being created for
	public Molecule generate(int type);
	public LinkedList<Molecule> fullSeries();
	public LinkedList<Molecule> medianCut(int tier);
	public int getTiers();
	public LinkedList<Molecule> relations(Molecule in);
	public Molecule mutate(Molecule in);
	public Molecule breed(Molecule dis, Molecule dat);
	// used by molecules created to link back
	public int compare(Molecule dis, Molecule dat);
	public Boolean equal(Molecule dis, Molecule dat);
}
