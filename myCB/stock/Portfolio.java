package myCB.stock;

import java.io.*;
import javax.swing.*;
import myCB.gui.*;
import myCB.data.*;

//--------------
// -> Update portfolio
// -> Make sure days are ok, if not... refresh completely

public class Portfolio implements Serializable 
	{
	private String Title;
	private IndexList Stocks;
	private static final long serialVersionUID = 1L;
	
	public Portfolio() 
		{
		Stocks = new IndexList();
		Title = null;
		}
	
	public void reload()
		{
		for (firstStock();moreStock();nextStock())
			{
			getStock().upload();
			}
		}
	
	public void update()
		{
		for (firstStock();moreStock();nextStock())
			{
			getStock().refresh();
			}
		}
	
	public void firstStock()
		{
		Stocks.start();
		}
	
	public void nextStock()
		{
		Stocks.next();
		}
	
	public boolean moreStock()
		{
		return !Stocks.done();
		}
	
	public StockData getStock()
		{
		return (StockData)Stocks.get(); 
		}
	
	public StockData findStock(String tag) 
		{
		tag = tag.toUpperCase();
		Stocks.firstTag(tag);
		return (StockData)Stocks.get();
		}
	
	public void removeStock(String tag)
		{
		tag = tag.toUpperCase();
		Stocks.firstTag(tag);
		Stocks.remove();
		}
	
	public JList heldStock()
		{
		return new JList(Stocks.getIndexs());
		}
	
	public portfolioGUI getGUI()
		{
		return new portfolioGUI(this);
		}
	
	public void addStock(String tag)
		{
		tag = tag.toUpperCase();
		
		if (!Stocks.findTag(tag))
			{
			StockData data = new StockData(tag);
	
			try
				{
				data.upload();
				Stocks.push_back(tag,data);
				}
			catch (Exception ex)
				{
				System.out.println("(Portfolio) "+tag
						+": Stock could not be loaded or added");
				ex.printStackTrace();
				}
			}
		else
			System.out.println("Stock already exists");
		}
	
	public boolean hasTitle()
		{
		return (Title != null);
		}
	
	public void setTitle(String in)
		{
		Title = in;
		}
	
	public String getTitle()
		{
		return Title;
		}
	}