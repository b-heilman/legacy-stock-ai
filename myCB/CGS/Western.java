/**
 * 
 */
package myCB.CGS;

import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;

import myCB.ai.AIlogicalSerial;
import myCB.ai.AIvalidator;
import myCB.CGS.Genecode;

/**
 * @author Bro
 *
 */
public class Western extends Thread{
	private AIlogicalSerial Logic;
	private AIvalidator Valid;
	private CGSData Data;
	private Genecode Code;
	private boolean How;
	private int Mode;
	private Double Value;
	private int Count;
	
	public Western(AIlogicalSerial lt, AIvalidator val, CGSData data){
		Logic = lt;
		Valid = val;
		Data = data; 
		}
	
	public Double getValue(){
		return Value;
		}
	
	public void run(){
		if (Mode == 0){
			eval(Code, How);
			}
		else{
			test(Code, How);
			}
		}
	
	public void prime(int mode, Genecode gc, boolean how){
		Mode = mode;
		Code = gc;
		How = how;
		}
	
	private void go(){
		Value = 0d;
		Count = 0;
		
		try {
			for(Code.start(); Code.hasNext();)
				{
				Count++;
				Expresser ex = new Expresser(Logic.clone(), Valid, Data);
				ex.setParent(this);
				ex.prime(Mode, Code.next(), How);
				//ex.start();
				ex.run();
				//while (this.activeCount() > 3){sleep(1);}
				}
		
			while(Count != 0)
				sleep(5);
			} 
		catch (InterruptedException e) {
			e.printStackTrace();
			}
			
		Value /= Code.size();
		}
	
	public void eval(Genecode gc, boolean how){
		Mode = 1;
		Code = gc;
		How = how;
		
		if (gc.size() == 0)
			{
			Value = 0.0;
			}
		else{
			go();
			}
		}
	
	public void test(Genecode gc, boolean how){
		Mode = 0;
		Code = gc;
		How = how;
		
		if (gc.size() == 0)
			{
			Value = 0.0;
			gc.setValue(0.0);
			}
		else{
			go();
			gc.setValue(Value);
			}
		}
	
	public void reportTo(double in){
		Count--;
		Value += in;
		}
	}
