package myCB.stock;

import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javax.swing.JTable;
import com.mysql.jdbc.ResultSet;
import myCB.data.Page;
import myCB.web.miners.StockHistMiner;
import mysql.mysqlHandler;
import mysql.mysqlToken;

public class StockData implements Serializable
	{
	private static final long serialVersionUID = 3;
	private static final String dbName = "Stocki";
	private String Stock;
	private Date Today;
	private static mysqlHandler db;
	private boolean Created;
	private float[][] Vals, Vols;
	
	public StockData(String stock){
		LinkedList<mysqlToken> Types;
		
		db = new mysqlHandler(dbName);
		Stock = stock;
		
		Types = new LinkedList<mysqlToken>();
		Types.add(new mysqlToken("Date", "'2007-01-01'", "DATE", false));
		Types.add(new mysqlToken("Open", "0", "FLOAT", false));
		Types.add(new mysqlToken("Close", "0", "FLOAT", false));
		Types.add(new mysqlToken("High", "0", "FLOAT", false));
		Types.add(new mysqlToken("Low", "0", "FLOAT", false));
		Types.add(new mysqlToken("Volume", "0", "INT", false));
		
		if(!db.tableExists(Stock)){
			Created = true;
			db.createTable(Stock,Types,false);
			}
		else
			Created = false;
		}
	
	static public boolean exists(String stock){
		db = new mysqlHandler(dbName);
		return db.tableExists(stock);
		}
	
	public void load(){
		LinkedList<float[]> res = new LinkedList<float[]>();
		/*Run a table dump and then set up internal variables*/
		ResultSet val = db.tableDump(Stock,"Date",true);
		
		
		try{
			val.next();
			Today = val.getDate("Date");
			val.beforeFirst();
			
			while(val.next()){
				// open, high, low, close, volume
				float[] row = new float[5];
				row[0] = val.getFloat("Open");
				row[1] = val.getFloat("High"); 
				row[2] = val.getFloat("Low");
				row[3] = val.getFloat("Close");
				row[4] = (float)val.getInt("Volume");
				res.add(row);
				}
			
			Vals = new float[res.size()][4];
			Vols = new float[res.size()][1];
			
			for (int i = 0; i < res.size(); i++){
				Vals[i][0] = res.get(i)[0];
				Vals[i][1] = res.get(i)[1];
				Vals[i][2] = res.get(i)[2];
				Vals[i][3] = res.get(i)[3];
				
				Vols[i][0] = res.get(i)[4];
				}
			}
		catch (Exception ex){
			ex.printStackTrace();
			}
		}
	
	public void upload()
		{
		Page hold;
		boolean needed = true;
		// this means that this is a new stock
		StockHistMiner temp;
		if (Created)
			temp = new StockHistMiner(Stock, 10);
		else
			temp = new StockHistMiner(Stock, 1);
		
		Created = false;
		hold = temp.getData();
		
		for(hold.start();!hold.done() && needed;hold.next())
			{
			try
				{
				needed = addDay((String)hold.get());
				}
			catch (NoSuchElementException ex)
				{
				System.out.println("---> "+(String)hold.get());			
				}
			}
		
		refresh();
		load();
		}
	
	public void refresh()
		{
		/*//--------------------
		// variables to be read for the new day
		String tHigh, tLow;	// |Day's Range:|29.84 - 30.15|
		String Vol;			// |Volume:|15,000,672|
		String Close;		// |Last Trade:|29.95|
		String Open;		// |Open:|30.10|
		String Date;		// |Trade Time:|Dec 30|	
		//--------------------
		StockNowMiner Today = new StockNowMiner(Stock);
		StringTokenizer hold, token = new StringTokenizer(Today.getData(),"|");
		// |Day's Range:|29.84 - 30.15|
		token.nextToken();
		hold = new StringTokenizer(token.nextToken(),"-");
		tLow = hold.nextToken();
		tHigh = hold.nextToken();
		// |52wk Range:|28.62 - 42.44|
		token.nextToken();
		hold = new StringTokenizer(token.nextToken(),"-");
		fLow = hold.nextToken();
		fHigh = hold.nextToken();
		// |Volume:|15,000,672|
		token.nextToken();
		Vol = token.nextToken();
		// |Avg Vol (3m):|22,354,100|
		token.nextToken();
		aVol = token.nextToken();
		// |Market Cap:|70.49B|
		token.nextToken();
		mCap = token.nextToken();
		// |P/E (ttm):|23.16|
		token.nextToken();
		PE = token.nextToken();
		// |EPS (ttm):|1.29|
		token.nextToken();
		EPS = token.nextToken();
		// |Div &amp; Yield:|N/A (N/A)|
		token.nextToken();
		token.nextToken();
		// |Last Trade:|29.95|
		token.nextToken();
		Close = token.nextToken();
		// |Trade Time:|Dec 30|	
		token.nextToken();
		token.nextToken();
		Date = getDate();
		// |Change:|0.33 (1.09%)|
		token.nextToken();
		token.nextToken();
		// |Prev Close:|30.28|
		token.nextToken();
		token.nextToken();
		// |Open:|30.10|
		token.nextToken();
		Open = token.nextToken();
		// |Bid:|27.95 x 1000|
		token.nextToken();
		Bid = token.nextToken();
		// |Ask:|31.96 x 1000|
		token.nextToken();
		Ask = token.nextToken();
		// |1y Target Est:|36.70|
		token.nextToken();
		Est = token.nextToken();
		
		addDay(Date,Open,Close,tHigh,tLow,Vol);
		}
	
	private String getDate()
		{
		SimpleDateFormat oDate = new SimpleDateFormat("yyyy-MM-dd");
		return oDate.format(Calendar.getInstance());*/
		}
	
	protected boolean addDay(String D,String O,String C,String H,String L,String V)
		{
		LinkedList<mysqlToken> Types = new LinkedList<mysqlToken>();
		LinkedList<mysqlToken> Where = new LinkedList<mysqlToken>();
		if (D.indexOf('-') >= 0)
			{
			try{
				SimpleDateFormat date = new SimpleDateFormat("dd-MMM-yy");
				SimpleDateFormat oDate = new SimpleDateFormat("yyyy-MM-dd");
				
				D = oDate.format(date.parse(D)); 
				}
			catch (Exception ex){
				// Shit happens
				}
			}
		Types.add(new mysqlToken("Date", D));
		Where.add(new mysqlToken("Date", D));
		
		Types.add(new mysqlToken("Open", O));
		Types.add(new mysqlToken("Close", C));
		Types.add(new mysqlToken("High", H));
		Types.add(new mysqlToken("Low", L));
		
		if (V.indexOf(',') >= 0)
			{
			StringTokenizer tokens = new StringTokenizer(V,",");
			String temp = new String();
			while(tokens.hasMoreTokens())
				temp += tokens.nextToken();
			V = temp;
			}
		Types.add(new mysqlToken("Volume", V));
		
		if(!db.tableHolds(Where,Stock)){
			db.tableInsert(Types,Stock);
			return true;
			}
		else{
			db.tableUpdate(Where,Types,Stock);
			return false;
			}
		}
	// Adds a day to the back of the list	
	protected boolean addDay(String Line)
		{
		StringTokenizer token = new StringTokenizer(Line,"|");
		String Date = token.nextToken().trim();
		String Open = token.nextToken().trim();
		String High = token.nextToken().trim();
		String Low = token.nextToken().trim();
		String Close = token.nextToken().trim();
		String Vol = token.nextToken().trim();
		
		return addDay(Date, Open, Close, High, Low, Vol);
		}
	
	public JTable spreadSheet()
	{
	String[][] hold = new String[0][StockDay.headers().length];
	
	int i = 0;
	/*for(Days.start(); !Days.done(); Days.next())
		{
		hold[i] = ((StockDay)(Days.get())).toArray();
		i++;
		}*/
						
	return new JTable((Object[][])hold, StockDay.headers());
	}
	
public Date getNewestDay(){
	return Today;	
	}

public float[][][] getCategories()
	{
	float[][][] temp = new float[2][][];
	
	/*temp[0] = values();
	temp[1] = volume();*/
	return temp;
	}
	public float[] getCloseValues()
		{
		float[] hold = new float[0];
		/*int i = 0;
		
		for(Days.start(); !Days.done(); Days.next())
			{
			hold[i] = Float.valueOf(((StockDay)(Days.get())).getClose());
			i++;
			}*/
		
		return hold;
		}

	public float[][] volume()
		{
		return Vols;
		}
	
	public float[][] values()
		{
		return Vals;
		}
	public String getTicker(){
		return Stock;
		}
	}