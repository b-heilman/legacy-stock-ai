package myCB.CGS;

import java.util.Date;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;
import myCB.ai.*;

public class MadScientist {
	private AIlogicalSerial Table;
	private AIgenerator Generator;
	private AIvalidator Valid;
	private Ribosome Beaker;
	private Seeder Seed;
	private CGSData Data;
	private String Name;
	
	public MadScientist(AIlogicalSerial table, CGSData data, String n){
		Table = table;
		Generator = new StatGenerator(table.stats());
		Valid = new StatValidator(table.getOps());
		Data = data;
		Beaker = new Ribosome(Generator);
		Seed = Seeder.loadSeed(n);
		Name = n;
		}
	
	public Chain fullCut(boolean how){
		ListIterator<Molecule> i = Generator.fullSeries().listIterator();
		Chain c = cut(i, Data.maxChainSize(), how);
		
		return c;
		}
	
	public Chain medianCut(boolean how){
		Chain c = new Chain();
		for (int j = 0; j < Generator.getTiers(); j++){
			ListIterator<Molecule> i = Generator.medianCut(j).listIterator();
			c.add( cut(i, Data.maxChainSize(),how) );
			}
		
		c = cut(c.listIterator(), Data.maxChainSize(),how);
		
		return c;
		}
	
	public Chain primeCut(Chain c, boolean how){
		LinkedList<Molecule> best = new LinkedList<Molecule>();
		
		for (int i = 0; i < c.size(); i++){
			Molecule m = c.get(i);
			best.add(cut(Generator.relations(m).listIterator(),1,how).get(0));
			}
		
		return new Chain(best);
		}
	
	public Chain primeCut(boolean how){
		Chain c = medianCut(how);
		
		return primeCut(c, how);
		}
	
	private Chain cut (ListIterator<Molecule> i, int size, boolean how){
		Western tester = new Western(Table, Valid, Data);
		Genecode testTube = new Genecode();
		Chain ayr = new Chain();
		
		while(i.hasNext()){
			Chain test = new Chain();
			test.add(i.next());
			testTube.add(test);
			}
		
		tester.test(testTube,how);
		
		testTube.sort();
		
		for (int j = 0; j < size; j++)
			ayr.add((testTube.get(j)).remove_front());
		
		return ayr;
		}
	
	public Genecode growFromSeed(boolean how){
		Genecode gc = Seed.getSeed(how);

		if (gc == null){
			createSeed(10);
			gc = Seed.getSeed(how);
			}
		
		gc = evolve(gc, how);
		return gc;
		}
//Create a seed to grow others from
	public void createSeed(int back){
		CGSData d = Data;
		Data = new CGSData(2,8,8,20,3,.8,.8);
		AIlogicalSerial t = Table;
		Table = Table.subSection(back);
		
		Seed.setSeed( genSeed(true), true );
		Seed.setSeed( genSeed(false), false );
		
		Seeder.saveSeed(Seed, Name);
		
		Table = t;
		Data = d;
		}
	
	public boolean seedExists(boolean how){
		return Seed.hasSeed(how);
		}
	
	
	private Genecode genSeed(boolean how){
		Genecode gp = new Genecode();
		
		Chain mc = medianCut(how);
		gp.add(mc);
		
		Chain pc = primeCut(mc, how);
		Seed.setKernel(pc, how);
		gp.add(pc);
		
		gp.add(genesis(pc, 4));

		gp =	evolve(gp, how);

		return gp;
		}
	
	public Genecode evolve(Genecode gp, boolean how){	
		Nucleus creator = new Nucleus(new StatGenerator(Table.stats()), Data);
		Chain aryian = Seed.getKernel(how);
		gp = cleanUp(creator, gp, how);
		
		gp.add(creator.variant(gp));
		gp.add(creator.generate(50));
		gp.add(pedegree(aryian, 40,4));
		gp.add(pedegree(aryian, 10,1));
		gp.add(creator.rebreed(gp));
		
		gp = cleanUp(creator, gp, how);
		
		gp = creator.asexual(gp);
		gp.add(creator.rebreed(gp));
		
		gp = cleanUp(creator, gp, how);
		
		return gp;
		}
	
	private Genecode cleanUp(Nucleus creator, Genecode in, boolean how){
		Genecode gp = new Genecode(in);
		Western tester = new Western(Table, Valid, Data);
		
		gp.clean();
		tester.test(gp,how);
		gp.sort();
		Chain best = gp.get_front();
		gp = creator.prune(gp,Data.chainCount());
		gp.add(best);
		gp.clean();
		
		return gp;
		}
	
	public Genecode pedegree(Chain Ayrian, int howMany, int size){
		Chain hold = new Chain();
		Genecode res = new Genecode();
		
		for(int i = 0; i < howMany; i++){
			hold = new Chain();
			hold.add(engineer(Ayrian, size-1));
			hold.add(Ayrian.get(i%Ayrian.size()));
			}
		
		return res;
		}
	// Creates a random chain of elements from the Ayrian chain
	public Chain engineer(Chain Ayrian, int size){
		Random rand = new Random((new Date()).getTime()); 
		Chain temp = new Chain(Ayrian);
		Chain res = new Chain();
		
		while(res.size() < size && temp.size() > 0)
			res.add(temp.remove(rand.nextInt(temp.size())));
		
		return res;
		}
	// Returns a permution of all posibilities
	public Genecode genesis(Chain Ayrian, int size){
		Genecode res = new Genecode();
		
		int[] places = new int[size];
		int sliding = size-1;
		
		for (int j = 0; j < size; j++)
			places[j] = j;
		
		while(places[0] < Ayrian.size() - size){
			Chain temp = new Chain();
			for (int i = 0; i < places.length; i++)
				temp.add(Ayrian.get(places[i]));
			
			res.add(temp);
			
			places[sliding]++;
			
			if (places[sliding] == Ayrian.size()){
				places[sliding]--;
				int i;
				for (i = sliding-1; i > 0 && (places[i]+1 == places[i+1]); i--){}
				
				int base = ++places[i];
				
				if (places[i]+1 != places[i+1]){
					for (i++; i < places.length; i++)
						places[i] = ++base;
					}
				}
			}
		
		return res;
		}
	}
