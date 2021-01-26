package myCB.web.miners;

import java.io.*;
import java.util.*;
import myCB.data.*;
import myCB.web.*;

// http://studio.financialcontent.com/Engine?Account=lebanon&Ticker=AMD&PageName=HISTORICAL&Month=2&Day=20&Year=2005

public class Miner
{
	Object TableData;
	Page Href;
	HTMLPage Raw, Linked;
	public Miner()
		{
		
		}
	//-------------
	// 
	//-------------	
	public void connect(String website)
		{
		Raw = new HTMLPage();
		
		try
			{
			Raw.access(website);
			}
		catch (FileNotFoundException ex)
			{
			System.out.println(website);
			System.out.println("File could not be accessed"); 
			}
		Raw.parseFormat();	
		Raw.delNBSP();
		//Raw.save("Raw.bro");
		}
	//-------------
	// 
	//-------------	
	public void parsedTable_loadLowest(String ID /*Table having String*/, 
										String type /*td or th*/)
		{
		TagHandler screen, sifter;
		HTMLPage data;
		MyList rows = new MyList();
		screen = new TagHandler(Raw);

		for (screen.set_classified("table");
			screen.get_classified() != null &&
			!((HTMLPage)screen.get_classified()).findDC(ID); 
			screen.next_classified())
			{
			}
			
		screen = new TagHandler((HTMLPage)screen.get_classified());
		
		for (screen.set_classified("tr");
			screen.get_classified() != null;
			screen.next_classified())
			{
			sifter = new TagHandler((HTMLPage)screen.get_classified());
			data = sifter.onePage_classified(type);
			
			for(data.start();!data.done();data.next())
				{
				data.delControl();
				data.clean();
				}
			rows.push_back(data.toArray());
			}
		
		TableData = rows.toArray();
		}
	//-------------
	// 
	//-------------	
	public Page parsedTable_toPage()
		{
		Page temp = new Page();
		String hold = "|";
		
		for (int i = 0; i < ((Object[])TableData).length; i++)
			{
			for(int j = 0; j < ((Object[])(((Object[])TableData)[i])).length; j++)
				hold = hold + (String)((Object[])((Object[])TableData)[i])[j] +"|"; 
			temp.push_back(hold);
			hold = "|";
			}
			
		return temp;
		}
	//-------------
	// 
	//-------------	
	public String parsedTable_accessElement(int i, int j)
		{
		return (String)((Object[])((Object[])TableData)[i])[j];
		}
	//-------------
	// 
	//-------------	
	public int parsedTable_accessRows()
		{
		return ((Object[])TableData).length;
		}
	//-------------
	// 
	//-------------	
	public int parsedTable_accessCollums(int i)
		{
		return ((Object[])(((Object[])TableData)[i])).length;
		}
	//-------------
	// 
	//-------------	
	public void href_load()
		{
		Href = new Page();
		Linked = new HTMLPage();

		TagHandler i = new TagHandler(Raw);
		
		Linked = i.onePage_classified("a");

		for (Linked.start(); !Linked.done(); Linked.next())
			{
			try
				{
				Href.push_back(getHref((String)Linked.get()));
				}
			catch (Exception e)
				{
				Linked.remove();
				Linked.prev();
				}
			Linked.delControl();
			}
		}
	//---------------
	// 
	//---------------
	public Page href_target(String x)
		{
		Page hold = new Page();
		for (Linked.start(); !Linked.done(); Linked.next())
			{
			if (Linked.findHereDC(x))
				{
				Href.to(Linked.pointingAt());
				hold.push_back(Href.get());
				}
			}
		return hold;
		}
	//---------------
	// 
	//---------------	
	public Page href_all()
		{
		return Href;
		}
	//---------------
	// Pulls the value of the given variable from the bxml command
	//---------------
	private String getHref(String Line)
		{
		int front;
		int back;
		String holder;
		
		holder = Line.toLowerCase();
		front = holder.indexOf('\'', holder.indexOf("href"));
		
		if(front == -1)
			{
			front = holder.indexOf('\"', holder.indexOf("href"));
			back = holder.indexOf('\"', front+1);
			}
		else
			{
			back = holder.indexOf('\'', front+1);
			}
		return Line.substring(front+1,back);
		}
}