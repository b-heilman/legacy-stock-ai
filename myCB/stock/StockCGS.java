package myCB.stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import myCB.CGS.*;
import myCB.ai.AIlogicalSerial;
import myCB.ai.StatValidator;


public class StockCGS {
	public static String dir_NAME = "brains";
	private CGSData Data;
	private String 	NAME;
	private Genecode Truthiness;
	private Genecode Falsiness;
	private Western Tester;
	private MadScientist Master;
	private AIlogicalSerial Table;
	private int AHEAD;
	
	public StockCGS(String stock, int inter, int ahead, Double ass, Double tol){
		Data = new CGSData(2,16,10,inter,ahead,tol,ass);
		NAME = stock;
		AHEAD = ahead;
		Table = getTable();
		
		Master = new MadScientist(Table, Data, NAME);
		// if an instance doesn't already exist, make one
		load();
		}
	
	public Double workingValue(){
		return getTable().getHeadWeight();
		}
	
	public void upgrade(){
		upgrade(0);
		}
	
	public void reseed(int back){
		Master = new MadScientist(Table.subSection(back), Data, NAME);
		Master.createSeed(back);
		}
	
	public void upgrade(int back){
		// maybe check to see if it was actually an upgrade
		Master = new MadScientist(Table.subSection(back), Data, NAME);
		
		Truthiness = Master.evolve(Truthiness,true);
		Falsiness = Master.evolve(Falsiness, false);
		}
	
	public void rebirth(int back){
		Master = new MadScientist(Table.subSection(back), Data, NAME);
		
		Master.createSeed(back);
		
		Truthiness.add(Master.growFromSeed(true));
		Falsiness.add(Master.growFromSeed(false));
		
		Truthiness = Master.evolve(Truthiness,true);
		Falsiness = Master.evolve(Falsiness, false);
		}
	
	public String printChange(int i){
		return getTable().getWeight(i-AHEAD)+"<---"+getTable().getWeight(i);
		}
	
	public Double change(int i){
		// that's the past i+head is past, i-head is negative
		// return getTable().getWeight(i)/getTable().getWeight(i+1);
		return getTable().getWeight(i-AHEAD)/getTable().getWeight(i);
		}
	
	public Double check(){
		return check(0);
		}
	public Double check(int at){
		return goingUp(at) - goingDown(at);
		}
	
	public Double goingUp(int at){
		Tester = new Western(Table.subSection(at), new StatValidator(Table.getOps()), Data);
		Tester.eval(Truthiness,true);
		
		return Tester.getValue();
		}
	
	public Double goingDown(int at){
		Tester = new Western(Table.subSection(at), new StatValidator(Table.getOps()), Data);
		Tester.eval(Falsiness,false);
		
		return Tester.getValue();
		}
	
	public Double odds(){
		Tester = new Western(Table, new StatValidator(Table.getOps()), Data);
		Tester.test(Truthiness, true);
		Tester.test(Falsiness, false);
		
		return (Truthiness.getValue() + Falsiness.getValue())/2;
		}
	
	public Double oddsUp(int i){
		Tester = new Western(Table.subSection(i), new StatValidator(Table.getOps()), Data);
		Tester.test(Truthiness, true);
		
		return Tester.getValue();
		}
	
	public Double oddsDown(int i){
		Tester = new Western(Table.subSection(i), new StatValidator(Table.getOps()), Data);
		Tester.test(Falsiness, false);
		
		return Tester.getValue();
		}
	
	public Date currentDate(){
		StockValues data = StockValues.load(NAME);
		
		return data.newestDate();
		}
	
	private void load(){
		if ( !Master.seedExists(true) || !Master.seedExists(true) ){
			Master.createSeed(0);
			}
		// todo: have a way to upgrade the seed
		if (!loadFile(true)){
			Truthiness = Master.growFromSeed(true);
			}
		
		if (!loadFile(false)){
			Falsiness = Master.growFromSeed(false);
			}
		}
	
	private Boolean loadFile(Boolean how){
		try
			{
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(getName(how)));
		
			if (how)
				Truthiness = (Genecode)(os.readObject());
			else
				Falsiness = (Genecode)(os.readObject());
			
			os.close();
			}
		catch (Exception ex)
			{
			return false;
			}
		return true;
		}
	
	public Boolean save(){
		try 
			{
			File dir = new File(dir_NAME);
			if (!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(getName(true)));
			
			os.writeObject(Truthiness);
			os.close();
			
			os = new ObjectOutputStream(new FileOutputStream(getName(false)));
			
			os.writeObject(Falsiness);
			os.close();
			}
		catch (Exception ex)
			{
			System.out.println("can't save: "+ex.getMessage());
			ex.printStackTrace();
			return false;
			}
		return true;
		}
	
	private String getName(Boolean how){
		return (dir_NAME+File.separator+how+NAME+"s"+Data.span()+
						"t"+((int)(Data.tolerance()*1000))+
						"c"+((int)(Data.confidence()*1000))+
						"l"+Data.ahead()+".cgs");
		}
	
	private AIlogicalSerial getTable(){
		return StockValues.load(NAME);
		}
	}
