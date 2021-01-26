/**
 * 
 */
package myCB.CGS;

import myCB.ai.AIgenerator;
import myCB.CGS.Chain;

/**
 * One of the creator classes which creates chains of molecules
 */
public class Ribosome {
	AIgenerator gen;
	public Ribosome(AIgenerator g){
		gen = g;
		}
	
	public Chain generate(int size){
		Chain result = new Chain();
		
		for (int i = 0; i < size-1; i++)
			result.add(gen.generate(i));
		
		do {
			result.add(gen.generate(size-1));
			result.sort();
			}while(result.size() < size);
		return result;
		}
	
	public Chain mutate(Chain ch){
		Chain result = new Chain();
		
		for(ch.start(); ch.hasNext();){
			result.add(gen.mutate(ch.next()));
			}
		
		return result;
		}
	
	public Genecode variant(Chain ch){
		Genecode result = new Genecode();
		
		for(int i = 0; i < ch.size(); i++){
			Chain c = new Chain();
			Chain o = new Chain();
			for (int j = 0; j < ch.size(); j++){
				if (j == i){
					c.add(ch.get(j));
					}
				else{
					c.add(gen.generate(j));
					o.add(gen.mutate(ch.get(j)));
					}
				}
			result.add(c);
			result.add(o);
			}
		
		return result;
		}
	
	public Chain merge(Chain dis, Chain dat){
		dis.sort();
		dat.sort();
		
		Chain i = new Chain(dis);
		Chain a = new Chain(dat);
		Chain result = new Chain();
		
		int size = dis.size() < dat.size()?dis.size():dat.size();
		
		while (result.size() < size){
			result.add(i.remove_front());
			result.add(a.remove_front());
			}
		
		return result;
		}
	
	public Chain breed(Chain dis, Chain dat){
		dis.sort();
		dat.sort();
		
		Chain i = new Chain(dis);
		Chain a = new Chain(dat);
		Chain result = new Chain();
		
		int size = i.size() < a.size()?i.size():a.size();
		
		i.start();
		a.start();
		
		while (result.size() < size){
			result.add(gen.breed(i.next(), a.next()));
			}
		
		return result;
		}
}
