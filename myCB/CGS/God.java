package myCB.CGS;

import java.util.Date;
import java.util.Random;

import myCB.CGS.Genecode;
import myCB.CGS.Genepool;
import myCB.CGS.Nucleus;
import myCB.ai.AIgenerator;

/**
 * This is the highest level creator.  It creates genepools, which are collections
 * of chains and molecules.
 */
public class God {
	private static Random rand = new Random((new Date()).getTime()); 
	private Nucleus Creator;
	private CGSData Data;

	public God(AIgenerator gen, CGSData data){
		Data = data;
		Creator = new Nucleus(gen, data);
		}
	
	public Genepool generate(int j){
		Genepool gp = new Genepool();
		
		for (int i = 0; i < j; i++)
			gp.add(Creator.generate(Data.chainCount()));
		
		return gp;
		}
	
	public Genepool allOptions(Genepool in)
		{
		Genepool bred = new Genepool();
		
		bred.add(breed(in));
		bred.add(mate(in));
		bred.add(merge(in));
		bred.add(mutate(in));
		
		return bred;
		}
	
	public Genepool breed(Genepool in){
		Genepool bred = new Genepool();
		int size = bred.size();
		
		for (int i = 0; i < size; i++){
			for (int j = i + 1; j < size; j++){
				bred.add(Creator.breed(in.get(i),in.get(j)));
				}
			}
		return bred;
		}
	
	public Genepool mate(Genepool in){
		Genepool bred = new Genepool();
		int size = bred.size();
		
		for (int i = 0; i < size; i++){
			for (int j = i + 1; j < size; j++){
				bred.add(Creator.mate(in.get(i),in.get(j)));
				}
			}
		return bred;
		}
	
	public Genepool merge(Genepool in){
		Genepool bred = new Genepool();
		int size = bred.size();
		
		for (int i = 0; i < size; i++){
			for (int j = i + 1; j < size; j++){
				bred.add(Creator.merge(in.get(i),in.get(j)));
				}
			}
		return bred;
		}
	
	public Genepool mutate(Genepool in){
		Genepool bred = new Genepool();
		int size = bred.size();
		
		for (int i = 0; i < size; i++){
			bred.add(Creator.mutate(in.get(i)));
			}
		return bred;
		}
	
	public Genepool prune(Genepool Pool){
		Genepool gp = new Genepool();
		Genecode gc = new Genecode();
		
		for (Pool.start(); Pool.hasNext();){
			gc = Creator.prune(Pool.next());
			if (gc.size() != 0)
				gp.add(gc);
			}
		
		return gp;
		}
	
	public Genepool rebreed(Genepool dis){
		Genecode hold, it = Creator.rebreed(unify(dis));
		Genepool result = new Genepool();
		
		while (it.size() > 0){
			hold = new Genecode();
			for (int i = 0; i < Data.chainCount() && it.size() > 0; i++){
				hold.add(it.remove(rand.nextInt(it.size())));
				}
			result.add(hold);
			}
		
		return result;
		}
	
	public Genepool rebreed(Genepool dis, float top){
		Genecode  hold, it = unify(dis);
		Genepool result = new Genepool();
		int count = (int)(it.size() * top);
		int codes = (int)(count / (float)Data.chainCount()+.5);

		while (codes > 0)
			{
			hold = new Genecode();
			for (int j = 0; j < Data.chainCount() && count > 0; j++){
				hold.add(it.remove());
				count--;
				}
			result.add(hold);
			codes--;
			}
		
		return result;
		}
	
	public Genepool rebreed(Genepool dis, int codes){
		Genecode  hold, it = unify(dis);
		Genepool result = new Genepool();

		while (codes > 0 && it.size() > 0)
			{
			hold = new Genecode();
			for (int j = 0; j < Data.chainCount() && it.size() > 0; j++){
				hold.add(it.remove());
				}
			result.add(hold);
			codes--;
			}
		return result;
		}
	/* ------------
	 * PRIVATE CLASS which takes an entire Genecode, breaks it into its
	 * individual molecules, and then recombine them into a new Chain
	 */ 
	private Genecode unify(Genepool dis){
		Genecode it = new Genecode();
		
		for (dis.start(); dis.hasNext();){
			it.add(dis.next());
			}
		
		it.sort();
		
		return it;
		}
	}
