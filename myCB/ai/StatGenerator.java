package myCB.ai;

import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

import myCB.CGS.Molecule;

/**
 * This creates a Molecule which is compliant with AIstats type
 *
 */
public class StatGenerator implements AIgenerator{
	private static final long serialVersionUID = 1L;
	private static Random rand = new Random((new Date()).getTime()); 
	private AIstats stats;
	
	public StatGenerator(AIstats s){
		stats = s;
		}
	/*--------
	 *  (non-Javadoc)
	 * @see myCB.ai.AIgenerator#generate(int)
	 * Returns a Molecule; this passed back allows for the molecule to use the
	 * compare and equal aspecs of this function.  That allows the molecules to have
	 * access to things they may not know... only the AIGenerator needs to change
	 * to allow for different system usage.
	 */
	
	public Molecule generate(int type) {
		int row = rand.nextInt(stats.theTypes());
		int numOfChoices = stats.countVal(row);
		int numOfOps = stats.countOps(row);
		return new Molecule(this,row,
							rand.nextInt(numOfChoices),
							rand.nextInt(numOfOps),
							rand.nextInt(numOfChoices));
		}
	// create every possible option, mainly for the CGS.MadScientist
	public LinkedList<Molecule> fullSeries(){
		LinkedList<Molecule> list = new LinkedList<Molecule>();
		
		int types = stats.theTypes();
		for (int i = 0; i < types; i++){ // row
			int numOfChoices = stats.countVal(i); // j,k
			int numOfOps = stats.countOps(i); // l
			for (int j = 0; j < numOfChoices; j++)
				for (int k = 0; k < numOfChoices; k++)
					for (int l = 0; l < numOfOps; l+=1){ // I know, but memory issues...
						list.add(new Molecule(this,i,j,l,k));
						}
			}
		
		return list;
		}
	
	public int getTiers(){
		return stats.theTypes();
		}
	
	public LinkedList<Molecule> medianCut(int tier){
		LinkedList<Molecule> list = new LinkedList<Molecule>();
		int numOfChoices = stats.countVal(tier); // j,k
		int numOfOps = stats.countOps(tier); // l
		
		for (int j = 0; j < numOfChoices; j++)
			for (int k = 0; k < numOfChoices; k++){
				int prime = numOfOps;
				list.add(new Molecule(this,tier,j,0,k));
				list.add(new Molecule(this,tier,j,(int)(Math.floor(prime*.33)),k));
				list.add(new Molecule(this,tier,j,(int)(Math.floor(prime*.66)),k));
				}
		return list;
		}
	
	public LinkedList<Molecule> relations(Molecule in){
		LinkedList<Molecule> list = new LinkedList<Molecule>();
		int numOfOps = stats.countOps(in.getRow());
		
		for (int i = 0; i < numOfOps; i++)
			list.add(new Molecule(this,in.getRow(),in.getLeft(),i,in.getRight()));
		
		return list;
		}
	
	public Molecule mutate(Molecule in) {
		int row = in.getRow();
		int numOfChoices = stats.countVal(row);
		int numOfOps = stats.countOps(row);
		
		return new Molecule(this,in.getRow(),
						rand.nextBoolean()?in.getLeft():rand.nextInt(numOfChoices),
						rand.nextBoolean()?in.getOp():rand.nextInt(numOfOps),
						rand.nextBoolean()?in.getRight():rand.nextInt(numOfChoices));
		}

	public Molecule breed(Molecule dis, Molecule dat) {
		if (dis.getRow() != dat.getRow())
			{
			if (rand.nextBoolean())
				return dis;
			else
				return dat;
			}
		else{
			switch (rand.nextInt(3)){
				case 0:
					return dis;
				case 1:
					return dat;
				default:
					return new Molecule(this,dis.getRow(),
									rand.nextBoolean()?dis.getLeft():dat.getLeft(),
									rand.nextBoolean()?dis.getOp():dat.getOp(),
									rand.nextBoolean()?dis.getRight():dat.getRight());
				}
			}
		}
	
	public int compare(Molecule dis, Molecule dat) {
		return (int)((dis.getValue() - dat.getValue()) * 10000);
		}

	public Boolean equal(Molecule dis, Molecule dat) {
		return (dis.getLeft() == dat.getLeft() &&	
						dis.getOp() >= dat.getOp() &&
						dis.getRow() == dat.getRow() && 
						dis.getRight() == dat.getRight());
		}
}
