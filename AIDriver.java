import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import myCB.stock.*;
import mysql.mysqlHandler;
import mysql.mysqlToken;
 
public class AIDriver {
	Portfolio p;
	private static final Double ASSURANCE = .8; // Only at this point does a guess count
	private static final Double TOLERANCE = .75; // Must be accurate x% of time to not be pruned
	private static final int[] LENGTHS = {5, 10, 20, 30, 60, 120, 240};
	private static final int[] TYPES = {1,2,3,5,10};
	private static final int LOOK_AHEAD = TYPES[1];
	private static final int GO_BACK = 90; //90, 180
	
	private static final String dbName = "Stockr";
	private static mysqlHandler db;
	
	private static DecimalFormat df = new DecimalFormat("0.000");
	public AIDriver()
		{
		p = new Portfolio();
		}
	
	public static void main(String[] args) {
		String[] stock = loadStocks();
		//String[] stock = {"AAPL","BMRN","CRGN","F"};
		upload(stock);
		System.out.println("Shit: "+Integer.parseInt(args[0]));
		switch (Integer.parseInt(args[0])){
			default:
				farm(stock, 15);
			case 2:
				reseed(stock);
			case 1:
				process(stock);
				break;
			case 3:
				for(int i = 0; i < stock.length; i++){
					if ( StockData.exists(stock[i]))
						saveToSql(stock[i]);
					}
				break;
				}
		}
	
	public static String[] loadStocks(){
		LinkedList<String> hold = new LinkedList<String>();
		try{
			BufferedReader in = new BufferedReader(new FileReader("stocklist.stkl"));
			String str;
			while ((str = in.readLine()) != null) {
				String[] result = str.split("\\s");
				for(int i= 0; i < result.length; i++){
					if (!result[i].matches("\\s") && result[i].length() > 0){
						hold.add(result[i]);
						}
					}
		    }
			in.close();
		 	} 
		catch (IOException e) {
		    }
		
		String[] junk = new String[hold.size()];
		for (int i = 0; i < hold.size(); i++){
			junk[i] = hold.get(i);
			}
		
		return junk;
		}
	
	public static void reseed(String[] stock){
		for(int i = 0; i < stock.length; i++){
			if ( StockData.exists(stock[i])){
					StockCGS control = new StockCGS(stock[i], LENGTHS[0], 0, ASSURANCE, TOLERANCE);
					control.reseed(0);
					control.save();
					}
				}
		}
	
	public static void process(String[] stock){
		for(int i = 0; i < stock.length; i++){
			if ( StockData.exists(stock[i])){
				for (int j = 0; j < TYPES.length; j++)
					upgrade(stock[i], TYPES[j], 0);
				
				saveToSql(stock[i]);
				}
			}
		}
	
	public static void saveToSql(String stock){
		LinkedList<mysqlToken> Types, Vals, Where;
		System.out.println("Saving: "+stock);
		db = new mysqlHandler(dbName);
		
		if(!db.tableExists(stock)){
			Types = new LinkedList<mysqlToken>();
			Types.add(new mysqlToken("Date", "'2007-01-01'", "DATE", false));
			for (int i = 0; i < TYPES.length; i++)
				Types.add(new mysqlToken(TYPES[i]+"", "0", "FLOAT", false));
			
			db.createTable(stock,Types,false);
			}
		
		Where = new LinkedList<mysqlToken>();
		Vals = new LinkedList<mysqlToken>();
		Date today = currentDate(stock);
		SimpleDateFormat oDate = new SimpleDateFormat("yyyy-MM-dd");
		
		StockCGS control = new StockCGS(stock, LENGTHS[0], 1, ASSURANCE, TOLERANCE);
		System.out.println(oDate.format(control.currentDate()));
		Where.add(new mysqlToken("Date", oDate.format(control.currentDate()), "DATE", false));
		Vals.add(new mysqlToken("Date", oDate.format(control.currentDate()), "DATE", false));
		for (int i = 0; i < TYPES.length; i++)
			Vals.add(new mysqlToken(TYPES[i]+"", ""+checkAverage(stock, TYPES[i], 0), "FLOAT", false));
		
		if(!db.tableHolds(Where,stock)){
			db.tableInsert(Vals,stock);
			}
		else{
			db.tableUpdate(Where,Vals,stock);
			}
		}
	
	public static void farm(String[] stock, int days)
		{
		for (int i=0; i < stock.length; i++){
			if ( StockData.exists(stock[i]) ){
				System.out.println("farming --> "+stock[i]);
				for (int j = 0; j < TYPES.length; j++){
					for (int k = 0; k < LENGTHS.length; k++){
						for (int z = days; z >= 0; z -= 5) {
							create(stock[i], LENGTHS[k], TYPES[j], z);
							}
						}
					}
				}
			}
		}
	
	public static Double[] check(String stock, int ahead, int daysBack){
		Double[] value = new Double[LENGTHS.length];
			
		for (int k = 0; k < LENGTHS.length; k++){
			StockCGS control = new StockCGS(stock, LENGTHS[k], ahead, ASSURANCE, TOLERANCE);
			value[k]= control.check(daysBack);
			}
		
		return value;
		}
	
	public static Double checkAverage(String stock, int ahead, int daysBack){
		Double value = 0.0;
		Double[] res = check(stock, ahead, daysBack);
		
		if (res.length == 0)
			return 0.0;
		else{
			for (int i = 0; i < res.length; i++){
				value += res[i];
				}
			value /= res.length;
			}
			
		return value;
		}
	
	public static Date currentDate(String stock){
		StockCGS control = new StockCGS(stock, LENGTHS[0], TYPES[0], ASSURANCE, TOLERANCE);
		
		return control.currentDate();
		}
	
	public static void upgrade(String stock, int ahead, int daysBack){
		for (int k = 0; k < LENGTHS.length; k++){
			StockCGS control = new StockCGS(stock, LENGTHS[k], ahead, ASSURANCE, TOLERANCE);
			control.upgrade(daysBack);
			control.save();
			}
		}
	
	public static void create(String stock, int hist, int ahead, int where){
		StockCGS control = new StockCGS(stock, hist, ahead, ASSURANCE, TOLERANCE);
		
		control.rebirth(where);
		control.save();
		
		System.out.println(hist+"-"+ahead+"@"+df.format(control.workingValue())
												+":"+df.format(control.odds())
												+" ---> "+df.format(control.check()));
		control = null;
		}
	
	public static void prime(String[] stock){
		for (int i=0; i < stock.length; i++){
			StockValues.save(stock[i], false);
			}
		}
	
	public static void upload(String[] stock){
		for (int i=0; i < stock.length; i++){
			try{
				StockValues.save(stock[i]);
				}
			catch (Exception ex){
				System.out.println("error => "+stock[i]);
				stock[i] = "AMD";
				ex.printStackTrace();
				}
			}
		
		System.out.println("**** Upload done ****");
		}
	
	public static Double testDrive(String[] stock, Double in, int daysBack){
		Double maxHigh, maxLow;
		Double value =0.0;
		int lowPlace = 0, highPlace = 0;
		while (daysBack >= 0){
			maxHigh = maxLow = 0.0;
			System.out.println("Next Day");
			for (int i=0; i < stock.length; i++){
				System.out.println("Do Stock");
				try{
				/*	if ((daysBack % 5) == 0){
							System.out.println("Upgrading");
						 upgrade(stock[i], 1, daysBack);
						 System.out.println("Upgrading 2");
						 upgrade(stock[i], 2, daysBack);
						 System.out.println("Upgrading 3");
						 upgrade(stock[i], 3, daysBack);
						} */
					value = (checkAverage(stock[i], 1, daysBack) +
							checkAverage(stock[i], 2, daysBack) +
							checkAverage(stock[i], 3, daysBack))/3;
							
					if(value > maxHigh)
						{
						maxHigh = value;
						highPlace = i;
						}
					else if (value < maxLow)
						{
						maxLow = value;
						lowPlace = i;
						}
					}
				catch (Exception ex){
					ex.printStackTrace();
					}
				}
			System.out.println(maxHigh+":"+maxLow);
			highPlace = (maxHigh>Math.abs(maxLow))?highPlace:lowPlace;
			value = (maxHigh>=Math.abs(maxLow))?maxHigh:maxLow;
			
			StockCGS high = new StockCGS(stock[highPlace], GO_BACK, LOOK_AHEAD, ASSURANCE, TOLERANCE);
			StockCGS low = high;//new StockCGS(stock[lowPlace], GO_BACK, LOOK_AHEAD, ASSURANCE);
			//if (Math.abs(value) > ASSURANCE)
				if (value > 0)
					{
					in *= high.change(daysBack);
					System.out.println(daysBack+") Reg Choosing:"+stock[highPlace]+" Value: "+in
																+" c:"+high.printChange(daysBack));
					}
				else
					{
					in /= low.change(daysBack);
					System.out.println(daysBack+") Sho Choosing:"+stock[highPlace]+" Value: "+in
																+" c:"+low.printChange(daysBack));
					}
			
			daysBack -= LOOK_AHEAD;
			}
		return in;
		}
	}
