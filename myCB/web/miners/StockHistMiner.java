package myCB.web.miners;

import myCB.data.*;

public class StockHistMiner extends Miner
	{
	Page data;
	public StockHistMiner(String stock){
		super();
		create(stock, 10);
		}
	public StockHistMiner(String stock, int back){
		super();
		create(stock, back);
		}
	private void create(String stock, int back)
		{
		data = new Page();
		Page hold, oh;
		String root = "http://finance.yahoo.com";
		String temp = root+"/q/hp?s="+stock.toUpperCase();
	
		int i;
		int LIMIT = back;
		
		try
			{
			for (i = 0; i < LIMIT; i++)
				{
				connect(temp);
				
				parsedTable_loadLowest("Volume","td");
		
				hold = (Page)parsedTable_toPage();
		
				hold.pop_front();
				hold.pop_back();
		
				// next loading
				href_load();
				try	{
					oh = href_target("Next");
					oh.start();
					oh.eraseWord("amp;");
					temp = root+oh.get();
					}
				catch (Exception ex)
					{
					i = LIMIT;
					//ex.printStackTrace();
					System.out.println("\n-- error loading page, no next found --");
					}
				//------------------
				data.append(hold);
				}
			} 
		catch (Exception ex)
			{
			//ex.printStackTrace();
			System.out.println("\n-- error loading page, keeping what we have --");
			}	
		//System.out.println("Data Collected: "+data.size());
		}
	public Page getData()
		{
		return data;
		}
	}