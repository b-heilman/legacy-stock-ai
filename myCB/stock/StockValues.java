package myCB.stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import myCB.ai.*;
import myCB.math.*;
import myCB.math.lines.Line2D;

// this is the AI interface between the stock data and the AI system
public class StockValues implements AIlogicalSerial, Serializable
	{
	private static String dir_root = "svs";
	private static final long serialVersionUID = 8;
	private Date Recent;
	private LinkedList<StockDayValue> days;  // 0 is today
	private int onDay;
	private Double price[];

//copy constuctor
	public static StockValues load(String name){
		StockValues data;
		
		try
			{
			ObjectInputStream os = new ObjectInputStream(
					new FileInputStream(dir_root+File.separator+name.toLowerCase()+".sv"));
	
			data = (StockValues)(os.readObject());
		
			os.close();
			}
		catch (Exception ex)
			{
			if ( save(name, false) ){
				System.out.println("recover load: \n"+dir_root+File.separator+name.toLowerCase()+".sv");
				
				try{
					ObjectInputStream os = new ObjectInputStream(
							new FileInputStream(dir_root+File.separator+name.toLowerCase()+".sv"));
		
					data = (StockValues)(os.readObject());
			
					os.close();
					}
				catch (Exception e){
					e.printStackTrace();
					return null;
					}
				}
			else{
				System.out.println("can not recover from loading stock value");
				return null;
				}
			}
		return data;
		}
	
	public static boolean save(String name){
		return save(name, true);
		}
	
	public static boolean save(String name, boolean upload){
		StockData sd = new StockData(name);
		
		if (upload)
			sd.upload();
		else
			sd.load();
		
		StockValues data = new StockValues(sd);
		
		try 
			{
			File dir = new File(dir_root);
			if (!dir.exists())
				dir.mkdir();
			
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(dir_root+File.separator+name.toLowerCase()+".sv"));
			
			os.writeObject(data);
			os.close();
			return true;
			}
		catch (Exception ex)
			{
			System.out.println("can't save: "+ex.getMessage());
			ex.printStackTrace();
			return false;
			}
		}
	
	public StockValues(StockValues that)
		{
		this.days = that.days;
		this.onDay = 0;
		//Copy the values
		this.price = that.price;
		}
	// copy constuctor
	public StockValues(StockValues that, int days)
		{
		//	Copy the days
		this.days = new LinkedList<StockDayValue>(that.days.subList(days, that.days.size()));	
		this.onDay = 0;
		//Copy the values
		this.price = new Double[that.price.length-days];
		for (int i = days; i < that.price.length; i++)
			{
			this.price[i-days] = that.price[i];
			}
		}
	// standard constructor.  take all of the data and manipulate it to a form which
	// the ai system can work with.  index day->category->type=>value
	public StockValues(StockData in)
		{
		days = new LinkedList<StockDayValue>();
		onDay = 0;
		Recent = in.getNewestDay();
			
		float values[][] = in.values(); // open, high, low, close -> 0 is today
		float volume[][] = in.volume();
		price = new Double[values.length];
		
		LinkedList<Double> val;
		LinkedList<Double> vol;
		LinkedList<Double> vect;
		
		LinkedList<Double> maxs = new LinkedList<Double>();
		LinkedList<Double> mins = new LinkedList<Double>();
		
		Float convert;
		StockDayValue day;
		
		int[] range = {1, 3, 5, 10, 20};
		int[] counts = {5,20,40,90};
		for (int j = 0; j < range.length; j++){
			maxs.add(0d);
			}
		for (int j = 0; j < range.length; j++){
			mins.add(999d);
			}
		// format all of that days data
		for (int i = 0; i < values.length; i++)
			{
			// load all the data into the linked lists
			val = new LinkedList<Double>();
			vol = new LinkedList<Double>();
			vect = new LinkedList<Double>();
			convert = new Float(values[i][3]);
			price[i] = convert.doubleValue();
			for (int j = 0; j < values[i].length; j++){
				convert = new Float(values[i][j]);
				val.add(convert.doubleValue());
				}
			for (int j = 0; j < volume[i].length; j++){
				convert = new Float(volume[i][j]);
				vol.add(convert.doubleValue());
				}
			// create the value vectors
			for (int j = 0; j < range.length; j++){
				int back = (i - range[j] < 0)?i:range[j];
				int vo = 0;
				double v;
				v = price[i] - price[i - back];
				
				for (int k = 0; k <= back; k++)
					vo += volume[i-back][0];
				
				v *= vo/((back > 0)?back:1);
				
				if (maxs.get(j) < v)
					maxs.set(j,v);
				if (mins.get(j) > v)
					mins.set(j,v);
				
				vect.add(v);
				}
			// create the new day, edit it
			day = new StockDayValue();
			day.newCategory(val);
			day.newCategory(vol);
			day.newCategory(vect);
			// add the new day
			days.add(day);
			}
		
		// run through all of the days
		for (int i = 0; i < days.size(); i++){
			for (int k = 0; k < days.get(i).stats().length; k++){
				// normalize that data in row 2 (vector data)... fun
				Double d = (days.get(i).get(2,k) - mins.get(k))/(maxs.get(k) - mins.get(k));
				days.get(i).set(2,k,d);
				// calculate all the values based on equations
				for (int j = 0; j < counts.length; j++){
					days.get(i).appendTo(k,calcEstimates(i, counts[j], range, k));
					days.get(i).appendTo(k,calcExtremes(i,counts[j],k));
					days.get(i).appendTo(k,calcAverage(i,counts[j],k));
					days.get(i).appendTo(k,valueBack(i,counts[j],k));
					}
				}
			// calc the means for the day, values row (0) values (0 - 4)
			days.get(i).appendTo(0,calcMean(days.get(i),0,4));
			days.get(i).newCategory(daysRange(i));
			}
		}
	
	private LinkedList<Double> daysRange(int day){
		LinkedList<Double> val = new LinkedList<Double>();
		
		for (int i = 0; i < 4; i++){
			val.add(bandLength(i, day, .01));
			val.add(bandLength(i, day, .025));
			val.add(bandLength(i, day, .05));
			if (day+1 < days.size()){
				val.add(bandLength(i, day+1, .01));
				val.add(bandLength(i, day+1, .025));
				val.add(bandLength(i, day+1, .05));
				}
			else{
				val.add(0d);
				val.add(0d);
				val.add(0d);
				}
			
			if (day+3 < days.size()){
				val.add(bandLength(i, day+3, .01));
				val.add(bandLength(i, day+3, .025));
				val.add(bandLength(i, day+3, .05));
				}
			else{
				val.add(0d);
				val.add(0d);
				val.add(0d);
				}
			
			if (day+5 < days.size()){
				val.add(bandLength(i, day+5, .01));
				val.add(bandLength(i, day+5, .025));
				val.add(bandLength(i, day+5, .05));
				}
			else{
				val.add(0d);
				val.add(0d);
				val.add(0d);
				}
			}
		// add some constants for comparison
		val.add(0d);
		val.add(1d);
		val.add(2d);
		val.add(3d);
		val.add(5d);
		val.add(10d);
		
		return val;
		}
	
	private double bandLength(int row, int back, double variance){
		double hi = days.get(back).get(0, row) * (1 + variance);
		double low = days.get(back).get(0, row) * (1 - variance);
		
		return bandLength(row, back, hi, low);
		}
	
	private double bandLength(int row, int back, double high, double low){
		int i = back; 
		
		while ( i < days.size() && days.get(i).get(0, row) < high
						&& days.get(i).get(0, row) > low) { i++;}
		
		return i - back;
		}
	//----------- Assisting functions 
	// designed to calculate more data points
	private LinkedList<Double> calcMean(StockDayValue day, int num, int vals)
		{
		LinkedList<Double> list = new LinkedList<Double>();
		
		Permutator p = new Permutator(vals);
		
		while (p.morePermutations())
			{
			double val = (day.get(num,p.getLeft())+day.get(num,p.getRight()))/2;
			list.add(val);
			p.nextPermutation();
			}
		return list;
		}
	
	private LinkedList<Double> calcAverage(int date, int count, int type)
		{
		LinkedList<Double> list = new LinkedList<Double>();
		
		StockDayValue day = days.get(date);
		date++;
		
		for (int j = 0; j < day.getOriginalSize(type); j++)
			{
			list.add(sumValues(type,j,date,date+count)/count);
			}
		
		return list;
		}
	
	private LinkedList<Double> valueBack(int date, int back, int type)
		{
		LinkedList<Double> list = new LinkedList<Double>();
		
		StockDayValue day = days.get(date);
		date++;
		
		for (int j = 0; j < day.getOriginalSize(type); j++)
			{
			if (days.size() > date+back)
				list.add(days.get(date+back).get(type,j));
			else
				list.add(days.get(days.size()-1).get(type,j));
			}
		
		return list;
		}
	
	private LinkedList<Double> calcExtremes(int date, int count, int type){
		LinkedList<Double> list = new LinkedList<Double>();
		
		StockDayValue day = days.get(date);
		date++;
		
		for (int j = 0; j < day.getOriginalSize(type); j++)
			{
			list.add(getHigh(type,j,date,date+count));
			list.add(getLow(type,j,date,date+count));
			}
		
		return list;
		}
	
	private LinkedList<Double> calcEstimates(int date, int count, int[] ahead, int type)
		{
		LinkedList<Double> list = new LinkedList<Double>();
		Line2D ln = new Line2D();
		
		StockDayValue day = days.get(date);
		
		for (int j = 0; j < day.getOriginalSize(type); j++)
			{
			for (int i = 1; i <= count; i++)
				{
				try{
					ln.add(i,days.get(date+i).get(type,j));
					}
				catch (IndexOutOfBoundsException ex)
					{
					break;
					}
				}
			ln.calcBestReq();
			for (int k = 0; k < ahead.length; k++)
				list.add(ln.calcValue(1-ahead[k]));
			}
		
		return list;
		}
	
	private double getHigh(int type, int column, int from /*Day*/, int till/*Day*/)
		{
		double high = 0;
		
		for (int i = from; i < till; i++)
			{
			try {
				if (high < days.get(i).get(type,column))
					high = days.get(i).get(type,column);
				}
			catch (Exception ex){
				return high;
				}
			}
		
		return high;
		}
	
	private double getLow(int type, int column, int from /*Day*/, int till/*Day*/)
		{
		double low = 0;
		
		for (int i = from; i < till; i++)
			{
			try {
				if (low > days.get(i).get(type,column))
					low = days.get(i).get(type,column);
				}
			catch (Exception ex){
				return low;
				}
			}
		
		return low;
		}
	private double sumValues(int type, int column, int from /*Day*/, int till/*Day*/)
		{
		double val = 0;
		
		for (int i = from; i < till; i++)
			{
			try {
				val += days.get(i).get(type,column);
				}
			catch (Exception ex){
				return val;
				}
			}
		
		return val;
		}
//----------- Assisting functions 
	public AIlogicalSerial clone(){
		return new StockValues(this);
		}
	
	public StockValues subSection(int x){
		try{
			return new StockValues(this,x);
			}
		catch (IllegalArgumentException ex){
			return new StockValues(this,days.size()-2);
			}
		}
	
	public void present()
		{
		onDay = 0;
		}
	
	public void past()
		{
		onDay = days.size()-1;
		}
	
	public void past(int i){
		if (i >= days.size())
			onDay = days.size()-1;
		else
			onDay = i;
		}
	
	public Double[][] toPresent() throws IndexOutOfBoundsException
		{
		onDay--;
		return days.get(onDay).getData();
		}
	
	public Double[][] toPast() throws IndexOutOfBoundsException
		{
		onDay++;
		return days.get(onDay).getData();
		}
	
	public Double[][] getLogicTable()
		{
		return days.get(onDay).getData();
		}
	
	public Double getValue(int row, int element){
		return days.get(onDay).getData()[row][element];
		}
	
	public boolean hasFuture()
		{
		return (onDay > 0);
		}
	
	public boolean hasPast()
		{
		return onDay < days.size();
		}
	// returns the stat matrix, the number of elements in each row
	public AIstats stats() 	
		{
		return new AIstats(days.get(0).getData(),getOps());
		}
	
	public AIoptions[] getOps() {
		AIoptions[] hold = {new OpsValues(), new OpsValues(), new OpsValues(), new OpsValues()};
		return hold;
		}
	
	public Double getHeadWeight()
		{
		return price[0];
		}
	
	public Double getWeight() 
		{
		return price[onDay];
		}
	
	public Double getWeight(int i){
		try{
			if (i < 0)
				return price[0];
			else
				return price[i];
			}
		catch (ArrayIndexOutOfBoundsException ex){
			return price[price.length-1];
			}
		}
	
	public Double getChange(int i)
		{
		try
			{
			return peekAhead(i) - getWeight();
			}
		catch (Exception ex)
			{
			return 0.0;
			}
		}
	
	public Double getChange()
		{
		try
			{
			return peekAhead(1) - getWeight();
			}
		catch (Exception ex)
			{
			return 0.0;
			}
		}
	
	public Double peekAhead(int ahead)
		{
		try
			{
			return price[onDay-ahead];
			}
		catch (Exception ex)
			{
			return price[0];
			}
		}
	
	public Double lookBack(int back)
		{
		try
			{
			return price[onDay+back];
			}
		catch (Exception ex)
			{
			return price[0];
			}
		}
	
	public int length()
		{
		return days.size();
		}
	
	public Date newestDate(){
		return Recent;
		}
	//----------- Additional sub classes
	class StockDayValue implements Serializable{
		private static final long serialVersionUID = 1;
		private LinkedList<LinkedList> list;
		private LinkedList<Integer> original;
		public StockDayValue(){
			original = new LinkedList<Integer>();
			list = new LinkedList<LinkedList>();
			}
		//Append linked list data to what is already there
		@SuppressWarnings("unchecked")
		public void appendTo(int at, LinkedList<Double> in){
			((LinkedList)list.get(at)).addAll(in);
			}
		//Insert a linked list into the list
		public int newCategory(LinkedList in){
			original.add(in.size());
			list.add(in);
			return list.size()-1;
			}
		// Get back all of that data as an array
		@SuppressWarnings("unchecked")
		public Double[][] getData(){
			Double[][] hold = new Double[list.size()][];
			
			for(int i = 0; i < list.size(); i++){
				hold[i] = ((LinkedList<Double>)list.get(i)).toArray(new Double[list.get(i).size()]);
				}
			
			return hold;
			}
		public Integer getOriginalSize(int i){
			return original.get(i);
			}
		@SuppressWarnings("unchecked")
		public String toString()
			{
			String hold = new String();
			for(int i = 0; i < list.size(); i++){
				hold += ((LinkedList<Double>)list.get(i)).toString()+'\n';
				}
			return hold;
			}
		public int[] stats()
			{
			int[] temp = new int[list.size()];
			
			for (int i = 0; i < list.size(); i++)
				{
				temp[i] = list.get(i).size();
				}
			return temp;
			}
		@SuppressWarnings("unchecked")
		public Double get(int x, int y)
			{
			return ((LinkedList<Double>)list.get(x)).get(y);
			}
		
		@SuppressWarnings("unchecked")
		public void set(int x, int y, Double d){
			((LinkedList<Double>)list.get(x)).set(y,d);
			}
		}
	}
