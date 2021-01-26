package myCB.CGS;

import java.util.LinkedList;

import myCB.ai.AIlogicalSerial;
import myCB.ai.AIvalidator;
import myCB.data.Serial;
import myCB.CGS.Chain;

public class Expresser extends Thread{
	private Western Parent;
	private AIvalidator Valid;
	private AIlogicalSerial logicTable;
	private CGSData Data;
	private Chain ch;
	private Double Value;
	private boolean how;
	private int Mode;
	
	public Expresser(AIlogicalSerial lt, AIvalidator val, CGSData data){
		logicTable = lt;
		Data = data;

		if (Data.span() > logicTable.length())
			logicTable.past();
		else
			logicTable.past(Data.span());
		
		Valid = val;
		Valid.logicTable(logicTable);
		Parent = null;
		}
	
	public void setParent(Western par){
		Parent = par;
		}
	
	public void prime(int mode, Chain c, boolean h){
		ch = c;
		how = h;
		Mode = mode;
		}
	
	public void run(){
		if (Mode == 0){
			Value = test(ch, how);
			}
		else{
			Value = eval(ch,how);
			}
		
		Parent.reportTo(Value);
		}
	
	public Double getValue(){
		return Value;
		}
	
	public Double eval(Chain ch, boolean how){
		LinkedList<Boolean> ll;
		Double count = 0.0;
		
		logicTable.past(1); // places it on today
		Valid.logicTable(logicTable);
		
		ll = evaluate(ch);
		
		for (int i = 0; i < ch.size(); i++)
			{
			if (ll.get(i) == how)
				count++;
			}
		
		return  (count / ch.size());
		}
	
	public Double test(Chain ch, boolean how)
		{
		LinkedList<Boolean> ll;
		Double[] results = new Double[ch.size()];
		int count;
		Double master = 0.0, sum = 0.0;
		
		if (Data.span() > logicTable.length())
			logicTable.past();
		else
			logicTable.past(Data.span());
		// prime the results list
		for (int i = 0; i < results.length; i++) { results[i] = 0.0;}
		// check through all of the tables
		for(firstTable(); moreTables(); nextTable()){
			// express the chain and get back the results
			ll = evaluate(ch);
			count = 0;
	
			for (int i = 0; i < ch.size(); i++)
				{
				if (ll.get(i) == how)
					{
					count++;
					// if it matches what you're looking for
					if (how)
						{
						// if it's true, see if it's going up
						if (logicTable.getChange(Data.ahead()) > 0)
							results[i]++;
						}
					else
						{
						// if it's false, see if it's going down
						if (logicTable.getChange(Data.ahead()) < 0)
							results[i]++;
						}
					}
				else
					{
					// If it doesn't match what you're looking for
					if (how)
						{
						// if it's true, see if it's going up
						if (logicTable.getChange(Data.ahead()) < 0)
							results[i]++;
						}
					else
						{
						// if it's false, see if it's going down
						if (logicTable.getChange(Data.ahead()) > 0)
							results[i]++;
						}
					}
				}
			// the assurance level, check if it's right
			float val = count/(float)ch.size();
		
			// sum is the number of times that this element is within the confidence level and 
			// should count.  master is the correctness level, how many times it's right.
			Double then = logicTable.peekAhead(Data.ahead());
			Double now = logicTable.getWeight();
			
			if (how)
				{
				// if it's true, see if it's going up
				if (val > Data.confidence())
					{
					// it should be positive, so test it
					if (then > now){
						Double change = then/now;
						
						sum += change;
						master += change;
						}
					else{
						sum++;
						}
					}
				else if (val < 1-Data.confidence())
					{
					sum += .5;
					// it should be negative, so test it
					if (logicTable.getChange(Data.ahead()) < 0)
						master += .5;
					}
				}
			else
				{
				// if it's true, see if it's going up
				if (val > Data.confidence())
					{
					if (then < now){
						Double change = then/now;
						
						sum += change;
						master += change;
						}
					else{
						sum++;
						}
					}
				else if (val < 1-Data.confidence())
					{
					sum += .5;
					// it should be negative, so test it
					if (logicTable.getChange(Data.ahead()) > 0)
						master += .5;
					}
				}
			}
		// assign the values to the Molecules of how many times they were right
		Valid.setValues((Serial<Molecule>)ch,results);
		
		// if the total number of days checked was less then .2 of the days
		// check it against that 20%.  Makes sure that the elements that come 
		// up often get preference
		Double checkAgainst = .2 * Data.span();
		if (sum == 0)
			{
			ch.setValue(0.0);
			return 0.0;
			}
		else if (sum < checkAgainst)
			{
			ch.setValue(master / checkAgainst);
			return master / checkAgainst;
			}
		// return the value of the chain
		else
			{
			ch.setValue(master / sum);
			return master / sum;
			}
		}
	
	private LinkedList<Boolean> evaluate(Chain ch){
		LinkedList<Boolean> ll = new LinkedList<Boolean>();
		
		ch.start();
		
		while(ch.hasNext())
			ll.add(Valid.validate(ch.next()));
		
		return ll;
		}
	
	private void firstTable(){
		try{
			logicTable.past(Data.span());
			Valid.logicTable(logicTable);
			}
		catch (IndexOutOfBoundsException ex){
			logicTable.past();
			Valid.logicTable(logicTable);
			}
		}
	
	private void nextTable(){
		logicTable.toPresent();
		Valid.logicTable(logicTable);
		}
	
	private boolean moreTables(){
		return logicTable.hasFuture();
		}
	}
