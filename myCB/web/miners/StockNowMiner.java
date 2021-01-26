package myCB.web.miners;

import myCB.data.*;
import myCB.web.*;

// http://finance.aol.com/usw/quotes/quotesandnews?exch=USA&from=his&sym=E%3ANYS%3Aamd
public class StockNowMiner extends Miner
	{
	Page hold;
	public StockNowMiner(String stock)
		{
		super();
		
		
		connect("http://finance.yahoo.com/q?s="+stock.toUpperCase());
		
		parsedTable_loadLowest("Volume:","td");
		hold = (Page)parsedTable_toPage();
		
		parsedTable_loadLowest("Open:","td");
		hold.append((Page)parsedTable_toPage());
		
		hold.oneLine();
		}

	public String getData()
		{
		return (String)hold.get();
		}
	}