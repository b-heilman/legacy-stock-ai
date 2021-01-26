/**
 * 
 */
package myCB.CGS;

import java.util.Date;
import java.util.Random;

import myCB.ai.AIgenerator;

/**
 * One of the creator classes which creates Genecodes of chains
 * Uses ribosomes to create the chains
 */
public class Nucleus {

	private static final long serialVersionUID = 1;
	private static Random rand = new Random((new Date()).getTime()); 
	private CGSData Data;
	private Ribosome Ribo;
	
	public Nucleus(AIgenerator gen, CGSData data){
		Data = data;
		Ribo = new Ribosome(gen);
		}
	
	public Genecode generate(int i){
		Genecode gen = new Genecode();
		int val = Data.maxChainSize() - Data.minChainSize();
		
		for (int j = 0; j < i; j++){
			gen.add(Ribo.generate(rand.nextInt(val)+Data.minChainSize()));
			}
			
		return gen;
		}
	
	public Genecode fill(Genecode in){
		Genecode gen = new Genecode(in);
		
		int val = Data.maxChainSize() - Data.minChainSize();
		
		for (; 
			gen.size() < Data.chainCount(); 
			gen.add(Ribo.generate(rand.nextInt(val)+Data.minChainSize()))){
			}
		
		return gen;
		}
	//****************
	// Duel genecode operatators
	/*------------
	 * Combines two genecodes in a way that their best aspects are passed
	 * onto the child.  The best chains are breed with the the other best chains.
	 * This will merge chains which have the same values compared to the rest of
	 * the other chains in the genecodes.
	 */
	public Genecode breed(Genecode dis, Genecode dat){
		dis.sort();
		dat.sort();
		
		Genecode result = new Genecode();
		
		int size = dis.size() < dat.size()?dis.size():dat.size();
		
		dis.start();
		dat.start();
		
		while (result.size() < size){
			result.add(Ribo.breed(dis.next(), dat.next()));
			}
		
		return result;
		}
	/* ------------
	 * Combines two Genecodes to create a genecode which is the result of
	 * random pairing of the two genecodes' chains.  Allows for the posibility 
	 * of mutation.
	 */
	public Genecode mate(Genecode dis, Genecode dat){
		dis.sort();
		dat.sort();
		
		Genecode result = new Genecode();
		
		int size = dis.size() < dat.size()?dis.size():dat.size();
		
		dis.start();
		dat.start();
		
		while (result.size() < size){
			if (rand.nextBoolean()){
				result.add(Ribo.breed(dis.remove(rand.nextInt(dis.size())), 
															dat.remove(rand.nextInt(dat.size()))));
				}
			else{
				if (rand.nextBoolean())
					result.add(Ribo.mutate(dis.remove(rand.nextInt(dis.size()))));
				else
					result.add(Ribo.mutate(dat.remove(rand.nextInt(dat.size()))));
				}
			}
		
		return result;
		}
	/* ------------
	 * Combines two genecodes to create a genecode which is the size
	 * of the smallest genecode passed in.  Each chain will come
	 * from the similar place in the parent's code.  chain one
	 * will only come from either parent's chain one.  The chains are
	 * not touched or effected.
	 */
	public Genecode merge(Genecode dis, Genecode dat){
		dis.sort();
		dat.sort();
		
		Genecode i = new Genecode(dis);
		Genecode a = new Genecode(dat);
		Genecode result = new Genecode();
		
		int size = dis.size() < dat.size()?dis.size():dat.size();
		
		while (result.size() < size){
			result.add(i.remove_front());
			result.add(a.remove_front());
			}
		
		return result;
		}
	//****************
	// Single genecode operatators
	/* ------------
	 * Takes in a chain and returns a version of it which has been
	 * mutated in one way or another.
	 */ 
	public Genecode mutate(Genecode dis){
		Genecode result = new Genecode();
		
		for(dis.start(); dis.hasNext(); result.add(Ribo.mutate(dis.next()))){}
		
		return result;
		}
	/* ------------
	 * Takes in a chain and returns a version of it which has been
	 * mutated in one way or another.
	 */
	public Genecode variant(Genecode dis){
		Genecode result = new Genecode();
		
		for(dis.start(); dis.hasNext(); result.add(Ribo.variant(dis.next()))){}
		
		return result;
		}
	/* ------------
	 * Remove all chains from a genecode, which are below a certain value
	 */
	public Genecode prune(Genecode in){
		Genecode gen = new Genecode();
		
		Chain ch;
		in.start();
		while(in.hasNext())
			{
			ch = in.next();
			if (ch.getValue() > Data.tolerance() && ch.size() > 0)
				gen.add(ch);
			}
		
		return gen;
		}
	/* ------------
	 * Remove all chains from a genecode, which are below a certain value.
	 * This controls the number returned.
	 */
	public Genecode prune(Genecode in, int count){
		Genecode gen = new Genecode();
		
		Chain ch;
		in.sort();
		in.start();
		while(in.hasNext() && gen.size() < count)
			{
			ch = in.next();
			if (ch.getValue() > Data.tolerance() && ch.size() > 0)
				gen.add(ch);
			}
		
		return gen;
		}
	
	/* ------------
	 * this reconstitues the entire genecode.  Combines all chains, then breaks them up again
	 * randomly.
	 */
	public Genecode rebreed(Genecode dis){
		Chain hold, it = unify(dis);
		Genecode result = new Genecode();
		
		while (it.size() > 0){
			hold = new Chain();
			int size = rand.nextInt(Data.maxChainSize() - Data.minChainSize()) + Data.minChainSize();
			for (int i = 0; i < size && it.size() > 0; i++){
				hold.add(it.remove(rand.nextInt(it.size())));
				}
			result.add(hold);
			}
		
		return result;
		}
	
	public Genecode asexual(Genecode dis){
		dis.sort();
		
		int size = dis.size();
		for (int i = 0; i < size; i++){
			for (int j = 0; j < size; j++){
				dis.add(Ribo.breed(dis.get(i),dis.get(j)));
				dis.add(Ribo.merge(dis.get(i),dis.get(j)));
				dis.add(Ribo.mutate(dis.get(i)));
				}
			}
		
		return dis;
		}
	/* ------------
	 * PRIVATE CLASS which takes an entire Genecode, breaks it into its
	 * individual molecules, and then recombine them into a new Chain
	 */ 
	private Chain unify(Genecode dis){
		Chain it = new Chain();
		
		for (dis.start(); dis.hasNext();){
			it.add(dis.next());
			}
		
		it.sort();
		
		return it;
		}
	}
